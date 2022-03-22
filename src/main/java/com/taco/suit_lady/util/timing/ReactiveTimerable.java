package com.taco.suit_lady.util.timing;

/**
 * <p>A {@link ReadOnlyReactiveTimerable} implementation that permits changes to the <i>{@link #onTimeoutProperty()}</i>.</p>
 *
 * @see ReadOnlyTimeable
 * @see ReadOnlyTimerable
 * @see Timeable
 * @see Timerable
 * @see ReactiveTimerable
 * @see Timer
 * @see DebugTimer
 * @see Timers
 * @see Timing
 */
public interface ReactiveTimerable
        extends ReadOnlyReactiveTimerable {
    
    void setOnTimeout(Runnable onTimeout);
}
