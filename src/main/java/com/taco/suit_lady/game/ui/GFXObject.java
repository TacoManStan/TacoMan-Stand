package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.logic.Tickable;

public interface GFXObject<E extends Tickable<E>> extends Tickable<E> {
    
    boolean needsGfxUpdate();
    void onGfxUpdate();
    
    //
    
    default void onGfxUpdateAlways() { }
    default void updateGfx() {
        if (needsGfxUpdate())
            onGfxUpdate();
    }
}
