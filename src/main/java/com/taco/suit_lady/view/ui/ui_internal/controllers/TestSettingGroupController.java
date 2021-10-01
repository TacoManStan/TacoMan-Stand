package com.taco.suit_lady.view.ui.ui_internal.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/settings/test_setting_group.fxml")
@Scope("prototype")
public class TestSettingGroupController extends SettingGroupController
{
	//<editor-fold desc="FXML">

	@FXML private BorderPane root;
	
	@FXML private VBox contentsVBox;

	@FXML private CheckBox testCheckBox1;
	@FXML private CheckBox testCheckBox2;
	@FXML private CheckBox testCheckBox3;
	@FXML private CheckBox testCheckBox4;
	@FXML private CheckBox testCheckBox5;

	//</editor-fold>

	public TestSettingGroupController(FxWeaver weaver, ConfigurableApplicationContext ctx)
	{
		super(weaver, ctx);
	}
	
	@Override public BorderPane root()
	{
		return root;
	}
	
	public final void initialize() {
		testCheckBox1.setSelected(true);
		testCheckBox3.setSelected(true);
	}

	@Override public final String getName() {
		return "Setting Group";
	}
}
