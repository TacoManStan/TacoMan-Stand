package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.springable.Springable;
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
            boolean scaleToParent, int priority,
            int x, int y, int width, int height) {
        super(lock, springable, name, autoRemoveCondition, scaleToParent, priority, x, y, width, height);
        
        this.rectangle = new Rectangle();
        this.rectangle.setFill(null);
        this.rectangle.setStroke(Color.BLACK);
    }
    
    // TODO: Add additional painting properties, probably to the parent intermediary SLShapePaintCommand class
    @Override
    protected Rectangle refreshNode() {
        return sync(() -> {
            rectangle.setX(getX());
            rectangle.setY(getY());
            rectangle.setWidth(getWidth());
            rectangle.setHeight(getHeight());
            
            //            rectangle.setManaged(false); // Otherwise, the above x and y coordinates are ignored by the parent StackPane
            
            //            System.out.println("Refreshing node... " + rectangle);
            //            System.out.println("Parent... " + getOwner());
            
            return rectangle;
        });
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}
