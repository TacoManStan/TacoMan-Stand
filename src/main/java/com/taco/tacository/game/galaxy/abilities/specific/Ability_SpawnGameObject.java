package com.taco.tacository.game.galaxy.abilities.specific;

import com.taco.tacository.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.tacository.game.galaxy.effects.Effect_SpawnGameObject;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Ability_SpawnGameObject extends Ability_TargetEffect<Effect_SpawnGameObject> {
    
    public Ability_SpawnGameObject(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean execute(@NotNull Map<String, Object> params) {
        return new Effect_SpawnGameObject(getSource()).trigger(params);
    }
    
    @Override public @NotNull List<Value2D<String, Class<?>>> requiredParams() {
        return Arrays.asList(new Value2D<>("target", Num2D.class),
                             new Value2D<>("factory", Supplier.class));
    }
    
    //</editor-fold>
}
