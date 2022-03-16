package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import com.taco.tacository.numbers.Numbers;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Supplier;

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
    
    public static int iOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).intValue(); }
    public static int iOf(@Nullable ObservableValue<? extends Number> observableValue) { return iOf(observableValue, null); }
    
    public static long lOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).longValue(); }
    public static long lOf(@Nullable ObservableValue<? extends Number> observableValue) { return lOf(observableValue, null); }
    
    public static float fOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).floatValue(); }
    public static float fOf(@Nullable ObservableValue<? extends Number> observableValue) { return fOf(observableValue, null); }
    
    public static double dOf(@Nullable ObservableValue<? extends Number> observableValue, @Nullable Supplier<Number> fallbackSupplier) { return of(observableValue, fallbackSupplier).doubleValue(); }
    public static double dOf(@Nullable ObservableValue<? extends Number> observableValue) { return dOf(observableValue, null); }
    
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
}
