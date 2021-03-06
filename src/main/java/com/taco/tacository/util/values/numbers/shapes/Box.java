package com.taco.tacository.util.values.numbers.shapes;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.values.enums.Axis;
import com.taco.tacository.util.values.enums.LocType;
import com.taco.tacository.util.values.numbers.Bounds;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public class Box extends Shape {
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public Box(@NotNull Springable springable, @Nullable Lock lock,
               @NotNull Number locX, @NotNull Number locY,
               @NotNull Number dimX, @NotNull Number dimY,
               @Nullable LocType locType,
               @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        super(springable, lock, locX, locY, dimX, dimY, locType, pixelGenerator);
    }
    
    public Box(@NotNull Springable springable, @Nullable Lock lock,
               @NotNull NumExpr2D<?> loc, @NotNull NumExpr2D<?> dims,
               @Nullable LocType locType,
               @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        this(springable, lock, loc.a(), loc.b(), dims.a(), dims.b(), locType, pixelGenerator);
    }
    
    public Box(@NotNull Springable springable, @Nullable Lock lock, @Nullable LocType locType, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        this(springable, lock, 0, 0, 0, 0, locType, pixelGenerator);
    }
    public Box(@NotNull Springable springable, @Nullable Lock lock, @Nullable LocType locType) { this(springable, lock, locType, null); }
    public Box(@NotNull Springable springable, @Nullable Lock lock, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) { this(springable, lock, null, pixelGenerator); }
    public Box(@NotNull Springable springable, @Nullable Lock lock) { this(springable, lock, null, null); }
    public Box(@NotNull Springable springable) { this(springable, null, null, null); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public Box init() { return (Box) super.init(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean containsPoint(@NotNull Number x, @NotNull Number y) {
        boolean checkX = x.doubleValue() > getLocation(Axis.X_AXIS, LocType.MIN) && x.doubleValue() < getLocation(Axis.X_AXIS, LocType.MAX);
        boolean checkY = y.doubleValue() > getLocation(Axis.Y_AXIS, LocType.MIN) && y.doubleValue() < getLocation(Axis.Y_AXIS, LocType.MAX);
        return checkX && checkY;
    }
    
    @Override protected @NotNull List<Num2D> regenerateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        final double xModD = xMod.doubleValue();
        final double yModD = yMod.doubleValue();
        
        final ArrayList<Num2D> borderPoints = new ArrayList<>();
        
        //Corner Points
        borderPoints.add(point(translate, 0, 0));
        borderPoints.add(point(translate, 0, getHeight() - 1));
        borderPoints.add(point(translate, getWidth() - 1, 0));
        borderPoints.add(point(translate, getWidth() - 1, getHeight() - 1));
        
        for (int i = 1; i < getWidth() - 1; i++) {
            borderPoints.add(point(translate, i, 0));
            borderPoints.add(point(translate, i, getHeight() - 1));
        }
        for (int j = 1; j < getHeight() - 1; j++) {
            borderPoints.add(point(translate, 0, j));
            borderPoints.add(point(translate, 0, getWidth() - 1));
        }
        
        return borderPoints;
    }
    @Override protected @NotNull Box copyTo(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            final Box copy = new Box(this, getLock());
            copy.setLocType(getLocType());
            if (translate) {
                copy.setLocation(getLocation());
                copy.translateLocation(xMod, yMod);
            } else {
                copy.setLocation(xMod, yMod);
            }
            copy.setDimensions(getDimensions());
            return copy;
        });
    }
    @Override protected Object clone() {
        return sync(() -> new Box(this, getLock(), getLocation(LocType.MIN), getDimensions(), LocType.MIN, getPixelGenerator()));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected @NotNull Num2D point(boolean translate, @NotNull Number x, @NotNull Number y) {
        if (translate)
            return getLocation(LocType.MIN).applyEach(x, y).asNum2D();
        return new Num2D(x, y);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    public static @NotNull Box newInstance(@NotNull Springable springable, @Nullable Lock lock, @NotNull Bounds bounds, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        return new Box(springable, lock, bounds.getLocation(), bounds.getDimensions(), bounds.locType(), pixelGenerator);
    }
    public static @NotNull Box newInstance(@NotNull Springable springable, @Nullable Lock lock, @NotNull Bounds bounds) { return newInstance(springable, lock, bounds, null); }
    
    //</editor-fold>
}
