package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class PaintCommandV2
        implements SpringableWrapper, PaintableV2<PaintCommandV2, BoundCanvas> {
    
    private final PaintableDataContainerV2<PaintCommandV2, BoundCanvas> data;
    
    public PaintCommandV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableDataContainerV2<>(springable, lock, this);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableDataContainerV2<PaintCommandV2, BoundCanvas> data() { return data; }
    
    @Override public void onAdd(BoundCanvas surface) { }
    @Override public void onRemove(BoundCanvas surface) { }
    
    //</editor-fold>
}
