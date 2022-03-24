package com.taco.tacository.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Defines all implementing {@link Object Objects} as containing a {@link #a() Value} of type {@link A}.</p>
 *
 * @param <A> The type of {@link #a() Value} contained by this {@link ValueExpr}.
 */
//TO-EXPAND
public interface ValueExpr<A> {
    
    @Nullable A a();
    
    //
    
    default @NotNull ValueExpr<A> asValueable() { return new Value<>(a()); }
    
    default @NotNull <B> ValueExpr2D<A, B> asValuePairable(B b) { return new Value2D<>(a(), b); }
    default @NotNull <B, C> ValueExpr3D<A, B, C> asValueTrioable(B b, C c) { return new Value3D<>(a(), b, c); }
}
