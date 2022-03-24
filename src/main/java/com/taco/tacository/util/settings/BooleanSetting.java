package com.taco.tacository.util.settings;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.WritableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class BooleanSetting extends SavableSetting<Boolean, BooleanProperty>
		implements ObservableBooleanValue, WritableBooleanValue {

	private final StringProperty enabledDescriptionProperty;
	private final StringProperty disabledDescriptionProperty;

	public BooleanSetting(BooleanProperty observable) {
		this(observable, "", "");
	}

	public BooleanSetting(BooleanProperty observable, String settingDescription) {
		this(observable, settingDescription, null);
	}

	public BooleanSetting(BooleanProperty observable, String enabledDescription, String disabledDescription) {
		this(observable, enabledDescription, disabledDescription, false);
	}

	public BooleanSetting(BooleanProperty observable, String enabledDescription, String disabledDescription, boolean defaultValue) {
		super(observable, defaultValue);
		this.enabledDescriptionProperty = new SimpleStringProperty();
		this.disabledDescriptionProperty = new SimpleStringProperty();
		this.init(enabledDescription, disabledDescription);
	}

	public BooleanSetting(String settingName) {
		this(settingName, "", "");
	}

	public BooleanSetting(String settingName, String settingDescription) {
		this(settingName, "", "", false);
	}

	public BooleanSetting(String settingName, String enabledDescription, String disabledDescription) {
		this(settingName, enabledDescription, disabledDescription, false);
	}

	public BooleanSetting(String settingName, String enabledDescription, String disabledDescription, boolean defaultValue) {
		super(settingName, enabledDescription == null ? null : "", defaultValue);
		this.enabledDescriptionProperty = new SimpleStringProperty();
		this.disabledDescriptionProperty = new SimpleStringProperty();

		this.init(enabledDescription, disabledDescription);
	}

	private void init(String enabledDescription, String disabledDescription) {
		descriptionProperty().bind(Bindings.createObjectBinding(() -> {
			if (get())
				return getEnabledDescription();
			return getDisabledDescription();
		}, enabledDescriptionProperty, disabledDescriptionProperty));
		setEnabledDescription(enabledDescription);
		setDisabledDescription(disabledDescription);
	}

	//

	public final StringProperty enabledDescriptionProperty() {
		return enabledDescriptionProperty;
	}

	public final String getEnabledDescription() {
		return enabledDescriptionProperty.get();
	}

	public final void setEnabledDescription(String enabledDescription) {
		enabledDescriptionProperty.set(enabledDescription);
	}

	//

	public final StringProperty disabledDescriptionProperty() {
		return disabledDescriptionProperty;
	}

	public final String getDisabledDescription() {
		return disabledDescriptionProperty.get();
	}

	public final void setDisabledDescription(String disabledDescription) {
		this.disabledDescriptionProperty.set(disabledDescription);
	}

	//

	@Override public boolean createBinding(Node node) {
		if (node instanceof CheckBox) {
			((CheckBox) node).selectedProperty().bindBidirectional(this);
			return true;
		}
		return false;
	}

	//<editor-fold desc="Get/Set">

	/**
	 * Returns the {@link #getValue() value} of this {@link BooleanSetting} a primitive.
	 * <p>
	 * If the {@link #getValue() value} is null, this method returns false.
	 *
	 * @return The {@link #getValue() value} of this {@link BooleanSetting} a primitive.
	 */
	@Override public boolean get() {
		final Boolean value = getValue();
		if (value != null)
			return value;
		return false;
	}

	/**
	 * Sets the {@link #getValue() value} of this {@link BooleanSetting} to the specified value.
	 *
	 * @param value The {@link #getValue() value}.
	 */
	@Override public void set(boolean value) {
		setValue(value);
	}

	@Override protected BooleanProperty createProperty() {
		return new SimpleBooleanProperty();
	}

	//</editor-fold>

	//<editor-fold desc="Saving/Loading">
	
	// CHANGE-HERE

//	@Override public void xmlSave(XMLTools.Saver saver, Element parent) {
//		saver.write(parent, getSettingName(), getValue());
//	}
//
//	@Override public void xmlLoad(XMLTools.Loader loader, String path) {
//		setValue(loader.readBoolean(getSettingName(), path));
//	}

	//</editor-fold>
}
