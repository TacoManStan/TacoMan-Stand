package com.taco.suit_lady.util.timing;

/**
 * <p>Defines a {@link ReadOnlyTimeable} implementation that offers additional control over the {@link Timeable} instance:</p>
 * <ul>
 *     <li>{@link #start()}: Starts this {@link Timeable} instance.</li>
 *     <li>{@link #reset()}: Resets this {@link Timeable} instance.</li>
 *     <li>{@link #stop()}: Stops this {@link Timeable} instance.</li>
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