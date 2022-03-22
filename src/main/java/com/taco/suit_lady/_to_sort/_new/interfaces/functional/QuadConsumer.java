package com.taco.suit_lady._to_sort._new.interfaces.functional;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <p>A {@link Consumer} that {@link #accept(Object, Object, Object, Object) accepts} {@code 4} input parameters to produce an action.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li><i>See {@link Consumer} for a {@link Consumer} that accepts {@code 1} input parameter.</i></li>
 *     <li><i>See {@link BiConsumer} for a {@link Consumer} that accepts {@code 2} input parameters.</i></li>
 *     <li><i>See {@link TriConsumer} for a {@link Consumer} that accepts {@code 3} input parameters.</i></li>
 * </ol>
 *
 * @param <A> The {@code first} input parameter {@link Class type} of the {@link QuadConsumer}.
 * @param <B> The {@code second} input parameter {@link Class type} of the {@link QuadConsumer}.
 * @param <C> The {@code third} input parameter {@link Class type} of the {@link QuadConsumer}.
 * @param <D> The {@code fourth} input parameter {@link Class type} of the {@link QuadConsumer}.
 *
 * @see Consumer
 * @see BiConsumer
 * @see TriConsumer
 */
@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {
    
    /**
     * <p>The {@link FunctionalInterface Functional} method of the {@link QuadConsumer} interface.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Accepts {@code 4} input parameters of types {@link A}, {@link B}, {@link C}, and {@link D}.</li>
     *     <li>Executes a {@link Void void} operation based on the aforementioned input parameters.</li>
     * </ol>
     *
     * @param a The {@code first} input parameter of type {@link A}.
     * @param b The {@code second} input parameter of type {@link B}.
     * @param c The {@code third} input parameter of type {@link C}.
     * @param d The {@code fourth} input parameter of type {@link D}.
     */
    void accept(A a, B b, C c, D d);
}
