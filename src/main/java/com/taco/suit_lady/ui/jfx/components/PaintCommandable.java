package com.taco.suit_lady.ui.jfx.components;

/**
 * <p>A {@link FunctionalInterface Functional Interface} that defines a single abstract method: <i>{@link #paint(BoundCanvas)}.</i></p>
 */
// TO-EXPAND
@FunctionalInterface
public interface PaintCommandable
{
    void paint(BoundCanvas canvas);
    
    default void onAdded(BoundCanvas canvas) { }
    
    default void onRemoved(BoundCanvas canvas) { }
}
