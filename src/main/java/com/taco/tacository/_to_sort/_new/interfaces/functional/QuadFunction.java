package com.taco.tacository._to_sort._new.interfaces.functional;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>A {@link Function} that accepts {@code 4} input parameters to produce a {@code result} of type {@link R}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li><i>See {@link Function} for a {@link Function} that accepts {@code 1} input parameter.</i></li>
 *     <li><i>See {@link BiFunction} for a {@link Function} that accepts {@code 2} input parameters.</i></li>
 *     <li><i>See {@link TriFunction} for a {@link Function} that accepts {@code 3} input parameters.</i></li>
 * </ol>
 *
 * @param <A> The {@code first} input parameter {@link Class type} of this {@link QuadFunction}.
 * @param <B> The {@code second} input parameter {@link Class type} of this {@link QuadFunction}.
 * @param <C> The {@code third} input parameter {@link Class type} of this {@link QuadFunction}.
 * @param <D> The {@code fourth} input parameter {@link Class type} of this {@link QuadFunction}.
 * @param <R> The {@code result} {@link Class type} returned by the abstract <i>{@link #apply(Object, Object, Object, Object)}</i> method.
 *
 * @see Function
 * @see BiFunction
 * @see QuadFunction
 */
@FunctionalInterface
public interface QuadFunction<A, B, C, D, R> {
    
    /**
     * <p>The {@link FunctionalInterface Functional} method of the {@link QuadFunction} interface.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Accepts {@code 4} input parameters of types {@link A}, {@link B}, {@link C}, and {@link D}.</li>
     *     <li>Returns a {@code value} of type {@link R} based on aforementioned {@code parameter input values}.</li>
     * </ol>
     *
     * @param a The {@code first} input parameter of type {@link A}.
     * @param b The {@code second} input parameter of type {@link B}.
     * @param c The {@code third} input parameter of type {@link C}.
     * @param d The {@code fourth} input parameter of type {@link D}.
     *
     * @return The {@code result value} of this {@link QuadFunction} based on input parameters {@link A a}, {@link B b}, {@link C c}, and {@link D d}.
     */
    R apply(A a, B b, C c, D d);
}
