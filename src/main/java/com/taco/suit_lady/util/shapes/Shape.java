package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.Exceptions;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public abstract class Shape
        implements SpringableWrapper, Lockable, GFXObject<Shape> {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    //
    
    private final ReadOnlyDoubleWrapper xProperty;
    private final ReadOnlyDoubleWrapper yProperty;
    
    private final ReadOnlyDoubleWrapper widthProperty;
    private final ReadOnlyDoubleWrapper heightProperty;
    
    private final ReadOnlyObjectWrapper<LocType> locTypeProperty;
    
    
    private final ReadOnlyObjectWrapper<BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color>> pixelGeneratorProperty;
    
    //
    
    private final ObjectBinding<NumberValuePair> locationBinding;
    private final ObjectBinding<NumberValuePair> dimensionsBinding;
    
    
    private final MapProperty<String, DoubleBinding> locationMap;
    
    //
    
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    private boolean needsUpdate;
    
    public Shape(@NotNull Springable springable, @Nullable Lock lock, @Nullable LocType locType, @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) {
        this.lock = lock != null ? lock : new ReentrantLock();
        this.springable = springable.asStrict();
        
        //
        
        this.xProperty = new ReadOnlyDoubleWrapper();
        this.yProperty = new ReadOnlyDoubleWrapper();
        
        this.widthProperty = new ReadOnlyDoubleWrapper();
        this.heightProperty = new ReadOnlyDoubleWrapper();
        
        this.locTypeProperty = new ReadOnlyObjectWrapper<>(locType != null ? locType : LocType.MIN);
        
        
        this.pixelGeneratorProperty = new ReadOnlyObjectWrapper<>(pixelGenerator);
        
        //
        
        this.locationBinding = BindingsSL.numPairBinding(xProperty, yProperty);
        this.dimensionsBinding = BindingsSL.numPairBinding(widthProperty, heightProperty);
        
        this.locationMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        
        //
        
        this.imageProperty = new ReadOnlyObjectWrapper<>();
        this.needsUpdate = false;
    }
    
    public Shape init() {
        // TODO: Add pixel factory property — w/ both preset implementations & the option to create custom implementations — for generating various types of images to represent the collision data
        
        locationBinding.addListener((observable, oldValue, newValue) -> needsUpdate = true);
        dimensionsBinding.addListener((observable, oldValue, newValue) -> needsUpdate = true);
        
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Location & Dimensions">
    
    public final ReadOnlyDoubleProperty readOnlyXProperty() { return xProperty.getReadOnlyProperty(); }
    public final double getX() { return xProperty.get(); }
    protected final double setX(@NotNull Number newValue) { return PropertiesSL.setProperty(xProperty, newValue); }
    
    public final ReadOnlyDoubleProperty readOnlyYProperty() { return yProperty.getReadOnlyProperty(); }
    public final double getY() { return yProperty.get(); }
    protected final double setY(@NotNull Number newValue) { return PropertiesSL.setProperty(yProperty, newValue); }
    
    
    public final ReadOnlyDoubleProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final double getWidth() { return widthProperty.get(); }
    protected final double setWidth(@NotNull Number newValue) { return PropertiesSL.setProperty(widthProperty, newValue); }
    
    public final ReadOnlyDoubleProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final double getHeight() { return heightProperty.get(); }
    protected final double setHeight(@NotNull Number newValue) { return PropertiesSL.setProperty(heightProperty, newValue); }
    
    //
    
    public final ReadOnlyObjectProperty<LocType> readOnlyLocTypeProperty() { return locTypeProperty.getReadOnlyProperty(); }
    public final LocType getLocType() { return locTypeProperty.get(); }
    protected final LocType setLocType(@NotNull LocType newValue) { return PropertiesSL.setProperty(locTypeProperty, newValue); }
    
    
    protected ReadOnlyDoubleWrapper locationProperty(@NotNull Axis axis) {
        return switch (axis) {
            case X_AXIS -> xProperty;
            case Y_AXIS -> yProperty;
            
            default -> throw Exceptions.unsupported("Cannot find location property matching axis: " + axis);
        };
    }
    protected ReadOnlyDoubleProperty readOnlyLocationProperty(@NotNull Axis axis) { return locationProperty(axis).getReadOnlyProperty(); }
    protected double getLocation(@NotNull Axis axis) { return locationProperty(axis).get(); }
    protected double setLocation(@NotNull Number newValue, @NotNull Axis axis) { return PropertiesSL.setProperty(locationProperty(axis), newValue); }
    
    protected ReadOnlyDoubleWrapper dimensionProperty(@NotNull Axis axis) {
        return sync(() -> switch (axis) {
            case X_AXIS -> widthProperty;
            case Y_AXIS -> heightProperty;
            
            default -> throw Exceptions.unsupported("Cannot find dimension property matching axis: " + axis);
        });
    }
    protected ReadOnlyDoubleProperty readOnlyDimensionProperty(@NotNull Axis axis) { return dimensionProperty(axis).getReadOnlyProperty(); }
    protected double getDimension(@NotNull Axis axis) { return dimensionProperty(axis).get(); }
    protected double setDimension(@NotNull Number newValue, @NotNull Axis axis) { return PropertiesSL.setProperty(dimensionProperty(axis), newValue); }
    
    //<editor-fold desc="> Bindings">
    
    public final DoubleBinding locationBinding(@NotNull Axis axis, @NotNull LocType locType) {
        return sync(() -> {
            final String key = locKey(axis, locType);
            
            DoubleBinding binding = locationMap.get(key);
            if (binding == null) {
                binding = BindingsSL.doubleBinding(
                        () -> getLocation(axis, locType),
                        locationProperty(axis), dimensionProperty(axis));
                locationMap.put(key, binding);
            }
            
            return binding;
        });
    }
    public double getLocation(@NotNull Axis axis, @NotNull LocType locType) {
        return sync(() -> {
            final double loc = getLocation(axis);
            final double dim = getDimension(axis);
            
            return switch (getLocType()) {
                case MIN -> switch (locType) {
                    case MIN -> loc;
                    case MAX -> loc + dim;
                    case CENTER -> loc + (dim / 2);
                    
                    default -> throw unsupportedLocType(locType);
                };
                
                case MAX -> switch (locType) {
                    case MIN -> loc - dim;
                    case MAX -> loc;
                    case CENTER -> loc - (dim / 2);
                    
                    default -> throw unsupportedLocType(locType);
                };
                
                case CENTER -> switch (locType) {
                    case MIN -> loc - (dim / 2);
                    case MAX -> loc + (dim / 2);
                    case CENTER -> loc;
                    
                    default -> throw unsupportedLocType(locType);
                };
                
                default -> throw unsupportedLocType(null);
            };
        });
    }
    public @NotNull NumberValuePair getLocation(@NotNull LocType locType) {
        return new NumberValuePair(getLocation(Axis.X_AXIS, locType), getLocation(Axis.Y_AXIS, locType));
    }
    
    //
    
    public final ObjectBinding<NumberValuePair> locationBinding() { return locationBinding; }
    public final NumberValuePair getLocation() { return locationBinding.get(); }
    protected final NumberValuePair setLocation(@NotNull NumberValuePairable<?> newValue) { return setLocation(newValue.a(), newValue.b()); }
    protected final NumberValuePair setLocation(@NotNull Number newX, @NotNull Number newY) {
        final NumberValuePair oldValue = getLocation();
        setX(newX);
        setY(newY);
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
    
    //</editor-fold>
    
    public final @NotNull ReadOnlyObjectProperty<BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color>> readOnlyPixelGeneratorProperty() { return pixelGeneratorProperty.getReadOnlyProperty(); }
    public final @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> getPixelGenerator() { return pixelGeneratorProperty.get(); }
    protected final @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> setPixelGenerator(@NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> newValue) {
        return PropertiesSL.setProperty(pixelGeneratorProperty, newValue);
    }
    
    
    public final @NotNull ReadOnlyObjectProperty<Image> readOnlyImageProperty() { return imageProperty.getReadOnlyProperty(); }
    public final @NotNull Image getImage() { return imageProperty.get(); }
    private @Nullable Image setImage(@NotNull Image newValue) { return PropertiesSL.setProperty(imageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    protected final boolean contains(@NotNull Point2D point) { return contains(point.getX(), point.getY()); }
    protected final boolean contains(@NotNull NumberValuePairable<?> point) { return contains(point.asPoint()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    @Override public boolean needsGfxUpdate() {
        return needsUpdate;
    }
    
    @Override public void onGfxUpdate() {
        //TODO: Sync?
        setImage(regenerateImage());
        needsUpdate = false;
    }
    
    
    //
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean contains(@NotNull Number x, @NotNull Number y);
    protected abstract boolean intersects(@NotNull Shape other);
    
    protected abstract @NotNull List<Observable> observables();
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private RuntimeException unsupportedLocType(@Nullable LocType input) {
        final String prefix = "Cannot find location binding matching Shape LocType [" + getLocType() + "]";
        final String suffix = input != null ? " and input LocType [" + input + "]" : "";
        return Exceptions.unsupported(prefix + suffix);
    }
    
    private @NotNull String locKey(@NotNull Axis axis, @NotNull LocType locType) {
        return (axis.name().replace("_", "") + "-" + locType.name().replace("_", "")).toLowerCase();
    }
    
    //
    
    private @NotNull Image regenerateImage() {
        return sync(() -> ToolsFX.generateImage(
                getLock(),
                getLocation(LocType.MIN),
                getDimensions(),
                getPixelGenerator()));
    }
    
    private @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> defaultPixelGenerator() { return (imgLoc, loc) -> contains(loc) ? Color.BLACK : Color.TRANSPARENT; }
    
    //</editor-fold>
}
