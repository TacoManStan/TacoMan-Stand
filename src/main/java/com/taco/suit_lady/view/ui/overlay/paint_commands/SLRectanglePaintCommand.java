package com.taco.suit_lady.view.ui.overlay.paint_commands;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.view.ui.overlay.Overlay;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class SLRectanglePaintCommand extends SLShapePaintCommand<Rectangle> {
    
    private final Rectangle rectangle;
    
    public SLRectanglePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<Rectangle>> autoRemoveCondition,
            boolean scaleToParent, int priority) {
        super(lock, springable, name, autoRemoveCondition, scaleToParent, priority, 0, 0, 0, 0);
        
        this.rectangle = new Rectangle();
        this.rectangle.setFill(null);
        this.rectangle.setStroke(Color.BLACK);
    }
    
    // TODO: Add additional painting properties, probably to the parent intermediary SLShapePaintCommand class
    @Override
    protected Rectangle refreshNode() {
        return rectangle;
    }
    
    @Override
    protected void syncBounds(@NotNull Rectangle rectangle, @NotNull Bounds2D newBounds) {
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setWidth(getWidthSafe());
        rectangle.setHeight(getHeightSafe());
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
