package com.taco.tacository.game.galaxy.abilities.specific;

import com.taco.tacository.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.tacository.game.galaxy.effects.specific.Effect_Cleave;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Ability_Cleave extends Ability_TargetEffect<Effect_Cleave> {
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public Ability_Cleave(@NotNull GameObject source) {
        super(source);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean execute(@NotNull Map<String, Object> params) {
        return Effect_Cleave.newInstance(getSource()).trigger(params);
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class),
                             new Value2D<>("cleave_size", Number.class),
                             new Value2D<>("cleave_range", Number.class));
    }
    
    //</editor-fold>
}
