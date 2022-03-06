package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record Value2D<A, B>(A a, B b)
        implements ValueExpr2D<A, B> {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    
    public @NotNull <C> Value3D<A, B, C> trio(@Nullable C c) { return new Value3D<>(a(), b(), c); }
}