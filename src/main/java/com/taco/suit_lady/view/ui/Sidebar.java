package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.BindingTools;
import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.util.ObjectTools;
import com.taco.suit_lady.view.ui.jfx.button.ImageButtonGroup;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.image.ImagePane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Sidebar
{
    private final ReentrantLock lock;
    
    private final StackPane childButtonPane;
    private final StackPane contentPane;
    private final ImageButton backImageButton;
    
    private final ObservableList<SidebarNodeGroup> nodeGroupsProperty;
    private final ReadOnlyObjectWrapper<SidebarNodeGroup> selectedNodeGroupProperty;
    
    /**
     * <p>Refer to {@link #Sidebar(StackPane, StackPane, ImagePane) Fully-Parameterized Constructor} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote><code>new Sidebar(childButtonPane, contentPane, <u>null</u>)</code></blockquote>
     */
    public Sidebar(
            @NotNull StackPane childButtonPane,
            @NotNull StackPane contentPane)
    {
        this(childButtonPane, contentPane, null);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p>Constructs a new {@link Sidebar} instance used to contain {@link SidebarNodeGroup SidebarNodeGroups}.</p>
     * <p><hr>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <b>Child Button Pane</b> — Refer to <code><i>{@link #getChildButtonPane()}</i></code> for additional information.
     *     </li>
     *     <li>
     *         <b>Content Pane</b> — Refer to <code><i>{@link #getContentPane()}</i></code> for additional information.
     *     </li>
     *     <li>
     *         <b>Back Image Pane</b> — Refer to <code><i>{@link #getBackButton()}</i></code> for additional information.
     *         <ol>
     *             <li>If the specified value is {@code null}, a new {@link ImagePane} is automatically constructed.</li>
     *             <li>To access the aforementioned automatically-constructed {@link ImagePane}, call <i>{@link ImageButton#getImagePane() getImagePane()}</i> on the {@link #back() Back} {@link ImageButton Button}.</li>
     *             <li>The {@link ImagePane} can then be added to a {@link Region  JavaFX Region} and the {@link #back() Back} {@link #getBackButton() Button} will work as intended. No additional setup is required.</li>
     *             <li>Note that every {@link Node JavaFX Node} instance can have a maximum of <u>one</u> parent at any given time.</li>
     *         </ol>
     *     </li>
     * </ol>
     * <p><b>Sidebar Contents Pattern</b></p>
     * <ol>
     *     <li><b>{@link Sidebar}: </b>Contains and manages a group of {@link UINodeGroup UINodeGroups} in the form of {@link SidebarNodeGroup SidebarNodeGroups}.</li>
     *     <li><b>{@link SidebarNodeGroup}: </b>Contains and manages a group of {@link UINode UINodes}.</li>
     *     <li><b>{@link UINode}: </b>{@link Displayable} implementations that contain, manage, and display the {@link UIPage#getContent() contents} of {@link UIPage UIPages}.</li>
     * </ol>
     *
     * @param childButtonPane The {@link StackPane} that the {@link ImageButton ImageButtons} linked to each {@link UINode} in the currently selected {@link SidebarNodeGroup} are displayed on.
     * @param contentPane     The {@link StackPane} that the {@link UINode#getContent() contents} of the currently displayed {@link UINode} based on the currently selected {@link SidebarNodeGroup} are displayed on.
     * @param backImagePane   The {@link ImagePane} on which the {@link #back() Back} {@link #getBackButton() Button} is displayed on.
     *
     * @throws NullPointerException If the {@code childButtonPane} parameter is {@code null}.
     * @throws NullPointerException If the {@code contentPane} parameter is {@code null}.
     */
    public Sidebar(
            @NotNull StackPane childButtonPane,
            @NotNull StackPane contentPane,
            @Nullable ImagePane backImagePane)
    {
        this.lock = new ReentrantLock();
        
        ExceptionTools.nullCheck(childButtonPane, "Sidebar Child Button Pane");
        ExceptionTools.nullCheck(contentPane, "Sidebar Content Pane");
        
        this.childButtonPane = childButtonPane;
        this.contentPane = contentPane;
        this.backImageButton = new ImageButton(
                backImagePane, "back_arrow",
                null,
                this::back,
                false,
                ImageButton.SMALL
        );
        
        this.nodeGroupsProperty = FXCollections.observableArrayList();
        this.selectedNodeGroupProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.backImageButton.getImagePane().visibleProperty().bind(
                BindingTools.createRecursiveBinding(selectedNodeGroup -> {
                    if (selectedNodeGroup != null)
                        return Bindings.and(selectedNodeGroup.hasPagedContentBinding(), selectedNodeGroup.getNodeDisplayer().visibleBinding());
                    return null;
                }, selectedNodeGroupProperty));
        
        this.backImageButton.getImagePane().managedProperty().bind(this.backImageButton.getImagePane().visibleProperty());
        
        this.selectedNodeGroupProperty.addListener((observable, oldNodeGroup, newNodeGroup) -> {
            if (!Objects.equals(oldNodeGroup, newNodeGroup)) { //If the menu base is already selected, return silently and do nothing.
                if (oldNodeGroup != null) {
                    final VBox oldButtonBox = oldNodeGroup.getButtonBox();
                    if (oldButtonBox != null)
                        this.childButtonPane.getChildren().remove(oldButtonBox);
                }
                if (newNodeGroup != null) {
                    final VBox buttonBox = newNodeGroup.getButtonBox();
                    
                    this.childButtonPane.getChildren().add(buttonBox);
                    this.contentPane.getChildren().clear();
                    this.contentPane.getChildren().add(newNodeGroup.getContent());
                }
            }
        });
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes this {@link Sidebar} and associated components and functionality..</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Executed on the {@link FXTools#runFX(Runnable, boolean) JavaFX Thread}.</li>
     *     <li>The calling {@link Thread} blocks until initialization process is complete.</li>
     *     <li><code><i>{@link #initialize()}</i></code> can <u>only</u> be called <u>once</u>.</li>
     *     <li>Calling <code><i>{@link #initialize()}</i></code> more than once on a single {@link Sidebar} instance can (and likely will) result in unexpected and/or unpredictable runtime functionality.</li>
     * </ol>
     * <p><b>Initialization</b></p>
     * <ol>
     *     <li><code><i>{@link #getBackButton()}<b>.</b>{@link ImageButton#initialize() initialize()}</i></<code> to initialize the {@link #back() Back} {@link #getBackButton() Button} assigned to this {@link Sidebar}.</li>
     *     <li><code><i>{@link #nodeGroupsProperty()}<b>.forEach(</b>{@link SidebarNodeGroup#initialize() initialize()}<b>)</b></i></code> to initialize all {@link SidebarNodeGroup SidebarNodeGroups} in this {@link Sidebar}.</li>
     *     <li>{@link #selectedNodeGroupProperty() Selects} the first (<code><i>{@link #nodeGroupsProperty() nodeGroupsProperty().get(0)}</i></code>) {@link SidebarNodeGroup} in this {@link Sidebar}.</li>
     * </ol>
     */
    public void initialize()
    {
        FXTools.get().runFX(() -> {
            backImageButton.initialize();
            childButtonPane.setAlignment(Pos.TOP_LEFT);
            nodeGroupsProperty().forEach(SidebarNodeGroup::initialize);
            SidebarNodeGroup firstNodeGroup = nodeGroupsProperty().get(0);
            if (firstNodeGroup != null)
                setSelectedNodeGroup(firstNodeGroup);
        }, true);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReentrantLock lock} assigned to handle the {@code synchronization} of this {@link Sidebar} instance.</p>
     *
     * @return The {@link ReentrantLock lock} assigned to handle the {@code synchronization} of this {@link Sidebar} instance.
     */
    protected final ReentrantLock getLock()
    {
        return lock;
    }
    
    /**
     * <p>Returns the {@link StackPane} whose {@link StackPane#getChildren() contents} are bound to the {@link SidebarNodeGroup#getButtonBox() Button Box} that contains the {@link ImageButton ImageButtons}
     * corresponding to each {@link UINode} child in the parent {@link SidebarNodeGroup} that is currently {@link #selectedNodeGroupProperty() selected} by this {@link Sidebar} instance.</p>
     *
     * @return The {@link StackPane} whose {@link StackPane#getChildren() contents} are bound to the {@link SidebarNodeGroup#getButtonBox() Button Box} that contains the {@link ImageButton ImageButtons}
     * corresponding to each {@link UINode} child in the parent {@link SidebarNodeGroup} that is currently {@link #selectedNodeGroupProperty() selected} by this {@link Sidebar} instance.
     */
    public StackPane getChildButtonPane()
    {
        return childButtonPane;
    }
    
    public StackPane getContentPane()
    {
        return contentPane;
    }
    
    public ImageButton getBackButton()
    {
        return backImageButton;
    }
    
    /**
     * <p>Returns the {@link ObservableList} containing the {@link SidebarNodeGroup NodeGroups} in this {@link Sidebar}.</p>
     *
     * @return The {@link ObservableList} containing the {@link SidebarNodeGroup NodeGroups} in this {@link Sidebar}.
     */
    public ObservableList<SidebarNodeGroup> nodeGroupsProperty()
    {
        return nodeGroupsProperty;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link #getSelectedNodeGroup() selected} {@link SidebarNodeGroup}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link #getSelectedNodeGroup() selected} {@link SidebarNodeGroup}.
     *
     * @see #getSelectedNodeGroup()
     * @see #setSelectedNodeGroup(SidebarNodeGroup)
     * @see #isNodeGroupSelected(SidebarNodeGroup)
     */
    public ReadOnlyObjectProperty<SidebarNodeGroup> selectedNodeGroupProperty()
    {
        return selectedNodeGroupProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link #selectedNodeGroupProperty() selected} {@link SidebarNodeGroup}.</p>
     *
     * @return The {@link #selectedNodeGroupProperty() selected} {@link SidebarNodeGroup}.
     *
     * @see #selectedNodeGroupProperty()
     * @see #setSelectedNodeGroup(SidebarNodeGroup)
     * @see #isNodeGroupSelected(SidebarNodeGroup)
     */
    public SidebarNodeGroup getSelectedNodeGroup()
    {
        return selectedNodeGroupProperty.get();
    }
    
    /**
     * <p>Sets the {@link #selectedNodeGroupProperty() selected} {@link SidebarNodeGroup} to the specified value.</p>
     *
     * @param menu The {@link SidebarNodeGroup} to be {@link #selectedNodeGroupProperty() selected}.
     *
     * @see #selectedNodeGroupProperty()
     * @see #getSelectedNodeGroup()
     * @see #isNodeGroupSelected(SidebarNodeGroup)
     */
    protected void setSelectedNodeGroup(SidebarNodeGroup menu)
    {
        selectedNodeGroupProperty.set(menu);
    }
    
    /**
     * <p>Checks if the specified {@link SidebarNodeGroup} is currently {@link #selectedNodeGroupProperty() selected} or not.</p>
     *
     * @param menu The {@link SidebarNodeGroup} being checked.
     *
     * @return True if the specified {@link SidebarNodeGroup} is currently {@link #selectedNodeGroupProperty() selected}, false if it is not.
     *
     * @see #selectedNodeGroupProperty()
     * @see #getSelectedNodeGroup()
     * @see #setSelectedNodeGroup(SidebarNodeGroup)
     * @see ObjectTools#equalsExcludeNull(Object, Object)
     */
    public boolean isNodeGroupSelected(SidebarNodeGroup menu)
    {
        return ObjectTools.equalsExcludeNull(menu, getSelectedNodeGroup());
    }
    
    //</editor-fold>
    
    /**
     * <p>{@link SidebarNodeGroup#clearSelection() Clears} the {@link ImageButtonGroup#selectedButtonProperty() selection} of every {@link SidebarNodeGroup SidebarNodeGroups} in this {@link Sidebar}.</p>
     *
     * @see SidebarNodeGroup#clearSelection()
     * @see SidebarNodeGroup#getButtonGroup()
     * @see ImageButtonGroup#selectedButtonProperty()
     * @see ImageButtonGroup#clearSelection(ImageButton...)
     */
    protected void clearAllSelections()
    {
        nodeGroupsProperty().forEach(menu -> menu.clearSelection());
    }
    
    /**
     * <p>{@link UIPageHandler#turnTo(UIPage) Turns} the {@link UINode} that is currently {@link UINodeGroup#getNodeDisplayer() selected} by the {@link UINodeGroup} that is currently {@link #selectedNodeGroupProperty() selected} by this {@link Sidebar} instance {@link UIPageHandler#back() back} one {@link UIPage page}.</p>
     */
    public void back()
    {
        UINodeGroup selectedNodeGroup = getSelectedNodeGroup();
        if (selectedNodeGroup != null) {
            UINode selectedNode = selectedNodeGroup.getNodeDisplayer().getDisplay();
            if (selectedNode != null)
                selectedNode.getPageHandler().back();
        }
    }
}
