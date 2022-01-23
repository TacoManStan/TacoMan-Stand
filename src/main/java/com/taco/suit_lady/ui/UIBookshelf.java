package com.taco.suit_lady.ui;

import com.taco.suit_lady.util.tools.SLBindings;
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
public abstract class UIBookshelf
        implements Displayable
{
    
    private final Displayer<UIBook> bookDisplayer;
    private final ObservableList<UIBook> books;
    
    private final BooleanBinding isEmptyBinding;
    
    public UIBookshelf()
    {
        this(null);
    }
    
    // TO-DOC
    public UIBookshelf(@Nullable StackPane contentPane)
    {
        this.bookDisplayer = new Displayer<>(contentPane != null ? contentPane : new StackPane());
        
        this.books = FXCollections.observableArrayList();
        
        //
        
        final Binding<Boolean> innerBinding = SLBindings.createRecursiveBinding((UIBook uiBook) -> {
            if (uiBook != null) {
                final UIPageHandler pageHandler = uiBook.getPageHandler();
                if (pageHandler != null)
                    return pageHandler.isEmptyBinding();
            }
            return null;
        }, bookDisplayer.displayProperty());
        
        this.isEmptyBinding = Bindings.createBooleanBinding(
                () -> innerBinding.getValue(),
                innerBinding, bookDisplayer.displayProperty());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link Displayer} responsible for both containing and displaying the {@link UIBook} that is currently {@link #selectionProperty() selected} by this {@link UIBookshelf}.</p>
     *
     * @return The {@link Displayer} responsible for both containing and displaying the {@link UIBook} that is currently {@link #selectionProperty() selected} by this {@link UIBookshelf}.
     */
    public @NotNull Displayer<UIBook> getBookDisplayer()
    {
        return bookDisplayer;
    }
    
    /**
     * <p>Returns the {@link ObservableList} containing the {@link UIBook UIBooks} in this {@link UIBookshelf}.</p>
     *
     * @return The {@link ObservableList} containing the {@link UIBook UIBooks} in this {@link UIBookshelf}.
     */
    public @NotNull ObservableList<UIBook> getBooks()
    {
        return books;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the currently selected {@link UIBook} in this {@link UIBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getBookDisplayer()}<b>.</b>{@link Displayer#displayProperty() displayProperty()}</code></i></blockquote>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the currently selected {@link UIBook} in this {@link UIBookshelf}.
     *
     * @see #getBookDisplayer()
     */
    public @NotNull ReadOnlyObjectProperty<UIBook> selectionProperty()
    {
        return getBookDisplayer().displayProperty();
    }
    
    /**
     * <p>Returns the currently selected {@link UIBook} in this {@link UIBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getBookDisplayer()}<b>.</b>{@link Displayer#displayProperty() displayProperty()}<b>.</b>{@link ReadOnlyObjectProperty#get() get()}</code></i></blockquote>
     *
     * @return The currently selected {@link UIBook content} in this {@link UIBookshelf}.
     *
     * @see #selectionProperty()
     * @see #getBookDisplayer()
     */
    public @Nullable UIBook getSelection()
    {
        return getBookDisplayer().displayProperty().get();
    }
    
    /**
     * <p>Returns a {@link BooleanBinding} that reflects if this {@link UIBookshelf} is {@link UIPageHandler#isEmptyBinding() empty}.</p>
     * <blockquote><b>Bound Passthrough Definition:</b> <i><code>{@link Bindings#not(ObservableBooleanValue) Bindings.not}<b>(</b>{@link #getBookDisplayer()}<b>.</b>{@link Displayer#getDisplay() getDisplay()}<b>.</b>{@link UIBook#getPageHandler() getPageHandler()}<b>.</b>{@link UIPageHandler#isEmptyBinding() isEmptyBinding()}<b>)</b></code></i></blockquote>
     *
     * @return A {@link BooleanBinding} that reflects if this {@link UIBookshelf} is {@link UIPageHandler#isEmptyBinding() empty}.
     */
    public @NotNull BooleanBinding isEmptyBinding()
    {
        return isEmptyBinding;
    }
    
    /**
     * <p>Checks if the {@link #getContent() content} of this {@link UIBookshelf} is {@link UIPageHandler#isEmptyBinding() empty}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #isEmptyBinding()}<b>.</b>{@link BooleanBinding#get() get()}</code></i></blockquote>
     *
     * @return True if the {@link #getContent() content} of this {@link UIBookshelf} is {@link UIPageHandler#isEmpty() empty}, false if it is not.
     *
     * @see #isEmptyBinding()
     * @see UIPageHandler#isEmpty()
     */
    public boolean isEmpty()
    {
        return isEmptyBinding.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /**
     * <p>Returns the {@link Pane} that contains the {@link #getSelection() content} of this {@link UIBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getBookDisplayer()}<b>.</b>{@link Displayer#getDisplayContainer() getDisplayContainer()}</code></i></blockquote>
     *
     * @return The {@link Pane} that contains the {@link #getSelection() content} of this {@link UIBookshelf}.
     */
    @Override
    public Pane getContent()
    {
        return bookDisplayer.getDisplayContainer();
    }
    
    //</editor-fold>
}
