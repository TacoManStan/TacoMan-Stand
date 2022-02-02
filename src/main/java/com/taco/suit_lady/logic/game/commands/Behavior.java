package com.taco.suit_lady.logic.game.commands;

import com.taco.suit_lady.logic.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

public class Behavior {
    
    private final GameObject owner;
    
    public Behavior(@NotNull GameObject owner) {
        this.owner = owner;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    //</editor-fold>
}
