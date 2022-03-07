package com.taco.suit_lady.util.values.bounds;

import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public record Bounds(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h, @NotNull LocType locType)
        implements Boundable, Cloneable, Serializable {
    
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
    
    public static @NotNull Bounds create(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h, @Nullable LocType locType) {
        return new Bounds(x, y, w, h, locType != null ? locType : Enu.get(LocType.class));
    }
    
    //
    
    public static @NotNull Bounds create() { return create(Enu.get(LocType.class)); }
    public static @NotNull Bounds create(@Nullable LocType locType) { return create(0, 0, 0, 0, locType); }
    public static @NotNull Bounds create(@NotNull Number x, @NotNull Number y, @NotNull Number w, @NotNull Number h) { return create(x, y, w, h, null); }
    
    public static @NotNull Bounds create(@NotNull Num2D locs, @NotNull Num2D dims, @Nullable LocType locType) { return create(locs.a(), locs.b(), dims.a(), dims.b(), locType); }
    public static @NotNull Bounds create(@NotNull Boundable from) { return create(from, from.locType()); }
    public static @NotNull Bounds create(@NotNull Boundable from, @Nullable LocType locType) { return create(from.getLocation(locType), from.getDimensions(), locType); }
    
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
