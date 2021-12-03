package com.taco.suit_lady.view.ui.ui_internal.contents_old;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;

public interface ReadOnlySelectedDummyInstanceProperty
		extends ReadOnlyProperty<DummyInstance> {

	//<editor-fold desc="UI Bindings">

	ReadOnlyStringProperty titleBinding();
	String getTitle();

	ReadOnlyStringProperty descriptionBinding();
	String getDescription();

	//</editor-fold>
}