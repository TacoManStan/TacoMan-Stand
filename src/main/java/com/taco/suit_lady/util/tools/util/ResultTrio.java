package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record ResultTrio<A, B, C>(A a, B b, C c) {
    
    public @NotNull Result<A> aResult() { return new Result<>(a()); }
    public @NotNull Result<B> bResult() { return new Result<>(b()); }
    public @NotNull Result<C> cResult() { return new Result<>(c()); }
    
    public @NotNull ResultPair<A, B> abPair() { return new ResultPair<>(a(), b()); }
    public @NotNull ResultPair<A, C> acPair() { return new ResultPair<>(a(), c()); }
    public @NotNull ResultPair<B, C> bcPair() { return new ResultPair<>(b(), c()); }
    
    //
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    public @NotNull Supplier<B> bSupplier() { return this::b; }
    public @NotNull Supplier<C> cSupplier() { return this::c; }
}
