package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.Paintable;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.PaintableData;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class PaintCommand
        implements SpringableWrapper, Paintable<PaintCommand, CanvasSurface> {
    
    private final PaintableData<PaintCommand, CanvasSurface> data;
    
    public PaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableData<>(springable, lock, this);
    }
    
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onPaint();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableData<PaintCommand, CanvasSurface> data() { return data; }
    
    @Override public void onAdd(CanvasSurface surface) { }
    @Override public void onRemove(CanvasSurface surface) { }
    
    @Override public @NotNull PaintCommand paint() {
        if (isActive())
            ToolsFX.runFX(() -> sync(() -> {
                Predicate<CanvasSurface> autoRemoveCondition = getAutoRemoveCondition();
                if (autoRemoveCondition != null && autoRemoveCondition.test(getSurface()))
                    getSurface().removePaintableV2(this);
                else
                    onPaint();
            }), true);
        return this;
    }
    
    //</editor-fold>
}
