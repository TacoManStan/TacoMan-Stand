package com.taco.suit_lady.logic.game.execution;

import org.jetbrains.annotations.NotNull;

public interface WrappedTickable<T>
        extends Tickable {
    
    @NotNull AutoManagedTickable<T> tickable();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @Override void tick() {
        tickable().tick();
    }
    
    default @Override long getTickCount() {
        return tickable().getTickCount();
    }
    
    //</editor-fold>
}
