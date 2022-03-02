package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record ValueTrio<A, B, C>(A a, B b, C c)
        implements ValueTrioable<A, B, C> {
    
    public @NotNull Value<A> aResult() { return new Value<>(a()); }
    public @NotNull Value<B> bResult() { return new Value<>(b()); }
    public @NotNull Value<C> cResult() { return new Value<>(c()); }
    
    public @NotNull ValuePair<A, B> abPair() { return new ValuePair<>(a(), b()); }
    public @NotNull ValuePair<A, C> acPair() { return new ValuePair<>(a(), c()); }
    public @NotNull ValuePair<B, C> bcPair() { return new ValuePair<>(b(), c()); }
    
    //
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    public @NotNull Supplier<C> cSupplier() { return this::c; }
}
