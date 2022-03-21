package com.taco.suit_lady.util.values.numbers.shapes;

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
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import javafx.beans.InvalidationListener;
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

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

/**
 * <p>Defines the logic pertaining to all {@link Shape} implementations.</p>
 * <br><hr><br>
 * <p><b>Implementation</b></p>
 * <ol>
 *     <li>
 *         <b>Example {@link Shape} Implementations</b>
 *         <ul>
 *             <li><i>{@link Box}</i></li>
 *             <li><i>{@link Circle}</i></li>
 *             <li><i>{@link Cone}</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>Abstract Methods</b>
 *         <ul>
 *             <li>
 *                 {@link #observables()}
 *                 <ul>
 *                     <li><i>Defines the {@link Observable} values that {@link #regenerateBorderPoints(boolean, Number, Number) Regenerate} this {@link Shape} instance upon {@link Observable#addListener(InvalidationListener) Invalidation}.</i></li>
 *                     <li><i>In most contexts, the default {@link Observable} values that are automatically set by each {@link Shape} instance are sufficient.</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 {@link #regenerateBorderPoints(boolean, Number, Number)}
 *                 <ul>
 *                     <li><i>Defines a {@link List} of {@link Num2D Points} that, when connected sequentially, form the {@code border} of this {@link Shape} instance.</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 {@link #containsPoint(Number, Number)}
 *                 <ul>
 *                     <li><i>Defines the logic for determining if this {@link Shape} implementation contains the {@link Num2D Point} defined by the {@code X} and {@code Y} parameters.</i></li>
 *                     <li><i>{@link #containsPoint(NumExpr2D)} and {@link #containsPoint(Point2D)} methods call {@link #containsPoint(Number, Number)}.</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 {@link #copyTo(boolean, Number, Number)}
 *                 <ul>
 *                     <li><i>Translates the {@link #getLocation() Location} of this {@link Shape} using the {@code X} and {@code Y} parameters passed to the {@link #copyTo(boolean, Number, Number)} method.</i></li>
 *                     <li><i>If the {@code translate} {@link Boolean} parameter is {@code true}, the specified {@code X} and {@code Y} values are {@code added} to the existing {@link #getLocation() Location} of this {@link Shape} object.</i></li>
 *                     <li><i>If the {@code translate} {@link Boolean} parameter is {@code false}, the specified {@code X} and {@code Y} values are {@code set} as the new {@link #getLocation() Location} of this {@link Shape} object.</i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 * <p><b>Logic</b></p>
 * <ol>
 *     <li>
 *         <b>{@link #locationMap() Location Map}</b>
 *         <ul>
 *             <li>Contains a {@link Map} of {@link DoubleBinding DoubleBindings}.</li>
 *             <li>Each {@link DoubleBinding} is {@code mapped} by an {@link Axis} and {@link LocType} defining the {@link #getLocation() Location Value} of that {@link DoubleBinding}.</li>
 *             <li>
 *                 The values contained within the {@link #locationMap() Location Map} are {@code cached}.
 *                 <ul>
 *                     <li>In other words, the {@link #locationMap() Location Map} is {@link Map#isEmpty() Empty} upon {@link Shape} construction.</li>
 *                     <li>When the {@link #locationMap() Location Map} is {@link #locationBinding(Axis, LocType) Accessed}, a new {@link DoubleBinding} reflecting the {@link #getLocation(Axis, LocType) Location} defined by the specified {@link Axis} and {@link LocType} is created.</li>
 *                     <li>Once constructed, the {@link DoubleBinding} is then added to the {@link #locationMap() Location Map} with the {@link Axis} and {@link LocType} parameters as the {@code key}.</li>
 *                     <li>Use <i>{@link #clearCache()}</i> to clear the {@link #locationMap() Location Map Cache}.</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link #taskManager() Task Manager}</b>
 *         <ul>
 *             <li>Every {@link Shape} defines a {@link TaskManager} to handle the {@code Execution Logic} for this {@link Shape} object.</li>
 *             <li>The primary use of the {@link TaskManager} is to execute the {@code JavaFX Graphics Logic} for representing a {@link Shape} visually.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link #readOnlyImageProperty() Shape Image}</b>
 *         <ul>
 *             <li>
 *                 <i>{@link #readOnlyImageEnabledProperty()}</i> contains the {@code boolean} value indicating whether this {@link Shape} should store a <i>{@link #readOnlyImageProperty() Image Representation} of the {@link Shape}.</i>
 *                 <ul>
 *                     <li><i>{@link #readOnlyImageEnabledProperty()}</i> is {@code false} by {@code default}.</li>
 *                     <li>If <i>{@link #readOnlyImageProperty()}</i> is {@code false}, <i>{@link #getImage()}</i> will always return {@code null}.</li>
 *                 </ul>
 *             </li>
 *             <li>The value of <i>{@link #readOnlyImageProperty()}</i> is automatically updated when the {@link Shape} defined by this {@link Shape} instance is changed.</li>
 *             <li>
 *                 The {@link Image} set to <i>{@link #readOnlyImageProperty()}</i> is defined by the {@link #readOnlyPixelGeneratorProperty() Pixel Generator} assigned to this {@link Shape} instance.
 *                 <ul>
 *                     <li>If left undefined, the {@link #readOnlyPixelGeneratorProperty() Pixel Generator} for a {@link Shape} is set to the value of <i>{@link #defaultPixelGenerator()}</i>.</li>
 *                     <li>Use <i>{@link #setPixelGenerator(BiFunction)}</i> to define the {@link #readOnlyPixelGeneratorProperty() Pixel Generator} for this {@link Shape} instance.</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 */
//TO-EXPAND: Examples
public abstract class Shape
        implements SpringableWrapper, Lockable, GFXObject<Shape>, Cloneable {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    private final TaskManager<Shape> taskManager;
    
    //
    
    private final ReadOnlyDoubleWrapper xProperty;
    private final ReadOnlyDoubleWrapper yProperty;
    
    private final ReadOnlyDoubleWrapper widthProperty;
    private final ReadOnlyDoubleWrapper heightProperty;
    
    private final ReadOnlyObjectWrapper<LocType> locTypeProperty;
    
    
    private final ReadOnlyObjectWrapper<BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color>> pixelGeneratorProperty;
    private final ReadOnlyBooleanWrapper imageEnabledProperty;
    
    //
    
    private final ObjectBinding<Num2D> locationBinding;
    private final ObjectBinding<Num2D> dimensionsBinding;
    
    
    private final MapProperty<String, DoubleBinding> locationMap;
    
    //
    
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    
    private boolean needsGfxUpdate;
    
    public Shape(@NotNull Springable springable, @Nullable Lock lock,
                 @Nullable LocType locType,
                 @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        this(springable, lock, 0, 0, 0, 0, locType, pixelGenerator);
    }
    
    public Shape(@NotNull Springable springable, @Nullable Lock lock,
                 @NotNull Number locX, @NotNull Number locY,
                 @NotNull Number dimX, @NotNull Number dimY,
                 @Nullable LocType locType,
                 @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
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
        
        //
        
        setLocation(locX, locY);
        setDimensions(dimX, dimY);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public Shape init() {
        // TODO: Add pixel factory property — w/ both preset implementations & the option to create custom implementations — for generating various types of images to represent the collision data
        
        for (ObjectBinding<Num2D> binding: Arrays.asList(locationBinding, dimensionsBinding))
            binding.addListener((observable, oldValue, newValue) -> {
                if (isImageEnabled())
                    needsGfxUpdate = true;
            });
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final MapProperty<String, DoubleBinding> locationMap() { return locationMap; }
    
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
    
    public double translateLocation(@NotNull Number mod, @NotNull Axis axis) { return sync(() -> setLocation(getLocation(axis) + mod.doubleValue(), axis)); }
    public @NotNull Num2D translateLocation(@NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> new Num2D(translateLocation(xMod, Axis.X_AXIS), translateLocation(yMod, Axis.Y_AXIS)));
    }
    
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
    public @NotNull Num2D getLocation(@NotNull LocType locType) {
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
    
    //
    
    public final @NotNull ObjectBinding<Num2D> locationBinding() { return locationBinding; }
    public final @NotNull Num2D getLocation() { return locationBinding.get(); }
    public final @NotNull Num2D setLocation(@NotNull Number newX, @NotNull Number newY) { return sync(() -> new Num2D(setX(newX), setY(newY))); }
    
    public final @NotNull Num2D setLocation(@NotNull NumExpr2D<?> newValue) { return setLocation(newValue.a(), newValue.b()); }
    public final @NotNull Point2D setLocation(@NotNull Point2D newValue) { return setLocation(newValue.getX(), newValue.getY()).asPoint(); }
    
    
    public final @NotNull ObjectBinding<Num2D> dimensionsBinding() { return dimensionsBinding; }
    public final @NotNull Num2D getDimensions() { return dimensionsBinding.get(); }
    public final @NotNull Num2D setDimensions(@NotNull Number newWidth, @NotNull Number newHeight) { return sync(() -> new Num2D(setWidth(newWidth), setHeight(newHeight))); }
    
    public final @NotNull Num2D setDimensions(@NotNull NumExpr2D<?> newValue) { return setDimensions(newValue.a(), newValue.b()); }
    public final @NotNull Point2D setDimensions(@NotNull Point2D newValue) { return setDimensions(newValue.getX(), newValue.getY()).asPoint(); }
    
    //
    
    //<editor-fold desc="> Border Points">
    
    public final @NotNull List<Num2D> getBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) { return sync(() -> new ArrayList<>(regenerateBorderPoints(translate, xMod, yMod))); }
    public final @NotNull List<Num2D> getBorderPoints(boolean translate, @NotNull Point2D mod) { return getBorderPoints(translate, mod.getX(), mod.getY()); }
    public final @NotNull List<Num2D> getBorderPoints(boolean translate, @NotNull NumExpr2D<?> mod) { return getBorderPoints(translate, mod.asPoint()); }
    
    public final @NotNull List<Num2D> getBorderPoints() { return getBorderPoints(true, 0, 0); }
    
    //
    
    public final @NotNull List<Num2D> getBorderPointsTranslated(@NotNull Number xMod, @NotNull Number yMod) { return getBorderPoints(true, xMod, yMod); }
    public final @NotNull List<Num2D> getBorderPointsTranslated(@NotNull Point2D mod) { return getBorderPointsTranslated(mod.getX(), mod.getY()); }
    public final @NotNull List<Num2D> getBorderPointsTranslated(@NotNull NumExpr2D<?> mod) { return getBorderPointsTranslated(mod.asPoint()); }
    
    public final @NotNull List<Num2D> getBorderPointsMoved(@NotNull Number xMod, @NotNull Number yMod) { return getBorderPoints(true, xMod, yMod); }
    public final @NotNull List<Num2D> getBorderPointsMoved(@NotNull Point2D mod) { return getBorderPointsMoved(mod.getX(), mod.getY()); }
    public final @NotNull List<Num2D> getBorderPointsMoved(@NotNull NumExpr2D<?> mod) { return getBorderPointsMoved(mod.asPoint()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull ReadOnlyObjectProperty<BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color>> readOnlyPixelGeneratorProperty() { return pixelGeneratorProperty.getReadOnlyProperty(); }
    public final @NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> getPixelGenerator() { return pixelGeneratorProperty.get(); }
    protected final @NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> setPixelGenerator(@NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> newValue) {
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
    public final boolean containsPoint(@NotNull NumExpr2D<?> point) { return sync(() -> containsPoint(point.asPoint())); }
    
    public boolean intersects(@NotNull Shape other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            final Shape copy = copyTo(translate, xMod, yMod);
            return copy.getBorderPoints().stream().anyMatch(point -> other.containsPoint(point));
            //            other.getBorderPoints().stream().anyMatch(oPoint -> copy.containsPoint(oPoint));
        });
    }
    
    //
    
    public @NotNull Shape clearCache() {
        return sync(() -> {
            locationMap.clear();
            return this;
        });
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
    
    //
    
    @Override public String toString() {
        return "Shape{" +
               "nativeLoc=" + getLocation() +
               ", minLoc=" + getLocation(LocType.MIN) +
               ", maxLoc=" + getLocation(LocType.MAX) +
               ", centerLoc=" + getLocation(LocType.CENTER) +
               ", dimensions=" + getDimensions() +
               ", locType=" + getLocType() +
               ", imageEnabled=" + isImageEnabled() +
               '}';
    }
    
    @Override protected abstract Object clone() throws CloneNotSupportedException;
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract boolean containsPoint(@NotNull Number x, @NotNull Number y);
    protected abstract @NotNull List<Num2D> regenerateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod);
    protected abstract @NotNull Shape copyTo(boolean translate, @NotNull Number xMod, @NotNull Number yMod);
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
    
    private @NotNull BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> defaultPixelGenerator() { return (imgLoc, loc) -> containsPoint(loc) ? Color.BLACK : Color.TRANSPARENT; }
    
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
