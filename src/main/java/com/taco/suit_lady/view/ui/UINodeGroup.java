package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.BindingTools;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

// TO-DOC
public abstract class UINodeGroup
        implements Displayable
{
    
    private final Displayer<UINode> nodeDisplayer;
    private final ObservableList<UINode> nodes;
    
    private final BooleanBinding hasPagedContentBinding;
    
    public UINodeGroup()
    {
        this(null);
    }
    
    // TO-DOC
    public UINodeGroup(StackPane contentPane)
    {
        this.nodeDisplayer = new Displayer<>(contentPane != null ? contentPane : new StackPane());
        
        this.nodes = FXCollections.observableArrayList();
        
        //
        
        final Binding<Boolean> temp_binding = BindingTools.createRecursiveBinding((UINode uiNode) -> {
            if (uiNode != null) {
                UIPageHandler pageHandler = uiNode.getPageHandler();
                if (pageHandler != null)
                    return pageHandler.hasPagedContentBinding();
            }
            return null;
        }, nodeDisplayer.displayProperty());
        
        this.hasPagedContentBinding = Bindings.createBooleanBinding(() -> {
            final Boolean hasPagedContent = temp_binding.getValue();
            return hasPagedContent != null && hasPagedContent;
        }, BindingTools.createRecursiveBinding((UINode uiNode) -> {
            if (uiNode != null) {
                final UIPageHandler pageHandler = uiNode.getPageHandler();
                if (pageHandler != null)
                    return pageHandler.hasPagedContentBinding();
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
    public Displayer<UINode> getNodeDisplayer()
    {
        return nodeDisplayer;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the currently selected {@link UINode} in this {@link UINodeGroup}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#displayProperty() displayProperty()}</code></i></blockquote>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the currently selected {@link UINode} in this {@link UINodeGroup}.
     * @see #getNodeDisplayer()
     */
    public ReadOnlyObjectProperty<UINode> selectionProperty()
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
    public UINode getSelection()
    {
        return getNodeDisplayer().displayProperty().get();
    }
    
    /**
     * <p>Returns the {@link ObservableList} containing the {@link UINode UINodes} in this {@link UINodeGroup}.</p>
     *
     * @return The {@link ObservableList} containing the {@link UINode UINodes} in this {@link UINodeGroup}.
     */
    public ObservableList<UINode> getNodes()
    {
        return nodes;
    }
    
    /**
     * <p>Returns a {@link BooleanBinding} that reflects if this {@link UINodeGroup} has {@link UIPageHandler#hasPagedContentBinding() paged content}.</p>
     * <blockquote><b>Bound Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#getDisplay() getDisplay()}<b>.</b>{@link UINode#getPageHandler() getPageHandler()}<b>.</b>{@link UIPageHandler#hasPagedContentBinding() hasPagedContentBinding()}</code></i></blockquote>
     *
     * @return A {@link BooleanBinding} that reflects if this {@link UINodeGroup} has {@link UIPageHandler#hasPagedContentBinding() paged content}.
     */
    public BooleanBinding hasPagedContentBinding()
    {
        return hasPagedContentBinding;
    }
    
    /**
     * <p>Checks if the {@link #getContent() content} of this {@link UINodeGroup} has {@link UIPageHandler#hasPagedContentBinding() paged content} or not.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getNodeDisplayer()}<b>.</b>{@link Displayer#getDisplay() getDisplay()}<b>.</b>{@link UINode#getPageHandler() getPageHandler()}<b>.</b>{@link UIPageHandler#hasPagedContent() hasPagedContent()}</code></i></blockquote>
     *
     * @return True if the {@link #getContent() content} of this {@link UINodeGroup} has {@link UIPageHandler#hasPagedContentBinding() paged content}, false if it does not.
     *
     * @see #hasPagedContent()
     */
    public boolean hasPagedContent()
    {
        return hasPagedContentBinding.get();
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
