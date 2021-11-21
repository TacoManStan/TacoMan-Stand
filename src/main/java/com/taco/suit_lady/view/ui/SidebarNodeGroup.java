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
    
    /**
     * <p>Initializes this {@link Sidebar} and associated components and functionality..</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Defines style, formatting, and other UI settings for the {@link #getButtonBox() Button Box} for this {@link SidebarNodeGroup}.</li>
     *     <li>Adds the {@link UINode#getButtonView() buttons} assigned to the {@link UINode UINodes} in this {@link SidebarNodeGroup} to the {@link #getButtonBox() Button Box}.</li>
     *     <li>Defines the {@link Button#setOnAction(EventHandler) functionality} of the {@link #getButton() button} assigned to this {@link SidebarNodeGroup}.</li>
     * </ol>
     */
    protected void initialize()
    {
        // TODO - It might be a good idea to run the majority (if not all) of this on the JFX Thread.
        // TODO - The synchronization works for making most aspects of SidebarNodeGroup Thread-Safe, but if a thread already has a reference to the node list, the list can still be modified asynchronously.
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
    
    /**
     * <p>Returns the {@link StringProperty} containing the {@link #getName() name} of this {@link SidebarNodeGroup}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Exists only to allow a text description to be used somewhere in the UI alongside the {@link #getButton() button}.</li>
     *     <li>An example usage would be as the {@link #getButton() button's} {@link Button#tooltipProperty() tooltip}.</li>
     *     <li>Can be {@code null}.</li>
     * </ol>
     * <blockquote>— Currently Unused —</blockquote>
     *
     * @return The {@link StringProperty} containing the {@link #getName() name} of this {@link SidebarNodeGroup}.
     * @see #getName()
     * @see #setName(String)
     */
    public StringProperty nameProperty()
    {
        return nameProperty;
    }
    
    /**
     * <p>Returns the {@link #nameProperty() name} of this {@link SidebarNodeGroup}.</p>
     *
     * @return The {@link #nameProperty() name} of this {@link SidebarNodeGroup}.\
     * @see #nameProperty()
     * @see #setName(String)
     */
    public String getName()
    {
        return nameProperty().get();
    }
    
    /**
     * <p>Sets the {@link #nameProperty() name} of this {@link SidebarNodeGroup} to the specified value.</p>
     *
     * @param name The value to be set as the new {@link #nameProperty() name} of this {@link SidebarNodeGroup}.
     * @see #nameProperty()
     * @see #getName()
     */
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
    
    /**
     * <p>Returns the {@link Button} assigned to this {@link SidebarNodeGroup} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>When {@link Button#onActionProperty() pressed}, {@link #select()} is called on this {@link SidebarNodeGroup} instance.</li>
     *     <li>Refer to <code><i>{@link #select()}</i></code> for additional information.</li>
     * </ol>
     *
     * @return The {@link Button} assigned to this {@link SidebarNodeGroup} instance.
     * @see #select()
     */
    public Button getButton()
    {
        return button;
    }
    
    //
    
    /**
     * <p>Returns the {@link BoundButtonViewGroup} responsible for containing and managing the {@link ImageButton ImageButtons} used to switch between {@link ButtonViewGroup#selectedButtonProperty() selected} {@link UINode UINodes} in this {@link SidebarNodeGroup}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link BoundButtonViewGroup} returned by this method is used for managing {@link UINode} {@link ButtonViewGroup#selectedButtonProperty() selection}.</li>
     *     <li>The above works because {@link UINode} implements {@link ButtonViewable}.</li>
     *     <li>{@link BoundButtonViewGroup BoundButtonViewGroups} add functionality that allows you to {@link BoundButtonViewGroup#getViewableByButton(ImageButton) retrieve} the {@link ButtonViewable} (in this case, {@link UINode}) the specified {@link ImageButton} is assigned to.</li>
     *     <li>The {@link BoundButtonViewGroup value} returned by this method is guaranteed to be {@code non-null}.</li>
     * </ol>
     *
     * @return The {@link ButtonViewGroup} responsible for containing and managing the {@link ImageButton ImageButtons} used to switch between {@link ButtonViewGroup#selectedButtonProperty() selected} {@link UINode UINodes} in this {@link SidebarNodeGroup}.
     * <ol>
     *       <li>Guaranteed to be {@code non-null}.</li>
     * </ol>
     */
    public BoundButtonViewGroup<UINode> getButtonViewGroup()
    {
        return nodeButtonGroup;
    }
    
    //
    
    /**
     * <p>{@link ButtonViewGroup#clearSelection(ImageButton...) Clears} the {@link UINode} currently {@link ButtonViewGroup#selectedButtonProperty() selected} for this {@link SidebarNodeGroup}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@code Passthrough Definition:} <i><code>{@link #getButtonViewGroup()}<b>.</b>{@link ButtonViewGroup#clearSelection(ImageButton...) clearSelection()}</code></i></li>
     * </ol>
     *
     * @see ButtonViewGroup#clearSelection(ImageButton...)
     */
    public void clearSelection()
    {
        getButtonViewGroup().clearSelection();
    }
    
    //</editor-fold>
    
    /**
     * <p>Sets the {@link Sidebar#selectedNodeGroupProperty() selection} of the {@link Sidebar} {@link #getOwner() owner} to this {@link SidebarNodeGroup} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@code Passthrough Definition:} <i><code>{@link #getOwner()}<b>.</b>{@link Sidebar#setSelectedNodeGroup(SidebarNodeGroup) setSelectedNodeGroup(this)}</code></i></li>
     * </ol>
     */
    public void select()
    {
        getOwner().setSelectedNodeGroup(this);
    }
}
