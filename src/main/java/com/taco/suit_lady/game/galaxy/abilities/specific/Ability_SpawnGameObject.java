package com.taco.suit_lady.game.galaxy.abilities.specific;

import com.taco.suit_lady.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.suit_lady.game.galaxy.effects.Effect_LaunchMissile;
import com.taco.suit_lady.game.galaxy.effects.Effect_SpawnGameObject;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

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
    
    //</editor-fold>
}
