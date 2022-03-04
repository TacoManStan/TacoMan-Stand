package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.ValuePair;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Effect_Scan extends Effect_Targeted {
    
    private final ReadOnlyObjectWrapper<Effect_Targeted> scanEffectProperty;
    
    public Effect_Scan(@NotNull GameObject source) {
        super(source);
        this.scanEffectProperty = new ReadOnlyObjectWrapper<>();
    }
    
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
        
        getGameMap().scanMap(missile.getLocation(true), radius)
                    .forEach(gameObject -> getScanEffectTest(missile).trigger(
                            L.map(new ValuePair<>("target", gameObject))));
        
        return true;
    }
    
    //</editor-fold>
}
