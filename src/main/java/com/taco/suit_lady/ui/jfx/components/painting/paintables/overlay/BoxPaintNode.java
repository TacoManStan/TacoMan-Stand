package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class BoxPaintNode extends ShapePaintNode {
    
    private final Rectangle shape;
    
    public BoxPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.shape = new Rectangle();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull BoxPaintNode init() {
        this.shape.fillProperty().bind(fillProperty());
        this.shape.strokeProperty().bind(strokeProperty());
        
        return (BoxPaintNode) super.init();
    }
    
    @Override protected Rectangle refreshNode() { return shape; }
    @Override protected Rectangle syncBounds(@NotNull Node n) {
        shape.setX(xD());
        shape.setY(yD());
        shape.setWidth(wD());
        shape.setHeight(hD());
        return shape;
    }
    
    //</editor-fold>
}
