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
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean execute(@NotNull Map<String, Object> params) { return new Effect_LaunchMissile(getSource()).trigger(params); }
    
    //</editor-fold>
}
