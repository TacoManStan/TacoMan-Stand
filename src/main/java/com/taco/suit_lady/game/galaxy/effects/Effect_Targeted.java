package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

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
            @Override public boolean trigger(@NotNull Map<String, Object> params) { return triggerResultSupplier.get(); }
        };
    }
    
    //</editor-fold>
}
