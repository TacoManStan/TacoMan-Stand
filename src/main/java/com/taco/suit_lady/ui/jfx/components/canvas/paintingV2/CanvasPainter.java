package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.canvas.CanvasSurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class CanvasPainter
        implements SpringableWrapper, PaintableV2<CanvasPainter, CanvasSurface> {
    
    private final PaintableDataContainerV2<CanvasPainter, CanvasSurface> data;
    
    public CanvasPainter(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableDataContainerV2<>(springable, lock, this);
    }
    
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onPaint();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableDataContainerV2<CanvasPainter, CanvasSurface> data() { return data; }
    
    @Override public void onAdd(CanvasSurface surface) { }
    @Override public void onRemove(CanvasSurface surface) { }
    
    
    @Override public @NotNull CanvasPainter paint() {
        if (!isDisabled())
            FXTools.runFX(() -> sync(() -> {
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
