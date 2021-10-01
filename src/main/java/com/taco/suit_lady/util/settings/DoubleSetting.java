package com.taco.suit_lady.util.settings;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.WritableDoubleValue;

public class DoubleSetting extends NumberSetting<Double>
		implements ObservableDoubleValue, WritableDoubleValue {

	public DoubleSetting(DoubleProperty observable) {
		this(observable, "");
	}

	public DoubleSetting(DoubleProperty observable, String description) {
		this(observable, description, -1);
	}

	public DoubleSetting(DoubleProperty observable, String description, double defaultValue) {
		super(observable, defaultValue);
		setDescription(description);
	}

	public DoubleSetting(String settingName) {
		this(settingName, "");
	}

	public DoubleSetting(String settingName, String description) {
		this(settingName, description, 0);
	}

	public DoubleSetting(String settingName, String description, double defaultValue) {
		super(settingName, description, defaultValue);
	}

	//<editor-fold desc="Get/Set">

	/**
	 * Returns the {@link #getValue() value} of this {@link DoubleSetting} a primitive.
	 * <p>
	 * If the {@link #getValue() value} is null, this method returns -1.
	 * @return The {@link #getValue() value} of this {@link DoubleSetting} a primitive.
	 */
	@Override public double get() {
		Number value = getValue();
		if (value != null)
			return value.doubleValue();
		return -1;
	}

	/**
	 * Sets the {@link #getValue() value} of this {@link DoubleSetting} to the specified value.
	 * @param value The {@link #getValue() value}.
	 */
	@Override public void set(double value) {
		setValue(value);
	}

	@Override protected DoubleProperty createProperty() {
		return new SimpleDoubleProperty();
	}

	//</editor-fold>

	//<editor-fold desc="Saving/Loading">

	@Override protected Double memoryValue(boolean min) {
		return min ? -Double.MAX_VALUE : Double.MAX_VALUE;
	}

	// CHANGE-HERE
	
//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readDouble(getSettingName(), path));
//	}

	//</editor-fold>
}
