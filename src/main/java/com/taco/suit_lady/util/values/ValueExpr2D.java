package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueExpr2D<A, B>
        extends ValueExpr<A> {
    
    @Nullable B b();
    
    //
    
    default @NotNull ValueExpr2D<A, B> asValuePairable() { return new Value2D<>(a(), b()); }
    
    default @NotNull <C> ValueExpr3D<A, B, C> asValueTrioable(C c) { return new Value3D<>(a(), b(), c); }
}
