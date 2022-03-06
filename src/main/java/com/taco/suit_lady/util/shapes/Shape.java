package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.fx_tools.FX;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public abstract class Shape
        implements SpringableWrapper, Lockable, GFXObject<Shape> {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    private final TaskManager<Shape> taskManager;
    
    //
    
    private final ReadOnlyDoubleWrapper xProperty;
    private final ReadOnlyDoubleWrapper yProperty;
    
    private final ReadOnlyDoubleWrapper widthProperty;
    private final ReadOnlyDoubleWrapper heightProperty;
    
    private final ReadOnlyObjectWrapper<LocType> locTypeProperty;
    
    
    private final ReadOnlyObjectWrapper<BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color>> pixelGeneratorProperty;
    private final ReadOnlyBooleanWrapper imageEnabledProperty;
    
    //
    
    private final ObjectBinding<NumberValuePair> locationBinding;
    private final ObjectBinding<NumberValuePair> dimensionsBinding;
    
    
    private final MapProperty<String, DoubleBinding> locationMap;
    
    //
    
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    
    private boolean needsGfxUpdate;
    private boolean needsBorderPointsUpdate;
    
    public Shape(@NotNull Springable springable, @Nullable Lock lock, @Nullable LocType locType, @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) {
        this.lock = lock != null ? lock : new ReentrantLock();
        this.springable = springable.asStrict();
        
        this.taskManager = new TaskManager<>(this);
        
        //
        
        this.xProperty = new ReadOnlyDoubleWrapper();
        this.yProperty = new ReadOnlyDoubleWrapper();
        
        this.widthProperty = new ReadOnlyDoubleWrapper();
        this.heightProperty = new ReadOnlyDoubleWrapper();
        
        this.locTypeProperty = new ReadOnlyObjectWrapper<>(locType != null ? locType : LocType.MIN);
        
        
        this.pixelGeneratorProperty = new ReadOnlyObjectWrapper<>(pixelGenerator);
        this.imageEnabledProperty = new ReadOnlyBooleanWrapper(false);
        
        //
        
        this.locationBinding = Bind.numPairBinding(xProperty, yProperty);
        this.dimensionsBinding = Bind.numPairBinding(widthProperty, heightProperty);
        
        this.locationMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        
        //
        
        this.imageProperty = new ReadOnlyObjectWrapper<>();
        
        this.needsGfxUpdate = false;
        this.needsBorderPointsUpdate = false;
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public Shape init() {
        // TODO: Add pixel factory property — w/ both preset implementations & the option to create custom implementations — for generating various types of images to represent the collision data
        
        for (ObjectBinding<NumberValuePair> binding: Arrays.asList(locationBinding, dimensionsBinding))
            binding.addListener((observable, oldValue, newValue) -> {
                if (isImageEnabled())
                    needsGfxUpdate = true;
            });
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Location & Dimensions">
    
    public final ReadOnlyDoubleProperty readOnlyXProperty() { return xProperty.getReadOnlyProperty(); }
    public final double getX() { return xProperty.get(); }
    public final double setX(@NotNull Number newValue) { return Props.setProperty(xProperty, newValue); }
    
    public final ReadOnlyDoubleProperty readOnlyYProperty() { return yProperty.getReadOnlyProperty(); }
    public final double getY() { return yProperty.get(); }
    public final double setY(@NotNull Number newValue) { return Props.setProperty(yProperty, newValue); }
    
    
    protected final ReadOnlyDoubleWrapper widthProperty() { return widthProperty; }
    public final ReadOnlyDoubleProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final double getWidth() { return widthProperty.get(); }
    public final double setWidth(@NotNull Number newValue) { return Props.setProperty(widthProperty, newValue); }
    
    protected final ReadOnlyDoubleWrapper heightProperty() { return heightProperty; }
    public final ReadOnlyDoubleProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final double getHeight() { return heightProperty.get(); }
    public final double setHeight(@NotNull Number newValue) { return Props.setProperty(heightProperty, newValue); }
    
    //
    
    public final ReadOnlyObjectProperty<LocType> readOnlyLocTypeProperty() { return locTypeProperty.getReadOnlyProperty(); }
    public final LocType getLocType() { return locTypeProperty.get(); }
    public final LocType setLocType(@NotNull LocType newValue) { return Props.setProperty(locTypeProperty, newValue); }
    
    
    protected ReadOnlyDoubleWrapper locationProperty(@NotNull Axis axis) {
        return switch (axis) {
            case X_AXIS -> xProperty;
            case Y_AXIS -> yProperty;
            
            default -> throw Exc.unsupported("Cannot find location property matching axis: " + axis);
        };
    }
    public ReadOnlyDoubleProperty readOnlyLocationProperty(@NotNull Axis axis) { return locationProperty(axis).getReadOnlyProperty(); }
    public double getLocation(@NotNull Axis axis) { return locationProperty(axis).get(); }
    public double setLocation(@NotNull Number newValue, @NotNull Axis axis) { return Props.setProperty(locationProperty(axis), newValue); }
    
    protected ReadOnlyDoubleWrapper dimensionProperty(@NotNull Axis axis) {
        return sync(() -> switch (axis) {
            case X_AXIS -> widthProperty;
            case Y_AXIS -> heightProperty;
            
            default -> throw Exc.unsupported("Cannot find dimension property matching axis: " + axis);
        });
    }
    public ReadOnlyDoubleProperty readOnlyDimensionProperty(@NotNull Axis axis) { return dimensionProperty(axis).getReadOnlyProperty(); }
    public double getDimension(@NotNull Axis axis) { return dimensionProperty(axis).get(); }
    public double setDimension(@NotNull Number newValue, @NotNull Axis axis) { return Props.setProperty(dimensionProperty(axis), newValue); }
    
    //<editor-fold desc="> Bindings">
    
    public final DoubleBinding locationBinding(@NotNull Axis axis, @NotNull LocType locType) {
        return sync(() -> {
            final String key = locKey(axis, locType);
            
            DoubleBinding binding = locationMap.get(key);
            if (binding == null) {
                binding = Bind.doubleBinding(
                        () -> getLocation(axis, locType),
                        locationProperty(axis), dimensionProperty(axis));
                locationMap.put(key, binding);
            }
            
            return binding;
        });
    }
    
    public double getLocation(@NotNull Axis axis, @NotNull LocType locType) {
        return LocType.translate(getLock(), isNullableLock(), getLocation(axis), getDimension(axis), getLocType(), locType);
    }
    public @NotNull NumberValuePair getLocation(@NotNull LocType locType) {
        return LocType.translate(getLock(), isNullableLock(), getLocation(), getDimensions(), getLocType(), locType);
    }
    
    public double setLocation(@NotNull Number newValue, @NotNull Axis axis, @NotNull LocType locType) {
        printer().get().err("WARNING: Untested Method");
        return sync(() -> {
            final double newLoc = LocType.translate(newValue, getDimension(axis), locType, getLocType());
            printer().get().print("Setting New Location: " + newLoc);
            return setLocation(newLoc, axis);
        });
    }
    
    public double getLocationLegacy(@NotNull Axis axis, @NotNull LocType locType) {
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
    
    //
    
    public final @NotNull ObjectBinding<NumberValuePair> locationBinding() { return locationBinding; }
    public final @NotNull NumberValuePair getLocation() { return locationBinding.get(); }
    public final @NotNull NumberValuePair setLocation(@NotNull Number newX, @NotNull Number newY) { return sync(() -> new NumberValuePair(setX(newX), setY(newY))); }
    
    public final @NotNull NumberValuePair setLocation(@NotNull NumberValuePairable<?> newValue) { return setLocation(newValue.a(), newValue.b()); }
    public final @NotNull Point2D setLocation(@NotNull Point2D newValue) { return setLocation(newValue.getX(), newValue.getY()).asPoint(); }
    
    
    public final @NotNull ObjectBinding<NumberValuePair> dimensionsBinding() { return dimensionsBinding; }
    public final @NotNull NumberValuePair getDimensions() { return dimensionsBinding.get(); }
    public final @NotNull NumberValuePair setDimensions(@NotNull Number newWidth, @NotNull Number newHeight) { return sync(() -> new NumberValuePair(setWidth(newWidth), setHeight(newHeight))); }
    
    public final @NotNull NumberValuePair setDimensions(@NotNull NumberValuePair newValue) { return setDimensions(newValue.a(), newValue.b()); }
    public final @NotNull Point2D setDimensions(@NotNull Point2D newValue) { return setDimensions(newValue.getX(), newValue.getY()).asPoint(); }
    
    //
    
    //<editor-fold desc="> Border Points">
    
    public final @NotNull List<NumberValuePair> getBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) { return sync(() -> new ArrayList<>(regenerateBorderPoints(translate, xMod, yMod))); }
    public final @NotNull List<NumberValuePair> getBorderPoints(boolean translate, @NotNull Point2D mod) { return getBorderPoints(translate, mod.getX(), mod.getY()); }
    public final @NotNull List<NumberValuePair> getBorderPoints(boolean translate, @NotNull NumberValuePairable<?> mod) { return getBorderPoints(translate, mod.asPoint()); }
    
    public final @NotNull List<NumberValuePair> getBorderPoints() { return getBorderPoints(true, 0, 0); }
    
    //
    
    public final @NotNull List<NumberValuePair> getBorderPointsTranslated(@NotNull Number xMod, @NotNull Number yMod) { return getBorderPoints(true, xMod, yMod); }
    public final @NotNull List<NumberValuePair> getBorderPointsTranslated(@NotNull Point2D mod) { return getBorderPointsTranslated(mod.getX(), mod.getY()); }
    public final @NotNull List<NumberValuePair> getBorderPointsTranslated(@NotNull NumberValuePairable<?> mod) { return getBorderPointsTranslated(mod.asPoint()); }
    
    public final @NotNull List<NumberValuePair> getBorderPointsMoved(@NotNull Number xMod, @NotNull Number yMod) { return getBorderPoints(true, xMod, yMod); }
    public final @NotNull List<NumberValuePair> getBorderPointsMoved(@NotNull Point2D mod) { return getBorderPointsMoved(mod.getX(), mod.getY()); }
    public final @NotNull List<NumberValuePair> getBorderPointsMoved(@NotNull NumberValuePairable<?> mod) { return getBorderPointsMoved(mod.asPoint()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull ReadOnlyObjectProperty<BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color>> readOnlyPixelGeneratorProperty() { return pixelGeneratorProperty.getReadOnlyProperty(); }
    public final @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> getPixelGenerator() { return pixelGeneratorProperty.get(); }
    protected final @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> setPixelGenerator(@NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> newValue) {
        return Props.setProperty(pixelGeneratorProperty, newValue);
    }
    
    public final @NotNull ReadOnlyBooleanProperty readOnlyImageEnabledProperty() { return imageEnabledProperty.getReadOnlyProperty(); }
    public final boolean isImageEnabled() { return imageEnabledProperty.get(); }
    public final boolean setImageEnabled(boolean newValue) { return Props.setProperty(imageEnabledProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<Image> readOnlyImageProperty() { return imageProperty.getReadOnlyProperty(); }
    public final @NotNull Image getImage() { return imageProperty.get(); }
    private @Nullable Image setImage(@NotNull Image newValue) { return Props.setProperty(imageProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean containsPoint(@NotNull Point2D point) { return sync(() -> containsPoint(point.getX(), point.getY())); }
    public final boolean containsPoint(@NotNull NumberValuePairable<?> point) { return sync(() -> containsPoint(point.asPoint())); }
    
    public boolean intersects(@NotNull Shape other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> { return getBorderPoints(translate, xMod, yMod).stream().anyMatch(other::containsPoint); });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean needsGfxUpdate() { return needsGfxUpdate; }
    
    @Override public void onGfxUpdate() {
        //TODO: Sync?
        sync(() -> {
            setImage(regenerateImage());
            needsGfxUpdate = false;
        });
    }
    
    //
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @Nullable Lock getLock() { return lock; }
    
    @Override public @NotNull TaskManager<Shape> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract boolean containsPoint(@NotNull Number x, @NotNull Number y);
    protected abstract @NotNull List<NumberValuePair> regenerateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod);
    //    protected abstract boolean intersects(@NotNull Shape other);
    
    protected @NotNull List<Observable> observables() { return Collections.emptyList(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private RuntimeException unsupportedLocType(@Nullable LocType input) {
        final String prefix = "Cannot find location binding matching Shape LocType [" + getLocType() + "]";
        final String suffix = input != null ? " and input LocType [" + input + "]" : "";
        return Exc.unsupported(prefix + suffix);
    }
    
    private @NotNull String locKey(@NotNull Axis axis, @NotNull LocType locType) {
        return (axis.name().replace("_", "") + "-" + locType.name().replace("_", "")).toLowerCase();
    }
    
    //
    
    protected @NotNull Image regenerateImage() {
        return sync(() -> FX.generateImage(
                getLock(),
                getLocation(LocType.MIN),
                getDimensions(),
                getPixelGenerator()));
    }
    
    private @NotNull BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> defaultPixelGenerator() { return (imgLoc, loc) -> containsPoint(loc) ? Color.BLACK : Color.TRANSPARENT; }
    
    private @NotNull Observable @NotNull [] getObservables(@NotNull Observable... excluded) {
        return sync(() -> {
            final ArrayList<Observable> observables = new ArrayList<>(observables());
            
            observables.addAll(Arrays.asList(locationBinding(), dimensionsBinding()));
            observables.removeAll(Arrays.asList(excluded));
            
            return observables.toArray(new Observable[0]);
        });
    }
    
    //</editor-fold>
}
