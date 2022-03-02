package com.taco.suit_lady.ui.jfx.util;

import com.taco.suit_lady.util.tools.util.values.ValuePair;
import com.taco.suit_lady.util.tools.util.values.NumberValuePairable;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.Function;

public record Dimensions(int width, int height)
        implements Serializable, NumberValuePairable<Dimensions> {
    
    //<editor-fold desc="--- COPY METHODS ---">
    
    @Contract(" -> new") public @NotNull Dimensions copy() { return new Dimensions(width(), height()); }
    
    public @NotNull ValuePair<Integer, Integer> copyAsPair() { return new ValuePair<>(width(), height()); }
    public @NotNull Point2D copyAsPoint() { return new Point2D(width(), height()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    @Contract("_ -> new") public static @NotNull Dimensions copyFrom(@NotNull Dimensions dimensions) { return dimensions.copy(); }
    
    public static @NotNull Dimensions copyFromPair(@NotNull ValuePair<? extends Number, ? extends Number> pair) { return new Dimensions(pair.a().intValue(), pair.b().intValue()); }
    public static @NotNull Dimensions copyFromPoint(@NotNull Point2D point) { return new Dimensions((int) point.getX(), (int) point.getY()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @Nullable Integer a() { return width(); }
    @Override public @Nullable Integer b() { return height(); }
    
    
    @Override public @NotNull Dimensions modify(Function<Number, Number> aFunction) { return new Dimensions(aFunction.apply(a()).intValue(), b()); }
    @Override public @NotNull Dimensions modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction) {
        return new Dimensions(aFunction.apply(a()).intValue(), bFunction.apply(b()).intValue());
    }
    
    //
    
    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return "Dimensions{" +
               "width=" + width +
               ", height=" + height +
               '}';
    }
    
    //</editor-fold>
}
