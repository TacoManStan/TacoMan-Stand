package com.taco.suit_lady.view.ui.ui_internal.settings;

import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class SettingsTitledPane extends TitledPane
{
    
    private final SettingContainer settingContainer;
    private ListView<SettingGroup> settingGroupListView;
    
    public SettingsTitledPane(SettingContainer settingContainer)
    {
        this.settingContainer = settingContainer;
        
        //
        
        settingContainer.settingGroups().addListener(new ListChangeListener<SettingGroup>()
        {
            @Override public void onChanged(Change<? extends SettingGroup> change)
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
    
    public final ReadOnlyObjectProperty<SettingGroup> selectedGroupProperty()
    {
        return settingGroupListView.getSelectionModel().selectedItemProperty();
    }
    
    public final SettingGroup getSelectedGroup()
    {
        return selectedGroupProperty().get();
    }
    
    //</editor-fold>
}
