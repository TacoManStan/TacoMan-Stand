package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

public class BoundsBinding
        implements Boundable, Binding<Bounds> {
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Bounds> boundsBinding;
    
    public BoundsBinding() {
        this.xProperty = new SimpleIntegerProperty();
        this.yProperty = new SimpleIntegerProperty();
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        this.boundsBinding = Bindings.createObjectBinding(
                () -> new Bounds(getX(), getY(), getWidth(), getHeight()),
                xProperty, yProperty, widthProperty, heightProperty);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final IntegerProperty xProperty() {
        return xProperty;
    }
    
    @Override
    public final int getX() {
        return xProperty.get();
    }
    
    public final int setX(int newValue) {
        return PropertyTools.setProperty(xProperty, newValue);
    }
    
    
    public final IntegerProperty yProperty() {
        return yProperty;
    }
    
    @Override
    public final int getY() {
        return yProperty.get();
    }
    
    public final int setY(int newValue) {
        return PropertyTools.setProperty(yProperty, newValue);
    }
    
    
    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    @Override
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int setWidth(int newValue) {
        return PropertyTools.setProperty(widthProperty, newValue);
    }
    
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    @Override
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int setHeight(int newValue) {
        return PropertyTools.setProperty(heightProperty, newValue);
    }
    
    
    public final ObjectBinding<Bounds> boundsBinding() {
        return boundsBinding;
    }
    
    @Override
    public final Bounds getBounds() {
        return boundsBinding.get();
    }
    
    public final void setBounds(@NotNull Bounds newValue) {
        setX(newValue.getX());
        setY(newValue.getY());
        setWidth(newValue.getWidth());
        setHeight(newValue.getHeight());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final boolean isValid() {
        return boundsBinding.isValid();
    }
    
    @Override
    public final void invalidate() {
        boundsBinding.invalidate();
    }
    
    @Override
    public final ObservableList<?> getDependencies() {
        return boundsBinding.getDependencies();
    }
    
    @Override
    public final void dispose() {
        boundsBinding.dispose();
    }
    
    @Override
    public final void addListener(ChangeListener<? super Bounds> listener) {
        boundsBinding.addListener(listener);
    }
    
    @Override
    public final void removeListener(ChangeListener<? super Bounds> listener) {
        boundsBinding.removeListener(listener);
    }
    
    @Override
    public final Bounds getValue() {
        return boundsBinding.getValue();
    }
    
    @Override
    public final void addListener(InvalidationListener listener) {
        boundsBinding.addListener(listener);
    }
    
    @Override
    public final void removeListener(InvalidationListener listener) {
        boundsBinding.removeListener(listener);
    }
    
    //</editor-fold>
}
