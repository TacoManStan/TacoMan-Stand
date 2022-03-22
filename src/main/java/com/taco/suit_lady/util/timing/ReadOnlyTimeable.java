package com.taco.suit_lady.util.timing;

/**
 * <p>Defines implementing {@link Object Objects} as a {@code Read Only Timeable}.</p>
 *
 * @see ReadOnlyTimerable
 * @see ReadOnlyReactiveTimerable
 * @see Timeable
 * @see Timerable
 * @see ReactiveTimerable
 * @see Timer
 * @see DebugTimer
 * @see Timers
 * @see Timing
 */
public interface ReadOnlyTimeable {
    
    long getElapsedTime();
    //	double getElapsedTime(TimeUnit timeUnit);
    
    long getStartTime();
    //	double getStartTime(TimeUnit timeUnit);
}
