package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.enums.LocType;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <p>Defines an {@code immutable}, {@code default} implementation of {@link BoundsExpr}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>All {@link Bounds} objects are {@link Serializable}.</li>
 *     <li>
 *         {@link Bounds} overrides <i>{@link Object#equals(Object)}</i> such that two {@link Bounds} instances are {@code equal} if the {@code values} of the following {@code members} for each {@link Bounds} object are {@link #equals(Object) equal}:
 *         <ul>
 *             <li><i>{@link #x()}</i></li>
 *             <li><i>{@link #y()}</i></li>
 *
 *             <li><i>{@link #w()}</i></li>
 *             <li><i>{@link #h()}</i></li>
 *
 *             <li><i>{@link #locType()}</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link Bounds} is {@link Cloneable}, overriding <i>{@link Object#clone()}</i> such that <i>{@link #clone() Bounds.clone()}</i> returns a {@code new} {@link Bounds} instance with {@code members} equal to the {@code values} of the {@link Bounds} instance being {@link #clone() cloned}.
 *         <ul>
 *             <li><i>The {@link #clone() cloned} {@code members} are the same as the {@code values} listed above for {@link #equals(Object) Equality Checks}.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * <hr><br>
 * <p><b>Static Factory Methods</b></p>
 * <p><i>
 *     A new {@link Bounds} instance can be constructed either using the {@link Bounds} {@link Bounds#Bounds(Number, Number, Number, Number, LocType) Constructor}<br>
 *     or any of the following {@link #newInstance() Factory Methods}:
 * </i></p>
 * <ol>
 *     <li>
 *         <b>New Instance Factory Methods</b>
 *         <ul>
 *             <li><i>{@link #newInstance(Num2D, Num2D, LocType)}</i></li>
 *             <li><i>{@link #newInstance(BoundsExpr, LocType)}</i></li>
 *             <li><i>{@link #newInstance(Number, Number, Number, Number)}</i></li>
 *             <li><i>{@link #newInstance(BoundsExpr)}</i></li>
 *             <li><i>{@link #newInstance(LocType)}</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>New Instance From Factory Methods</b>
 *         <ul>
 *             <li><i>{@link #newInstanceFrom(Number, Number, Number, Number, LocType)}</i></li>
 *             <li><i>{@link #newInstanceFrom(NumExpr2D, NumExpr2D, LocType)}</i></li>
 *             <li><i>{@link #newInstanceFrom(Point2D, Point2D, LocType)}</i></li>
 *
 *             <li><i>{@link #newInstanceFrom(Number, Number, Number, Number)}</i></li>
 *             <li><i>{@link #newInstanceFrom(NumExpr2D, NumExpr2D)}</i></li>
 *             <li><i>{@link #newInstanceFrom(Point2D, Point2D)}</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>Specialty Factory Methods</b>
 *         <ul>
 *             <li><i>{@link #boundsMin(LocType)}</i></li>
 *             <li><i>{@link #boundsMin()}</i></li>
 *
 *             <li><i>{@link #boundsMax(LocType)}</i></li>
 *             <li><i>{@link #boundsMax()}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param x       The {@link #x() X} value of the {@link #getLocation() Location Coordinates} of this {@link Bounds} instance.
 * @param y       The {@link #y() Y} value of the {@link #getLocation() Location Coordinates} of this {@link Bounds} instance.
 * @param w       The {@link #w() Width} value of the {@link #getDimensions() Dimensions} of this {@link Bounds} instance.
 * @param h       The {@link #h() Height} value of the {@link #getDimensions() Dimensions} of this {@link Bounds} instance.
 * @param locType The {@link LocType} of the {@link #getLocation() Location} of this {@link Bounds} instance.
 */
public record Bounds(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h, @NotNull LocType locType)
        implements BoundsExpr, Cloneable, Serializable {
    
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Foundational">
    
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bounds bounds)) return false;
        
        if (!x.equals(bounds.x)) return false;
        if (!y.equals(bounds.y)) return false;
        if (!w.equals(bounds.w)) return false;
        if (!h.equals(bounds.h)) return false;
        
        return locType == bounds.locType;
    }
    
    @Override public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + w.hashCode();
        result = 31 * result + h.hashCode();
        result = 31 * result + locType.hashCode();
        return result;
    }
    
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Contract(" -> new")
    @Override public @NotNull Object clone() { return new Bounds(x(), y(), w(), h(), locType()); }
    
    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return "Bounds{" +
               "x=" + x +
               ", y=" + y +
               ", w=" + w +
               ", h=" + h +
               ", locType=" + locType +
               '}';
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    //<editor-fold desc="> Factory Methods">
    
    //<editor-fold desc=">> New Instance Factory Methods">
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@code 0} and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@code 0} and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds newInstance() { return newInstance(Enu.get(LocType.class)); }
    
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@code 0} and {@link LocType} set to the specified value.</p>
     *
     * @param locType The {@link LocType} the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@code 0} and {@link LocType} set to the specified value.
     */
    public static @NotNull Bounds newInstance(@Nullable LocType locType) { return new Bounds(0, 0, 0, 0, Enu.get(locType, LocType.class)); }
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to the specified values and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @param x The {@link #x() x} value the returned {@link Bounds} object is to be set to.
     * @param y The {@link #y() y} value the returned {@link Bounds} object is to be set to.
     * @param w The {@link #w() width} value the returned {@link Bounds} object is to be set to.
     * @param h The {@link #h() height} value the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to the specified values and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds newInstance(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h) { return new Bounds(x, y, w, h, Enu.get(LocType.class)); }
    
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to the specified values and {@link LocType} set to the specified value.</p>
     *
     * @param locs    The {@link #getLocation() location} value the returned {@link Bounds} object is to be set to.
     * @param dims    The {@link #getDimensions() location} value the returned {@link Bounds} object is to be set to.
     * @param locType The {@link LocType} the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to the specified values and {@link LocType} set to the specified value.
     */
    public static @NotNull Bounds newInstance(@NotNull Num2D locs, @NotNull Num2D dims, @Nullable LocType locType) { return new Bounds(locs.a(), locs.b(), dims.a(), dims.b(), Enu.get(locType, LocType.class)); }
    
    /**
     * <p>Returns a new {@link Bounds} object with values matching those of the specified {@link BoundsExpr} instance.</p>
     *
     * @param from The {@link BoundsExpr} instance containing the values the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object with values matching those of the specified {@link BoundsExpr} instance.
     */
    public static @NotNull Bounds newInstance(@NotNull BoundsExpr from) { return newInstance(from, Enu.get(from.locType(), LocType.class)); }
    
    /**
     * <p>Returns a new {@link Bounds} object with values matching those of the specified {@link BoundsExpr} instance but with a {@link LocType} of the specified value.</p>
     *
     * @param from    The {@link BoundsExpr} instance containing the values the returned {@link Bounds} object is to be set to.
     * @param locType The {@link LocType} constant the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object with values matching those of the specified {@link BoundsExpr} instance but with a {@link LocType} of the specified value.
     */
    public static @NotNull Bounds newInstance(@NotNull BoundsExpr from, @Nullable LocType locType) { return newInstance(from.getLocation(locType), from.getDimensions(), locType); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> New Instance From Factory Methods">
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.</p>
     *
     * @param minX    The x value of the minimum point used to construct the returned {@link Bounds} object.
     * @param minY    The y value of the minimum point used to construct the returned {@link Bounds} object.
     * @param maxX    The x value of the maximum point used to construct the returned {@link Bounds} object.
     * @param maxY    The y value of the maximum point used to construct the returned {@link Bounds} object.
     * @param locType The {@link LocType} constant the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull Number minX, @NotNull Number minY, @NotNull Number maxX, @NotNull Number maxY, @Nullable LocType locType) {
        if (minX.doubleValue() >= maxX.doubleValue())
            throw Exc.inputMismatch("Input Min X cannot be greater than Input Max X: " + "[" + minX + ", " + maxX + "]");
        else if (minY.doubleValue() >= maxY.doubleValue())
            throw Exc.inputMismatch("Input Min Y cannot be greater than Input Max Y: " + "[" + minY + ", " + maxY + "]");
        else
            return new Bounds(minX, minY, N.subD(maxX, minX), N.subD(maxY, minY), Enu.get(locType, LocType.class));
    }
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @param minX The x value of the minimum point used to construct the returned {@link Bounds} object.
     * @param minY The y value of the minimum point used to construct the returned {@link Bounds} object.
     * @param maxX The x value of the maximum point used to construct the returned {@link Bounds} object.
     * @param maxY The y value of the maximum point used to construct the returned {@link Bounds} object.
     *
     * @return A new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull Number minX, @NotNull Number minY, @NotNull Number maxX, @NotNull Number maxY) { return newInstanceFrom(minX, minY, maxX, maxY, Enu.get(LocType.class)); }
    
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.</p>
     *
     * @param min     The {@link NumExpr2D} instance defining the minimum x and y values used to construct the returned {@link Bounds} object.
     * @param max     The {@link NumExpr2D} instance defining the maximum x and y values used to construct the returned {@link Bounds} object.
     * @param locType The {@link LocType} constant the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull NumExpr2D<?> min, @NotNull NumExpr2D<?> max, @Nullable LocType locType) { return newInstanceFrom(min.a(), min.b(), max.a(), max.b(), locType); }
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @param min The {@link NumExpr2D} instance defining the minimum x and y values used to construct the returned {@link Bounds} object.
     * @param max The {@link NumExpr2D} instance defining the maximum x and y values used to construct the returned {@link Bounds} object.
     *
     * @return Returns a new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull NumExpr2D<?> min, @NotNull NumExpr2D<?> max) { return newInstanceFrom(min, max, Enu.get(LocType.class)); }
    
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.</p>
     *
     * @param min     The {@link Point2D} instance defining the minimum x and y values used to construct the returned {@link Bounds} object.
     * @param max     The {@link Point2D} instance defining the maximum x and y values used to construct the returned {@link Bounds} object.
     * @param locType The {@link LocType} constant the returned {@link Bounds} object is to be set to.
     *
     * @return A new {@link Bounds} object based on the specified minimum and maximum point values and {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull Point2D min, @NotNull Point2D max, @Nullable LocType locType) { return newInstanceFrom(min.getX(), min.getY(), max.getX(), max.getY(), locType); }
    
    /**
     * <p>Returns a new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @param min The {@link Point2D} instance defining the minimum x and y values used to construct the returned {@link Bounds} object.
     * @param max The {@link Point2D} instance defining the maximum x and y values used to construct the returned {@link Bounds} object.
     *
     * @return Returns a new {@link Bounds} object based on the specified minimum and maximum point values and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds newInstanceFrom(@NotNull Point2D min, @NotNull Point2D max) { return newInstanceFrom(min, max, Enu.get(LocType.class)); }
    
    //</editor-fold>
    
    
    //<editor-fold desc=">> Specialty Factory Methods">
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MAX_VALUE} and {@link LocType} set to the specified value.</p>
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MAX_VALUE} and {@link LocType} set to the specified value.
     */
    public static @NotNull Bounds boundsMax(@Nullable LocType locType) {
        return new Bounds(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, locType);
    }
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MAX_VALUE} and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MAX_VALUE} and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds boundsMax() { return boundsMax(null); }
    
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MIN_VALUE} and {@link LocType} set to the specified value.</p>
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MIN_VALUE} and {@link LocType} set to the specified value.
     */
    public static @NotNull Bounds boundsMin(@Nullable LocType locType) {
        return new Bounds(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, locType);
    }
    
    /**
     * <p>Returns a new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MIN_VALUE} and a {@link Enu#get(Class) default} {@link LocType}.</p>
     *
     * @return A new {@link Bounds} object with {@link BoundsExpr#getLocation() location} and {@link BoundsExpr#getDimensions() dimensions} set to {@link Integer#MIN_VALUE} and a {@link Enu#get(Class) default} {@link LocType}.
     */
    public static @NotNull Bounds boundsMin() { return boundsMin(null); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
}
