package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.GameMap;
import javafx.beans.binding.NumberExpression;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines implementing {@link Object Objects} as being a member of a {@link GameMap}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@code abstract} <i>{@link #xLocationProperty()}</i> and <i>{@link #yLocationProperty()}</i> methods define the {@code Map Location} of this {@link MapObject} instance.</li>
 *     <li>
 *         {@link MapObject} also offers several convenience methods. Examples include:
 *         <ul>
 *             <li><i>{@link #getLocationX()}</i></li>
 *             <li><i>{@link #getLocationY()}</i></li>
 *             <li><i>{@link #getLocation()}</i></li>
 *             <li><i>{@link #inRange(MapObject, double)}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 */
//TO-EXPAND: Examples
public interface MapObject {
    
    @NotNull NumberExpression xLocationProperty();
    @NotNull NumberExpression yLocationProperty();
    
    //
    
    default @NotNull Number getLocationX() { return xLocationProperty().getValue(); }
    default @NotNull Number getLocationY() { return yLocationProperty().getValue(); }
    default @NotNull Point2D getLocation() { return new Point2D(getLocationX().doubleValue(), getLocationY().doubleValue()); }
    
    default boolean inRange(@NotNull MapObject mapObject, double radius) { return getLocation().distance(mapObject.getLocation()) <= radius; }
}
