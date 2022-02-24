package com.taco.suit_lady.game.objects;

import javafx.beans.binding.NumberExpression;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public interface MapObject {
    
    @NotNull NumberExpression xLocationProperty();
    @NotNull NumberExpression yLocationProperty();
    
    //
    
    default @NotNull Number getLocationX() { return xLocationProperty().getValue(); }
    default @NotNull Number getLocationY() { return yLocationProperty().getValue(); }
    default @NotNull Point2D getLocation() { return new Point2D(getLocationX().doubleValue(), getLocationY().doubleValue()); }
    
    default boolean inRange(@NotNull MapObject mapObject, double radius) { return getLocation().distance(mapObject.getLocation()) <= radius; }
}
