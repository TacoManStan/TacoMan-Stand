package com.taco.suit_lady.game.galaxy.abilities;

import com.taco.suit_lady.game.interfaces.GameObjectComponent;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class Ability
        implements GameObjectComponent {
    
    private final GameObject owner;
    
    public Ability(@NotNull GameObject owner) {
        this.owner = owner;
    }
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract void use(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    public final @NotNull GameObject getOwner() { return owner; }
    
    //</editor-fold>
}
