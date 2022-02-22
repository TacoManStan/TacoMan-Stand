package com.taco.suit_lady.game.ui;

public interface GFXObject {
    
    boolean needsUpdate();
    void update();
    
    //
    
    default void execute() {
        if (needsUpdate())
            update();
    }
}
