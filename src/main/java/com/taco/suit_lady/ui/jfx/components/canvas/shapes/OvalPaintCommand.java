package com.taco.suit_lady.ui.jfx.components.canvas.shapes;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class OvalPaintCommand extends ShapePaintCommand {
    
    public OvalPaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    @Override
    protected void onPaint(BoundCanvas canvas) {
    
    }
}
