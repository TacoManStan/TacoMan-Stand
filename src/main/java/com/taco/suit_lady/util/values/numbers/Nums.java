package com.taco.suit_lady.util.values.numbers;

import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

public class Nums {
    private Nums() { } //No Instance
    
    //<editor-fold desc="--- DIMENSIONS OF METHODS ---">
    
    public static @NotNull Num2D dimensionsOf(@NotNull Region region) { return new Num2D(region.getWidth(), region.getHeight()); }
    public static @NotNull Num2D dimensionsOfPref(@NotNull Region region) { return new Num2D(region.getPrefWidth(), region.getPrefHeight()); }
    public static @NotNull Num2D dimensionsOfMin(@NotNull Region region) { return new Num2D(region.getMinWidth(), region.getMinHeight()); }
    public static @NotNull Num2D dimensionsOfMax(@NotNull Region region) { return new Num2D(region.getMaxWidth(), region.getMaxHeight()); }
    
    //</editor-fold>
}
