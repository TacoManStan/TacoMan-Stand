package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Defines all implementing {@link Object Objects} as containing a {@code pair} of {@code values} of types {@link A} and {@link B}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Use <i>{@link #a()}</i> to access the {@code first} value contained within this {@link ValueExpr2D}.</li>
 *     <li>Use <i>{@link #b()}</i> to access the {@code second} value contained within this {@link ValueExpr2D}.</li>
 * </ol>
 *
 * @param <A> The {@link Class} type of the {@link #a() First Value} contained within this {@link ValueExpr2D} instance.
 * @param <B> The {@link Class} type of the {@link #b() Second Value} contained within this {@link ValueExpr2D} instance.
 */
//TO-EXPAND
public interface ValueExpr2D<A, B>
        extends ValueExpr<A> {
    
    @Nullable B b();
    
    //
    
    default @NotNull ValueExpr2D<A, B> asValuePairable() { return new Value2D<>(a(), b()); }
    
    default @NotNull <C> ValueExpr3D<A, B, C> asValueTrioable(C c) { return new Value3D<>(a(), b(), c); }
}
