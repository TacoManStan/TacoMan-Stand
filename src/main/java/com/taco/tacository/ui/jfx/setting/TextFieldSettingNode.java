package com.taco.tacository.ui.jfx.setting;

import com.taco.tacository.util.settings.StringSetting;
import javafx.scene.control.TextField;

public class TextFieldSettingNode extends SettingNode<String, TextField, StringSetting> {

	public TextFieldSettingNode(String name) {
		super(name, NodeOrder.NAME_CONTENT);
	}

	//<editor-fold desc="Properties">

	@Override public final String getValue() {
		return getInputNode().getText();
	}

	//</editor-fold>

	//<editor-fold desc="Implementation">

	@Override protected final TextField createInputNode(Object... params) {
		return new TextField();
	}

	@Override protected final StringSetting createSetting() {
		return new StringSetting(getName());
	}

	//</editor-fold>
}
