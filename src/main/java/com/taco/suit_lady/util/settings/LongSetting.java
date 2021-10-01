package com.taco.suit_lady.util.settings;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.WritableLongValue;

public class LongSetting extends NumberSetting<Long>
		implements ObservableLongValue, WritableLongValue {

	public LongSetting(LongProperty observable) {
		this(observable, "");
	}

	public LongSetting(LongProperty observable, String description) {
		this(observable, description, -1);
	}

	public LongSetting(LongProperty observable, String description, long defaultValue) {
		super(observable, defaultValue);
		setDescription(description);
	}

	public LongSetting(String settingName) {
		this(settingName, "");
	}

	public LongSetting(String settingName, String description) {
		this(settingName, description, 0);
	}

	public LongSetting(String settingName, String description, long defaultValue) {
		super(settingName, description, defaultValue);
	}

	//<editor-fold desc="Get/Set">

	/**
	 * Returns the {@link #getValue() value} of this {@link LongSetting} a primitive.
	 * <p>
	 * If the {@link #getValue() value} is null, this method returns -1.
	 * @return The {@link #getValue() value} of this {@link LongSetting} a primitive.
	 */
	@Override public long get() {
		final Number value = getValue();
		if (value != null)
			return value.longValue();
		return -1;
	}

	/**
	 * Sets the {@link #getValue() value} of this {@link IntegerSetting} to the specified value.
	 * @param value The {@link #getValue() value}.
	 */
	@Override public void set(long value) {
		setValue(value);
	}

	@Override protected LongProperty createProperty() {
		return new SimpleLongProperty();
	}

	//</editor-fold>

	@Override protected Long memoryValue(boolean min) {
		return min ? Long.MIN_VALUE : Long.MAX_VALUE;
	}

	// CHANGE-HERE

//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readLong(getSettingName(), path));
//	}
}
