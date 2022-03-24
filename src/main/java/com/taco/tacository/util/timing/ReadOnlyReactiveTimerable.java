package com.taco.tacository.util.timing;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * <p>Defines implementing {@link Object Objects} as being able to {@link #onTimeoutProperty() respond} to a {@link Timerable} {@link Timerable#timeoutProperty() Timeout Event}.</p>
 *
 * @see ReadOnlyTimeable
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
//TO-EXPAND: Elaborate
public interface ReadOnlyReactiveTimerable {
    
    ReadOnlyObjectProperty<Runnable> onTimeoutProperty();
    Runnable getOnTimeout();
}
