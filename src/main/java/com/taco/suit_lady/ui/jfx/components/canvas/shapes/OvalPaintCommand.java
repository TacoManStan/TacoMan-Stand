package com.taco.suit_lady.ui.jfx.components.canvas.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class OvalPaintCommand extends ShapePaintCommand {
    
    public OvalPaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    @Override
    protected void onPaint() {
        if (isValidDimensions())
            FXTools.drawOval(getOwner(), getBounds(), false, isFill());
    }
}
