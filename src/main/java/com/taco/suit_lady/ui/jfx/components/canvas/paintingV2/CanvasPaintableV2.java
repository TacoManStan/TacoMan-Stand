package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class CanvasPaintableV2
        implements SpringableWrapper, PaintableV2<CanvasPaintableV2, BoundCanvas> {
    
    private final PaintableDataContainerV2<CanvasPaintableV2, BoundCanvas> data;
    
    public CanvasPaintableV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableDataContainerV2<>(springable, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableDataContainerV2<CanvasPaintableV2, BoundCanvas> data() { return data; }
    
    @Override public void onAdd(BoundCanvas surface) { }
    @Override public void onRemove(BoundCanvas surface) { }
    
    
    @Override public @NotNull CanvasPaintableV2 paint() {
        //TODO
        return this;
    }
    
    //</editor-fold>
}
