package com.taco.suit_lady.view.ui.ui_internal.contents_old;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class DummyContentsInstanceUI
{
    private final DummyInstance owner;
    
    //
    
    private final StackPane containerPane;
    private final ImagePane instanceImagePane;
    
    private final ReadOnlyStringWrapper titleProperty;
    private final ReadOnlyStringWrapper descriptionProperty;
    
    protected DummyContentsInstanceUI(DummyInstance owner)
    {
        this.owner = ExceptionTools.nullCheck(owner, "Dummy Contents");
        
        //
        
        this.containerPane = new StackPane();
        this.instanceImagePane = new ImagePane();
        
        this.titleProperty = new ReadOnlyStringWrapper("Test Title"); // TODO
        this.descriptionProperty = new ReadOnlyStringWrapper("Test Description"); // TODO
        
        this.init();
    }
    
    private boolean updating;
    
    private void init()
    {
        containerPane.getChildren().add(instanceImagePane);
        instanceImagePane.setImage(TB.resources().getTestImage("example"));
        bindToContainer(instanceImagePane, containerPane);
        
        updating = false;
    }
    
    //
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final DummyInstance owner()
    {
        return this.owner;
    }
    
    //
    
    public final StackPane getContainerPane()
    {
        return this.containerPane;
    }
    
    //
    
    public final ReadOnlyStringProperty titleProperty()
    {
        return this.titleProperty;
    }
    
    public final String getTitle()
    {
        return titleProperty.get();
    }
    
    public final ReadOnlyStringProperty descriptionProperty()
    {
        return this.descriptionProperty;
    }
    
    public final String getDescription()
    {
        return this.descriptionProperty.get();
    }
    
    //</editor-fold>
    
    private void bindToContainer(Pane pane, Pane container) {
        pane.prefWidthProperty().bind(containerPane.widthProperty());
        pane.prefHeightProperty().bind(containerPane.heightProperty());
        
        pane.maxWidthProperty().bind(containerPane.widthProperty());
        pane.maxHeightProperty().bind(containerPane.heightProperty());
    }
}
