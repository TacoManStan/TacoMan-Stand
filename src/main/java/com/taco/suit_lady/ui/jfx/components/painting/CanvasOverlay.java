package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.components.canvas.CanvasPane;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.scene.canvas.Canvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class CanvasOverlay extends Overlay {
    
    private final CanvasPane canvasPane;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public CanvasOverlay(@NotNull Springable springable) {
        super(springable);
    }
    
    public CanvasOverlay(@NotNull Springable springable, @Nullable String name) {
        super(springable, name);
    }
    
    public CanvasOverlay(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
    }
    
    public CanvasOverlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name) {
        super(springable, lock, name);
    }
    
    public CanvasOverlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name, int paintPriority) {
        super(springable, lock, name, paintPriority);
    }
    
    
    {
        this.canvasPane = new CanvasPane(this);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public CanvasOverlay init() {
        FXTools.bindToParent(canvasPane, root(), true);
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Canvas getCanvas() {
        return canvasPane.canvas();
    }
    
    //</editor-fold>
}
