package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.tools.CalculationTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

// TODO - Rewrite/update ALL JavaDocs

/**
 * Allows for the creation of various countdown timers.
 *
 * To create a new Timer object, refer to the {@link Timers} class.
 *
 * @see Timers
 */
public class Timer
		implements Timeable {

	private final ReadOnlyLongWrapper timeoutProperty;
	private long startTime;
	private boolean stopped;

	private final ReadOnlyObjectWrapper<Runnable> onTimeoutProperty;

	/**
	 * Constructs a new timer with the specified timeout (ms) and timeout action.
	 *
	 * @param timeout   The timeout. In milliseconds.
	 * @param onTimeout A Runnable that is executed when the timer times out. Null for no action.
	 * @see Timers
	 */
	protected Timer(Number timeout, Runnable onTimeout) {
		this.timeoutProperty = new ReadOnlyLongWrapper(timeout.longValue());
		this.startTime = 0L;
		this.stopped = false;

		this.onTimeoutProperty = new ReadOnlyObjectWrapper<>(onTimeout);
	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the timeout property for this timer.
	 *
	 * @return The timeout property for this timer.
	 */
	public final ReadOnlyLongProperty timeoutProperty() {
		return timeoutProperty.getReadOnlyProperty();
	}

	/**
	 * Returns the timeout of the timer.
	 *
	 * @return The timeout.
	 */
	public final long getTimeout() {
		return timeoutProperty.get();
	}

	/**
	 * Sets the timeout of the timer to be equal to the specified timeout.
	 *
	 * @param timeout The timeout. Any number less than 0 will result in no timeout being used.
	 */
	public final void setTimeout(Number timeout) {
		timeoutProperty.set(CalculationTools.clampMin(timeout.longValue(), -1L));
	}

	//

	/**
	 * Checks to see if the timer has been started or not.
	 * <p>
	 * This method differs from {@link Timer#isStopped()} in that this method will return true if this method has ever been started, regardless if the timer is currently stopped.
	 *
	 * @return True if the timer has been started, false otherwise.
	 */
	public final boolean isStarted() {
		return startTime != 0;
	}

	/**
	 * Checks to see if the timer is stopped or not.
	 * <p>
	 * This method differs from {@link Timer#isStarted()} in that this method will return true even if the timer has been previously started.
	 *
	 * @return True if the timer is stopped, false otherwise.
	 */
	public final boolean isStopped() {
		return stopped;
	}

	//

	public final ReadOnlyObjectProperty<Runnable> onTimeoutProperty() {
		return onTimeoutProperty.getReadOnlyProperty();
	}

	public final Runnable getOnTimeout() {
		return onTimeoutProperty.get();
	}

	public final void setOnTimeout(Runnable onTimeout) {
		onTimeoutProperty.set(onTimeout);
	}

	//</editor-fold>

	//

	/**
	 * Gets the start time of the timer.
	 *
	 * @return The start time. 0 if the timer has not yet been started.
	 */
	@Override public final long getStartTime() {
		return startTime;
	}

	/**
	 * Gets the time that has elapsed since the start of the timer.
	 *
	 * @return The time that has elapsed since the start of the timer.
	 */
	@Override public final long getElapsedTime() {
		return Timing.currentTimeMillis() - getStartTime();
	}

	/**
	 * Gets the time left before the timer times out.
	 * <p>
	 * If this timer is not using a timeout, {@link Double#POSITIVE_INFINITY} is returned.
	 *
	 * @return The amount of milliseconds before the timer times out.
	 */
	public final long getRemainingTime() {
		return (getStartTime() + getTimeout()) - Timing.currentTimeMillis();
	}

	/**
	 * Checks to see whether or not the timer has timed out.
	 *
	 * @return True if the timer has timed out, false otherwise.
	 */
	public final boolean isTimedOut() {
		final long timeout = getTimeout();
		return timeout >= 0 && (stopped || Timing.currentTimeMillis() > startTime + timeout);
	}

	//

	/**
	 * Starts this timer.
	 */
	@Override public final void start() {
		this.stopped = false;
		this.startTime = Timing.currentTimeMillis();
	}

	/**
	 * Starts and then returns this timer.
	 *
	 * @return This timer.
	 */
	@Override public final Timer startAndGet() {
		start();
		return this;
	}

	/**
	 * Starts this timer, setting the timer's timeout to the given value.
	 *
	 * @param newTimeout The new timeout for this timer. -1 to start the timer without changing the timeout.
	 */
	public final void start(Number newTimeout) {
		long long_new_timeout = ExceptionTools.nullCheck(newTimeout, "New Timeout").longValue();
		setTimeout(long_new_timeout);
		start();
	}

	/**
	 * Resets the timer. Calling this method has no effect if the timer has not yet been started.
	 */
	@Override public final void reset() {
		reset(-1);
	}

	/**
	 * Resets this timer, setting the timer's timeout to the given value.
	 *
	 * @param newTimeout The new timeout for this timer. -1 to not change the timeout. Cannot be less than -1.
	 */
	public final void reset(Number newTimeout) {
		long long_new_timeout = ExceptionTools.nullCheck(newTimeout, "New Timeout").longValue();
		if (getStartTime() == 0)
			return;
		start(long_new_timeout);
	}

	/**
	 * Stops this timer.
	 */
	@Override public final void stop() {
		stopped = true;
	}
}
