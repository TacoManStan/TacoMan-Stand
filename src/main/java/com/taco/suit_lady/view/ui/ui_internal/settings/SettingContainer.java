package com.taco.suit_lady.view.ui.ui_internal.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.locks.ReentrantLock;

public class SettingContainer {

	private final ReentrantLock lock;

	private final StringProperty nameProperty;
	private final ObservableList<SettingGroup> groups;

	public SettingContainer(String name) {
		this.lock = new ReentrantLock();

		this.nameProperty = new SimpleStringProperty(name);
		this.groups = FXCollections.observableArrayList();
	}

	//<editor-fold desc="Properties">

	public final StringProperty nameProperty() {
		return nameProperty;
	}

	public final ObservableList<SettingGroup> settingGroups() {
		return groups;
	}

	//</editor-fold>
}
