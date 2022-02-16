package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import org.jetbrains.annotations.NotNull;

public class OneTimeTask<E extends Entity> extends GameTask<E> {
    
    private final Runnable action;
    
    public OneTimeTask(@NotNull E owner, @NotNull Runnable action) {
        super(owner);
        this.action = action;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void tick() { action.run(); }
    @Override protected void shutdown() { }
    
    @Override protected boolean isDone() { return getTickCount() >= 1; }
    
    //</editor-fold>
}
