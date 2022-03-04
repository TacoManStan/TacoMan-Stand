package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.Objs;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoundsBinding
        implements Boundable, Binding<Bounds> {
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    
    private final ObjectBinding<Bounds> boundsBinding;
    
    private final IntegerBinding xSafeBinding;
    private final IntegerBinding ySafeBinding;
    private final IntegerBinding widthSafeBinding;
    private final IntegerBinding heightSafeBinding;
    
    public BoundsBinding(@Nullable ObservableValue<? extends Number> observableX, ObservableValue<? extends Number> observableY,
                         @Nullable ObservableValue<? extends Number> observableWidth, @Nullable ObservableValue<? extends Number> observableHeight,
                         boolean bind) {
        this.xProperty = new SimpleIntegerProperty();
        this.yProperty = new SimpleIntegerProperty();
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        
        this.boundsBinding = Bindings.createObjectBinding(
                () -> new Bounds(x(), y(), width(), height()),
                xProperty, yProperty, widthProperty, heightProperty);
        
        this.xSafeBinding = Bindings.createIntegerBinding(() -> getX(true), xProperty);
        this.ySafeBinding = Bindings.createIntegerBinding(() -> getY(true), yProperty);
        this.widthSafeBinding = Bindings.createIntegerBinding(() -> getWidth(true), widthProperty);
        this.heightSafeBinding = Bindings.createIntegerBinding(() -> getHeight(true), heightProperty);
        
        //
        
        if (bind) {
            Objs.doIfNonNull(() -> observableX, xProperty::bind);
            Objs.doIfNonNull(() -> observableY, yProperty::bind);
            Objs.doIfNonNull(() -> observableWidth, widthProperty::bind);
            Objs.doIfNonNull(() -> observableHeight, heightProperty::bind);
        } else {
            Objs.doIfNonNull(() -> observableX, obs -> setX(obs.getValue()));
            Objs.doIfNonNull(() -> observableY, obs -> setY(obs.getValue()));
            Objs.doIfNonNull(() -> observableWidth, obs -> setWidth(obs.getValue()));
            Objs.doIfNonNull(() -> observableHeight, obs -> setHeight(obs.getValue()));
        }
    }
    public BoundsBinding(@Nullable ObservableValue<? extends Number> observableX, ObservableValue<? extends Number> observableY,
                         @Nullable ObservableValue<? extends Number> observableWidth, @Nullable ObservableValue<? extends Number> observableHeight) {
        this(observableX, observableY, observableWidth, observableHeight, true);
    }
    public BoundsBinding(@NotNull Number x, @NotNull Number y, @NotNull Number width, @NotNull Number height) {
        this(BindingsSL.constDoubleBinding(x), BindingsSL.constDoubleBinding(y), BindingsSL.constDoubleBinding(width), BindingsSL.constDoubleBinding(height), false);
    }
    public BoundsBinding() { this(null, null, null, null, false); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final IntegerProperty xProperty() { return xProperty; }
    @Override public final int x() { return xProperty.get(); }
    public final int setX(@NotNull Number newValue) { return PropertiesSL.setProperty(xProperty, newValue.intValue()); }
    
    public final IntegerProperty yProperty() { return yProperty; }
    @Override public final int y() { return yProperty.get(); }
    public final int setY(@NotNull Number newValue) { return PropertiesSL.setProperty(yProperty, newValue.intValue()); }
    
    public final IntegerProperty widthProperty() { return widthProperty; }
    @Override public final int width() { return widthProperty.get(); }
    public final int setWidth(@NotNull Number newValue) { return PropertiesSL.setProperty(widthProperty, newValue.intValue()); }
    
    public final IntegerProperty heightProperty() { return heightProperty; }
    @Override public final int height() { return heightProperty.get(); }
    public final int setHeight(@NotNull Number newValue) { return PropertiesSL.setProperty(heightProperty, newValue.intValue()); }
    
    //<editor-fold desc="> Bindings">
    
    public final ObjectBinding<Bounds> boundsBinding() { return boundsBinding; }
    @Override public final Bounds getBounds() { return boundsBinding.get(); }
    public final void setBounds(@NotNull Bounds newValue) {
        setX(newValue.x());
        setY(newValue.y());
        setWidth(newValue.width());
        setHeight(newValue.height());
    }
    
    public final IntegerBinding xSafeBinding() { return xSafeBinding; }
    public final IntegerBinding ySafeBinding() { return ySafeBinding; }
    public final IntegerBinding widthSafeBinding() { return widthSafeBinding; }
    public final IntegerBinding heightSafeBinding() { return heightSafeBinding; }
    
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
