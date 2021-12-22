package com.taco.suit_lady.ui.painting;

import com.taco.suit_lady.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.shape.Ellipse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class SLEllipsePaintCommand extends SLShapePaintCommand<Ellipse> {
    
    private final Ellipse ellipse;
    
    public SLEllipsePaintCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super SLPaintCommand<Ellipse>> autoRemoveCondition, int priority) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.ellipse = new Ellipse();
        //        this.ellipse.setFill(null);
        //        this.ellipse.setStroke(Color.BLACK);
    }
    
    @Override
    protected Ellipse refreshNode() {
        return ellipse;
    }
    
    @Override
    protected void syncBounds(@NotNull Ellipse ellipse, @NotNull Bounds2D newBounds) {
        ellipse.setCenterX(getX() + ((double) getWidthSafe() / 2));
        ellipse.setCenterY(getY() + ((double) getHeightSafe() / 2));
        ellipse.setRadiusX((double) getWidthSafe() / 2);
        ellipse.setRadiusY((double) getHeightSafe() / 2);
    }
    
    @Override
    protected void onAdded(@NotNull Overlay owner) { }
    
    @Override
    protected void onRemoved(@NotNull Overlay owner) { }
}