package com.taco.suit_lady.util.values.numbers.bindings;

import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Bounds;
import com.taco.suit_lady.util.values.numbers.expressions.BoundsExpr;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoundsBinding
        implements BoundsExpr, Binding<Bounds> {
    
    private final DoubleProperty xProperty;
    private final DoubleProperty yProperty;
    private final DoubleProperty widthProperty;
    private final DoubleProperty heightProperty;
    
    private final ObjectProperty<LocType> locTypeProperty;
    
    private final ObjectBinding<Bounds> boundsBinding;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public BoundsBinding(@Nullable ObservableValue<? extends Number> observableX, ObservableValue<? extends Number> observableY,
                         @Nullable ObservableValue<? extends Number> observableWidth, @Nullable ObservableValue<? extends Number> observableHeight,
                         @Nullable ObservableValue<LocType> observableLocType, boolean bind) {
        this.xProperty = new SimpleDoubleProperty();
        this.yProperty = new SimpleDoubleProperty();
        this.widthProperty = new SimpleDoubleProperty();
        this.heightProperty = new SimpleDoubleProperty();
        
        this.locTypeProperty = new SimpleObjectProperty<>();
        
        //
        
        if (bind) {
            Obj.doIfNonNull(() -> observableX, xProperty::bind);
            Obj.doIfNonNull(() -> observableY, yProperty::bind);
            Obj.doIfNonNull(() -> observableWidth, widthProperty::bind);
            Obj.doIfNonNull(() -> observableHeight, heightProperty::bind);
            
            Obj.doIfNonNull(() -> observableLocType, locTypeProperty::bind, obs -> locTypeProperty.set(Enu.get(LocType.class)));
        } else {
            Obj.doIfNonNull(() -> observableX, obs -> setX(obs.getValue()), obs -> setX(1));
            Obj.doIfNonNull(() -> observableY, obs -> setY(obs.getValue()), obs -> setY(1));
            Obj.doIfNonNull(() -> observableWidth, obs -> setWidth(obs.getValue()), obs -> setWidth(1));
            Obj.doIfNonNull(() -> observableHeight, obs -> setHeight(obs.getValue()), obs -> setHeight(1));
            
            Obj.doIfNonNull(() -> observableLocType, obs -> setLocType(obs.getValue()), obs -> locTypeProperty.set(Enu.get(LocType.class)));
        }
        
        //
    
        this.boundsBinding = Bindings.createObjectBinding(() -> new Bounds(x(), y(), w(), h(), locType()), xProperty, yProperty, widthProperty, heightProperty, locTypeProperty);
    }
    
    //
    
    public BoundsBinding(@Nullable ObservableValue<? extends Number> observableX, ObservableValue<? extends Number> observableY,
                         @Nullable ObservableValue<? extends Number> observableWidth, @Nullable ObservableValue<? extends Number> observableHeight,
                         @Nullable ObservableValue<LocType> observableLocType) {
        this(observableX, observableY, observableWidth, observableHeight, observableLocType, true);
    }
    public BoundsBinding(@Nullable ObservableValue<? extends Number> observableX, ObservableValue<? extends Number> observableY,
                         @Nullable ObservableValue<? extends Number> observableWidth, @Nullable ObservableValue<? extends Number> observableHeight) {
        this(observableX, observableY, observableWidth, observableHeight, null, true);
    }
    
    public BoundsBinding(@NotNull Number x, @NotNull Number y, @NotNull Number width, @NotNull Number height, @NotNull LocType locType) {
        this(Bind.constDoubleBinding(x), Bind.constDoubleBinding(y),
             Bind.constDoubleBinding(width), Bind.constDoubleBinding(height),
             Bind.constObjBinding(locType), false);
    }
    public BoundsBinding(@NotNull Number x, @NotNull Number y, @NotNull Number width, @NotNull Number height) {
        this(x, y, width, height, null);
    }
    
    public BoundsBinding() {
        this(null, null, null, null, null, false);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final DoubleProperty xProperty() { return xProperty; }
    @Override public final @NotNull Number x() { return xProperty.get(); }
    public final double setX(@NotNull Number newValue) { return Props.setProperty(xProperty, newValue.doubleValue()); }
    
    public final DoubleProperty yProperty() { return yProperty; }
    @Override public final @NotNull Number y() { return yProperty.get(); }
    public final double setY(@NotNull Number newValue) { return Props.setProperty(yProperty, newValue.doubleValue()); }
    
    public final DoubleProperty widthProperty() { return widthProperty; }
    @Override public final @NotNull Number w() { return widthProperty.get(); }
    public final double setWidth(@NotNull Number newValue) { return Props.setProperty(widthProperty, newValue.doubleValue()); }
    
    public final DoubleProperty heightProperty() { return heightProperty; }
    @Override public final @NotNull Number h() { return heightProperty.get(); }
    public final double setHeight(@NotNull Number newValue) { return Props.setProperty(heightProperty, newValue.doubleValue()); }
    
    
    public final @NotNull ObjectProperty<LocType> locTypeProperty() { return locTypeProperty; }
    @Override public final @NotNull LocType locType() { return locTypeProperty.get(); }
    public final @NotNull LocType setLocType(@NotNull LocType newValue) { return Props.setProperty(locTypeProperty, newValue); }
    
    //<editor-fold desc="> Bindings">
    
    public final ObjectBinding<Bounds> boundsBinding() { return boundsBinding; }
    @Override public final @NotNull Bounds getBounds() { return boundsBinding.get(); }
    public final void setBounds(@NotNull Bounds newValue) {
        setX(newValue.x());
        setY(newValue.y());
        setWidth(newValue.w());
        setHeight(newValue.h());
        
        setLocType(newValue.locType());
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Binding">
    
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#getValue() getValue()}</i></p>
     */
    @Override public final Bounds getValue() { return boundsBinding.getValue(); }
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#getDependencies() getDependencies()}</i></p>
     */
    @Override public final ObservableList<?> getDependencies() { return boundsBinding.getDependencies(); }
    
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#dispose() dispose()}</i></p>
     */
    @Override public final void dispose() { boundsBinding.dispose(); }
    
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#isValid() isValid()}</i></p>
     */
    @Override public final boolean isValid() { return boundsBinding.isValid(); }
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#invalidate() invalidate()}</i></p>
     */
    @Override public final void invalidate() { boundsBinding.invalidate(); }
    
    
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#addListener(ChangeListener) addListener}<b>(</b>{@link ChangeListener}<b>)</b></i></p>
     */
    @Override public final void addListener(ChangeListener<? super Bounds> listener) { boundsBinding.addListener(listener); }
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#removeListener(ChangeListener) removeListener}<b>(</b>{@link ChangeListener}<b>)</b></i></p>
     */
    @Override public final void removeListener(ChangeListener<? super Bounds> listener) { boundsBinding.removeListener(listener); }
    
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#addListener(InvalidationListener) addListener}<b>(</b>{@link InvalidationListener}<b>)</b></i></p>
     */
    @Override public final void addListener(InvalidationListener listener) { boundsBinding.addListener(listener); }
    /**
     * <p><b>Passthrough Definition:</b> <i>{@link #boundsBinding()}<b>.</b>{@link Binding#removeListener(InvalidationListener) removeListener}<b>(</b>{@link InvalidationListener}<b>)</b></i></p>
     */
    @Override public final void removeListener(InvalidationListener listener) { boundsBinding.removeListener(listener); }
    
    //</editor-fold>
    
    //</editor-fold>
}
