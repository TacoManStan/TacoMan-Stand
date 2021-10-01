package com.taco.suit_lady.util.settings;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;

public class ColorSetting extends XMLObjectSetting<ColorWrapper> {

	public ColorSetting(ObjectProperty<ColorWrapper> observable) {
		super(observable);
	}

	public ColorSetting(ObjectProperty<ColorWrapper> observable, String settingDescription) {
		super(observable, settingDescription);
	}

	public ColorSetting(ObjectProperty<ColorWrapper> observable, String settingDescription, ColorWrapper defaultValue) {
		super(observable, settingDescription, defaultValue);
	}

	public ColorSetting(String settingName) {
		super(settingName);
	}

	public ColorSetting(String settingName, String settingDescription) {
		super(settingName, settingDescription);
	}

	public ColorSetting(String settingName, String settingDescription, ColorWrapper defaultValue) {
		super(settingName, settingDescription, defaultValue);
	}

	@Override public boolean createBinding(Node node) {
		if (node instanceof ColorPicker) {
			((ColorPicker) node).valueProperty().bindBidirectional(getValue().colorProperty());
			return true;
		}
		return false;
	}
}
