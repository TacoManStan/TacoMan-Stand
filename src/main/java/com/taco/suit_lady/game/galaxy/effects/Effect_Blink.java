package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Effect_Blink extends Effect_Targeted {
    
    private final ReadOnlyObjectWrapper<Effect> impactEffectProperty;
    
    public Effect_Blink(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        super(source);
        this.impactEffectProperty = new ReadOnlyObjectWrapper<>(impactEffect);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Effect> readOnlyImpactEffectProperty() { return impactEffectProperty.getReadOnlyProperty(); }
    public final Effect getImpactEffect() { return impactEffectProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final Point2D target = (Point2D) params.get("target");
        getSource().setLocation(target, true);
        return true;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    public static @NotNull Effect_Blink newInstance(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        return new Effect_Blink(source, impactEffect != null ? impactEffect : Effect_Targeted.empty(source));
    }
    public static @NotNull Effect_Blink newInstance(@NotNull GameObject source) { return newInstance(source, null); }
    
    //</editor-fold>
}
