package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.util.CardinalDirection;
import com.taco.suit_lady.util.tools.util.NumberValuePair;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CollisionRange extends CollisionArea {
    
    private final ReadOnlyIntegerWrapper radiusProperty;
    
    private final ReadOnlyIntegerWrapper xProperty;
    private final ReadOnlyIntegerWrapper yProperty;
    
    private final ObjectBinding<NumberValuePair> locationBinding;
    
    protected CollisionRange(CollisionMap owner) {
        super(owner);
        
        this.radiusProperty = new ReadOnlyIntegerWrapper();
        
        this.xProperty = new ReadOnlyIntegerWrapper();
        this.yProperty = new ReadOnlyIntegerWrapper();
        
        this.locationBinding = BindingsSL.objBinding(() -> new NumberValuePair(getX(), getY()), xProperty, yProperty);
        
        this.init();
    }
    
    private void init() {
        printer().get(CollisionMap.class).setTitle("CollisionMap");
        printer().get(CollisionMap.class).setEnabled(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Dimensions & Location">
    
    public final ReadOnlyIntegerProperty readOnlyRadiusProperty() { return radiusProperty.getReadOnlyProperty(); }
    public final int getRadius() { return radiusProperty.get(); }
    protected final int setRadius(int newValue) { return PropertiesSL.setProperty(radiusProperty, newValue); }
    
    
    public final ReadOnlyIntegerProperty readOnlyXProperty() { return xProperty.getReadOnlyProperty(); }
    public final int getX() { return xProperty.get(); }
    protected final int setX(int newValue) { return PropertiesSL.setProperty(xProperty, newValue); }
    
    public final ReadOnlyIntegerProperty readOnlyYProperty() { return yProperty.getReadOnlyProperty(); }
    public final int getY() { return yProperty.get(); }
    protected final int setY(int newValue) { return PropertiesSL.setProperty(yProperty, newValue); }
    
    
    public final ObjectBinding<NumberValuePair> locationBinding() { return locationBinding; }
    public final NumberValuePair getLocation() { return locationBinding.get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean collidesWith(CollisionArea other) {
        if (Objects.equals(other.getOwner(), getOwner()))
            return false;
        
        return sync(() -> {
            if (other != null) {
                if (other instanceof CollisionBox otherBox) {
                    for (NumberValuePair point: otherBox.getLocations())
                        if (containsPoint(point))
                            return true;
                    return false;
                } else if (other instanceof CollisionRange otherRange) {
                    return otherRange.getLocation().asPoint().distance(getLocation().asPoint()) < otherRange.getRadius() + getRadius();
                }
                
                throw ExceptionsSL.unsupported("Unknown CollisionArea Implementation: " + other);
            }
            return false;
        });
    }
    
    @Override protected boolean containsPoint(NumberValuePair point) {
        return sync(() -> {
            if (point != null)
                return getLocation().asPoint().distance(point.asPoint()) < getRadius();
            return false;
        });
    }
    
    //</editor-fold>
}
