package com.taco.tacository._to_sort._new.interfaces.functional;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>A {@link Function} that accepts {@code 3} input parameters to produce a {@code result} of type {@link R}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li><i>See {@link Function} for a {@link Function} that accepts {@code 1} input parameter.</i></li>
 *     <li><i>See {@link BiFunction} for a {@link Function} that accepts {@code 2} input parameters.</i></li>
 *     <li><i>See {@link QuadFunction} for a {@link Function} that accepts {@code 4} input parameters.</i></li>
 * </ol>
 *
 * @param <A> The {@code first} input parameter {@link Class type} of the {@link TriFunction}.
 * @param <B> The {@code second} input parameter {@link Class type} of the {@link TriFunction}.
 * @param <C> The {@code third} input parameter {@link Class type} of the {@link TriFunction}.
 * @param <R> The {@code result} {@link Class type} returned by the abstract <i>{@link #apply(Object, Object, Object)}</i> method.
 *
 * @see Function
 * @see BiFunction
 * @see QuadFunction
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    
    /**
     * <p>The {@link FunctionalInterface Functional} method of the {@link TriFunction} interface.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Accepts {@code 3} input parameters of types {@link A}, {@link B}, and {@link C}.</li>
     *     <li>Returns a {@code value} of type {@link R} based on aforementioned {@code parameter input values}.</li>
     * </ol>
     *
     * @param a The {@code first} input parameter of type {@link A}.
     * @param b The {@code second} input parameter of type {@link B}.
     * @param c The {@code third} input parameter of type {@link C}.
     *
     * @return The {@code result value} of this {@link TriFunction} based on input parameters {@link A a}, {@link B b}, and {@link C c}.
     */
    R apply(A a, B b, C c);
}
