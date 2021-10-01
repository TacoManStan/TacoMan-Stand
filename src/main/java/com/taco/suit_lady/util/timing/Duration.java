package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.ExceptionTools;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.WritableLongValue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides an easy way to manage durations in various time units.
 */
public class Duration extends Number
		implements ObservableLongValue, WritableLongValue, ReadOnlyProperty<Number> {

	private Object bean;

	private final Lock lock;
	private final ReadOnlyLongWrapper durationProperty;

	private boolean isPrecise; // TODO [S]

	//

	public Duration() {
		this(0L, TimeUnit.MILLISECONDS);
	}

	public Duration(Lock lock) {
		this(lock, 0L, TimeUnit.MILLISECONDS);
	}

	public Duration(long duration) {
		this(duration, TimeUnit.MILLISECONDS);
	}

	public Duration(Lock lock, long duration) {
		this(lock, duration, TimeUnit.MILLISECONDS);
	}

	public Duration(long duration, TimeUnit inputTimeUnit) {
		this(new ReentrantLock(), duration, inputTimeUnit);
	}

	public Duration(Lock lock, long duration, TimeUnit inputTimeUnit) {
		ExceptionTools.nullCheck(inputTimeUnit, "Input Time Unit");

		this.lock = lock; // Lockable.isNullable() is true, so allow the Lock to be null.
		this.durationProperty = new ReadOnlyLongWrapper();

		//

		set(duration, inputTimeUnit);
	}

	public javafx.util.Duration toJFXDuration() {
		return new javafx.util.Duration(get());
	}

	//<editor-fold desc="Get/Set">

	ReadOnlyLongWrapper impl_durationProperty() {
		return durationProperty;
	}

	// Get

	/**
	 * Returns the time of this {@code Duration} in milliseconds.
	 *
	 * @return The time of this {@code Duration} in milliseconds.
	 */
	@Override public long get() {
		return durationProperty.get();
	}

	/**
	 * Returns the time of this {@code Duration} in the specified {@link TimeUnit}.
	 *
	 * @param outputTimeUnit The {@link TimeUnit} the value returned by this method should be in.
	 * @return The time of this {@code Duration} in the specified {@link TimeUnit}.
	 */
	public long getIn(TimeUnit outputTimeUnit) {
		ExceptionTools.nullCheck(outputTimeUnit, "Output Time Unit");
		if (outputTimeUnit == TimeUnit.MILLISECONDS)
			return get(); // No need for conversion if TimeUnit is MILLISECONDS
		return TimeUnit.MILLISECONDS.convert(durationProperty.get(), outputTimeUnit);
	}


	// Set

	@Override
	public void set(long duration) {
		ExceptionTools.boundsCheckZero(duration, "Duration");
		durationProperty.set(duration);
	}

	public void set(long duration, TimeUnit inputTimeUnit) {
		ExceptionTools.boundsCheckZero(duration, "Duration");
		ExceptionTools.nullCheck(inputTimeUnit, "Input Time Unit");
		if (inputTimeUnit != TimeUnit.MILLISECONDS) // No need for conversion if TimeUnit is MILLISECONDS
			durationProperty.set(duration);
		durationProperty.set(TimeUnit.MILLISECONDS.convert(duration, inputTimeUnit));
	}

	//</editor-fold>

	//<editor-fold desc="Implementations">

	// Copyable
	
	public boolean copyOf(Duration toCopy, Object... objs) {
		ExceptionTools.nullCheck(toCopy, "Copying Duration");
		set(toCopy.get());
		return true;
	}

	//

	//<editor-fold desc="Property">

	@Override public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	@Override public String getName() {
		return "Duration";
	}

	//

	@Override public Number getValue() {
		return get();
	}

	@Override public void setValue(Number value) {
		set(ExceptionTools.nullCheck(value).longValue());
	}

	//

	@Override public void addListener(InvalidationListener listener) {
		durationProperty.addListener(ExceptionTools.nullCheck(listener, "Invalidation Listener"));
	}

	@Override public void removeListener(InvalidationListener listener) {
		durationProperty.removeListener(ExceptionTools.nullCheck(listener, "Invalidation Listener"));
	}

	@Override public void addListener(ChangeListener<? super Number> listener) {
		durationProperty.addListener(ExceptionTools.nullCheck(listener, "Change Listener"));
	}

	@Override public void removeListener(ChangeListener<? super Number> listener) {
		durationProperty.removeListener(ExceptionTools.nullCheck(listener, "Change Listener"));
	}

	//</editor-fold>

	//<editor-fold desc="Number">

	@Override public long longValue() {
		return get();
	}

	@Override public int intValue() {
		return (int) longValue();
	}

	@Override public float floatValue() {
		return (float) longValue();
	}

	@Override public double doubleValue() {
		return (double) longValue();
	}

	//</editor-fold>

	//</editor-fold>

	//<editor-fold desc="Factory Methods">

	public static Duration nanos(long duration) {
		return nanos(null, duration);
	}

	public static Duration nanos(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.NANOSECONDS);
	}

	public static Duration micros(long duration) {
		return micros(null, duration);
	}

	public static Duration micros(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.MICROSECONDS);
	}

	public static Duration millis(long duration) {
		return millis(null, duration);
	}

	public static Duration millis(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.MILLISECONDS);
	}

	public static Duration seconds(long duration) {
		return seconds(null, duration);
	}

	public static Duration seconds(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.SECONDS);
	}

	public static Duration minutes(long duration) {
		return minutes(null, duration);
	}

	public static Duration minutes(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.MINUTES);
	}

	public static Duration hours(long duration) {
		return hours(null, duration);
	}

	public static Duration hours(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.HOURS);
	}

	public static Duration days(long duration) {
		return days(null, duration);
	}

	public static Duration days(Lock lock, long duration) {
		return new Duration(lock, duration, TimeUnit.DAYS);
	}

	//</editor-fold>
}
