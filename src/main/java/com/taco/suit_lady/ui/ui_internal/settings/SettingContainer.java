package com.taco.suit_lady.ui.ui_internal.settings;

import com.taco.suit_lady.ui.ui_internal.controllers.SettingGroupController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.locks.ReentrantLock;

public class SettingContainer {

	private final ReentrantLock lock;

	private final StringProperty nameProperty;
	private final ObservableList<SettingGroup<? extends SettingGroupController>> groups;

	public SettingContainer(String name) {
		this.lock = new ReentrantLock();

		this.nameProperty = new SimpleStringProperty(name);
		this.groups = FXCollections.observableArrayList();
	}

	//<editor-fold desc="Properties">

	public final StringProperty nameProperty() {
		return nameProperty;
	}

	public final ObservableList<SettingGroup<? extends SettingGroupController>> settingGroups() {
		return groups;
	}

	//</editor-fold>
}
