package com.taco.suit_lady.game.galaxy.abilities.specific;

import com.taco.suit_lady.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.suit_lady.game.galaxy.effects.Effect_LaunchMissile;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Ability_LaunchMissile extends Ability_TargetEffect<Effect_LaunchMissile> {
    
    public Ability_LaunchMissile(@NotNull GameObject source) {
        super(source);
    }
    
    @Override public void use(@NotNull Map<String, Object> params) {
        new Effect_LaunchMissile(getOwner()).trigger(params);
    }
}
