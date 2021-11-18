package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.BindingTools;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Sidebar
{
    
    private final ReentrantLock lock;
    
    private final StackPane childButtonPane;
    private final StackPane contentPane;
    private final ImageButton backImageButton;
    
    private final ObservableList<SidebarNodeGroup> nodeGroupProperty;
    private final ReadOnlyObjectWrapper<SidebarNodeGroup> selectedNodeGroupProperty;
    
    public Sidebar(StackPane childButtonPane, StackPane contentPane, ImagePane backImagePane)
    {
        this.lock = new ReentrantLock();
        
        this.childButtonPane = childButtonPane;
        this.contentPane = contentPane;
        this.backImageButton = new ImageButton(
                backImagePane, "back_arrow",
                () -> FXTools.get().runFX(this::back, false),
                false, true, ImageButton.SMALL
        );
        
        this.nodeGroupProperty = FXCollections.observableArrayList();
        this.selectedNodeGroupProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.backImageButton.getImagePane().visibleProperty().bind(
                BindingTools.get().recursiveBinding(selectedNodeGroup -> {
                    if (selectedNodeGroup != null)
                        return Bindings.and(selectedNodeGroup.hasPagedContentBinding(), selectedNodeGroup.getNodeDisplayer().showingBinding());
                    return null;
                }, selectedNodeGroupProperty));
        
        this.backImageButton.getImagePane().managedProperty().bind(this.backImageButton.getImagePane().visibleProperty());
        
        this.selectedNodeGroupProperty.addListener((observable, oldNodeGroup, newNodeGroup) -> {
            if (!Objects.equals(oldNodeGroup, newNodeGroup))
            { //If the menu base is already selected, return silently and do nothing.
                if (oldNodeGroup != null)
                {
                    final VBox oldButtonBox = oldNodeGroup.getButtonBox();
                    if (oldButtonBox != null)
                        this.childButtonPane.getChildren().remove(oldButtonBox);
                }
                if (newNodeGroup != null)
                {
                    final VBox buttonBox = newNodeGroup.getButtonBox();
                    
                    this.childButtonPane.getChildren().add(buttonBox);
                    this.contentPane.getChildren().clear();
                    this.contentPane.getChildren().add(newNodeGroup.getContent());
                }
            }
        });
    }
    
    public void onInitialize()
    {
        FXTools.get().runFX(() -> {
            backImageButton.initialize();
            childButtonPane.setAlignment(Pos.TOP_LEFT);
            nodeGroupProperty().forEach(SidebarNodeGroup::initialize);
            SidebarNodeGroup firstNodeGroup = nodeGroupProperty.get(0); // CHANGE-HERE
            if (firstNodeGroup != null)
                setSelectedNodeGroup(firstNodeGroup);
        }, true);
    }
    
    //<editor-fold desc="Properties">
    
    protected final ReentrantLock getLock()
    {
        return lock;
    }
    
    //
    
    protected final StackPane getChildButtonPane()
    {
        return childButtonPane;
    }
    
    protected final StackPane getContentPane()
    {
        return contentPane;
    }
    
    protected final ImageButton getBackImageButton()
    {
        return backImageButton;
    }
    
    //
    
    public ObservableList<SidebarNodeGroup> nodeGroupProperty()
    {
        return nodeGroupProperty;
    }
    
    //
    
    public ReadOnlyObjectProperty<SidebarNodeGroup> selectedNodeGroupProperty()
    {
        return selectedNodeGroupProperty.getReadOnlyProperty();
    }
    
    public SidebarNodeGroup getSelectedNodeGroup()
    {
        return selectedNodeGroupProperty.get();
    }
    
    protected void setSelectedNodeGroup(SidebarNodeGroup menu)
    {
        selectedNodeGroupProperty.set(menu);
    }
    
    public boolean isNodeGroupSelected(SidebarNodeGroup menu)
    {
        return Objects.equals(menu, getSelectedNodeGroup());
    }
    
    //</editor-fold>
    
    //
    
    protected void clearAllSelections()
    {
        nodeGroupProperty().forEach(this::clearSelection);
    }
    
    protected void clearSelection(SidebarNodeGroup menu)
    {
        menu.clearSelection();
    }
    
    //
    
    private void back()
    {
        UINodeGroup selectedNodeGroup = getSelectedNodeGroup();
        if (selectedNodeGroup != null)
        {
            UINode selectedNode = selectedNodeGroup.getNodeDisplayer().getDisplay();
            if (selectedNode != null)
                selectedNode.getPageHandler().back();
        }
    }
}
