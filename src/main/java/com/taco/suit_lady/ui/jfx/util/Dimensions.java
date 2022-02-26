package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.util.ValuePair;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record Dimensions(int width, int height)
        implements Serializable {
    
    //<editor-fold desc="--- COPY METHODS ---">
    
    @Contract(" -> new") public @NotNull Dimensions copy() { return new Dimensions(width(), height()); }
    
    public @NotNull ValuePair<Integer, Integer> copyAsPair() { return new ValuePair<>(width(), height()); }
    public @NotNull Point2D copyAsPoint() { return new Point2D(width(), height()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    @Contract("_ -> new") public static @NotNull Dimensions copyFrom(@NotNull Dimensions dimensions) { return dimensions.copy(); }
    
    public static @NotNull Dimensions copyFromPair(@NotNull ValuePair<Integer, Integer> pair) { return new Dimensions(pair.a(), pair.b()); }
    public static @NotNull Dimensions copyFromPoint(@NotNull Point2D point) { return new Dimensions((int) point.getX(), (int) point.getY()); }
    
    //</editor-fold>
}
