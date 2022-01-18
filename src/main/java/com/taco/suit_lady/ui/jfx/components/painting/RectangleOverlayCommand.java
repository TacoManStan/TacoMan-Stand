package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class RectangleOverlayCommand extends ShapeOverlayCommand<Rectangle> {
    
    private final Rectangle rectangle;
    
    public RectangleOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<Rectangle>> autoRemoveCondition, int priority,
            @Nullable Paint fill, @Nullable Paint stroke) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.rectangle = new Rectangle();
        this.rectangle.setFill(fill);
        this.rectangle.setStroke(stroke);
    }
    
    // TODO: Add additional painting properties, probably to the parent intermediary SLShapePaintCommand class
    @Override
    protected Rectangle refreshNode() {
        return rectangle;
    }
    
    @Override
    protected void syncBounds(@NotNull Rectangle rectangle) {
        rectangle.setX(getX());
        rectangle.setY(getY());
//        rectangle.setWidth(getWidthSafe());
//        rectangle.setHeight(getHeightSafe());
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
