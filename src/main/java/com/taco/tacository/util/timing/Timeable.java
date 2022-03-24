package com.taco.tacository.util.timing;

/**
 * <p>Defines a {@link ReadOnlyTimeable} implementation that offers additional control over the {@link Timeable} instance:</p>
 * <ul>
 *     <li><b>{@link #start()}:</b> Starts this {@link Timeable} instance.</li>
 *     <li><b>{@link #reset()}:</b> Resets this {@link Timeable} instance.</li>
 *     <li><b>{@link #stop()}:</b> Stops this {@link Timeable} instance.</li>
 * </ul>
 *
 * @see ReadOnlyTimeable
 * @see ReadOnlyTimerable
 * @see ReadOnlyReactiveTimerable
 * @see Timerable
 * @see ReactiveTimerable
 * @see Timer
 * @see DebugTimer
 * @see Timers
 * @see Timing
 */
public interface Timeable
        extends ReadOnlyTimeable {
    ReadOnlyTimeable start();
    ReadOnlyTimeable reset();
    ReadOnlyTimeable stop();
}


/*
 * TODO LIST:
 * [S] Finish implementation.
 * [S] Make sure functionality of all action methods (start, stop, reset, etc) are consistent across all implementations of Timeable.
 */