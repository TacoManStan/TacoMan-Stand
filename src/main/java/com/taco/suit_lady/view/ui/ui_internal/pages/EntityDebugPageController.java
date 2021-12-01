package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.jfx.setting.CheckBoxSettingNode;
import com.taco.suit_lady.view.ui.jfx.setting.SettingNode;
import com.taco.suit_lady.view.ui.ui_internal.controllers.SidebarNodeGroupController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/sidebar/entity_debug/entity_debug.fxml")
@Scope("prototype")
public final class EntityDebugPageController extends SidebarNodeGroupController<EntityDebugPage>
{
    
    //<editor-fold desc="FXML">
    
    @FXML private Pane root;
    
    @FXML private TextField searchField;
    @FXML private VBox settingsVBox;
    
    //</editor-fold>
    
    public EntityDebugPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    @Override public Pane root()
    {
        return root;
    }
    
    @Override
    @FXML public void initialize()
    {
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Heap Space"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("TRiBot Cache"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Mouse Positions"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Animation"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Inventory"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Position"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Base Position"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Menu Options"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Tile Projection"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("NPCs"));
        
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Object Names"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Interactive Objects"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Other Objects"));
        
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Models"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Ground Items"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Game State"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Uptext"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Destination"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Camera"));
        settingsVBox.getChildren().add(new CheckBoxSettingNode("Random Log"));
        
        settingsVBox.getChildren().stream().filter(
                _child -> _child instanceof SettingNode).forEach(_child -> {
            SettingNode _settingNode = (SettingNode) _child;
            //                App.settings().set(_settingNode.getName(), _settingNode.getSetting()); // CHANGE-HERE
        });
        
        //			for (int i = 0; i < 10; i++)
        //				settingsVBox.getChildren().add(new TextFieldSettingNode("Test Setting Node " + i));
    }
}