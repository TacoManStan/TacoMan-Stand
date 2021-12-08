package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;

@FunctionalInterface
public interface PaintCommandable
{
    void paint(BoundCanvas canvas);
    
    default void onAdded(BoundCanvas canvas) { }
    
    default void onRemoved(BoundCanvas canvas) { }
}
