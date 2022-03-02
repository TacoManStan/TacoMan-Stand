package com.taco.suit_lady.util.tools.util.shapes;

import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.util.values.NumberValuePair;
import com.taco.suit_lady.util.tools.util.values.NumberValuePairable;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Shape
        implements SpringableWrapper, Lockable {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    private final ReadOnlyDoubleWrapper xCenterProperty;
    private final ReadOnlyDoubleWrapper yCenterProperty;
    
    private final ReadOnlyDoubleWrapper widthProperty;
    private final ReadOnlyDoubleWrapper heightProperty;
    
    
    private final ObjectBinding<NumberValuePair> locationBinding;
    private final ObjectBinding<NumberValuePair> dimensionsBinding;
    
    private ObjectBinding<Image> imageBinding;
    
    public Shape(@NotNull Springable springable, @Nullable Lock lock, @NotNull Number xCenter, @NotNull Number yCenter, @NotNull Number width, @NotNull Number height) {
        this.lock = lock != null ? lock : new ReentrantLock();
        this.springable = springable.asStrict();
        
        this.xCenterProperty = new ReadOnlyDoubleWrapper(xCenter.doubleValue());
        this.yCenterProperty = new ReadOnlyDoubleWrapper(yCenter.doubleValue());
        
        this.widthProperty = new ReadOnlyDoubleWrapper(width.doubleValue());
        this.heightProperty = new ReadOnlyDoubleWrapper(height.doubleValue());
        
        
        this.locationBinding = BindingsSL.numPairBinding(xCenterProperty, yCenterProperty);
        this.dimensionsBinding = BindingsSL.numPairBinding(widthProperty, heightProperty);
    }
    
    public Shape init() {
        imageBinding = BindingsSL.objBinding(() -> , locationBinding, dimensionsBinding);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyDoubleProperty readOnlyCenterPropertyX() { return xCenterProperty.getReadOnlyProperty(); }
    public final double getCenterX() { return xCenterProperty.get(); }
    protected final double setCenterX(@NotNull Number newValue) { return PropertiesSL.setProperty(xCenterProperty, newValue); }
    
    public final ReadOnlyDoubleProperty readOnlyCenterPropertyY() { return yCenterProperty.getReadOnlyProperty(); }
    public final double getCenterY() { return yCenterProperty.get(); }
    protected final double setCenterY(@NotNull Number newValue) { return PropertiesSL.setProperty(yCenterProperty, newValue); }
    
    
    public final ReadOnlyDoubleProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final double getWidth() { return widthProperty.get(); }
    protected final double setWidth(@NotNull Number newValue) { return PropertiesSL.setProperty(widthProperty, newValue); }
    
    public final ReadOnlyDoubleProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final double getHeight() { return heightProperty.get(); }
    protected final double setHeight(@NotNull Number newValue) { return PropertiesSL.setProperty(heightProperty, newValue); }
    
    //
    
    public final ObjectBinding<NumberValuePair> locationBinding() { return locationBinding; }
    public final NumberValuePair getLocation() { return locationBinding.get(); }
    protected final NumberValuePair setLocation(@NotNull NumberValuePairable<?> newValue) { return setLocation(newValue.a(), newValue.b()); }
    protected final NumberValuePair setLocation(@NotNull Number newX, @NotNull Number newY) {
        final NumberValuePair oldValue = getLocation();
        setCenterX(newX);
        setCenterY(newY);
        return oldValue;
    }
    
    public final ObjectBinding<NumberValuePair> dimensionsBinding() { return dimensionsBinding; }
    public final NumberValuePair getDimensions() { return dimensionsBinding.get(); }
    protected final NumberValuePair setDimensions(@NotNull NumberValuePair newValue) { return setDimensions(newValue.a(), newValue.b()); }
    protected final NumberValuePair setDimensions(@NotNull Number newWidth, @NotNull Number newHeight) {
        final NumberValuePair oldValue = getDimensions();
        setWidth(newWidth);
        setHeight(newHeight);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean isPointInShape(@NotNull Point2D point, int pixelX, int pixelY);
    protected abstract @NotNull List<Observable> observables();
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private @NotNull Image regenerateImage() {
        return sync(() -> {
        
        });
    }
    
    //</editor-fold>
}
