package com.taco.suit_lady.game.galaxy.abilities.specific;

import com.taco.suit_lady.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.suit_lady.game.galaxy.effects.Effect;
import com.taco.suit_lady.game.galaxy.effects.Effect_LaunchMissile;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

public class LaunchMissileAbility extends Ability_TargetEffect<Effect_LaunchMissile> {
    
    public LaunchMissileAbility(@NotNull GameObject source) {
        super(source);
    }
}
