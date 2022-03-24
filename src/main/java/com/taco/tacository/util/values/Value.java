package com.taco.tacository.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * <p>Defines an {@code immutable}, {@code barebones} implementation of {@link ValueExpr}.</p>
 *
 * @param a   The {@link #a() Value} contained within this {@link Value} object.
 * @param <A> The {@link Class} type of the {@link #a() Value} contained within this {@link ValueExpr} instance.
 *
 * @see ValueExpr
 */
public record Value<A>(A a)
        implements ValueExpr<A> {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    
    public @NotNull <B> Value2D<A, B> pair(@Nullable B b) { return new Value2D<>(a(), b); }
    public @NotNull <B, C> Value3D<A, B, C> trio(@Nullable B b, @Nullable C c) { return new Value3D<>(a(), b, c); }
}
