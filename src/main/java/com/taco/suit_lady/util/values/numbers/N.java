package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.tools.list_tools.A;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class N {
    private N() { } //No Instance
    
    public static @NotNull Num2D num2D(@NotNull Point2D input) { return new Num2D(input.getX(), input.getY()); }
    
    //<editor-fold desc="--- MATH ---">
    
    //<editor-fold desc="> Addition">
    
    public static int addI(boolean exact, @NotNull Number... numbers) {
        if (exact) {
            double result = 0;
            for (Number num: numbers)
                result += num.doubleValue();
            return (int) result;
        } else
            return Arrays.stream(numbers).mapToInt(Number::intValue).sum();
    }
    public static long addL(boolean exact, @NotNull Number... numbers) {
        if (exact) {
            double result = 0;
            for (Number num: numbers)
                result += num.doubleValue();
            return (long) result;
        }
        return Arrays.stream(numbers).mapToLong(Number::longValue).sum();
    }
    public static float addF(@NotNull Number... numbers) { return (float) Arrays.stream(numbers).mapToDouble(Number::floatValue).sum(); }
    public static double addD(@NotNull Number... numbers) { return Arrays.stream(numbers).mapToDouble(Number::doubleValue).sum(); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Subtraction">
    
    public static int subI(boolean exact, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return 0;
        else if (numbers.length == 1)
            return numbers[0].intValue();
        double result = exact ? numbers[0].doubleValue() : numbers[0].intValue();
        for (int i = 1; i < numbers.length; i++)
            result -= exact ? numbers[i].doubleValue() : numbers[i].intValue();
        return (int) result;
    }
    public static long subL(boolean exact, @NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return 0;
        else if (numbers.length == 1)
            return numbers[0].longValue();
        double result = exact ? numbers[0].doubleValue() : numbers[0].longValue();
        for (int i = 1; i < numbers.length; i++)
            result -= exact ? numbers[i].doubleValue() : numbers[i].longValue();
        return (long) result;
    }
    public static float subF(@NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return 0;
        else if (numbers.length == 1)
            return numbers[0].floatValue();
        double result = numbers[0].floatValue();
        for (int i = 1; i < numbers.length; i++)
            result -= numbers[i].floatValue();
        return (float) result;
    }
    public static double subD(@NotNull Number... numbers) {
        if (A.isEmpty(numbers))
            return 0;
        else if (numbers.length == 1)
            return numbers[0].doubleValue();
        double result = numbers[0].doubleValue();
        for (int i = 1; i < numbers.length; i++)
            result -= numbers[i].doubleValue();
        return result;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- PRIMITIVE CONVERSION METHODS ---">
    
    public static int i(@NotNull Number num) { return num.intValue(); }
    public static long l(@NotNull Number num) { return num.longValue(); }
    
    public static float f(@NotNull Number num) { return num.floatValue(); }
    public static double d(@NotNull Number num) { return num.doubleValue(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- DIMENSIONS OF METHODS ---">
    
    public static @NotNull Num2D dimensionsOf(@NotNull Region region) { return new Num2D(region.getWidth(), region.getHeight()); }
    public static @NotNull Num2D dimensionsOfPref(@NotNull Region region) { return new Num2D(region.getPrefWidth(), region.getPrefHeight()); }
    public static @NotNull Num2D dimensionsOfMin(@NotNull Region region) { return new Num2D(region.getMinWidth(), region.getMinHeight()); }
    public static @NotNull Num2D dimensionsOfMax(@NotNull Region region) { return new Num2D(region.getMaxWidth(), region.getMaxHeight()); }
    
    //</editor-fold>
}
