package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.interfaces.GameComponent;
import org.jetbrains.annotations.NotNull;

public abstract class OneTimeTask<E extends Tickable<E>> extends GameTask<E> {
    
    public OneTimeTask(@NotNull E owner) {
        super(owner);
    }
    
    public OneTimeTask(@NotNull GameComponent gameComponent, @NotNull E owner) {
        super(gameComponent, owner);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void shutdown() { }
    
    @Override protected boolean isDone() { return getTickCount() >= 1; }
    
    //</editor-fold>
}
