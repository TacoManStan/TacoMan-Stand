package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class CanvasPaintableV2
        implements SpringableWrapper, PaintableV2<CanvasPaintableV2, BoundCanvas> {
    
    private final PaintableDataContainerV2<CanvasPaintableV2, BoundCanvas> data;
    
    public CanvasPaintableV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableDataContainerV2<>(springable, lock, this);
    }
    
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onPaint();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableDataContainerV2<CanvasPaintableV2, BoundCanvas> data() { return data; }
    
    @Override public void onAdd(BoundCanvas surface) { }
    @Override public void onRemove(BoundCanvas surface) { }
    
    
    @Override public @NotNull CanvasPaintableV2 paint() {
        if (!isDisabled())
            FXTools.runFX(() -> sync(() -> {
                Predicate<BoundCanvas> autoRemoveCondition = getAutoRemoveCondition();
                if (autoRemoveCondition != null && autoRemoveCondition.test(getSurface()))
                    getSurface().removePaintableV2(this);
                else
                    onPaint();
            }), true);
        return this;
    }
    
    //</editor-fold>
}
