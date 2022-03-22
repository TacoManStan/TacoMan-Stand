package com.taco.suit_lady.ui.content;

import com.taco.suit_lady.util.UndefinedRuntimeException;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * A {@link ContentView} that can display multiple pieces of content at the same time.
 */
public class GroupContentView extends ContentView<StackPane> {
    
    private final ListProperty<ContentView> children;
    
    /**
     * Constructs a new {@code GroupContentView} with a new {@link StackPane} as its content.
     */
    public GroupContentView() {
        this(new StackPane());
    }
    
    /**
     * Constructs a new {@code GroupContentView} with the specified {@link StackPane} as its content.
     *
     * @param contentPane The {@code StackPane} to be used as this {@code GroupContentView's} content.
     *
     * @throws NullPointerException If the specified {@code StackPane} is null.
     */
    public GroupContentView(StackPane contentPane) {
        super(contentPane);
        this.children = new SimpleListProperty<>(FXCollections.observableArrayList());
        init();
    }
    
    @Override
    protected void onContentChange(StackPane oldContent, StackPane newContent) {
        throw new UndefinedRuntimeException("NYI");
    } // TODO
    
    private void init() {
        children.addListener((ListChangeListener<ContentView>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(this::onContentAdded);
                change.getRemoved().forEach(this::onContentRemoved);
            }
        });
    }
    
    //<editor-fold desc="Properties">
    
    /**
     * Returns all {@link ContentView ContentViews} children contained within this {@code GroupContentView}.
     *
     * @return All {@link ContentView ContentViews} children contained within this {@code GroupContentView}.
     */
    public ListProperty<ContentView> getChildren() {
        return children;
    }
    
    //</editor-fold>
    
    //
    
    private void onContentAdded(ContentView content) {
        StackPane root_pane = getContent();
        Pane content_pane = content.getContent();
        
        content_pane.maxWidthProperty().bind(root_pane.widthProperty());
        content_pane.maxHeightProperty().bind(root_pane.heightProperty());
        root_pane.getChildren().add(content_pane);
    }
    
    private void onContentRemoved(ContentView content) {
        StackPane root_pane = getContent();
        Pane content_pane = content.getContent();
        
        root_pane.getChildren().remove(content_pane);
        content_pane.maxWidthProperty().unbind();
        content_pane.maxHeightProperty().unbind();
    }
}
