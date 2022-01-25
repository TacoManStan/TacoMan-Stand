package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class OvalPaintCommand extends ShapePaintCommand {
    
    public OvalPaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    @Override protected void onPaint() {
        Bounds bounds = getBounds();
        if (isValidDimensions())
            ToolsFX.drawOval(getSurface(), bounds, false, isFill());
    }
    
    @Override public @NotNull OvalPaintCommand init() {
        return (OvalPaintCommand) super.init();
    }
}