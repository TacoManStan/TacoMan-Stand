package com.taco.tacository.ui.ui_internal.settings;

import com.taco.tacository.ui.jfx.lists.Listable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.settings.SavableSetting;
import com.taco.tacository.ui.ui_internal.controllers.SettingGroupController;
import com.taco.tacository._to_sort.obj_traits.common.Nameable;
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
		this.controller = Exc.nullCheck(controllerSupplier.get(), "SettingGroup Controller");
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