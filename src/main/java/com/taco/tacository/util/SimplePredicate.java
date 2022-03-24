package com.taco.tacository.util;

import java.util.function.Predicate;

/**
 * <p>Defines a {@link Predicate} implementation that does not accept any <i>{@link #test(Object)}</i> parameters.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Defines a {@code default} implementation of <i>{@link Predicate#test(Object)}</i> that simply calls the <i>{@link #test()}</i> method of this {@link SimplePredicate} instance.</li>
 *     <li>The {@code default} <i>{@link #test(Object)}</i> implementation provided by {@link SimplePredicate} should never be overridden.</li>
 *     <li>Instead, a {@link SimplePredicate} is defined by the abstract <i>{@link #test()}</i> method.</li>
 * </ol>
 */
@FunctionalInterface
public interface SimplePredicate extends Predicate<Object> {
    
    /**
     * <p>Calculates and then returns a {@code boolean} reflecting the {@code result} of this {@link SimplePredicate}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li><i>{@link SimplePredicate#test() SimplePredicate.test()}</i> is identical to <i>{@link Predicate#test(Object)}</i> but with no {@code Input Parameter}.</li>
     * </ol>
     *
     * @return The value of this {@link SimplePredicate} {@link FunctionalInterface Function}.
     *
     * @see #test(Object)
     */
    boolean test();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    /**
     * <p><b>Passthrough Definition:</b></p>
     * <blockquote><i>{@link #test()}</i></blockquote>
     *
     * @param obj Ignored
     *
     * @see #test()
     */
    @Override default boolean test(Object obj) { return test(); }
    
    //</editor-fold>
}
