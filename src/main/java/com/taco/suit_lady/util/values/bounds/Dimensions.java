package com.taco.suit_lady.util.values.bounds;

import com.taco.suit_lady.util.values.OpResultType;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.Function;

public record Dimensions(@NotNull Number width, @NotNull Number height, @NotNull OpResultType resultType)
        implements Serializable, NumExpr2D<Dimensions> {
    
    public Dimensions(@NotNull Number width, @NotNull Number height) { this(width, height, OpResultType.EXACT); }
    
    //<editor-fold desc="--- COPY METHODS ---">
    
    @Contract(" -> new") public @NotNull Dimensions copy() { return new Dimensions(width(), height(), resultType()); }
    
    public @NotNull Value2D<Number, Number> copyAsPair() { return new Value2D<>(width(), height()); }
    public @NotNull Point2D copyAsPoint() { return new Point2D(aDouble(), bDouble()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    @Contract("_ -> new") public static @NotNull Dimensions copyFrom(@NotNull Dimensions dimensions) { return dimensions.copy(); }
    
    public static @NotNull Dimensions copyFromPair(@NotNull Value2D<? extends Number, ? extends Number> value2D, @NotNull OpResultType resultType) {
        return new Dimensions(value2D.a().intValue(), value2D.b().intValue(), resultType);
    }
    public static @NotNull Dimensions copyFromPair(@NotNull Value2D<? extends Number, ? extends Number> value2D) { return copyFromPair(value2D, OpResultType.EXACT); }
    
    public static @NotNull Dimensions copyFromPoint(@NotNull Point2D point, @NotNull OpResultType resultType) {
        return new Dimensions((int) point.getX(), (int) point.getY(), resultType);
    }
    public static @NotNull Dimensions copyFromPoint(@NotNull Point2D point) { return copyFromPoint(point, OpResultType.EXACT); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @Nullable Number a() { return width(); }
    @Override public @Nullable Number b() { return height(); }
    
    
    @Override public @NotNull Dimensions modify(Function<Number, Number> aFunction) {
        return new Dimensions(aFunction.apply(a()).intValue(), b(), resultType());
    }
    @Override public @NotNull Dimensions modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction) {
        return new Dimensions(aFunction.apply(a()).intValue(), bFunction.apply(b()).intValue(), resultType());
    }
    
    //
    
    @Contract(pure = true)
    @Override public @NotNull String toString() {
        return "Dimensions{" +
               "width=" + width +
               ", height=" + height +
               ", resultType=" + resultType +
               '}';
    }
    
    
    //</editor-fold>
}
