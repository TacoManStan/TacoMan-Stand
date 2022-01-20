package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class OvalCanvasPaintableV2 extends CanvasShapePaintableV2 {
    
    public OvalCanvasPaintableV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    @Override protected void onPaint() {
        Bounds bounds = getBounds();
        if (isValidDimensions())
            FXTools.drawOval(getSurface(), bounds, false, isFill());
    }
    
    @Override public @NotNull OvalCanvasPaintableV2 init() {
        return (OvalCanvasPaintableV2) super.init();
    }
}
