package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class BoxOverlayPaintNode extends ShapeOverlayPaintNode {
    
    private final Rectangle shape;
    
    public BoxOverlayPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.shape = new Rectangle();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull OverlayPaintNode init() {
        super.init();
        
        this.shape.fillProperty().bind(fillProperty());
        this.shape.strokeProperty().bind(strokeProperty());
        
        return this;
    }
    
    @Override protected Rectangle refreshNode() { return shape; }
    @Override protected Rectangle syncBounds(@NotNull Node n) {
        shape.setX(x());
        shape.setY(y());
        shape.setWidth(width());
        shape.setHeight(height());
        return shape;
    }
    
    //</editor-fold>
}
