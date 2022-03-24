package com.taco.tacository.util.values.enums;

import com.taco.tacository.util.enums.Enumable;
import com.taco.tacository.util.tools.Enu;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Exe;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * <p>Defines the location of a {@link Num2D Point} on a {@code 2D Object}.</p>
 */
//TO-EXPAND
public enum LocType
        implements Enumable<LocType> {
    
    MIN, MAX, CENTER;
    
    LocType() { }
    
    //<editor-fold desc="--- STATIC ---">
    
    /**
     * <p>Translates the specified {@link Number location} based on the specified {@link Number dimension}, {@link LocType Source LocType} and {@link LocType Target LocType} and then returns the result.</p>
     *
     * @param lock          The {@link Lock} instance used to synchronize calculations performed by this method, or {@code null} to perform calculations without synchronization.
     *                      If {@code allowNullLock} is set to {@code false}, a {@link RuntimeException} is thrown.
     * @param allowNullLock True if the specified {@link Lock} is permitted to be {@code null}, false if a {@link RuntimeException} should be thrown if the specified {@link Lock} is {@code null}.
     * @param loc           The x or y location to be translated.
     *                      Note that the location value must match the axis of the specified dimension value.
     * @param dim           The width or height dimension value to be used to perform translation.
     *                      Note that the dimension value must match the axis of the specified location value.
     * @param sourceLocType The {@link LocType} defining where the specified {@link Number location} value is located relative to the specified {@link Number dimension} value.
     * @param targetLocType The {@link LocType} defining where the returned {@link Number location} value is located relative to the specified {@link Number dimension} value.
     *
     * @return The translated value of the specified {@link Number location} based on the specified {@link Number dimension}, {@link LocType Source LocType} and {@link LocType Target LocType}.
     */
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
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link LocType}<b>.</b>{@link #translate(Lock, boolean, Number, Number, LocType, LocType) translate}<b>(</b><u>lock</u>, <u>allowNullLock</u>, <u>dim</u>, <u>locType</u>, <u>locType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static double translate(@Nullable Lock lock, boolean allowNullLock, @NotNull Number loc, @NotNull Number dim, @NotNull LocType locType) {
        return translate(lock, allowNullLock, loc, dim, locType, locType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link LocType}<b>.</b>{@link #translate(Lock, boolean, Number, Number, LocType, LocType) translate}<b>(</b>null, true, <u>loc</u>, <u>dim</u>, <u>sourceLocType</u>, <u>targetLocType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static double translate(@NotNull Number loc, @NotNull Number dim, @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, loc, dim, sourceLocType, targetLocType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link LocType}<b>.</b>{@link #translate(Number, Number, LocType, LocType) translate}<b>(</b><u>loc</u>, <u>dim</u>, <u>locType</u>, <u>locType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static double translate(@NotNull Number loc, @NotNull Number dim, @NotNull LocType locType) {
        return translate(loc, dim, locType, locType);
    }
    
    //
    
    //<editor-fold desc="> Location 2D">
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #runLocked(Lock, boolean, Supplier) runLocked}<b>(</b><u>lock</u>, <u>allowNullLock</u>, () -> {@link Num2D#Num2D(Number, Number) new} {@link Num2D}<b>(</b>
     * <br>
     * --- {@link #translate(Number, Number, LocType, LocType) translate}<b>(</b><u>locX</u>, <u>dimX</u>, <u>sourceLocType</u>, <u>targetLocType</u><b>)</b>,
     * <br>
     * --- {@link #translate(Number, Number, LocType, LocType) translate}<b>(</b><u>locY</u>, <u>dimY</u>, <u>sourceLocType</u>, <u>targetLocType</u><b>)))</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@Nullable Lock lock, boolean allowNullLock,
                                           @NotNull Number locX, @NotNull Number locY,
                                           @NotNull Number dimX, @NotNull Number dimY,
                                           @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return runLocked(lock, allowNullLock, () -> new Num2D(
                translate(locX, dimX, sourceLocType, targetLocType),
                translate(locY, dimY, sourceLocType, targetLocType)));
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, Number, Number, Number, Number, LocType, LocType) translate}<b>(</b>null, true, <u>locX</u>, <u>locY</u>, <u>dimX</u>, <u>dimY</u>, <u>sourceLocType</u>, <u>targetLocType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull Number locX, @NotNull Number locY,
                                           @NotNull Number dimX, @NotNull Number dimY,
                                           @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, locX, locY, dimX, dimY, sourceLocType, targetLocType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, Number, Number, Number, Number, LocType, LocType) translate}<b>(</b>null, true, <u>locX</u>, <u>locY</u>, <u>dimX</u>, <u>dimY</u>, <u>locType</u>, <u>locType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull Number locX, @NotNull Number locY, @NotNull Number dimX, @NotNull Number dimY, @NotNull LocType locType) {
        return translate(null, true, locX, locY, dimX, dimY, locType, locType);
    }
    
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, Number, Number, Number, Number, LocType, LocType) translate}<b>(</b>null, true, <u>locX</u>, <u>locY</u>, <u>dimX</u>, <u>dimY</u>,
     * {@link Enu}<b>.</b>{@link Enu#get(Class) get}<b>(</b>{@link LocType}<b>.</b>{@link LocType#getClass() class}<b>)</b>, {@link Enu}<b>.</b>{@link Enu#get(Class) get}<b>(</b>{@link LocType}<b>.</b>{@link LocType#getClass() class}<b>))</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull Number locX, @NotNull Number locY, @NotNull Number dimX, @NotNull Number dimY) {
        return translate(null, true, locX, locY, dimX, dimY, Enu.get(LocType.class), Enu.get(LocType.class));
    }
    
    //
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, Number, Number, Number, Number, LocType, LocType) translate}<b>(</b><u>lock</u>, <u>allowNullLock</u>,
     * <u>{@link NumExpr2D locs}</u><b>.</b>{@link NumExpr2D#a() a}<b>()</b>, <u>{@link NumExpr2D locs}</u><b>.</b>{@link NumExpr2D#b() b}<b>()</b>,
     * <u>{@link NumExpr2D dims}</u><b>.</b>{@link NumExpr2D#a() a}<b>()</b>, <u>{@link NumExpr2D dims}</u><b>.</b>{@link NumExpr2D#b() b}<b>()</b>, <u>sourceLocType</u>, <u>targetLocType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@Nullable Lock lock, boolean allowNullLock,
                                           @NotNull NumExpr2D<?> locs,
                                           @NotNull NumExpr2D<?> dims,
                                           @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(lock, allowNullLock, locs.a(), locs.b(), dims.a(), dims.b(), sourceLocType, targetLocType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, NumExpr2D, NumExpr2D, LocType, LocType) translate}<b>(</b>null, true, <u>locs</u>, <u>dims</u>, <u>sourceLocType</u>, <u>targetLocType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull NumExpr2D<?> locs,
                                           @NotNull NumExpr2D<?> dims,
                                           @NotNull LocType sourceLocType, @NotNull LocType targetLocType) {
        return translate(null, true, locs, dims, sourceLocType, targetLocType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, NumExpr2D, NumExpr2D, LocType, LocType) translate}<b>(</b>null, true, <u>locs</u>, <u>dims</u>, <u>locType</u>, <u>locType</u><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull NumExpr2D<?> locs, @NotNull NumExpr2D<?> dims, @NotNull LocType locType) {
        return translate(null, true, locs, dims, locType, locType);
    }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #translate(Lock, boolean, NumExpr2D, NumExpr2D, LocType, LocType) translate}<b>(</b>null, true, <u>locs</u>, <u>dims</u>,
     * {@link Enu}<b>.</b>{@link Enu#get(Class) get}<b>(</b>{@link LocType}<b>.</b>{@link LocType#getClass() class}<b>)</b>,
     * {@link Enu}<b>.</b>{@link Enu#get(Class) get}<b>(</b>{@link LocType}<b>.</b>{@link LocType#getClass() class}<b>)</b><b>)</b>
     * </code></i></blockquote>
     */
    public static @NotNull Num2D translate(@NotNull NumExpr2D<?> locs, @NotNull NumExpr2D<?> dims) {
        return translate(null, true, locs, dims, Enu.get(LocType.class), Enu.get(LocType.class));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal: Static">
    
    private static @Nullable <T> T runLocked(@Nullable Lock lock, boolean allowNullLock, @NotNull Supplier<T> operation) { return Exe.sync((lock != null || allowNullLock ? lock : new ReentrantLock()), operation, allowNullLock); }
    private static @NotNull RuntimeException unsupportedLocType(@Nullable LocType input) { return Exc.unsupported("Cannot find location matching LocType [" + input + "]"); }
    
    //</editor-fold>
    
    //</editor-fold>
}
