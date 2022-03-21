package com.taco.suit_lady.util.values.numbers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * <p>Defines an {@code immutable}, {@code default} implementation of {@link NumExpr2D}.</p>
 *
 * @param a The {@link #a() First} {@link Number Number Value} contained within this {@link NumExpr2D} instance.
 * @param b The {@link #b() Second} {@link Number Number Value} contained within this {@link NumExpr2D} instance.
 */
public record Num2D(Number a, Number b)
        implements NumExpr2D<Num2D> {
    
    @Contract("_ -> new")
    @Override public @NotNull Num2D modify(@NotNull Function<Number, Number> aFunction) {
        return new Num2D(aFunction.apply(a()), b());
    }
    
    @Override public String toString() {
        return getString(false);
    }
    
    //
    
    public static <T extends NumExpr2D<T>> @NotNull Function<Number, Number> emptyModifier() { return number -> number; }
    
}
