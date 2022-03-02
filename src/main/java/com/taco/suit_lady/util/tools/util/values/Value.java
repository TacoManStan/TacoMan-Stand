package com.taco.suit_lady.util.tools.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record Value<A>(A a)
        implements Valueable<A> {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    
    public @NotNull <B> ValuePair<A, B> pair(@Nullable B b) { return new ValuePair<>(a(), b); }
    public @NotNull <B, C> ValueTrio<A, B, C> trio(@Nullable B b, @Nullable C c) { return new ValueTrio<>(a(), b, c); }
}
