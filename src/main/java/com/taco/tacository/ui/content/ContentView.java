package com.taco.tacository.ui.content;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

// TO-DOC
public abstract class ContentView<P extends Pane>
{
    
    private final ReentrantLock lock;
    private final ReadOnlyObjectWrapper<P> contentProperty;
    
    /**
     * <p>Refer to {@link #ContentView(Pane)}  Fully-Parameterized Constructor} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote><code>new ContentView(<u>null</u>)</code></blockquote>
     */
    public ContentView()
    {
        this(null);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p>Constructs a new {@link ContentView} that displays the specified {@link P content}.</p>
     * <p><hr>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         The primary purpose of a {@link ContentView} implementation is to define the actions taken upon a {@link #getContent() content} {@link #onContentChange(Pane, Pane) change}.
     *         <ol>
     *             <li>Refer to <code><i>{@link TransitionContentView}<b>.</b>{@link TransitionContentView#onContentChange(Pane, Pane) onContentChange(Pane, Pane)}</i></code> for example usage.</li>
     *         </ol>
     *     </li>
     *     <li>The baseline {@link ContentView} contains a {@link ReadOnlyObjectWrapper property} containing the {@link #getContent() content} to be displayed by this {@link ContentView}.</li>
     *     <li>Implementing classes provide additional functionality, setup, management, and usage of the {@link #getContent() content}.</li>
     * </ol>
     *
     * @param content The {@link #getContent() content} to be displayed by this {@link ContentView}.
     */
    public ContentView(@Nullable P content)
    {
        this.lock = new ReentrantLock();
        this.contentProperty = new ReadOnlyObjectWrapper<>(content);
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onContentChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} the {@link Pane} {@link P implementation} displayed as the {@link #getContent() content} of this {@link ContentView}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} the {@link Pane} {@link P implementation} displayed as the {@link #getContent() content} of this {@link ContentView}.
     */
    public final @NotNull ReadOnlyObjectProperty<P> contentProperty()
    {
        return contentProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Pane} {@link P implementation} displayed as the {@link #getContent() content} of this {@link ContentView}.</p>
     *
     * @return The {@link Pane} {@link P implementation} displayed as the {@link #getContent() content} of this {@link ContentView}.
     */
    public final @Nullable P getContent()
    {
        return contentProperty.get();
    }
    
    /**
     * <p>Sets the {@link Pane} {@link P implementation} displayed as the {@link #contentProperty() content} of this {@link ContentView} to the specified value.</p>
     *
     * @param content The {@link Pane} {@link P implementation} to be set as the {@link #contentProperty() content} displayed by this {@link ContentView}.
     */
    public final void setContent(@Nullable P content)
    {
        contentProperty.set(content);
    }
    
    //</editor-fold>
    
    /**
     * <p>An abstract method that is executed whenever the {@link #contentProperty() content} of this {@link ContentView} changes.</p>
     *
     * @param oldContent The previous {@link #contentProperty() content} displayed by this {@link ContentView}.
     * @param newContent The new {@link #contentProperty() content} to be displayed by this {@link ContentView}.
     */
    protected abstract void onContentChange(@Nullable P oldContent, @Nullable P newContent);
}
