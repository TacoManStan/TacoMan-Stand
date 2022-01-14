package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Bounds2DProperty {
    
    private final IntegerProperty xProperty;
    private final IntegerProperty yProperty;
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private final ObjectBinding<Bounds2D> boundsBinding;
    
    public Bounds2DProperty() {
        this.xProperty = new SimpleIntegerProperty();
        this.yProperty = new SimpleIntegerProperty();
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        this.boundsBinding = Bindings.createObjectBinding(
                () -> new Bounds2D(getX(), getY(), getWidth(), getHeight()),
                xProperty, yProperty, widthProperty, heightProperty);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final IntegerProperty xProperty() {
        return xProperty;
    }
    
    public final int getX() {
        return xProperty.get();
    }
    
    public final int setX(int newValue) {
        return PropertyTools.setProperty(xProperty, newValue);
    }
    
    
    public final IntegerProperty yProperty() {
        return yProperty;
    }
    
    public final int getY() {
        return yProperty.get();
    }
    
    public final int setY(int newValue) {
        return PropertyTools.setProperty(yProperty, newValue);
    }
    
    
    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int setWidth(int newValue) {
        return PropertyTools.setProperty(widthProperty, newValue);
    }
    
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int setHeight(int newValue) {
        return PropertyTools.setProperty(heightProperty, newValue);
    }
    
    
    public final ObjectBinding<Bounds2D> boundsBinding() {
        return boundsBinding;
    }
    
    public final Bounds2D getBounds() {
        return boundsBinding.get();
    }
    
    //</editor-fold>
}
