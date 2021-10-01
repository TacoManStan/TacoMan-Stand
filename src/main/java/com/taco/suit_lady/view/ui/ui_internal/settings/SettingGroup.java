package com.taco.suit_lady.view.ui.ui_internal.settings;

import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.util.settings.SavableSetting;
import com.taco.suit_lady.view.ui.jfx.lists.Listable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SettingGroupController;
import com.taco.util.obj_traits.common.Nameable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Supplier;

public class SettingGroup<C extends SettingGroupController>
		implements Nameable, Listable
{

	private final String name;
	private final ObservableList<SavableSetting> settings;

	private final C controller;

	public SettingGroup(Supplier<C> controllerSupplier) {
		this.settings = FXCollections.observableArrayList();
		this.controller = ExceptionTools.nullCheck(controllerSupplier.get(), "SettingGroup Controller");
		this.name = controller.getName();

		// CHANGE-HERE

//		controller.load();
	}

	//<editor-fold desc="Properties">

	@Override public final String getName() {
		return name;
	}

	public final ObservableList<SavableSetting> settings() {
		return settings;
	}

	//

	public final C getController() {
		return controller;
	}

	//</editor-fold>

	//<editor-fold desc="Implementation">

	@Override public String getShortText() {
		return getName();
	}

	@Override public String getLongText() {
		return getShortText();
	}

	//</editor-fold>
}