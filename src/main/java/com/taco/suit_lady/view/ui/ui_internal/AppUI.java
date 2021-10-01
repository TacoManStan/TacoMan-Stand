package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.util.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;

public class AppUI
{
    public static AppUI get()
    {
        return TB.ui();
    }
    
    private final ReadOnlyObjectWrapper<AppController> controllerProperty;
    private final ReadOnlyObjectWrapper<Sidebar> sidebarProperty;
    
    private final ReadOnlyObjectWrapper<StackPane> containerPaneProperty;
    
    public AppUI()
    {
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
        this.sidebarProperty = new ReadOnlyObjectWrapper<>();
        
        this.containerPaneProperty = new ReadOnlyObjectWrapper<>();
    }
    
    protected void init() {
        TB.console().initialize();
    }
    
    // <editor-fold desc="Properties">
    
    public final ReadOnlyObjectProperty<AppController> controllerProperty()
    {
        return controllerProperty.getReadOnlyProperty();
    }
    
    public final AppController getController()
    {
        return controllerProperty.get();
    }
    
    protected final void setController(AppController controller)
    {
        controllerProperty.set(controller);
    }
    
    //
    
    public final ReadOnlyObjectProperty<Sidebar> sidebarProperty()
    {
        return sidebarProperty.getReadOnlyProperty();
    }
    
    public final Sidebar getSidebar()
    {
        return sidebarProperty.get();
    }
    
    protected final void setSidebar(Sidebar sidebar)
    {
        sidebarProperty.set(sidebar);
    }
    
    //
    
    public final ReadOnlyObjectProperty<StackPane> containerPaneProperty()
    {
        return containerPaneProperty.getReadOnlyProperty();
    }
    
    public final StackPane getContainerPane()
    {
        return containerPaneProperty.get();
    }
    
    protected final void setContainerPane(StackPane containerPane)
    {
        containerPaneProperty.set(containerPane);
    }
    
    // </editor-fold>
}
