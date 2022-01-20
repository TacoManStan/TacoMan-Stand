package com.taco.suit_lady.ui.jfx.components.canvas.painting;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class BoxOverlayPainter extends OverlayPainter {
    
    private final Rectangle shape;
    
    public BoxOverlayPainter(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.shape = new Rectangle();
    }
    @Override protected Node refreshNode() {
        return null;
    }
    
    @Override protected Node syncBounds(@NotNull Node n) {
        shape.setX(getX());
        shape.setY(getY());
        shape.setWidth(getWidth());
        shape.setHeight(getHeight());
        
        return shape;
    }
}
