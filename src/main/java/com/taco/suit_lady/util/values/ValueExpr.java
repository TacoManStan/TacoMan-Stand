package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("SpellCheckingInspection")
public interface ValueExpr<A> {
    
    @Nullable A a();
    
    //
    
    default @NotNull ValueExpr<A> asValueable() { return new Value<>(a()); }
    
    default @NotNull <B> ValueExpr2D<A, B> asValuePairable(B b) { return new Value2D<>(a(), b); }
    default @NotNull <B, C> ValueExpr3D<A, B, C> asValueTrioable(B b, C c) { return new Value3D<>(a(), b, c); }
}
