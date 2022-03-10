package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.expressions.BoundsExpr;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

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
    
    //<editor-fold desc="> Factory Construction Methods">
    
    public static @NotNull Bounds newInstance() { return newInstance(Enu.get(LocType.class)); }
    public static @NotNull Bounds newInstance(@Nullable LocType locType) { return new Bounds(0, 0, 0, 0, Enu.get(locType, LocType.class)); }
    public static @NotNull Bounds newInstance(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h) { return new Bounds(x, y, w, h, Enu.get(LocType.class)); }
    
    
    public static @NotNull Bounds newInstance(@NotNull Num2D locs, @NotNull Num2D dims, @Nullable LocType locType) { return new Bounds(locs.a(), locs.b(), dims.a(), dims.b(), Enu.get(locType, LocType.class)); }
    public static @NotNull Bounds newInstance(@NotNull BoundsExpr from) { return newInstance(from, Enu.get(from.locType(), LocType.class)); }
    public static @NotNull Bounds newInstance(@NotNull BoundsExpr from, @Nullable LocType locType) { return newInstance(from.getLocation(locType), from.getDimensions(), locType); }
    
    //
    
    public static @NotNull Bounds newInstanceFrom(@NotNull Number minX, @NotNull Number minY, @NotNull Number maxX, @NotNull Number maxY, @Nullable LocType locType) {
        if (minX.doubleValue() >= maxX.doubleValue())
            throw Exc.inputMismatch("Input Min X cannot be greater than Input Max X: " + "[" + minX + ", " + maxX + "]");
        else if (minY.doubleValue() >= maxY.doubleValue())
            throw Exc.inputMismatch("Input Min Y cannot be greater than Input Max Y: " + "[" + minY + ", " + maxY + "]");
        else
            return new Bounds(minX, minY, N.subD(maxX, minX), N.subD(maxY, minY), Enu.get(locType, LocType.class));
    }
    public static @NotNull Bounds newInstanceFrom(@NotNull Number minX, @NotNull Number minY, @NotNull Number maxX, @NotNull Number maxY) { return newInstanceFrom(minX, minY, maxX, maxY, Enu.get(LocType.class)); }
    
    public static @NotNull Bounds newInstanceFrom(@NotNull NumExpr2D<?> min, @NotNull NumExpr2D<?> max, @Nullable LocType locType) { return newInstanceFrom(min.a(), min.b(), max.a(), max.b(), locType); }
    public static @NotNull Bounds newInstanceFrom(@NotNull NumExpr2D<?> min, @NotNull NumExpr2D<?> max) { return newInstanceFrom(min, max, Enu.get(LocType.class)); }
    
    public static @NotNull Bounds newInstanceFrom(@NotNull Point2D min, @NotNull Point2D max, @Nullable LocType locType) { return newInstanceFrom(min.getX(), min.getY(), max.getX(), max.getY(), locType); }
    public static @NotNull Bounds newInstanceFrom(@NotNull Point2D min, @NotNull Point2D max) { return newInstanceFrom(min, max, Enu.get(LocType.class)); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Specialty Factory Methods">
    
    public static @NotNull Bounds boundsMax(@Nullable LocType locType) {
        return new Bounds(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, locType);
    }
    public static @NotNull Bounds boundsMax() { return boundsMax(null); }
    
    public static @NotNull Bounds boundsMin(@Nullable LocType locType) {
        return new Bounds(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, locType);
    }
    public static @NotNull Bounds boundsMin() { return boundsMin(null); }
    
    //</editor-fold>
    
    //</editor-fold>
}
