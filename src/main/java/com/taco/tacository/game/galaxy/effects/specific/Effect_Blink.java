package com.taco.tacository.game.galaxy.effects.specific;

import com.taco.tacository.game.galaxy.effects.Effect;
import com.taco.tacository.game.galaxy.effects.Effect_Targeted;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
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
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        final Num2D target = (Num2D) params.get("target");
        getSource().setLocation(target, true);
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class));
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
