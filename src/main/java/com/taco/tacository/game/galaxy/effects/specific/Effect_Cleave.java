package com.taco.tacository.game.galaxy.effects.specific;

import com.taco.tacository.game.galaxy.effects.Effect;
import com.taco.tacository.game.galaxy.effects.Effect_Targeted;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.tools.Calc;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Effect_Cleave extends Effect {
    
    public Effect_Cleave(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean onTrigger(@NotNull Map<String, Object> params) {
        final Num2D target = (Num2D) params.get("target");
        final double cleaveSize = ((Number) params.get("cleave_size")).doubleValue();
        final double cleaveRange = ((Number) params.get("cleave_range")).doubleValue();
        
        final Num2D center = getSource().getLocation(true);
        final double angleToTarget = Calc.angle(center, target);
        
        final ArrayList<GameObject> filtered = getGameMap().scan(
                gameObject -> Calc.isInCone(
                        center,
                        gameObject.getLocation(true),
                        cleaveRange,
                        target,
                        cleaveSize),
                gameObject -> !gameObject.equals(getSource()));
    
        Printer.print("Filtered:");
        Printer.print(filtered);
        
        filtered.forEach(gameObject -> gameObject.taskManager().shutdown());
        
        return true;
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class),
                             new Value2D<>("cleave_size", Number.class),
                             new Value2D<>("cleave_range", Number.class));
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
