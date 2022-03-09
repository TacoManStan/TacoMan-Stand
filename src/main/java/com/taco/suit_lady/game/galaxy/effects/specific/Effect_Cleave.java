package com.taco.suit_lady.game.galaxy.effects.specific;

import com.taco.suit_lady.game.galaxy.effects.Effect;
import com.taco.suit_lady.game.galaxy.effects.Effect_Targeted;
import com.taco.suit_lady.game.objects.GameObject;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Effect_Cleave extends Effect_Targeted {
    
    public Effect_Cleave(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final Point2D target = (Point2D) params.get("target_angle");
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    public static @NotNull Effect_Cleave newInstance(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        return new Effect_Cleave(source, impactEffect != null ? impactEffect : Effect_Targeted.empty(source));
    }
    public static @NotNull Effect_Cleave newInstance(@NotNull GameObject source) { return newInstance(source, null); }
    
    //</editor-fold>
}
