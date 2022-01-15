package com.taco.suit_lady.ui.jfx.components.canvas.shapes;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class RectanglePaintCommand extends ShapePaintCommand {
    
    public RectanglePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void onPaint(BoundCanvas canvas) {
        Bounds2D bounds = getBounds();
        if (isValidDimensions())
            FXTools.drawRectangle(canvas, bounds, false, isFill());
    }
    
    //</editor-fold>
}
