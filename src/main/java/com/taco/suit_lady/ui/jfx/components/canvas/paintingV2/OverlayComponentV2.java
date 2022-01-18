package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.painting.Overlay;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class OverlayComponentV2
        implements SpringableWrapper, PaintableV2<OverlayComponentV2, Overlay> {
    
    private final PaintableDataContainerV2<OverlayComponentV2, Overlay> data;
    
    public OverlayComponentV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableDataContainerV2<>(springable, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableDataContainerV2<OverlayComponentV2, Overlay> data() { return data; }
    
    @Override public void onAdd(Overlay surface) { }
    @Override public void onRemove(Overlay surface) { }
    
    //</editor-fold>
}
