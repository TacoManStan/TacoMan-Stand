package com.taco.suit_lady.game.galaxy.events.triggers;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import org.jetbrains.annotations.NotNull;

public abstract class Trigger<T extends TriggerEvent<T>>
        implements WrappedGameComponent {
    
    private final Entity owner;
    
    public Trigger(@NotNull Entity owner) {
        this.owner = owner;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public Entity getOwner() { return owner; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean test(@NotNull T event);
    protected abstract void trigger(@NotNull T event);
    
    protected abstract Class<T> type();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
}
