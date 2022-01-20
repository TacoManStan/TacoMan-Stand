package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.shape.Ellipse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class EllipseOverlayCommand extends ShapeOverlayCommand<Ellipse> {
    
    private final Ellipse ellipse;
    
    public EllipseOverlayCommand(
            @Nullable ReentrantLock lock, @NotNull Springable springable, @NotNull String name,
            @Nullable Predicate<? super OverlayCommand<Ellipse>> autoRemoveCondition, int priority) {
        super(lock, springable, name, autoRemoveCondition, priority);
        
        this.ellipse = new Ellipse();
        //        this.ellipse.setFill(null);
        //        this.ellipse.setStroke(Color.BLACK);
    }
    
    protected Ellipse refreshNode() {
        return ellipse;
    }
    
    @Override
    protected void syncBounds(@NotNull Ellipse ellipse) {
        //TODO
//        ellipse.setCenterX(getX() + ((double) getWidthSafe() / 2));
//        ellipse.setCenterY(getY() + ((double) getHeightSafe() / 2));
//        ellipse.setRadiusX((double) getWidthSafe() / 2);
//        ellipse.setRadiusY((double) getHeightSafe() / 2);
    }
}
