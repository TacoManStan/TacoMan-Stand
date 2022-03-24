package com.taco.tacository.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueExpr3D<A, B, C>
        extends ValueExpr2D<A, B> {
    
    @Nullable C c();
    
    //
    
    default @NotNull ValueExpr3D<A, B, C> asValueTrioable() { return new Value3D<>(a(), b(), c()); }
}