package com.taco.tacository.util.values.numbers;

import com.taco.tacository.util.tools.list_tools.A;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>A static utility class containing methods for working with {@link Number Numbers} and {@link NumExpr2D 2D Number Expressions} (e.g., {@link Num2D}).</p>
 */
//To-EXPAND
public class N {
    private N() { } //No Instance
    
    /**
     * <p>Returns a new {@link Num2D} instance set to the {@link Point2D#getX() x} and {@link Point2D#getY() y} values of the specified {@link Point2D} instance.</p>
     *
     * @param input The {@link Point2D} instance to be reflected by the returned {@link Num2D} object.
     *
     * @return A new {@link Num2D} instance set to the {@link Point2D#getX() x} and {@link Point2D#getY() y} values of the specified {@link Point2D} instance.
     */
    public static @NotNull Num2D num2D(@NotNull Point2D input) { return new Num2D(input.getX(), input.getY()); }
    
    public static boolean equalsNum2D(@NotNull NumExpr2D<?> input, @NotNull Number a, @NotNull Number b, boolean exact) {
        if (exact)
            return input.aD() == a.doubleValue() && input.bD() == b.doubleValue();
        else
            return input.aI() == a.intValue() && input.bI() == b.intValue();
    }
    
    //<editor-fold desc="--- MATH ---">
    
    //<editor-fold desc="> Addition">
    
    /**
     * <p>Calculates the {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.</p>
     *
     * @param exact   {@code True} if the {@link Number Numbers} should be summed as {@link Number#doubleValue() doubles} and then cast to an {@code int},
     *                false if each {@link Number} should be cast to an {@code int} prior to being added to the {@code sum}.
     * @param numbers The {@link Number Numbers} being {@code summed}.
     *
     * @return The {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.
     */
    public static int addI(boolean exact, @NotNull Number... numbers) {
        if (exact) {
            double result = 0;
            for (Number num: numbers)
                result += num.doubleValue();
            return (int) result;
        } else
            return Arrays.stream(numbers).mapToInt(Number::intValue).sum();
    }
    
    /**
     * <p>Calculates the {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.</p>
     *
     * @param exact   {@code True} if the {@link Number Numbers} should be summed as {@link Number#doubleValue() doubles} and then cast to an {@code long},
     *                false if each {@link Number} should be cast to an {@code long} prior to being added to the {@code sum}.
     * @param numbers The {@link Number Numbers} being {@code summed}.
     *
     * @return The {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.
     */
    public static long addL(boolean exact, @NotNull Number... numbers) {
        if (exact) {
            double result = 0;
            for (Number num: numbers)
                result += num.doubleValue();
            return (long) result;
        }
        return Arrays.stream(numbers).mapToLong(Number::longValue).sum();
    }
    
    /**
     * <p>Calculates the {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.</p>
     *
     * @param numbers The {@link Number Numbers} being {@code summed}.
     *
     * @return The {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.
     */
    public static float addF(@NotNull Number... numbers) { return (float) Arrays.stream(numbers).mapToDouble(Number::floatValue).sum(); }
    
    /**
     * <p>Calculates the {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.</p>
     *
     * @param numbers The {@link Number Numbers} being {@code summed}.
     *
     * @return The {@code sum} of the {@link Number Numbers} contained within the specified {@code array} and then returns the result.
     */
    public static double addD(@NotNull Number... numbers) { return Arrays.stream(numbers).mapToDouble(Number::doubleValue).sum(); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Subtraction">
    
    /**
     * <p>Calculates the {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.</p>
     *
     * @param exact     {@code True} if the {@link Number Numbers} should be subtracted as {@link Number#doubleValue() doubles} and then cast to an {@code int},
     *                  {@code false} if each {@link Number} should be cast to an {@code int} prior to being subtracted.
     * @param baseValue The {@link Number} the specified {@code array} of {@link Number Numbers} is to be subtracted from.
     * @param numbers   The {@link Number Numbers} being {@code subtracted} from the specified {@link Number Base Value}.
     *
     * @return The {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.
     */
    public static int subI(boolean exact, @NotNull Number baseValue, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return baseValue.intValue();
        double result = exact ? baseValue.doubleValue() : baseValue.intValue();
        for (Number number: numbers)
            result -= (exact ? number.doubleValue() : number.intValue());
        return (int) result;
    }
    
    /**
     * <p>Calculates the {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.</p>
     *
     * @param exact     {@code True} if the {@link Number Numbers} should be subtracted as {@link Number#doubleValue() doubles} and then cast to a {@code long},
     *                  {@code false} if each {@link Number} should be cast to a {@code long} prior to being subtracted.
     * @param baseValue The {@link Number} the specified {@code array} of {@link Number Numbers} is to be subtracted from.
     * @param numbers   The {@link Number Numbers} being {@code subtracted} from the specified {@link Number Base Value}.
     *
     * @return The {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.
     */
    public static long subL(boolean exact, @NotNull Number baseValue, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return baseValue.longValue();
        double result = exact ? baseValue.doubleValue() : baseValue.longValue();
        for (Number number: numbers)
            result -= (exact ? number.doubleValue() : number.longValue());
        return (long) result;
    }
    
    /**
     * <p>Calculates the {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.</p>
     *
     * @param baseValue The {@link Number} the specified {@code array} of {@link Number Numbers} is to be subtracted from.
     * @param numbers   The {@link Number Numbers} being {@code subtracted} from the specified {@link Number Base Value}.
     *
     * @return The {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.
     */
    public static float subF(@NotNull Number baseValue, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return baseValue.floatValue();
        double result = baseValue.floatValue();
        for (Number number: numbers)
            result -= number.floatValue();
        return (float) result;
    }
    
    /**
     * <p>Calculates the {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.</p>
     *
     * @param baseValue The {@link Number} the specified {@code array} of {@link Number Numbers} is to be subtracted from.
     * @param numbers   The {@link Number Numbers} being {@code subtracted} from the specified {@link Number Base Value}.
     *
     * @return The {@code difference} between the specified {@link Number Base Value} and the {@code sum} of the {@link Number Numbers} contained within the specified {@code array}.
     */
    public static double subD(@NotNull Number baseValue, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return baseValue.doubleValue();
        double result = baseValue.doubleValue();
        for (Number number: numbers)
            result -= number.doubleValue();
        return result;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Min/Max">
    
    /**
     * <p>Calculates the minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param min     {@code True} if the minimum value should be returned, {@code false} if the maximum value should be returned.
     * @param exact   {@code True} if the {@link Number Numbers} should be compared as {@link Number#doubleValue() doubles} and then the result cast to an {@code int},
     *                {@code false} if each {@link Number} should be cast to an {@code int} prior to being compared.
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static int minMaxI(boolean min, boolean exact, @NotNull Number @NotNull ... numbers) {
        double result = min ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (Number num: numbers)
            if (min ? ((exact ? num.doubleValue() : num.intValue()) < result) : ((exact ? num.doubleValue() : num.intValue()) > result))
                result = exact ? num.doubleValue() : num.intValue();
        return (int) result;
    }
    
    /**
     * <p>Calculates the minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param exact   {@code True} if the {@link Number Numbers} should be compared as {@link Number#doubleValue() doubles} and then the result cast to an {@code int},
     *                {@code false} if each {@link Number} should be cast to an {@code int} prior to being compared.
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static int minI(boolean exact, @NotNull Number... numbers) { return minMaxI(true, exact, numbers); }
    
    /**
     * <p>Calculates the maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param exact   {@code True} if the {@link Number Numbers} should be compared as {@link Number#doubleValue() doubles} and then the result cast to an {@code int},
     *                {@code false} if each {@link Number} should be cast to an {@code int} prior to being compared.
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static int maxI(boolean exact, @NotNull Number... numbers) { return minMaxI(false, exact, numbers); }
    
    /**
     * <p>Calculates the minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param min     {@code True} if the minimum value should be returned, {@code false} if the maximum value should be returned.
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static long minMaxL(boolean min, @NotNull Number @NotNull ... numbers) {
        long result = min ? Long.MAX_VALUE : Long.MIN_VALUE;
        for (Number num: numbers)
            if (min ? num.longValue() < result : num.longValue() > result)
                result = num.longValue();
        return result;
    }
    
    /**
     * <p>Calculates the minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static long minL(boolean exact, @NotNull Number... numbers) { return minMaxL(true, numbers); }
    
    /**
     * <p>Calculates the maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static long maxL(boolean exact, @NotNull Number... numbers) { return minMaxL(false, numbers); }
    
    /**
     * <p>Calculates the minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param min     {@code True} if the minimum value should be returned, {@code false} if the maximum value should be returned.
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum or maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static double minMaxD(boolean min, @NotNull Number @NotNull ... numbers) {
        double result = min ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (Number num: numbers)
            if (min ? num.doubleValue() < result : num.doubleValue() > result)
                result = num.doubleValue();
        return result;
    }
    
    /**
     * <p>Calculates the minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The minimum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static double minD(boolean exact, @NotNull Number... numbers) { return minMaxD(true, numbers); }
    
    /**
     * <p>Calculates the maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.</p>
     *
     * @param numbers The {@code array} of {@link Number Numbers} to be compared.
     *
     * @return The maximum {@link Number} contained within the specified {@code array} of {@link Number Numbers}.
     */
    public static double maxD(boolean exact, @NotNull Number... numbers) { return minMaxD(false, numbers); }
    
    //
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the minimum or maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param minX    True if the minimum x value should be returned, false if the maximum x value should be returned.
     * @param minY    True if the minimum y value should be returned, false if the maximum y value should be returned.
     * @param intOnly True if all {@link Num2D#a() x} and {@link Num2D#b() y} values should be accessed as {@link Integer ints}, false if they should be accessed as {@link Double doubles}.
     * @param inputs  The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the minimum or maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D minMaxP(boolean minX, boolean minY, boolean intOnly, @NotNull NumExpr2D<?>... inputs) {
        return new Num2D(minMaxD(minX, Arrays.stream(inputs).map(intOnly ? NumExpr2D::aI : NumExpr2D::a).toArray(Number[]::new)),
                         minMaxD(minY, Arrays.stream(inputs).map(intOnly ? NumExpr2D::bI : NumExpr2D::b).toArray(Number[]::new)));
    }
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the minimum or maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param minX   True if the minimum x value should be returned, false if the maximum x value should be returned.
     * @param minY   True if the minimum y value should be returned, false if the maximum y value should be returned.
     * @param inputs The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the minimum or maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D minMaxP(boolean minX, boolean minY, @NotNull NumExpr2D<?>... inputs) { return minMaxP(minX, minY, false, inputs); }
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the minimum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param intOnly True if all {@link Num2D#a() x} and {@link Num2D#b() y} values should be accessed as {@link Integer ints}, false if they should be accessed as {@link Double doubles}.
     * @param inputs  The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the minimum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D minP(boolean intOnly, @NotNull NumExpr2D<?>... inputs) { return minMaxP(true, true, intOnly, inputs); }
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the minimum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param inputs The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the minimum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D minP(@NotNull NumExpr2D<?>... inputs) { return minP(false, inputs); }
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param intOnly True if all {@link Num2D#a() x} and {@link Num2D#b() y} values should be accessed as {@link Integer ints}, false if they should be accessed as {@link Double doubles}.
     * @param inputs  The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D maxP(boolean intOnly, @NotNull NumExpr2D<?>... inputs) { return minMaxP(false, false, intOnly, inputs); }
    
    /**
     * <p>Calculates and then returns a {@link Num2D} containing the maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of any of the {@link Num2D} objects contained within the specified {@code array}.</p>
     *
     * @param inputs The {@code array} of {@link NumExpr2D} objects to be compared.
     *
     * @return A {@link Num2D} containing the maximum {@link Num2D#a() x} and {@link Num2D#b() y} values of the {@link Num2D} objects contained within the specified {@code array}.
     */
    public static @NotNull Num2D maxP(@NotNull NumExpr2D<?>... inputs) { return maxP(false, inputs); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- PRIMITIVE CONVERSION METHODS ---">
    
    /**
     * <p>Returns the specified {@link Number} as an {@link Integer int} primitive.</p>
     *
     * @param num The {@link Number} to be converted to a primitive.
     *
     * @return The specified {@link Number} as an {@link Integer int} primitive.
     */
    public static int i(@NotNull Number num) { return num.intValue(); }
    
    /**
     * <p>Returns the specified {@link Number} as an {@link Long long} primitive.</p>
     *
     * @param num The {@link Number} to be converted to a primitive.
     *
     * @return The specified {@link Number} as an {@link Long long} primitive.
     */
    public static long l(@NotNull Number num) { return num.longValue(); }
    
    
    /**
     * <p>Returns the specified {@link Number} as an {@link Float float} primitive.</p>
     *
     * @param num The {@link Number} to be converted to a primitive.
     *
     * @return The specified {@link Number} as an {@link Float float} primitive.
     */
    public static float f(@NotNull Number num) { return num.floatValue(); }
    
    /**
     * <p>Returns the specified {@link Number} as an {@link Double double} primitive.</p>
     *
     * @param num The {@link Number} to be converted to a primitive.
     *
     * @return The specified {@link Number} as an {@link Double double} primitive.
     */
    public static double d(@NotNull Number num) { return num.doubleValue(); }
    
    //
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Integer}.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue, Supplier)} for details.</i></p></blockquote>
     */
    public static int iOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).intValue(); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Integer} with no {@link Supplier Fallback Supplier} provided.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue)} for details.</i></p></blockquote>
     */
    public static int iOf(@Nullable ObservableValue<? extends Number> observableValue) { return iOf(observableValue, null); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Long}.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue, Supplier)} for details.</i></p></blockquote>
     */
    public static long lOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).longValue(); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Long} with no {@link Supplier Fallback Supplier} provided.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue)} for details.</i></p></blockquote>
     */
    public static long lOf(@Nullable ObservableValue<? extends Number> observableValue) { return lOf(observableValue, null); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Float}.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue, Supplier)} for details.</i></p></blockquote>
     */
    public static float fOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).floatValue(); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Float} with no {@link Supplier Fallback Supplier} provided.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue)} for details.</i></p></blockquote>
     */
    public static float fOf(@Nullable ObservableValue<? extends Number> observableValue) { return fOf(observableValue, null); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Double}.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue, Supplier)} for details.</i></p></blockquote>
     */
    public static double dOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).doubleValue(); }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a primitive {@link Double} with no {@link Supplier Fallback Supplier} provided.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue)} for details.</i></p></blockquote>
     */
    public static double dOf(@Nullable ObservableValue<? extends Number> observableValue) { return dOf(observableValue, null); }
    
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a {@link Number} object.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         If the specified {@link ObservableValue} or the {@link ObservableValue#getValue() value} it contains is {@code null} and the specified {@link Supplier Fallback Supplier} is {@code non-null},
     *         return the {@link Supplier#get() Fallback Value}.
     *     </li>
     *     <li>
     *         If the specified {@link ObservableValue} or the {@link ObservableValue#getValue() value} it contains is {@code null} and the specified {@link Supplier Fallback Supplier} is also {@code null},
     *         return {@code 0}.
     *     </li>
     * </ol>
     *
     * @param observableValue  The {@link ObservableValue} instance containing the {@link ObservableValue#getValue() value} to be {@code returned}.
     * @param fallbackSupplier The {@link Supplier} used to provide a {@code return value} if the specified {@link ObservableValue} or the {@link ObservableValue#getValue() value} it contains is {@code null}.
     *
     * @return The {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a {@link Number} object.
     */
    public static @NotNull Number of(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<? extends Number> fallbackSupplier) {
        if (observableValue != null) {
            final Number value = observableValue.getValue();
            if (value != null)
                return value;
        } else if (fallbackSupplier != null) {
            final Number fallbackValue = fallbackSupplier.get();
            if (fallbackValue != null)
                return fallbackValue;
        }
        
        return 0;
    }
    
    /**
     * <p>Returns the {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a {@link Number} object with no {@link Supplier Fallback Supplier} provided.</p>
     * <blockquote><p><i>See {@link #of(ObservableValue, Supplier)} for details.</i></p></blockquote>
     *
     * @param observableValue The {@link ObservableValue} instance containing the {@link ObservableValue#getValue() value} to be {@code returned}.
     *
     * @return The {@link ObservableValue#getValue() value} of the specified {@link ObservableValue} as a {@link Number} object with no {@link Supplier Fallback Supplier} provided.
     */
    public static @NotNull Number of(@Nullable ObservableValue<? extends Number> observableValue) { return of(observableValue, null); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- DIMENSIONS OF METHODS ---">
    
    /**
     * <p>Returns the dimensions of the specified {@link Region} as a {@link Num2D} object.</p>
     *
     * @param region The {@link Region}.
     *
     * @return The dimensions of the specified {@link Region} as a {@link Num2D} object.
     */
    public static @NotNull Num2D dimensionsOf(@NotNull Region region) { return new Num2D(region.getWidth(), region.getHeight()); }
    
    /**
     * <p>Returns the preferred dimensions of the specified {@link Region} as a {@link Num2D} object.</p>
     *
     * @param region The {@link Region}.
     *
     * @return The preferred dimensions of the specified {@link Region} as a {@link Num2D} object.
     */
    public static @NotNull Num2D dimensionsOfPref(@NotNull Region region) { return new Num2D(region.getPrefWidth(), region.getPrefHeight()); }
    
    /**
     * <p>Returns the minimum dimensions of the specified {@link Region} as a {@link Num2D} object.</p>
     *
     * @param region The {@link Region}.
     *
     * @return The minimum dimensions of the specified {@link Region} as a {@link Num2D} object.
     */
    public static @NotNull Num2D dimensionsOfMin(@NotNull Region region) { return new Num2D(region.getMinWidth(), region.getMinHeight()); }
    
    /**
     * <p>Returns the maximum dimensions of the specified {@link Region} as a {@link Num2D} object.</p>
     *
     * @param region The {@link Region}.
     *
     * @return The maximum dimensions of the specified {@link Region} as a {@link Num2D} object.
     */
    public static @NotNull Num2D dimensionsOfMax(@NotNull Region region) { return new Num2D(region.getMaxWidth(), region.getMaxHeight()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TACOSITORY NUMBERS PORT ---">
    
    public static boolean isNumber(String input, List<String> args)
    {
        if (args == null)
            throw new NullPointerException("Filter List cannot be null.");
        return isNumber(input, args.toArray(new String[0]));
    }
    
    public static boolean isNumber(String input, String... args)
    {
        final List<String> argsList = Arrays.asList(args);
        
        if (input == null)
            if (NumFilter.THROW_NPE.matches(argsList))
                throw new NullPointerException("Input cannot be null.");
            else
                return false;
        
        try
        {
            final double parsedInput = Double.parseDouble(input);
            
            if (Double.isNaN(parsedInput) && !NumFilter.ALLOW_NaN.matches(argsList))
                return false;
            else if (Double.isInfinite(parsedInput) && !NumFilter.ALLOW_INFINITY.matches(argsList))
                return false;
            else if (decimals(parsedInput) != 0 && NumFilter.INT_ONLY.matches(argsList))
                return false;
            
            return true;
        }
        catch (NumberFormatException e)
        {
            if (NumFilter.THROW_NFE.matches(argsList))
                throw e;
            return false;
        }
    }
    
    //
    
    public static boolean isInteger(String input)
    {
        return isInteger(input, false, false);
    }
    
    public static boolean isInteger(String input, boolean throwNPE, boolean throwNFE)
    {
        final ArrayList<String> filters = new ArrayList<>();
        
        filters.add(NumFilter.INT_ONLY.key());
        if (throwNPE)
            filters.add(NumFilter.THROW_NPE.key());
        if (throwNFE)
            filters.add(NumFilter.THROW_NFE.key());
        
        return isNumber(input, filters);
    }
    
    public static double decimals(double input)
    {
        return input - ((int) input);
    }
    
    //
    
    public static boolean isEven(double input)
    {
        input = (int) input;
        return isInteger("" + (input / 2));
    }
    
    public static boolean isOdd(int input)
    {
        return !isEven(input);
    }
    
    //</editor-fold>
}
