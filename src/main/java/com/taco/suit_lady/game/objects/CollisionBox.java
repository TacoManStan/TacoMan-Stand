package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.printer.PrintData;
import com.taco.suit_lady.util.tools.printer.Printer;
import com.taco.suit_lady.util.tools.util.CardinalDirection;
import com.taco.suit_lady.util.tools.util.NumberValuePair;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class CollisionBox extends CollisionArea {
    
    private final ReadOnlyIntegerWrapper widthProperty;
    private final ReadOnlyIntegerWrapper heightProperty;
    
    private final ReadOnlyIntegerWrapper xProperty;
    private final ReadOnlyIntegerWrapper yProperty;
    
    
    private final ObjectBinding<NumberValuePair> dimensionsBinding;
    
    private final IntegerBinding xMaxBinding;
    private final IntegerBinding yMaxBinding;
    
    private final ObjectBinding<NumberValuePair> locationBindingC;
    private final ObjectBinding<NumberValuePair> locationBindingNW;
    private final ObjectBinding<NumberValuePair> locationBindingNE;
    private final ObjectBinding<NumberValuePair> locationBindingSW;
    private final ObjectBinding<NumberValuePair> locationBindingSE;
    
    protected CollisionBox(CollisionMap owner) {
        super(owner);
        
        this.widthProperty = new ReadOnlyIntegerWrapper();
        this.heightProperty = new ReadOnlyIntegerWrapper();
        
        this.xProperty = new ReadOnlyIntegerWrapper();
        this.yProperty = new ReadOnlyIntegerWrapper();
        
        
        this.dimensionsBinding = BindingsSL.objBinding(() -> new NumberValuePair(getWidth(), getHeight()), widthProperty, heightProperty);
        
        this.xMaxBinding = BindingsSL.intBinding(() -> getX() + getWidth(), xProperty, widthProperty);
        this.yMaxBinding = BindingsSL.intBinding(() -> getY() + getHeight(), yProperty, heightProperty);
        
        this.locationBindingC = BindingsSL.objBinding(() -> new NumberValuePair(getX() + (getWidth() / 2D), getY() + (getHeight() / 2D)), xProperty, yProperty, dimensionsBinding);
        this.locationBindingNW = BindingsSL.objBinding(() -> new NumberValuePair(getX(), getY()), xProperty, yProperty);
        this.locationBindingNE = BindingsSL.objBinding(() -> new NumberValuePair(getMaxX(), getY()), xMaxBinding, yProperty);
        this.locationBindingSW = BindingsSL.objBinding(() -> new NumberValuePair(getX(), getMaxY()), xProperty, yMaxBinding);
        this.locationBindingSE = BindingsSL.objBinding(() -> new NumberValuePair(getMaxX(), getMaxY()), xMaxBinding, yMaxBinding);
        
        this.init();
    }
    
    private void init() {
        printer().get(CollisionMap.class).setTitle("CollisionMap");
        printer().get(CollisionMap.class).setEnabled(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Dimensions & Location">
    
    public final ReadOnlyIntegerProperty readOnlyWidthProperty() { return widthProperty.getReadOnlyProperty(); }
    public final int getWidth() { return widthProperty.get(); }
    protected final int setWidth(int newValue) { return PropertiesSL.setProperty(widthProperty, newValue); }
    
    public final ReadOnlyIntegerProperty readOnlyHeightProperty() { return heightProperty.getReadOnlyProperty(); }
    public final int getHeight() { return heightProperty.get(); }
    protected final int setHeight(int newValue) { return PropertiesSL.setProperty(heightProperty, newValue); }
    
    
    public final ReadOnlyIntegerProperty readOnlyXProperty() { return xProperty.getReadOnlyProperty(); }
    public final int getX() { return xProperty.get(); }
    protected final int setX(int newValue) { return PropertiesSL.setProperty(xProperty, newValue); }
    
    public final ReadOnlyIntegerProperty readOnlyYProperty() { return yProperty.getReadOnlyProperty(); }
    public final int getY() { return yProperty.get(); }
    protected final int setY(int newValue) { return PropertiesSL.setProperty(yProperty, newValue); }
    
    //
    
    public final IntegerBinding xMaxBinding() { return xMaxBinding; }
    public final int getMaxX() { return xMaxBinding.get(); }
    
    public final IntegerBinding yMaxBinding() { return yMaxBinding; }
    public final int getMaxY() { return yMaxBinding.get(); }
    
    
    public final ObjectBinding<NumberValuePair> dimensionsBinding() { return dimensionsBinding; }
    public final NumberValuePair getDimensions() { return dimensionsBinding.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBindingC() { return locationBindingC; }
    public final NumberValuePair getLocationC() { return locationBindingC.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBindingNW() { return locationBindingNW; }
    public final NumberValuePair getLocationNW() { return locationBindingNW.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBindingNE() { return locationBindingNE; }
    public final NumberValuePair getLocationNE() { return locationBindingNE.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBindingSW() { return locationBindingSW; }
    public final NumberValuePair getLocationSW() { return locationBindingSW.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBindingSE() { return locationBindingSE; }
    public final NumberValuePair getLocationSE() { return locationBindingSE.get(); }
    
    public final ObjectBinding<NumberValuePair> locationBinding(@NotNull CardinalDirection direction) {
        return switch (direction) {
            case CENTER -> locationBindingC;
            case NORTH_EAST -> locationBindingNE;
            case NORTH_WEST -> locationBindingNW;
            case SOUTH_EAST -> locationBindingSE;
            case SOUTH_WEST -> locationBindingSW;
            
            default -> throw ExceptionsSL.unsupported("Cardinal Direction is Not Supported  (" + direction + ")");
        };
    }
    public final NumberValuePair getLocation(@NotNull CardinalDirection direction) { return locationBinding(direction).get(); }
    
    //
    
    public final @NotNull ObjectBinding<NumberValuePair>[] locationBindings() {
        return sync(() -> ArraysSL.convert(
                this::locationBinding,
                new ObjectBinding[CardinalDirection.valuesMultiDirectionalC().length],
                CardinalDirection.valuesMultiDirectionalC()));
    }
    
    public final @NotNull NumberValuePair[] getLocations() {
        return sync(() -> ArraysSL.convert(
                this::getLocation,
                new NumberValuePair[CardinalDirection.valuesMultiDirectionalC().length],
                CardinalDirection.valuesMultiDirectionalC()));
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean collidesWith(CollisionArea other) {
        if (Objects.equals(other.getOwner(), getOwner()))
            return false;
        
        return sync(() -> {
            if (other != null) {
                if (other instanceof CollisionBox otherBox) {
                    printer().get(CollisionMap.class).print("Checking CollisionBox...");
                    //                    debugger().printList(Arrays.stream(getLocations()).toList(), "HeheXD 1");
                    //                    debugger().printList(Arrays.stream(otherBox.getLocations()).toList(), "HeheXD 2");
                    printer().get(CollisionMap.class).print("X: " + getX());
                    printer().get(CollisionMap.class).print("Y: " + getY());
                    printer().get(CollisionMap.class).print("Max X: " + getMaxX());
                    printer().get(CollisionMap.class).print("Max Y: " + getMaxY());
                    for (NumberValuePair point: otherBox.getLocations())
                        if (containsPoint(point))
                            return true;
                    return false;
                } else if (other instanceof CollisionRange otherRange) {
                    return otherRange.collidesWith(this);
                }
                throw ExceptionsSL.unsupported("Unknown CollisionArea Implementation: " + other);
            }
            return false;
        });
    }
    
    @Override protected boolean containsPoint(NumberValuePair point) {
        return sync(() -> {
            if (point != null)
                return (point.aDouble() > getX() && point.aDouble() < getMaxX())
                       && (point.bDouble() > getY() && point.bDouble() < getMaxY());
            //                return (getX() > point.aDouble() && getMaxX() < point.aDouble())
            //                       && (getY() > point.bDouble() && getMaxY() < point.bDouble());
            return false;
        });
    }
    
    //</editor-fold>
}
