package com.taco.tacository.util.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.WritableStringValue;
import javafx.scene.Node;

public class StringSetting extends SavableSetting<String, StringProperty>
		implements ObservableStringValue, WritableStringValue {

	public StringSetting(StringProperty observable) {
		super(observable, null);
	}

	public StringSetting(String settingName) {
		this(settingName, "");
	}

	public StringSetting(String settingName, String settingDescription) {
		this(settingName, settingDescription, null);
	}

	public StringSetting(String settingName, String settingDescription, String defaultValue) {
		super(settingName, settingDescription, defaultValue);
	}

	//

	@Override public boolean createBinding(Node node) {
		return false; // TODO
	}

	//<editor-fold desc="Get/Set">

	/**
	 * Returns the {@link #getValue() value} of this {@link StringSetting}.
	 * <p>
	 * This method is by default identical to {@link #getValue()}.
	 * @return The {@link #getValue() value} of this {@link StringSetting}.
	 */
	@Override public String get() {
		return getValue();
	}

	@Override public void set(String value) {
		setValue(value);
	}

	@Override protected StringProperty createProperty() {
		return new SimpleStringProperty();
	}

	//</editor-fold>

	//<editor-fold desc="Saving/Loading">
	
	// CHANGE-HERE

//	@Override public void xmlSave(XMLTools.Saver saver, Element parent) {
//		saver.write(parent, getSettingName(), get());
//	}
//
//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readString(getSettingName(), path));
//	}

	//</editor-fold>
}
