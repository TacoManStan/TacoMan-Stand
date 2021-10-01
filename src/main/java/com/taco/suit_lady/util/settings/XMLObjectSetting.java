package com.taco.suit_lady.util.settings;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.WritableObjectValue;
import javafx.scene.Node;

public class XMLObjectSetting<T extends Storable> extends SavableSetting<T, ObjectProperty<T>>
		implements ObservableObjectValue<T>, WritableObjectValue<T> {

	public XMLObjectSetting(ObjectProperty<T> observable) {
		super(observable, null);
	}

	public XMLObjectSetting(ObjectProperty<T> observable, String description) {
		this(observable, description, null);
	}

	public XMLObjectSetting(ObjectProperty<T> observable, String description, T defaultValue) {
		super(observable, defaultValue);
		setDescription(description);
	}

	public XMLObjectSetting(String settingName) {
		this(settingName, "");
	}

	public XMLObjectSetting(String settingName, String description) {
		this(settingName, description, null);
	}

	public XMLObjectSetting(String settingName, String description, T defaultValue) {
		super(settingName, description, defaultValue);
	}

	//

	@Override
	public boolean createBinding(Node node) {
		return false; // TODO
	}

	//<editor-fold desc="Get/Set">

	@Override public T get() {
		return getValue();
	}

	@Override public void set(T value) {
		setValue(value);
	}

	@Override protected ObjectProperty<T> createProperty() {
		return new SimpleObjectProperty<>();
	}

	//</editor-fold>

	//<editor-fold desc="Saving/Loading">

	// CHANGE-HERE
	
//	@Override public void xmlSave(XMLTools.Saver saver, Element parent) {
//		saver.write(parent, getSettingName(), getValue());
//	}
//
//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readXMLable(getSettingName(), path, getValueClass()));
//	}

	//</editor-fold>
}
