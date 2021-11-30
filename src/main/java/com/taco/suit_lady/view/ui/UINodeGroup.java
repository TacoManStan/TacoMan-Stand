package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.BindingTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TO-DOC
public abstract class UINodeGroup
        implements Displayable
{
    
    private final Displayer<UINode> nodeDisplayer;
    private final ObservableList<UINode> nodes;
    
    private final BooleanBinding isEmptyBinding;
    
    public UINodeGroup()
    {
        this(null);
    }
    
    // TO-DOC
    public UINodeGroup(@Nullable StackPane contentPane)
    {
        this.nodeDisplayer = new Displayer<>(contentPane != null ? contentPane : new StackPane());
        
        this.nodes = FXCollections.observableArrayList();
        
        //
        
        final Binding<Boolean> innerBinding = BindingTools.createRecursiveBinding((UINode uiNode) -> {
            if (uiNode != null) {
                final UIPageHandler pageHandler = uiNode.getPageHandler();
                if (pageHandler != null)
                    return pageHandler.isEmptyBinding();
            }
            return null;
        }, nodeDisplayer.displayProperty());
        
        this.isEmptyBinding = Bindings.createBooleanBinding(() -> {
            final Boolean hasPagedContent = innerBinding.getValue();
            return hasPagedContent != null && hasPagedContent;
        }, BindingTools.createRecursiveBinding((UINode uiNode) -> {
            if (uiNode != null) {
                final UIPageHandler pageHandler = uiNode.getPageHandler();
                if (pageHandler != null)
                    return pageHandler.isEmptyBinding();
            }
            return null;
        }, nodeDisplayer.displayProperty()));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link Displayer} responsible for both containing and displaying the {@link UINode} that is currently {@link #selectionProperty() selected} by this {@link UINodeGroup}.</p>
     *
     * @return The {@link Displayer} responsible for both containing and displaying the {@link UINode} that is currently {@link #selectionProperty() selected} by this {@link UINodeGroup}.
     */
    public @NotNull Displayer<UINode> getNodeDisplayer()
    {
        return nodeDisplayer;
    }
    
    /**
     * <p>Returns the {@link ObservableList} containing the {@link UINode UINodes} in this {@link UINodeGroup}.</p>
     *
     * @return The {@link ObservableList} containing the {@link UINode UINodes} in this {@link UINodeGroup}.
     */
    public @NotNull ObservableList<UINode> getNodes()
    {
        return nodes;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the currently selected {@link UINode} in this {@link UINodeGroup}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#displayProperty() displayProperty()}</code></i></blockquote>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the currently selected {@link UINode} in this {@link UINodeGroup}.
     * @see #getNodeDisplayer()
     */
    public @NotNull ReadOnlyObjectProperty<UINode> selectionProperty()
    {
        return getNodeDisplayer().displayProperty();
    }
    
    /**
     * <p>Returns the currently selected {@link UINode} in this {@link UINodeGroup}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#displayProperty() displayProperty()}<b>.</b>{@link ReadOnlyObjectProperty#get() get()}</code></i></blockquote>
     *
     * @return The currently selected {@link UINode content} in this {@link UINodeGroup}.
     * @see #selectionProperty()
     * @see #getNodeDisplayer()
     */
    public @Nullable UINode getSelection()
    {
        return getNodeDisplayer().displayProperty().get();
    }
    
    /**
     * <p>Returns a {@link BooleanBinding} that reflects if this {@link UINodeGroup} is {@link UIPageHandler#isEmptyBinding() empty}.</p>
     * <blockquote><b>Bound Passthrough Definition:</b> <i><code>{@link Bindings#not(ObservableBooleanValue) Bindings.not}<b>(</b>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#getDisplay() getDisplay()}<b>.</b>{@link UINode#getPageHandler() getPageHandler()}<b>.</b>{@link UIPageHandler#isEmptyBinding() isEmptyBinding()}<b>)</b></code></i></blockquote>
     *
     * @return A {@link BooleanBinding} that reflects if this {@link UINodeGroup} is {@link UIPageHandler#isEmptyBinding() empty}.
     */
    public @NotNull BooleanBinding isEmptyBinding()
    {
        return isEmptyBinding;
    }
    
    /**
     * <p>Checks if the {@link #getContent() content} of this {@link UINodeGroup} is {@link UIPageHandler#isEmptyBinding() empty}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #isEmptyBinding()}<b>.</b>{@link BooleanBinding#get() get()}</code></i></blockquote>
     *
     * @return True if the {@link #getContent() content} of this {@link UINodeGroup} is {@link UIPageHandler#isEmpty() empty}, false if it is not.
     *
     * @see #isEmptyBinding()
     * @see UIPageHandler#isEmpty()
     */
    public boolean isEmpty()
    {
        return isEmptyBinding.get();
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns the {@link Pane} that contains the {@link #getSelection() content} of this {@link UINodeGroup}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#getDisplayContainer() getDisplayContainer()}</code></i></blockquote>
     *
     * @return The {@link Pane} that contains the {@link #getSelection() content} of this {@link UINodeGroup}.
     */
    @Override
    public Pane getContent()
    {
        return nodeDisplayer.getDisplayContainer();
    }
}
