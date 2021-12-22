package com.taco.suit_lady.ui.jfx.setting;

import com.taco.suit_lady.util.settings.BooleanSetting;
import javafx.scene.control.CheckBox;

public class CheckBoxSettingNode extends SettingNode<Boolean, CheckBox, BooleanSetting> {

	public CheckBoxSettingNode(String name) {
		super(name, NodeOrder.CONTENT_NAME);
	}

	//<editor-fold desc="Properties">

	@Override public final Boolean getValue() {
		return getInputNode().isSelected();
	}

	public final boolean get() {
		return getValue();
	}

	//</editor-fold>

	//<editor-fold desc="Implementation">

	@Override protected final CheckBox createInputNode(Object... params) {
		CheckBox cb = new CheckBox();
//		cb.setStyle("-fx-background-color: le-very-dark-gray");
		cb.setId("dark-check-box");
		return cb;
	}

	@Override protected BooleanSetting createSetting() {
		return new BooleanSetting(getName());
	}

	//</editor-fold>
}
