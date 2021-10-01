package com.taco.suit_lady.util.timing;

import java.util.concurrent.TimeUnit;

/**
 * Created by spenc on 5/17/2016.
 */
public class Timers {
	private Timers() {} //No instance

	/* *************************************************************************** *
	 *                                                                             *
	 * Stopwatch Timers                                                              *
	 *                                                                             *
	 * *************************************************************************** */

	public static Timer newStopwatchTimer() {
		return newStopwatchTimer(null);
	}

	public static Timer newStopwatchTimer(Runnable onTimeout) {
		return new Timer(-1L, onTimeout);
	}

	/* *************************************************************************** *
	 *                                                                             *
	 * Countdown Timers                                                            *
	 *                                                                             *
	 * *************************************************************************** */

	/**
	 * Creates and then returns a new {@link Timer} that counts down from the specified {@code timeout} (milliseconds).
	 * <ul>
	 * <li>{@link Number#longValue()} is always used when determining the value used for the {@code timeout}.</li>
	 * <li>A {@link Duration} can be used as the {@code timeout} to easily provide additional {@link TimeUnit} options.</li>
	 * </ul>
	 *
	 * @param timeout The time in which the new {@code Timer} will count down from. Specified in milliseconds.
	 * @return The newly created {@code Countdown Timer}.
	 */
	public static Timer newCountdownTimer(Number timeout) {
		return new Timer(timeout, null);
	}

	/* *************************************************************************** *
	 *                                                                             *
	 * Debug Timers                                                                *
	 *                                                                             *
	 * *************************************************************************** */

	public static DebugTimer newDebugTimer() {
		return newDebugTimer(null, -1L);
	}

	public static DebugTimer newDebugTimer(long limit) {
		return newDebugTimer(null, limit);
	}

	public static DebugTimer newDebugTimer(String name) {
		return newDebugTimer(name, -1L);
	}

	public static DebugTimer newDebugTimer(String name, long limit) {
		return new DebugTimer(name, limit);
	}
}
