package com.taco.suit_lady.game.galaxy.effects.specific;

import com.taco.suit_lady.game.galaxy.effects.Effect;
import com.taco.suit_lady.game.galaxy.effects.Effect_Targeted;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class Effect_Cleave extends Effect {
    
    public Effect_Cleave(@NotNull GameObject source, @Nullable Effect_Targeted impactEffect) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean trigger(@NotNull Map<String, Object> params) {
        final Point2D target = (Point2D) params.get("target");
        final double cleaveSize = (double) params.get("cleave_size");
        final double cleaveRange = (double) params.get("cleave_range");
        
        final Num2D center = N.num2D(getSource().getLocation(true));
        final double angleToTarget = Calc.angle(center, target, Calc.AngleType.ACTUAL);
        
        final ArrayList<GameObject> filtered = getGameMap().scan(
                gameObject -> Calc.isInCone(
                        center,
                        N.num2D(gameObject.getLocation(true)),
                        cleaveRange,
                        N.num2D(target),
                        cleaveSize),
                gameObject -> !gameObject.equals(getSource()));
        
        filtered.forEach(gameObject -> gameObject.taskManager().shutdown());
        
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
