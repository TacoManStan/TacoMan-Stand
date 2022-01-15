package com.taco.suit_lady.ui.jfx.components.canvas;

import javafx.scene.Node;

public interface UICommand<N extends Node> {
    
    void paint(N n);
    
    default void onAdd(N n) { }
    default void onRemove(N n) { }
}
