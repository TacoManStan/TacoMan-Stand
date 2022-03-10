package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.util.values.Value2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class Effect_Targeted extends Effect {
    
    public Effect_Targeted(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- STATIC ---">
    
    public static @NotNull Effect_Targeted empty(@NotNull GameObject source) { return empty(source, true); }
    public static @NotNull Effect_Targeted empty(@NotNull GameObject source, boolean triggerResult) { return empty(source, () -> triggerResult); }
    
    public static @NotNull Effect_Targeted empty(@NotNull GameObject source, @NotNull Supplier<Boolean> triggerResultSupplier) {
        return new Effect_Targeted(source) {
            @Override public boolean onTrigger(@NotNull Map<String, Object> params) { return triggerResultSupplier.get(); }
            @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() { return new ArrayList<>(); }
        };
    }
    
    //</editor-fold>
}
