package com.taco.tacository.game.galaxy.effects;

import com.taco.tacository.game.galaxy.effects.specific.Effect_MissileImpact;
import com.taco.tacository.game.objects.Mover;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.logic.triggers.Galaxy;
import com.taco.tacository.util.tools.printing.PrintData;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
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
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        final Num2D target = (Num2D) params.get("target");
        launchMissileTest(target);
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    private @NotNull GameObject launchMissileTest(@NotNull NumExpr2D<?> target) {
        final GameObject missile = constructMissile();
        missile.setObjID("missile");
        missile.init(() -> {
            final PrintData p = printer().get("heheXD");
            
            missile.setLocation(getSource().getLocation(false), false);
            
            missile.collisionMap().collisionAreasCopy().forEach(gameObjectCollisionArea -> {
                gameObjectCollisionArea.includedShapes().forEach(shape -> printer().get("heheXD").print("Missile Collision Shape: " + shape));
            });
            
            p.print("Pathable @ Owner: " + getGameMap().isPathable(missile, false, getSource().getLocation(true)));
            
            
            p.print("Obj Location: " + missile.getLocation(true));
            p.print("Obj Dimensions: " + missile.getDimensions());
            final Num2D sourceLocation = getSource().getLocation(true);
            final Num2D targetLocation = target.asNum2D();
            p.print("Source Location: " + sourceLocation);
            p.print("Target Location: " + targetLocation);
            //            final double targetAngle = getSource().getLocation(true).angle(target);
            final double targetAngle = sourceLocation.angle(targetLocation);
            printer().get("heheXD").print("Target Angle: " + targetAngle);
            final Num2D nearestPathable = getGameMap().nearestPathablePoint(missile, 1, 200, targetAngle);
            printer().get("heheXD").print("Missile Location: " + nearestPathable);
            missile.setLocation(nearestPathable, true);
            
            missile.attributes().addDoubleAttribute(Mover.ACCELERATION_ID, 1.025D);
            missile.attributes().getDoubleAttribute(Mover.SPEED_ID).setValue(2D);
            
            getGameMap().addGameObject(missile);
            
            logiCore().triggers().register(Galaxy.newUnitArrivedTrigger(missile, event -> {
                Printer.print("Missile Arrived [" + missile + "]  ||  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
                final Effect impactEffect = getImpactEffectTest(missile);
                if (impactEffect != null) {
                    Printer.print("Triggering Impact Effect: " + impactEffect);
                    impactEffect.trigger(L.map(
                            new Value2D<>("target", event.getMovedTo()),
                            new Value2D<>("missile", missile),
                            new Value2D<>("radius", 25D),
                            new Value2D<>("impact_location", missile.getLocation(true))));
                }
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
