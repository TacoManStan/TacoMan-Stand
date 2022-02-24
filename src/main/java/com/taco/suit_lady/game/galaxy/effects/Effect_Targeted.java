package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

public abstract class Effect_Targeted extends Effect {
    
    public Effect_Targeted(@NotNull GameObject source) {
        super(source);
    }
}
