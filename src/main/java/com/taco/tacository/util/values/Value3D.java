package com.taco.tacository.util.values;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record Value3D<A, B, C>(A a, B b, C c)
        implements ValueExpr3D<A, B, C> {
    
    public @NotNull Value<A> aResult() { return new Value<>(a()); }
    public @NotNull Value<B> bResult() { return new Value<>(b()); }
    public @NotNull Value<C> cResult() { return new Value<>(c()); }
    
    public @NotNull Value2D<A, B> abPair() { return new Value2D<>(a(), b()); }
    public @NotNull Value2D<A, C> acPair() { return new Value2D<>(a(), c()); }
    public @NotNull Value2D<B, C> bcPair() { return new Value2D<>(b(), c()); }
    
    //
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    public @NotNull Supplier<C> cSupplier() { return this::c; }
}
