package com.taco.suit_lady.ui.jfx.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Provides factory methods for timers that are manipulated from and execute
 * their action on the JavaFX application thread.
 */
public class FXTimer
{

	/**
	 * Prepares a (stopped) timer that lasts for {@code delay} and whose action runs when timer <em>ends</em>.
	 */
	public static FXTimer create(java.time.Duration delay, Runnable action) {
		return new FXTimer(delay, delay, action, 1);
	}

	/**
	 * Equivalent to {@code create(delay, action).restart()}.
	 */
	public static FXTimer runLater(java.time.Duration delay, Runnable action) {
		FXTimer timer = create(delay, action);
		timer.restart();
		return timer;
	}

	/**
	 * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
	 * when the timer <em>ends</em>.
	 */
	public static FXTimer createPeriodic(java.time.Duration interval, Runnable action) {
		return new FXTimer(interval, interval, action, Animation.INDEFINITE);
	}

	/**
	 * Equivalent to {@code createPeriodic(interval, action).restart()}.
	 */
	public static FXTimer runPeriodically(java.time.Duration interval, Runnable action) {
		FXTimer timer = createPeriodic(interval, action);
		timer.restart();
		return timer;
	}

	/**
	 * Prepares a (stopped) timer that lasts for {@code interval} and that executes the given action periodically
	 * when the timer <em>starts</em>.
	 */
	public static FXTimer createPeriodic0(java.time.Duration interval, Runnable action) {
		return new FXTimer(java.time.Duration.ZERO, interval, action, Animation.INDEFINITE);
	}

	/**
	 * Equivalent to {@code createPeriodic0(interval, action).restart()}.
	 */
	public static FXTimer runPeriodically0(java.time.Duration interval, Runnable action) {
		FXTimer timer = createPeriodic0(interval, action);
		timer.restart();
		return timer;
	}

	private final Duration actionTime;
	private final Timeline timeline;
	private final Runnable action;

	private long seq = 0;

	private FXTimer(java.time.Duration actionTime, java.time.Duration period, Runnable action, int cycles) {
		this.actionTime = Duration.millis(actionTime.toMillis());
		this.timeline = new Timeline();
		this.action = action;

		timeline.getKeyFrames().add(new KeyFrame(this.actionTime)); // used as placeholder
		if (period != actionTime) {
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(period.toMillis())));
		}

		timeline.setCycleCount(cycles);
	}

	public void restart() {
		stop();
		long expected = seq;
		timeline.getKeyFrames().set(0, new KeyFrame(actionTime, ae -> {
			if (seq == expected) {
				action.run();
			}
		}));
		timeline.play();
	}

	public void stop() {
		timeline.stop();
		++seq;
	}
}