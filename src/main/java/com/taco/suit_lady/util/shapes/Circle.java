package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.Exceptions;
import com.taco.suit_lady.util.tools.Maths;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public class Circle extends Shape {
    
    private final ReadOnlyDoubleWrapper diameterProperty;
    private final DoubleBinding radiusBinding;
    
    public Circle(@NotNull Springable springable,
                  @Nullable Lock lock,
                  @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) {
        super(springable, lock, LocType.CENTER, pixelGenerator);
        
        this.diameterProperty = new ReadOnlyDoubleWrapper();
        this.radiusBinding = BindingsSL.doubleBinding(() -> getDiameter() / 2, diameterProperty);
    }
    public Circle(@NotNull Springable springable, @Nullable Lock lock) { this(springable, lock, null); }
    public Circle(@NotNull Springable springable, @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) { this(springable, null, pixelGenerator); }
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
    public final double setDiameter(@NotNull Number newValue) { return PropertiesSL.setProperty(diameterProperty, newValue); }
    
    public final DoubleBinding radiusBinding() { return radiusBinding; }
    public final double getRadius() { return radiusBinding.get(); }
    public final double setRadius(@NotNull Number newValue) { return setDiameter(newValue.doubleValue() * 2); }
    
    public final double getPrecision() { return 9; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    
    @Override public boolean intersects(@NotNull Shape other) {
        if (other instanceof Circle otherCircle) {
            return sync(() -> getLocation(LocType.CENTER).asPoint().distance(other.getLocation(LocType.CENTER).asPoint()) < getRadius() + otherCircle.getRadius());
        }
        return super.intersects(other);
    }
    @Override public boolean contains(@NotNull Number x, @NotNull Number y) {
        return getLocation(LocType.CENTER).asPoint().distance(x.doubleValue(), y.doubleValue()) < getRadius();
    }
    
    @Override protected @NotNull List<NumberValuePair> generateBorderPoints() {
        final ArrayList<NumberValuePair> borderPoints = new ArrayList<>();
        
        final double precision = getPrecision();
        for (int i = 0; i < 360 / precision; i++)
            borderPoints.add(Maths.pointOnCircle(getLocation(LocType.CENTER), getRadius(), i * precision));
        
        return borderPoints;
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
               ", borderPoints=" + getBorderPointsCopy() +
               '}';
    }
    
    
    //</editor-fold>
}
