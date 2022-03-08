package com.taco.suit_lady.util.values.numbers;

import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

public class N {
    private N() { } //No Instance
    
    public static @NotNull Num2D num2D(@NotNull Point2D input) { return new Num2D(input.getX(), input.getY()); }
    
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
