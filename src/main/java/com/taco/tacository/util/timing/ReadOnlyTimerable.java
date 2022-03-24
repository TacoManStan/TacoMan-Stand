package com.taco.tacository.util.timing;

import javafx.beans.property.ReadOnlyLongProperty;

/**
 * <p>Defines a {@link ReadOnlyTimeable} implementation with additional {@link Timing} properties.</p>
 * <p><b>Properties</b></p>
 * <ol>
 *     <li><b>{@link #timeoutProperty()}:</b> A {@link ReadOnlyLongProperty} reflecting the {@link #getTimeout() timeout} of this {@link ReadOnlyTimerable} instance.</li>
 *     <li><b>{@link #getTimeout()}:</b> The value returned by {@link #timeoutProperty()}.</li>
 *     <li><b>{@link #isStarted()}:</b> Checks if this {@link ReadOnlyTimerable} has been {@link #isStarted() started}.</li>
 *     <li><b>{@link #isStopped()}:</b> Checks if this {@link ReadOnlyTimerable} has been {@link #isStopped() stopped}.</li>
 *     <li>
 *         <b>{@link #getRemainingTime()}:</b> Returns the amount of time remaining for this {@link ReadOnlyTimerable}.
 *         <ul>
 *             <li><i>Equal to the value returned by {@link #getTimeout()} <b>-</b> {@link #getElapsedTime()}.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @see ReadOnlyTimeable
 * @see ReadOnlyReactiveTimerable
 * @see Timeable
 * @see Timerable
 * @see ReactiveTimerable
 * @see Timer
 * @see DebugTimer
 * @see Timers
 * @see Timing
 */
public interface ReadOnlyTimerable
        extends ReadOnlyTimeable {
    
    ReadOnlyLongProperty timeoutProperty();
    long getTimeout();
    
    boolean isStarted();
    boolean isStopped();
    
    long getRemainingTime();
}
