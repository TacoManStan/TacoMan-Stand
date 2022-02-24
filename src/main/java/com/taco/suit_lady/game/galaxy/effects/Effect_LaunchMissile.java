package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.commands.MoveCommand;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.list_tools.ListsSL;
import com.taco.suit_lady.util.tools.util.ValuePair;
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
    
    public final Effect getImpactEffectTest() { return new Effect_LaunchMissile(getSource()); }
    
    public final ReadOnlyStringProperty readOnlyMissileObjectProperty() { return missileObjectProperty.getReadOnlyProperty(); }
    public final String getMissileObject() { return missileObjectProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, ?> params) {
        final Point2D target = (Point2D) params.get("target");
        launchMissileTest(target);
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    private @NotNull GameObject launchMissileTest(@NotNull Point2D target) {
        final GameObject missile = constructMissile().init();
        
        missile.setLocationX(getSource().getLocationX(false), false);
        missile.setLocationY(getSource().getLocationY(false), false);
        
        missile.attributes().addDoubleAttribute(MoveCommand.ACCELERATION_ID, 1.025D);
        missile.attributes().getDoubleAttribute(MoveCommand.SPEED_ID).setValue(1D);
        
        logiCore().triggers().register(Galaxy.newUnitArrivedTrigger(missile, event -> {
            Print.print("Missile Arrived [" + missile + "]  ||  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
            final Effect impactEffect = getImpactEffectTest();
            if (impactEffect != null)
                impactEffect.trigger(ListsSL.map(
                        new ValuePair<>("missile", missile),
                        new ValuePair<>("radius", 25D)));
            missile.taskManager().shutdown();
        }));
        
        return missile;
    }
    
    private GameObject constructMissile() {
        return new GameObject(getGame());
    }
    
    //</editor-fold>
}
