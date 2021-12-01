package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.button.BoundImageButtonGroup;
import com.taco.suit_lady.view.ui.jfx.button.ButtonViewable;
import com.taco.suit_lady.view.ui.jfx.button.ImageButton;
import com.taco.suit_lady.view.ui.jfx.button.ImageButtonGroup;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// TODO - Add BooleanProperty bidirectionally-bound to the selected SidebarBookshelf of the parent Sidebar, strictly for convenience.
public class SidebarBookshelf extends UIBookshelf
{
    private final ReentrantLock lock;
    private final StringProperty nameProperty; // Currently Unused
    
    private final Sidebar owner;
    private final VBox buttonBox;
    private final Button button;
    
    private final BoundImageButtonGroup<UIBook> bookButtonGroup;
    
    /**
     * <p>Refer to {@link #SidebarBookshelf(Sidebar, Button, StackPane) Fully-Parameterized Constructor} for additional information.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>
     * new {@link #SidebarBookshelf(Sidebar, Button, StackPane) SidebarBookshelf}<b>(</b>owner<b>,</b> menuButton<b>,</b> <u>null</u><b>)</b>
     * </code></i></blockquote>
     */
    public SidebarBookshelf(@NotNull Sidebar owner, @NotNull Button menuButton)
    {
        this(owner, menuButton, null);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     *
     * @param owner       The {@link Sidebar} object in charge of managing this {@link SidebarBookshelf}.
     *                    <ul>
     *                          <li>Cannot be {@code null}.</li>
     *                    </ul>
     * @param menuButton  The {@link Button} that {@link #select() selects} this {@link SidebarBookshelf} when {@link Button#onActionProperty() pressed}.
     *                    <ul>
     *                          <li>Cannot be {@code null}.</li>
     *                    </ul>
     * @param contentPane The {@link StackPane} on which the {@link #getContent() contents} of this {@link SidebarBookshelf} are displayed.
     *                    <ul>
     *                          <li>Cannot be {@code null}.</li>
     *                    </ul>
     *
     * @throws NullPointerException If the {@code owner} parameter is {@code null}.
     * @throws NullPointerException If the {@code menuButton} parameter is {@code null}.
     * @see UIBookshelf#UIBookshelf(StackPane)
     */
    public SidebarBookshelf(@NotNull Sidebar owner, @NotNull Button menuButton, @Nullable StackPane contentPane)
    {
        super(contentPane);
        
        ExceptionTools.nullCheck(owner, "Sidebar (Owner/Parent)");
        ExceptionTools.nullCheck(menuButton, "Menu Button");
        
        this.lock = owner.getLock();
        this.nameProperty = new SimpleStringProperty();
        
        this.owner = owner;
        this.buttonBox = new VBox();
        this.button = menuButton;
        
        this.bookButtonGroup = new BoundImageButtonGroup<>(getBooks(), lock);
        
        //
        
        this.bookButtonGroup.selectedButtonProperty().addListener((observable, oldButton, newButton) -> getBookDisplayer().setDisplay(this.bookButtonGroup.getViewableByButton(newButton)));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes this {@link Sidebar} and associated components and functionality..</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Defines style, formatting, and other UI settings for the {@link #getButtonBox() Button Box} for this {@link SidebarBookshelf}.</li>
     *     <li>Adds the {@link UIBook#getButtonView() buttons} assigned to the {@link UIBook UIBooks} in this {@link SidebarBookshelf} to the {@link #getButtonBox() Button Box}.</li>
     *     <li>Defines the {@link Button#setOnAction(EventHandler) functionality} of the {@link #getButton() button} assigned to this {@link SidebarBookshelf}.</li>
     * </ol>
     */
    public void initialize()
    {
        // TODO - It might be a good idea to run the majority (if not all) of this on the JFX Thread.
        // TODO - The synchronization works for making most aspects of SidebarBookshelf Thread-Safe, but if a thread already has a reference to the book list, the list can still be modified asynchronously.
        lock.lock();
        try {
            buttonBox.setSpacing(1.0);
            buttonBox.setAlignment(Pos.TOP_LEFT);
            
            final List<UIBook> books = getBooks();
            for (UIBook book: books) {
                final ImageButton imageButton = book.getButtonView();
                if (imageButton != null)
                    buttonBox.getChildren().add(imageButton.getImagePane());
            }
            button.setOnAction(event -> select());
        } finally {
            lock.unlock();
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <b>— Not Yet Implemented —</b>
     * <p>Returns the {@link StringProperty} containing the {@link #getName() name} of this {@link SidebarBookshelf}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Exists only to allow a text description to be used somewhere in the UI alongside the {@link #getButton() button}.</li>
     *     <li>An example usage would be as the {@link #getButton() button's} {@link Button#tooltipProperty() tooltip}.</li>
     *     <li>Can be {@code null}.</li>
     * </ol>
     *
     * @return The {@link StringProperty} containing the {@link #getName() name} of this {@link SidebarBookshelf}.
     *
     * @see #getName()
     * @see #setName(String)
     */
    public @NotNull StringProperty nameProperty()
    {
        return nameProperty;
    }
    
    /**
     * <p>Returns the {@link #nameProperty() name} of this {@link SidebarBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #nameProperty()}<b>.</b>{@link StringProperty#get() get()}</code></i></blockquote>
     *
     * @return The {@link #nameProperty() name} of this {@link SidebarBookshelf}.
     *
     * @see #nameProperty()
     * @see #setName(String)
     */
    public @Nullable String getName()
    {
        return nameProperty.get();
    }
    
    /**
     * <p>Sets the {@link #nameProperty() name} of this {@link SidebarBookshelf} to the specified value.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #nameProperty()}<b>.</b>{@link StringProperty#set(Object) set}<b>(</b>name<b>)</b></code></i></blockquote>
     *
     * @param name The value to be set as the new {@link #nameProperty() name} of this {@link SidebarBookshelf}.
     *
     * @see #nameProperty()
     * @see #getName()
     */
    public void setName(@Nullable String name)
    {
        nameProperty.set(name);
    }
    
    /**
     * <p>Returns the {@link Sidebar} object containing this {@link SidebarBookshelf} instance.</p>
     *
     * @return The {@link Sidebar} object containing this {@link SidebarBookshelf} instance.
     */
    public @NotNull Sidebar getOwner()
    {
        return owner;
    }
    
    /**
     * <p>Returns the {@link VBox} {@link Region container} housing all {@link UIBook#buttonViewProperty() selection buttons} mapped to all {@link UIBook UIBooks} in this {@link SidebarBookshelf}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link VBox} returned by {@link #getButtonBox() this method} is created automatically in the {@link SidebarBookshelf} {@link #SidebarBookshelf(Sidebar, Button, StackPane) constructor}.</li>
     * </ol>
     * <p><b>Usage in {@link Sidebar}</b></p>
     * <ol>
     *     <li>The {@link #getOwner() Sidebar} class automatically sets the {@link #getButtonBox() button box} that is displayed on the UI at any given time to always reflect the {@link Sidebar#selectedBookshelfProperty() selected} {@link SidebarBookshelf}.</li>
     *     <li>Therefore, the {@link VBox#parentProperty() parent} of the {@link VBox} returned by {@link #getButtonBox() this method} must <i>never</i> be directly modified, changed, or otherwise manipulated.</li>
     *     <li>{@link #getButtonBox() This method} previously had {@code protected access} for that very reason, and was only changed to permit universal <i>read access</i> to the {@link VBox button box}.</li>
     * </ol>
     *
     * @return The {@link VBox} {@link Region container} housing all {@link UIBook#buttonViewProperty() selection buttons} mapped to all {@link UIBook UIBooks} in this {@link SidebarBookshelf}.
     *
     * @see Sidebar#selectedBookshelfProperty()
     */
    public @NotNull VBox getButtonBox()
    {
        // TODO - In cases such as this — where an object type is of an external source, is not read-only, but needs to be read-only — an inner class containing bindings or read-only properties bound to the internal default properties might be a good solution.
        return buttonBox;
    }
    
    /**
     * <p>Returns the {@link Button} assigned to this {@link SidebarBookshelf} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>When the {@link Button} is {@link Button#onActionProperty() pressed}, the <code><i>{@link #select()}</i></code> method is called on this {@link SidebarBookshelf} instance.</li>
     *     <li>Refer to <code><i>{@link #initialize()}</i></code> for {@link Button} implementation.</li>
     * </ol>
     *
     * @return The {@link Button} assigned to this {@link SidebarBookshelf} instance.
     */
    public @NotNull Button getButton()
    {
        return button;
    }
    
    /**
     * <p>Returns the {@link BoundImageButtonGroup} responsible for containing and managing the {@link ImageButton ImageButtons} used to switch between {@link ImageButtonGroup#selectedButtonProperty() selected} {@link UIBook UIBooks} in this {@link SidebarBookshelf}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link BoundImageButtonGroup} returned by this method is used for managing {@link UIBook} {@link ImageButtonGroup#selectedButtonProperty() selection}.</li>
     *     <li>The above works because {@link UIBook} implements {@link ButtonViewable}.</li>
     *     <li>{@link BoundImageButtonGroup BoundButtonViewGroups} add functionality that allows you to {@link BoundImageButtonGroup#getViewableByButton(ImageButton) retrieve} the {@link ButtonViewable} (in this case, {@link UIBook}) the specified {@link ImageButton} is assigned to.</li>
     *     <li>The {@link BoundImageButtonGroup value} returned by this method is guaranteed to be {@code non-null}.</li>
     * </ol>
     *
     * @return The {@link ImageButtonGroup} responsible for containing and managing the {@link ImageButton ImageButtons} used to switch between {@link ImageButtonGroup#selectedButtonProperty() selected} {@link UIBook UIBooks} in this {@link SidebarBookshelf}.
     */
    public @NotNull BoundImageButtonGroup<UIBook> getButtonGroup()
    {
        return bookButtonGroup;
    }
    
    /**
     * <p>{@link ImageButtonGroup#clearSelection(ImageButton...) Clears} the {@link UIBook} currently {@link ImageButtonGroup#selectedButtonProperty() selected} for this {@link SidebarBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getButtonGroup()}<b>.</b>{@link BoundImageButtonGroup#clearSelection(ImageButton...) clearSelection()}</code></i></blockquote>
     *
     * @return True if the selection was successfully cleared, false if it was not.
     */
    public boolean clearSelection()
    {
        return getButtonGroup().clearSelection();
    }
    
    /**
     * <p>Sets the {@link Sidebar#selectedBookshelfProperty() selection} of the {@link #getOwner() Sidebar} containing this {@link SidebarBookshelf} instance to this {@link SidebarBookshelf}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getOwner()}<b>.</b>{@link Sidebar#setSelectedBookshelf(SidebarBookshelf) setSelectedBookshelf}<b>(</b>this<b>)</b></code></i></blockquote>
     */
    public void select()
    {
        getOwner().setSelectedBookshelf(this);
    }
    
    //</editor-fold>
    
    
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    @Override
    public String toString()
    {
        return "SidebarBookshelf{" +
               "name=" + nameProperty.get() +
               ", buttonBox=" + buttonBox +
               ", button=" + button +
               ", bookButtonGroup=" + bookButtonGroup +
               '}';
    }
}
