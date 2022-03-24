package com.taco.tacository.ui.jfx.components.painting.paintables.canvas;

import com.taco.tacository.util.values.numbers.Bounds;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.fx_tools.FX;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class BoxPaintCommand extends ShapePaintCommand {
    
    public BoxPaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    @Override protected void onPaint() {
        Bounds bounds = getBounds();
        if (isValidDimensions())
            FX.drawRectangle(getSurface(), bounds, false, isFill());
    }
    
    @Override public @NotNull BoxPaintCommand init() {
        return (BoxPaintCommand) super.init();
    }
}
