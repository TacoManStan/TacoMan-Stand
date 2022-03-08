package com.taco.suit_lady.util.values.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Circle extends Shape {
    
    private final ReadOnlyDoubleWrapper diameterProperty;
    private final DoubleBinding radiusBinding;
    
    public Circle(@NotNull Springable springable,
                  @Nullable Lock lock,
                  @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        super(springable, lock, LocType.CENTER, pixelGenerator);
        
        this.diameterProperty = new ReadOnlyDoubleWrapper();
        this.radiusBinding = Bind.doubleBinding(() -> getDiameter() / 2, diameterProperty);
    }
    public Circle(@NotNull Springable springable, @Nullable Lock lock) { this(springable, lock, null); }
    public Circle(@NotNull Springable springable, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) { this(springable, null, pixelGenerator); }
    public Circle(@NotNull Springable springable) { this(springable, null, null); }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public Circle init() {
        widthProperty().bindBidirectional(diameterProperty);
        heightProperty().bindBidirectional(diameterProperty);
        
        return (Circle) super.init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyDoubleProperty readOnlyDiameterProperty() { return diameterProperty.getReadOnlyProperty(); }
    public final double getDiameter() { return diameterProperty.get(); }
    public final double setDiameter(@NotNull Number newValue) { return Props.setProperty(diameterProperty, newValue); }
    
    public final DoubleBinding radiusBinding() { return radiusBinding; }
    public final double getRadius() { return radiusBinding.get(); }
    public final double setRadius(@NotNull Number newValue) { return setDiameter(newValue.doubleValue() * 2); }
    
    public final int getPrecision() { return 9; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
        @Override public boolean intersects(@NotNull Shape other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
            return sync(() -> {
                final Circle circleImpl = generateCircle(translate, xMod, yMod);
                final Number xImpl = translate ? xMod : 0;
                final Number yImpl = translate ? yMod : 0;
                return sync(() -> {
                    final Point2D center = circleImpl.getLocation(LocType.CENTER).applyEach(xImpl, yImpl).asPoint();
                    if (other instanceof Circle otherCircle)
                        return center.distance(other.getLocation(LocType.CENTER).asPoint()) < getRadius() + otherCircle.getRadius();
                    else if (other instanceof Box otherBox)
                        return other.getBorderPoints(true, 0, 0).stream().anyMatch(otherBorderPoint -> center.distance(otherBorderPoint.asPoint()) < getRadius());
                    else
                        return super.intersects(other, translate, xMod, yMod);
                });
            });
        }
    
    @Override public boolean containsPoint(@NotNull Number x, @NotNull Number y) {
        return sync(() -> getLocation(LocType.CENTER).asPoint().distance(x.doubleValue(), y.doubleValue()) < getRadius());
    }
    
    @Override protected @NotNull List<Num2D> regenerateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> Calc.formCircle(
                (translate
                 ? getLocation(LocType.CENTER)
                 : new Num2D(xMod.doubleValue() + getRadius(), yMod.doubleValue() + getRadius())),
                getRadius(), getPrecision()));
    }
    
    @Override protected @NotNull Shape copyTo(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            final Circle copy = new Circle(this, getLock());
            copy.setLocType(getLocType());
            if (translate) {
                copy.setLocation(getLocation());
                copy.translateLocation(xMod, yMod);
            } else {
                copy.setLocation(xMod, yMod);
            }
            copy.setRadius(getRadius());
            return copy;
        });
    }
    
    @Override protected @NotNull Image regenerateImage() {
        return syncRequireFX(() -> {
            final Canvas canvas = new Canvas(getWidth(), getHeight());
            canvas.getGraphicsContext2D().setFill(Color.BLACK);
            canvas.getGraphicsContext2D().fillOval(getLocation(Axis.X_AXIS, LocType.MIN), getLocation(Axis.Y_AXIS, LocType.MIN), getWidth(), getHeight());
            return canvas.snapshot(new SnapshotParameters(), null);
        });
    }
    
    //
    
    @Override public String toString() {
        return "Circle{" +
               "diameter=" + (int) getDiameter() +
               ", radius=" + (int) getRadius() +
               ", location=" + getLocation().getString(true) +
               ", locationCenter=" + getLocation(LocType.CENTER).getString(true) +
               ", locationMin=" + getLocation(LocType.MIN).getString(true) +
               ", locationMax=" + getLocation(LocType.MAX).getString(true) +
               ", dimensions=" + getDimensions().getString(true) +
               ", borderPoints=" + getBorderPoints() +
               '}';
    }
    
    
    //</editor-fold>
    
    private Circle generateCircle(boolean translate, @NotNull Number x, @NotNull Number y) {
        return sync(() -> {
            if (translate)
                return this;
            else {
                final Circle circleImpl = new Circle(this);
                circleImpl.setLocation(x, y);
                circleImpl.setDiameter(getDiameter());
                return circleImpl;
            }
        });
    }
}
