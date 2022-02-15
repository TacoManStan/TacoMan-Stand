package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

public abstract class Effect {
    
    private final GameObject source;
    
    public Effect(@NotNull GameObject source) {
        this.source = source;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getSource() { return source; }
    
    //</editor-fold>
}
