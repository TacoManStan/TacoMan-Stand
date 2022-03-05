package com.taco.suit_lady.game.galaxy.abilities.specific;

import com.taco.suit_lady.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.suit_lady.game.galaxy.effects.Effect_Blink;
import com.taco.suit_lady.game.galaxy.effects.Effect_LaunchMissile;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Ability_Blink extends Ability_TargetEffect<Effect_LaunchMissile> {
    
    public Ability_Blink(@NotNull GameObject source) {
        super(source);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean execute(@NotNull Map<String, Object> params) { return Effect_Blink.newInstance(getSource()).trigger(params); }
    
    //</editor-fold>
}
