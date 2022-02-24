package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record Result<A>(A a) {
    
    public @NotNull Supplier<A> aSupplier() { return this::a; }
    
    public @NotNull <B> ResultPair<A, B> pair(@Nullable B b) { return new ResultPair<>(a(), b); }
    public @NotNull <B, C> ResultTrio<A, B, C> trio(@Nullable B b, @Nullable C c) { return new ResultTrio<>(a(), b, c); }
}
