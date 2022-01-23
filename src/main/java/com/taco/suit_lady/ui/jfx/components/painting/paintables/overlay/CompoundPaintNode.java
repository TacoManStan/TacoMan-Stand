package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.suit_lady.util.springable.Springable;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An instance of {@link PaintNode} that wraps a pre-defined group of {@link PaintCommand PaintCommands}.</p>
 */
public class CompoundPaintNode extends PaintNode {
    
    public final CanvasPane canvasPane;
    public final ReadOnlyListWrapper<PaintCommand> paintCommands;
    
    public CompoundPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.canvasPane = new CanvasPane(springable);
        this.paintCommands = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyListProperty<PaintCommand> paintCommands() { return paintCommands.getReadOnlyProperty(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CompoundPaintNode init() {
        return (CompoundPaintNode) super.init();
    }
    
    @Override protected CanvasPane refreshNode() { return canvasPane; }
    
    //</editor-fold>
}
