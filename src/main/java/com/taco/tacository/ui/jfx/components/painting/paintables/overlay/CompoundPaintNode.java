package com.taco.tacository.ui.jfx.components.painting.paintables.overlay;

import com.taco.tacository.ui.jfx.components.painting.paintables.canvas.PaintCommand;
import com.taco.tacository.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.tacository.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.tacository.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An instance of {@link PaintNode} that wraps a pre-defined group of {@link PaintCommand PaintCommands}.</p>
 */
public class CompoundPaintNode extends PaintNode {
    
    public final CanvasPane canvasPane;
    
    public CompoundPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.canvasPane = new CanvasPane(springable);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final CanvasSurface canvas() { return canvasPane.canvas(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CompoundPaintNode init() {
        return (CompoundPaintNode) super.init();
    }
    @Override protected CanvasPane refreshNode() { return canvasPane; }
    
    //</editor-fold>
}
