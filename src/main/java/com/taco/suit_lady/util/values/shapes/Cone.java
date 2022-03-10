package com.taco.suit_lady.util.values.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Bounds;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Cone extends Shape {
    
    private final ReadOnlyDoubleWrapper minAngleProperty;
    private final ReadOnlyDoubleWrapper maxAngleProperty;
    
    private final ReadOnlyDoubleWrapper radiusProperty;
    
    private final ReadOnlyDoubleWrapper xOriginProperty;
    private final ReadOnlyDoubleWrapper yOriginProperty;
    
    {
        this.minAngleProperty = new ReadOnlyDoubleWrapper();
        this.maxAngleProperty = new ReadOnlyDoubleWrapper();
        
        this.radiusProperty = new ReadOnlyDoubleWrapper();
        
        this.xOriginProperty = new ReadOnlyDoubleWrapper();
        this.yOriginProperty = new ReadOnlyDoubleWrapper();
    }
    
    public Cone(@NotNull Springable springable, @Nullable Lock lock,
                @NotNull Number originX, @NotNull Number originY,
                @NotNull Number minAngle, @NotNull Number maxAngle, @NotNull Number radius,
                @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        super(springable, lock, LocType.CENTER, pixelGenerator);
        
        setAngleBounds(Calc.normalizeAngleBounds(minAngle, maxAngle));
        setRadius(radius);
        setOrigin(originX, originY);
    }
    
    public Cone(@NotNull Springable springable, @Nullable Lock lock,
                @NotNull NumExpr2D<?> originPoint,
                @NotNull NumExpr2D<?> angleBounds, @NotNull Number radius,
                @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        this(springable, lock, originPoint.a(), originPoint.b(), angleBounds.a(), angleBounds.b(), radius, pixelGenerator);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public Cone init() {
        return (Cone) super.init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyDoubleProperty readOnlyRadiusProperty() { return radiusProperty.getReadOnlyProperty(); }
    public final double getRadius() { return radiusProperty.get(); }
    public final double setRadius(@NotNull Number newValue) { return Props.setProperty(radiusProperty, newValue); }
    
    public final @NotNull Num2D getAnglePoint(boolean min) { return Calc.interpolateTowards(getOrigin(), getAngle(min), getRadius()); }
    public final @NotNull Num2D getMinAnglePoint() { return getAnglePoint(true); }
    public final @NotNull Num2D getMaxAnglePoint() { return getAnglePoint(false); }
    
    //<editor-fold desc="> Angle Bounds Properties">
    
    public final double getAngle(boolean min) { return min ? getMinAngle() : getMaxAngle(); }
    
    public final @NotNull ReadOnlyDoubleProperty readOnlyMinAngleProperty() { return minAngleProperty.getReadOnlyProperty(); }
    public final double getMinAngle() { return minAngleProperty.get(); }
    public final double setMinAngle(@NotNull Number newValue) {
        final Num2D newBounds = Calc.normalizeAngleBounds(newValue, getMaxAngle());
        minAngleProperty.set(newBounds.bD());
        return Props.setProperty(minAngleProperty, newBounds.a());
    }
    
    public final @NotNull ReadOnlyDoubleProperty readOnlyMaxAngleProperty() { return maxAngleProperty.getReadOnlyProperty(); }
    public final double getMaxAngle() { return maxAngleProperty.get(); }
    public final double setMaxAngle(@NotNull Number newValue) {
        final Num2D newBounds = Calc.normalizeAngleBounds(getMinAngle(), newValue);
        maxAngleProperty.set(newBounds.aD());
        return Props.setProperty(maxAngleProperty, newBounds.b());
    }
    
    
    public final @NotNull Num2D getAngleBounds() { return new Num2D(getMinAngle(), getMaxAngle()); }
    public final @NotNull Num2D setAngleBounds(@NotNull Number newMinAngle, @NotNull Number newMaxAngle) { return new Num2D(setMinAngle(newMinAngle), setMaxAngle(newMaxAngle)); }
    public final @NotNull Num2D setAngleBounds(@NotNull NumExpr2D<?> newValue) { return setAngleBounds(newValue.a(), newValue.b()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Origin Point Properties">
    
    public final @NotNull ReadOnlyDoubleProperty readOnlyOriginPropertyX() { return xOriginProperty.getReadOnlyProperty(); }
    public final double getOriginX() { return xOriginProperty.get(); }
    public final double setOriginX(@NotNull Number newValue) { return Props.setProperty(xOriginProperty, newValue); }
    
    public final @NotNull ReadOnlyDoubleProperty readOnlyOriginPropertyY() { return yOriginProperty.getReadOnlyProperty(); }
    public final double getOriginY() { return yOriginProperty.get(); }
    public final double setOriginY(@NotNull Number newValue) { return Props.setProperty(yOriginProperty, newValue); }
    
    public final @NotNull Num2D getOrigin() { return new Num2D(getOriginX(), getOriginY()); }
    public final @NotNull Num2D setOrigin(@NotNull Number newX, @NotNull Number newY) { return new Num2D(setOriginX(newX), setOriginY(newY)); }
    public final @NotNull Num2D setOrigin(@NotNull NumExpr2D<?> newValue) { return setOrigin(newValue.a(), newValue.b()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull Num2D getPoint(boolean minX, boolean minY) {
        return N.minMaxP(minX, minY, getMinAnglePoint(), getMaxAnglePoint(), getOrigin());
    }
    
    public final @NotNull Bounds getBounds() {
        final Num2D minAnglePoint = getMinAnglePoint();
        final Num2D maxAnglePoint = getMaxAnglePoint();
        final Num2D originPoint = getOrigin();
        
        final Num2D min = N.minP(minAnglePoint, maxAnglePoint, originPoint);
        final Num2D max = N.maxP(minAnglePoint, maxAnglePoint, originPoint);
        
        return Bounds.newInstanceFrom(min, max);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean containsPoint(@NotNull Number x, @NotNull Number y) {
        return Calc.isInCone(getOrigin(), new Num2D(x, y), getRadius(), getMinAngle(), getMaxAngle());
    }
    
    @Override protected @NotNull List<Num2D> regenerateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        throw Exc.nyi();
//        final ArrayList<Num2D> borderPoints = new ArrayList<>();
//        final Num2D originPoint = getOrigin();
//        final Num2D minAnglePoint = getMinAnglePoint();
//        final Num2D maxAnglePoint = getMaxAnglePoint();
//        borderPoints.addAll(Calc.line(originPoint, minAnglePoint).stream().distinct().collect(Collectors.toCollection(ArrayList::new)));
//        borderPoints.addAll(Calc.line(originPoint, minAnglePoint).stream().distinct().collect(Collectors.toCollection(ArrayList::new)));
    }
    
    @Override protected @NotNull Cone copyTo(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        final Cone copy = (Cone) clone();
        if (translate) {
            copy.setOrigin(N.addD(getOriginX(), xMod), N.addD(getOriginY(), yMod));
        } else {
            copy.setOrigin(xMod, yMod);
        }
        return copy;
    }
    
    @Override protected Object clone() { return sync(() -> new Cone(this, getLock(), getOrigin(), getAngleBounds(), getRadius(), getPixelGenerator())); }
    
    //</editor-fold>
}
