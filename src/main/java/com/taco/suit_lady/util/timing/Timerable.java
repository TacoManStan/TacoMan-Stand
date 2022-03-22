package com.taco.suit_lady.util.timing;

/**
 * <p>An aggregate implementation of {@link Timeable} and {@link ReadOnlyTimerable}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li><b>{@link #setTimeout(Number)}:</b> Sets the {@link #timeoutProperty() timeout} of this {@link Timerable} to the specified value.</li>
 *     <li>
 *         <b>{@link #isTimedOut()}:</b> Checks if this {@link Timerable} has {@link #timeoutProperty() timed out}.
 *         <ul>
 *             <li><i>Equivalent to the value of [{@link #getTimeout()} <b>-</b> {@link #getElapsedTime()} < 0]</i></li>
 *             <li><i>Also, equivalent to the value of [{@link #getRemainingTime()} <= 0]</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @see ReadOnlyTimeable
 * @see ReadOnlyTimerable
 * @see ReadOnlyReactiveTimerable
 * @see Timeable
 * @see ReactiveTimerable
 * @see Timer
 * @see DebugTimer
 * @see Timers
 * @see Timing
 */
public interface Timerable
        extends ReadOnlyTimerable, Timeable {
    
    void setTimeout(Number timeout);
    boolean isTimedOut();
    
    ReadOnlyTimerable start(Number newTimeout);
    ReadOnlyTimerable reset(Number newTimeout);
}
