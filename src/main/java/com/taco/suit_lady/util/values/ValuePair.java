package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record ValuePair<A, B>(A a, B b)
        implements ValuePairable<A, B> {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    
    public @NotNull <C> ValueTrio<A, B, C> trio(@Nullable C c) { return new ValueTrio<>(a(), b(), c); }
}