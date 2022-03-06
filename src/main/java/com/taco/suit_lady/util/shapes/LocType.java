package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.util.enums.Enumable;
import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Exe;
import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public enum LocType
        implements Enumable<LocType> {
    
    MIN, MAX, CENTER;
    
    LocType() { }
    
    //<editor-fold desc="--- STATIC ---">
    
    public static double translate(@Nullable Lock lock, boolean allowNullLock, @NotNull Number loc, @NotNull Number dim, @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return runLocked(lock, allowNullLock, () -> {
            final double locD = loc.doubleValue();
            final double dimD = dim.doubleValue();
            
            return switch (sourceLocType) {
                case MIN -> switch (targetLocType) {
                    case MIN -> locD;
                    case MAX -> locD + dimD;
                    case CENTER -> locD + (dimD / 2);
                    
                    default -> throw unsupportedLocType(targetLocType);
                };
                
                case MAX -> switch (targetLocType) {
                    case MIN -> locD - dimD;
                    case MAX -> locD;
                    case CENTER -> locD - (dimD / 2);
                    
                    default -> throw unsupportedLocType(targetLocType);
                };
                
                case CENTER -> switch (targetLocType) {
                    case MIN -> locD - (dimD / 2);
                    case MAX -> locD + (dimD / 2);
                    case CENTER -> locD;
                    
                    default -> throw unsupportedLocType(targetLocType);
                };
                
                default -> throw unsupportedLocType(null);
                
            };
        });
    }
    public static double translate(@Nullable Lock lock, boolean allowNullLock, @NotNull Number loc, @NotNull Number dim, @NotNull LocType locType) {
        return translate(lock, allowNullLock, loc, dim, locType, locType);
    }
    
    public static double translate(@NotNull Number loc, @NotNull Number dim, @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, loc, dim, sourceLocType, targetLocType);
    }
    public static double translate(@NotNull Number loc, @NotNull Number dim, @NotNull LocType locType) {
        return translate(loc, dim, locType, locType);
    }
    
    //
    
    //<editor-fold desc="> Location 2D">
    
    public static @NotNull NumberValuePair translate(@Nullable Lock lock, boolean allowNullLock,
                                                     @NotNull Number locX, @NotNull Number locY,
                                                     @NotNull Number dimX, @NotNull Number dimY,
                                                     @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return runLocked(lock, allowNullLock, () -> new NumberValuePair(
                translate(locX, dimX, sourceLocType, targetLocType),
                translate(locY, dimY, sourceLocType, targetLocType)));
    }
    
    public static @NotNull NumberValuePair translate(@NotNull Number locX, @NotNull Number locY,
                                                     @NotNull Number dimX, @NotNull Number dimY,
                                                     @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, locX, locY, dimX, dimY, sourceLocType, targetLocType);
    }
    public static @NotNull NumberValuePair translate(@NotNull Number locX, @NotNull Number locY, @NotNull Number dimX, @NotNull Number dimY, @NotNull LocType locType) {
        return translate(null, true, locX, locY, dimX, dimY, locType, locType);
    }
    public static @NotNull NumberValuePair translate(@NotNull Number locX, @NotNull Number locY, @NotNull Number dimX, @NotNull Number dimY) {
        return translate(null, true, locX, locY, dimX, dimY, Enu.get(LocType.class), Enu.get(LocType.class));
    }
    
    //
    
    public static @NotNull NumberValuePair translate(@Nullable Lock lock, boolean allowNullLock,
                                                     @NotNull NumberValuePairable<?> locs,
                                                     @NotNull NumberValuePairable<?> dims,
                                                     @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(lock, allowNullLock, locs.a(), locs.b(), dims.a(), dims.b(), sourceLocType, targetLocType);
    }
    
    public static @NotNull NumberValuePair translate(@NotNull NumberValuePairable<?> locs,
                                                     @NotNull NumberValuePairable<?> dims,
                                                     @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, locs, dims, sourceLocType, targetLocType);
    }
    public static @NotNull NumberValuePair translate(@NotNull NumberValuePairable<?> locs, @NotNull NumberValuePairable<?> dims, @NotNull LocType locType) {
        return translate(null, true, locs, dims, locType, locType);
    }
    public static @NotNull NumberValuePair translate(@NotNull NumberValuePairable<?> locs, @NotNull NumberValuePairable<?> dims) {
        return translate(null, true, locs, dims, Enu.get(LocType.class), Enu.get(LocType.class));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal: Static">
    
    private static @Nullable <T> T runLocked(@Nullable Lock lock, boolean allowNullLock, @NotNull Supplier<T> operation) { return Exe.sync((lock != null || allowNullLock ? lock : new ReentrantLock()), operation, allowNullLock); }
    private static @NotNull RuntimeException unsupportedLocType(@Nullable LocType input) { return Exc.unsupported("Cannot find location matching LocType [" + input + "]"); }
    
    //</editor-fold>
    
    //</editor-fold>
}
