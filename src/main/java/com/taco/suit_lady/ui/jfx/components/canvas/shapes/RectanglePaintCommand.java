package com.taco.suit_lady.ui.jfx.components.canvas.shapes;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.ui.jfx.components.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.util.Bounds;
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
    protected void onPaint() {
        Bounds bounds = getBounds();
        if (isValidDimensions())
            FXTools.drawRectangle(getOwner(), bounds, false, isFill());
    }
    @Override public void onAdd(BoundCanvas owner) {
    
    }
    @Override public void onRemove(BoundCanvas owner) {
    
    }
    @Override public int compareTo(@NotNull PaintCommand o) {
        return 0;
    }
    
    //</editor-fold>
}
