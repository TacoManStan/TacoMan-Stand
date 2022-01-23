package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.SLProperties;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;

public class DimensionsBinding
        implements Dimensionable, Binding<Dimensions> {
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Dimensions> dimensionsBinding;
    
    public DimensionsBinding() {
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        this.dimensionsBinding = Bindings.createObjectBinding(
                () -> new Dimensions(getWidth(), getHeight()),
                widthProperty, heightProperty);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final IntegerProperty widthProperty() { return widthProperty; }
    public final int setWidth(int newValue) { return SLProperties.setProperty(widthProperty, newValue); }
    @Override public int getWidth() { return widthProperty.get(); }
    
    public final IntegerProperty heightProperty() { return heightProperty; }
    public final int setHeight(int newValue) { return SLProperties.setProperty(heightProperty, newValue); }
    @Override public int getHeight() { return heightProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Dimensions getValue() { return dimensionsBinding.getValue(); }
    @Override public ObservableList<?> getDependencies() { return dimensionsBinding.getDependencies(); }
    @Override public void dispose() { dimensionsBinding.dispose(); }
    
    @Override public boolean isValid() { return dimensionsBinding.isValid(); }
    @Override public void invalidate() { dimensionsBinding.invalidate(); }
    
    @Override public void addListener(ChangeListener<? super Dimensions> listener) { dimensionsBinding.addListener(listener); }
    @Override public void removeListener(ChangeListener<? super Dimensions> listener) { dimensionsBinding.removeListener(listener); }
    @Override public void addListener(InvalidationListener listener) { dimensionsBinding.addListener(listener); }
    @Override public void removeListener(InvalidationListener listener) { dimensionsBinding.removeListener(listener); }
    
    //</editor-fold>
}
