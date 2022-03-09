package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.shapes.Circle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class Effect_Scan extends Effect_Targeted {
    
    private final ReadOnlyObjectWrapper<Effect_Targeted> scanEffectProperty;
    
    public Effect_Scan(@NotNull GameObject source, @Nullable Effect_Targeted scanEffect) {
        super(source);
        this.scanEffectProperty = new ReadOnlyObjectWrapper<>(scanEffect);
    }
    
    public Effect_Scan(@NotNull GameObject source) { this(source, null); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Effect_Targeted> readOnlyScanEffectProperty() { return scanEffectProperty.getReadOnlyProperty(); }
    public final Effect_Targeted getScanEffect() { return scanEffectProperty.get(); }
    
    @Contract("_ -> new")
    public final @NotNull Effect_Targeted getScanEffectTest(@NotNull GameObject missile) {
        return new Effect_Targeted(missile) {
            @Override public boolean trigger(@NotNull Map<String, Object> params) {
                final GameObject target = (GameObject) params.get("target");
                target.taskManager().shutdown();
                return true;
            }
        };
    }
    
    public final Effect_Targeted setScanEffect(@Nullable Effect_Targeted newValue) { return Props.setProperty(scanEffectProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final GameObject missile = (GameObject) params.get("missile");
        final double radius = (double) params.get("radius");
        final Num2D impactLocation = (Num2D) params.get("impact_location");
        
        final Circle scanZone = new Circle(this, getLock());
        scanZone.setLocation(impactLocation);
        scanZone.setRadius(radius);
    
        Printer.print("Scanning: " + impactLocation + "  |  " + radius);
        
        final ArrayList<GameObject> scannedObjs = getGameMap().scan(gameObject -> {
            return !gameObject.equals(getSource()) && !gameObject.equals(missile) &&
                   gameObject.collidesWith(scanZone);
        });
        scannedObjs.forEach(gameObject -> getScanEffect().trigger(L.map(new Value2D<>("target", gameObject))));
        
        getGameMap().scanMap(missile.getLocation(true), radius)
                    .forEach(gameObject -> getScanEffect().trigger(
                            L.map(new Value2D<>("target", gameObject))));
        
        return true;
    }
    
    //</editor-fold>
}
