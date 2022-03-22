package com.taco.suit_lady._to_sort._new.interfaces.functional;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>A {@link Consumer} that {@link #accept(Object, Object, Object) accepts} {@code 3} input parameters to produce an action.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li><i>See {@link Consumer} for a {@link Consumer} that accepts {@code 1} input parameter.</i></li>
 *     <li><i>See {@link BiConsumer} for a {@link Consumer} that accepts {@code 2} input parameters.</i></li>
 *     <li><i>See {@link QuadConsumer} for a {@link Consumer} that accepts {@code 4} input parameters.</i></li>
 * </ol>
 *
 * @param <A> The {@code first} input parameter {@link Class type} of the {@link TriConsumer}.
 * @param <B> The {@code second} input parameter {@link Class type} of the {@link TriConsumer}.
 * @param <C> The {@code third} input parameter {@link Class type} of the {@link TriConsumer}.
 *
 * @see Consumer
 * @see BiConsumer
 * @see QuadConsumer
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    
    /**
     * <p>The {@link FunctionalInterface Functional} method of the {@link TriConsumer} interface.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Accepts {@code 3} input parameters of types {@link A}, {@link B}, and {@link C}.</li>
     *     <li>Executes a {@link Void void} operation based on the aforementioned input parameters.</li>
     * </ol>
     *
     * @param a The {@code first} input parameter of type {@link A}.
     * @param b The {@code second} input parameter of type {@link B}.
     * @param c The {@code third} input parameter of type {@link C}.
     */
    void accept(A a, B b, C c);
}
