package com.taco.suit_lady.view.ui.jfx.components.paint_commands;

import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;

@FunctionalInterface
public interface PaintCommandable
{
    void paint(BoundCanvas canvas);
}
