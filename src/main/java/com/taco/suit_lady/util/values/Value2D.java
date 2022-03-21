package com.taco.suit_lady.util.values;

import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * <p>Defines a {@code default}, {@code immutable} implementation of {@link ValueExpr2D}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link Num2D} record provides an alternative {@link ValueExpr2D} implementation that contains only {@link Number Numbers}.</li>
 * </ol>
 *
 * @param a   The {@code first} {@link #a() Value} contained within this {@link Value2D} instance.
 * @param b   The {@code second} {@link #b() Value} contained within this {@link Value2D} instance.
 * @param <A> The {@link Class} type of the {@link #a() First Value} contained within this {@link ValueExpr2D} instance.
 * @param <B> The {@link Class} type of the {@link #b() Second Value} contained within this {@link ValueExpr2D} instance.
 *
 * @see ValueExpr2D
 * @see NumExpr2D
 * @see Num2D
 */
//TO-EXPAND
public record Value2D<A, B>(A a, B b)
        implements ValueExpr2D<A, B> {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    
    public @NotNull <C> Value3D<A, B, C> trio(@Nullable C c) { return new Value3D<>(a(), b(), c); }
}