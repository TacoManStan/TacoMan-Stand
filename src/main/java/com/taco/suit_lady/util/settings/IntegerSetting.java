package com.taco.suit_lady.util.settings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.WritableIntegerValue;

public class IntegerSetting extends NumberSetting<Integer>
		implements ObservableIntegerValue, WritableIntegerValue {

	public IntegerSetting(IntegerProperty observable) {
		this(observable, "");
	}

	public IntegerSetting(IntegerProperty observable, String description) {
		this(observable, description, -1);
	}

	public IntegerSetting(IntegerProperty observable, String description, int defaultValue) {
		super(observable, defaultValue);
		setDescription(description);
	}

	public IntegerSetting(String settingName) {
		this(settingName, "");
	}

	public IntegerSetting(String settingName, String description) {
		this(settingName, description, 0);
	}

	public IntegerSetting(String settingName, String description, int defaultValue) {
		super(settingName, description, defaultValue);
	}

	//<editor-fold desc="Get/Set">

	/**
	 * Returns the {@link #getValue() value} of this {@link IntegerSetting} a primitive.
	 * <p>
	 * If the {@link #getValue() value} is null, this method returns -1.
	 * @return The {@link #getValue() value} of this {@link IntegerSetting} a primitive.
	 */
	@Override public int get() {
		Number value = getValue();
		if (value != null)
			return value.intValue();
		return -1;
	}

	/**
	 * Sets the {@link #getValue() value} of this {@link IntegerSetting} to the specified value.
	 * @param value The {@link #getValue() value}.
	 */
	@Override public void set(int value) {
		setValue(value);
	}

	@Override protected IntegerProperty createProperty() {
		return new SimpleIntegerProperty();
	}

	//</editor-fold>

	@Override protected Integer memoryValue(boolean min) {
		return min ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	}

	// CHANGE-HERE

//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readInt(getSettingName(), path));
//	}
}
