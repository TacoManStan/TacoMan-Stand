package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.galaxy.effects.specific.Effect_MissileImpact;
import com.taco.suit_lady.game.objects.Mover;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Effect_LaunchMissile extends Effect_Targeted {
    
    private final ReadOnlyObjectWrapper<Effect> impactEffectProperty;
    private final ReadOnlyStringWrapper missileObjectProperty;
    
    public Effect_LaunchMissile(@NotNull GameObject source) {
        super(source);
        
        this.impactEffectProperty = new ReadOnlyObjectWrapper<>();
        this.missileObjectProperty = new ReadOnlyStringWrapper();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Effect> readOnlyImpactEffectProperty() { return impactEffectProperty.getReadOnlyProperty(); }
    public final Effect getImpactEffect() { return impactEffectProperty.get(); }
    
    public final Effect getImpactEffectTest(@NotNull GameObject missile) {
//        return new Effect_LaunchMissile(getSource());
        return new Effect_Scan(getSource(), new Effect_MissileImpact(missile));
    }
    
    public final ReadOnlyStringProperty readOnlyMissileObjectProperty() { return missileObjectProperty.getReadOnlyProperty(); }
    public final String getMissileObject() { return missileObjectProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final Point2D target = (Point2D) params.get("target");
        launchMissileTest(target);
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    private @NotNull GameObject launchMissileTest(@NotNull Point2D target) {
        final GameObject missile = constructMissile();
        missile.init(() -> {
            missile.setLocation(getSource().getLocation(false), false);
    
            missile.collisionMap().collisionAreasCopy().forEach(gameObjectCollisionArea -> {
                gameObjectCollisionArea.includedShapes().forEach(shape -> printer().get("heheXD").print("Missile Collision Shape: " + shape));
            });
    
            printer().get("heheXD").print("Pathable @ Owner: " + getGameMap().isPathable(missile, false, getSource().getLocation(true)));
    
    
            printer().get("heheXD").print("Obj Location: " + missile.getLocation(true));
            printer().get("heheXD").print("Obj Dimensions: " + missile.getDimensions());
            final Num2D nearestPathable = getGameMap().nearestPathablePoint(missile, 1, 200);
            printer().get("heheXD").print("Missile Location: " + nearestPathable);
            missile.setLocation(nearestPathable.asPoint(), true);
    
            missile.attributes().addDoubleAttribute(Mover.ACCELERATION_ID, 1.025D);
            missile.attributes().getDoubleAttribute(Mover.SPEED_ID).setValue(2D);
    
            logiCore().triggers().register(Galaxy.newUnitArrivedTrigger(missile, event -> {
                Printer.print("Missile Arrived [" + missile + "]  ||  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
                final Effect impactEffect = getImpactEffectTest(missile);
                if (impactEffect != null)
                    impactEffect.trigger(L.map(
                            new Value2D<>("missile", missile),
                            new Value2D<>("radius", 25D)));
                missile.taskManager().shutdown();
            }));
    
            missile.mover().move(target);
        });
        
        return missile;
    }
    
    private GameObject constructMissile() {
        return new GameObject(getGame());
    }
    
    //</editor-fold>
}
