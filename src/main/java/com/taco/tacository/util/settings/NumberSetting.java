package com.taco.tacository.util.settings;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableNumberValue;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.scene.control.Slider;

public abstract class NumberSetting<T extends Number & Comparable<T>> extends SavableSetting<Number, Property<Number>>
		implements ObservableValue<Number>, WritableValue<Number>, ObservableNumberValue, WritableNumberValue {

	private final WritableValue<T> minValueProperty;
	private final WritableValue<T> maxValueProperty;

	public NumberSetting(Property<Number> observable, Number defaultValue) {
		super(observable, defaultValue);
		this.minValueProperty = createLimitProperty(true);
		this.maxValueProperty = createLimitProperty(false);
		this.construct();
	}

	public NumberSetting(String settingName, String settingDescription, Number defaultValue) {
		super(settingName, settingDescription, defaultValue);
		this.minValueProperty = createLimitProperty(true);
		this.maxValueProperty = createLimitProperty(false);
		this.construct();
	}

	private void construct() {
		resetMinValue();
		resetMaxValue();
		validateLimit();
		addListener((observable, oldValue, newValue) -> validateLimit());
	}

	//<editor-fold desc="Properties">

	public WritableValue<T> minValueProperty() {
		return minValueProperty;
	}

	public T getMinValue() {
		return minValueProperty.getValue();
	}

	public void setMinValue(T value) {
		minValueProperty.setValue(value);
	}

	public void resetMinValue() {
		setMinValue(memoryValue(true));
	}

	//

	public WritableValue<T> maxValueProperty() {
		return maxValueProperty;
	}

	public T getMaxValue() {
		return maxValueProperty.getValue();
	}

	public void setMaxValue(T value) {
		maxValueProperty.setValue(value);
	}

	public void resetMaxValue() {
		setMaxValue(memoryValue(false));
	}

	//</editor-fold>

	@Override public boolean createBinding(Node node) {
		if (node instanceof Slider) {
			((Slider) node).valueProperty().bindBidirectional(this);
			return true;
		}
		return false;
	}

	//

	private void validateMinLimit() {
		validateLimit(true);
	}

	private void validateMaxLimit() {
		validateLimit(false);
	}

	private void validateLimit() {
		validateMinLimit();
		validateMaxLimit();
	}

	//

	private WritableValue<T> createLimitProperty(boolean min) {
		ObservableValue<T> observableValue = new SimpleObjectProperty<>();
		observableValue.addListener((observable, oldValue, newValue) -> {
			if (newValue == null) { //Ensures the limit can never be null.
				if (min)
					resetMinValue();
				else
					resetMaxValue();
			} else if (!validateLimit(min)) //Ensures the value is never less than the min value or greater than the max value.
				setValue(newValue);
		});
		return (WritableValue<T>) observableValue;
	}

	//

	protected abstract T memoryValue(boolean min);

	protected boolean validateLimit(boolean min) {
		int compareValue = ((T) getValue()).compareTo(min ? getMinValue() : getMaxValue());
		return min ? compareValue >= 0 : compareValue <= 0;
	}

	//<editor-fold desc="Get/Set">

	@Override public int intValue() {
		Number value = getValue();
		if (value != null)
			return value.intValue();
		return 0;
	}

	@Override public long longValue() {
		Number value = getValue();
		if (value != null)
			return value.longValue();
		return 0;
	}

	@Override public float floatValue() {
		Number value = getValue();
		if (value != null)
			return value.floatValue();
		return 0;
	}

	@Override public double doubleValue() {
		Number value = getValue();
		if (value != null)
			return value.doubleValue();
		return 0;
	}

	//</editor-fold>

	// CHANGE-HERE
	
//	@Override public void xmlSave(XMLTools.Saver saver, Element parent) {
//		saver.write(parent, getSettingName(), getValue());
//	}
}
