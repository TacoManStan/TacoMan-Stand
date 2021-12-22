package com.taco.suit_lady.ui.ui_internal.settings;

import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.ui.ui_internal.controllers.SettingGroupController;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class SettingsTitledPane extends TitledPane
{
    
    private final SettingContainer settingContainer;
    private ListView<SettingGroup<? extends SettingGroupController>> settingGroupListView;
    
    public SettingsTitledPane(SettingContainer settingContainer)
    {
        this.settingContainer = settingContainer;
        
        //
        
        settingContainer.settingGroups().addListener(new ListChangeListener<SettingGroup<? extends SettingGroupController>>()
        {
            @Override public void onChanged(Change<? extends SettingGroup<? extends SettingGroupController>> change)
            {
                FXTools.get().runFX(() -> {
                    while (change.next())
                    {
                        if (change.wasAdded())
                            change.getAddedSubList().forEach(settingGroup -> settingGroupListView.getItems().add(settingGroup));
                        if (change.wasRemoved())
                            change.getRemoved().forEach(settingGroup -> settingGroupListView.getItems().add(settingGroup));
                    }
                }, true);
            }
        });
        
        initializeFX();
    }
    
    //<editor-fold desc="Initialize">
    
    protected void initializeFX()
    {
        this.settingGroupListView = new ListView<>();
        
        //
        
        FXTools.get().applyCellFactory(settingGroupListView);
        
        textProperty().bind(settingContainer.nameProperty());
        setContent(settingGroupListView);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Properties">
    
    public final SettingContainer getSettingContainer()
    {
        return settingContainer;
    }
    
    //
    
    public final ReadOnlyObjectProperty<SettingGroup<? extends SettingGroupController>> selectedGroupProperty()
    {
        return settingGroupListView.getSelectionModel().selectedItemProperty();
    }
    
    public final SettingGroup<? extends SettingGroupController> getSelectedGroup()
    {
        return selectedGroupProperty().get();
    }
    
    //</editor-fold>
}
