package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.view.ui.jfx.button.BoundButtonViewGroup;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewGroup;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewable;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// TODO - Add BooleanProperty bidirectionally-bound to the selected SidebarNodeGroup of the parent Sidebar, strictly for convenience.
public class SidebarNodeGroup extends UINodeGroup
{
    private final ReentrantLock lock;
    private final StringProperty nameProperty;
    
    private final Sidebar owner;
    private final VBox buttonBox;
    private final Button button;
    
    private final BoundButtonViewGroup<UINode> nodeButtonGroup;
    
    public SidebarNodeGroup(Sidebar owner, Button menuButton)
    {
        this(owner, menuButton, null);
    }
    
    public SidebarNodeGroup(Sidebar owner, Button menuButton, StackPane contentPane)
    {
        super(contentPane);
        
        this.lock = owner.getLock();
        this.nameProperty = new SimpleStringProperty();
        
        this.owner = owner;
        this.buttonBox = new VBox();
        this.button = menuButton;
        
        this.nodeButtonGroup = new BoundButtonViewGroup<>(getNodes(), lock);
        
        //
        
        this.nodeButtonGroup.selectedButtonProperty().addListener((observable, oldButton, newButton) -> getNodeDisplayer().setDisplay(this.nodeButtonGroup.getViewableByButton(newButton)));
    }
    
    protected void initialize()
    {
        lock.lock();
        try {
            buttonBox.setSpacing(1.0);
            buttonBox.setAlignment(Pos.TOP_LEFT);
            
            final List<UINode> nodes = getNodes();
            for (UINode node: nodes) {
                final ImageButton imageButton = node.getButtonView();
                if (imageButton != null)
                    buttonBox.getChildren().add(imageButton.getImagePane());
            }
            button.setOnAction(event -> select());
        } finally {
            lock.unlock();
        }
    }
    
    //<editor-fold desc="Properties">
    
    public StringProperty nameProperty()
    {
        return nameProperty;
    }
    
    public String getName()
    {
        return nameProperty().get();
    }
    
    public void setName(String name)
    {
        nameProperty().set(name);
    }
    
    //
    
    /**
     * <p>Returns the {@link Sidebar} object containing this {@link SidebarNodeGroup} instance.</p>
     *
     * @return The {@link Sidebar} object containing this {@link SidebarNodeGroup} instance.
     */
    public Sidebar getOwner()
    {
        return owner;
    }
    
    public VBox getButtonBox()
    {
        return buttonBox;
    }
    
    public Button getButton()
    {
        return button;
    }
    
    //
    
    public BoundButtonViewGroup<UINode> getButtonViewGroup()
    {
        return nodeButtonGroup;
    }
    
    //
    
    public void clearSelection()
    {
        getButtonViewGroup().clearSelection();
    }
    
    //</editor-fold>
    
    public void select()
    {
        getOwner().setSelectedNodeGroup(this);
    }
}
