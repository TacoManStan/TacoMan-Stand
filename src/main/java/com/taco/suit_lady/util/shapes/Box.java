package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public class Box extends Shape {
    
    public Box(@NotNull Springable springable,
               @Nullable Lock lock,
               @Nullable LocType locType,
               @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) {
        super(springable, lock, locType, pixelGenerator);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean contains(@NotNull Number x, @NotNull Number y) {
        boolean checkX = x.doubleValue() > getLocation(Axis.X_AXIS, LocType.MIN) && x.doubleValue() < getLocation(Axis.X_AXIS, LocType.MAX);
        boolean checkY = y.doubleValue() > getLocation(Axis.Y_AXIS, LocType.MIN) && y.doubleValue() < getLocation(Axis.Y_AXIS, LocType.MAX);
        return checkX && checkY;
    }
    
    @Override protected @NotNull List<NumberValuePair> generateBorderPoints() {
        final ArrayList<NumberValuePair> borderPoints = new ArrayList<>();
        
        //Corner Points
        borderPoints.add(point(0, 0));
        borderPoints.add(point(0, getHeight() - 1));
        borderPoints.add(point(getWidth() - 1, 0));
        borderPoints.add(point(getWidth() - 1, getHeight() - 1));
        
        for (int i = 1; i < getWidth() - 1; i++) {
            borderPoints.add(point(i, 0));
            borderPoints.add(point(i, getHeight() - 1));
        }
        for (int j = 1; j < getHeight() - 1; j++) {
            borderPoints.add(point(0, j));
            borderPoints.add(point(0, getWidth() - 1));
        }
        
        return borderPoints;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected @NotNull NumberValuePair point(@NotNull Number x, @NotNull Number y) { return getLocation(LocType.MIN).applyEach(x, y); }
    
    //</editor-fold>
}
