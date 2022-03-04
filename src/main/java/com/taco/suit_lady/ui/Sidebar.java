package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.jfx.components.button.ImageButtonGroup;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Sidebar
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReentrantLock lock;
    
    private final StackPane childButtonPane;
    private final StackPane contentPane;
    private final ImageButton backImageButton;
    private final ToolBar toolBar;
    
    private final ObservableList<SidebarBookshelf> bookshelvesProperty;
    private final ReadOnlyObjectWrapper<SidebarBookshelf> selectedBookshelfProperty;
    
    /**
     * <p>Refer to {@link #Sidebar(FxWeaver, ConfigurableApplicationContext, StackPane, StackPane, ImagePane, ToolBar) Fully-Parameterized Constructor} for details.</p>
     * <p><b>Identical to...</b></p>
     * <blockquote><code>new Sidebar(childButtonPane, contentPane, <u>null</u>)</code></blockquote>
     */
    public Sidebar(
            @NotNull FxWeaver weaver,
            @NotNull ConfigurableApplicationContext ctx,
            @NotNull StackPane childButtonPane,
            @NotNull StackPane contentPane,
            @NotNull ToolBar toolBar)
    {
        this(weaver, ctx, childButtonPane, contentPane, null, toolBar);
    }
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p>Constructs a new {@link Sidebar} instance used to contain {@link SidebarBookshelf SidebarBookshelf}.</p>
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
     *     <li><b>{@link Sidebar}: </b>Contains and manages a group of {@link UIBookshelf UIBookshelfs} in the form of {@link SidebarBookshelf SidebarBookshelfs}.</li>
     *     <li><b>{@link SidebarBookshelf}: </b>Contains and manages a group of {@link UIBook UIBooks}.</li>
     *     <li><b>{@link UIBook}: </b>{@link Displayable} implementations that contain, manage, and display the {@link UIPage#getContentPane() contents} of {@link UIPage UIPages}.</li>
     * </ol>
     *
     * @param childButtonPane The {@link StackPane} that the {@link ImageButton ImageButtons} linked to each {@link UIBook} in the currently selected {@link SidebarBookshelf} are displayed on.
     * @param contentPane     The {@link StackPane} that the {@link UIBook#getContentPane() contents} of the currently displayed {@link UIBook} based on the currently selected {@link SidebarBookshelf} are displayed on.
     * @param backImagePane   The {@link ImagePane} on which the {@link #back() Back} {@link #getBackButton() Button} is displayed on.
     *
     * @throws NullPointerException If the {@code childButtonPane} parameter is {@code null}.
     * @throws NullPointerException If the {@code contentPane} parameter is {@code null}.
     */
    public Sidebar(
            @NotNull FxWeaver weaver,
            @NotNull ConfigurableApplicationContext ctx,
            @NotNull StackPane childButtonPane,
            @NotNull StackPane contentPane,
            @Nullable ImagePane backImagePane,
            @NotNull ToolBar toolBar)
    {
        this.weaver = Exc.nullCheck(weaver, "FxWeaver");
        this.ctx = Exc.nullCheck(ctx, "Application Context");
        
        this.lock = new ReentrantLock();
        
        Exc.nullCheck(childButtonPane, "Sidebar Child Button Pane");
        Exc.nullCheck(contentPane, "Sidebar Content Pane");
        
        this.childButtonPane = childButtonPane;
        this.contentPane = contentPane;
        this.backImageButton = new ImageButton(
                this,
                "Back",
                "back_arrow",
                backImagePane,
                null,
                this::back,
                false,
                ImageButton.SMALL
        );
        this.toolBar = toolBar;
        
        this.bookshelvesProperty = FXCollections.observableArrayList();
        this.selectedBookshelfProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.backImageButton.getImagePane().visibleProperty().bind(
                Bind.recursiveBinding(selectedBookshelf -> {
                    if (selectedBookshelf != null)
                        return Bindings.and(Bindings.not(selectedBookshelf.isEmptyBinding()), selectedBookshelf.getBookDisplayer().visibleBinding());
                    return null;
                }, selectedBookshelfProperty));
        
        this.backImageButton.getImagePane().managedProperty().bind(this.backImageButton.getImagePane().visibleProperty());
        
        this.selectedBookshelfProperty.addListener((observable, oldBookshelf, newBookshelf) -> {
            if (!Objects.equals(oldBookshelf, newBookshelf)) { //If the menu base is already selected, return silently and do nothing.
                if (oldBookshelf != null) {
                    final VBox oldButtonBox = oldBookshelf.getButtonBox();
                    if (oldButtonBox != null)
                        this.childButtonPane.getChildren().remove(oldButtonBox);
                    
                    final Button oldButton = oldBookshelf.getButton();
                    if (oldButton != null)
                        oldButton.setText(oldBookshelf.getName());
                }
                if (newBookshelf != null) {
                    final VBox buttonBox = newBookshelf.getButtonBox();
                    
                    this.childButtonPane.getChildren().add(buttonBox);
                    this.contentPane.getChildren().clear();
                    this.contentPane.getChildren().add(newBookshelf.getContentPane());
    
                    final Button newButton = newBookshelf.getButton();
                    if (newButton != null)
                        newButton.setText("<< " + newBookshelf.getName() + " >>");
                }
            }
        });
        
        this.bookshelvesProperty.addListener((ListChangeListener<SidebarBookshelf>) c -> FX.runFX(() -> {
            while (c.next()) {
                c.getAddedSubList().forEach(sidebarBookshelf -> {
                    toolBar.getItems().add(sidebarBookshelf.getButton());
//                    sidebarBookshelf.select();
                });
                c.getRemoved().forEach(sidebarBookshelf -> toolBar.getItems().remove(sidebarBookshelf.getButton()));
            }
        }, true));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes this {@link Sidebar} and associated components and functionality..</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Executed on the {@link FX#runFX(Runnable, boolean) JavaFX Thread}.</li>
     *     <li>The calling {@link Thread} blocks until initialization process is complete.</li>
     *     <li><code><i>{@link #initialize()}</i></code> can <u>only</u> be called <u>once</u>.</li>
     *     <li>Calling <code><i>{@link #initialize()}</i></code> more than once on a single {@link Sidebar} instance can (and likely will) result in unexpected and/or unpredictable runtime functionality.</li>
     * </ol>
     * <p><b>Initialization</b></p>
     * <ol>
     *     <li>Calls <code><i>{@link #getBackButton()}<b>.</b>{@link ImageButton#init() initialize()}</i></code> to initialize the {@link #back() Back} {@link #getBackButton() Button} assigned to this {@link Sidebar}.</li>
     *     <li>Calls <code><i>{@link #bookshelvesProperty()}<b>.</b>{@link Iterable#forEach(Consumer) forEach}<b>(</b>{@link SidebarBookshelf#initialize() initialize()}<b>)</b></i></code> to initialize all {@link SidebarBookshelf SidebarBookshelves} in this {@link Sidebar}.</li>
     *     <li>{@link #selectedBookshelfProperty() Selects} the first (<code><i>{@link #bookshelvesProperty() bookshelvesProperty().get(0)}</i></code>) {@link SidebarBookshelf} in this {@link Sidebar}.</li>
     * </ol>
     */
    public void initialize()
    {
        FX.runFX(() -> {
            backImageButton.init();
            childButtonPane.setAlignment(Pos.TOP_LEFT);
            
            bookshelvesProperty().forEach(SidebarBookshelf::initialize);
            
            final SidebarBookshelf firstBookshelf = bookshelvesProperty().get(0);
            if (firstBookshelf != null)
                setSelectedBookshelf(firstBookshelf);
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
     * <p>Returns the {@link StackPane} whose {@link StackPane#getChildren() contents} are bound to the {@link SidebarBookshelf#getButtonBox() Button Box} that contains the {@link ImageButton ImageButtons}
     * corresponding to each {@link UIBook} child in the parent {@link SidebarBookshelf} that is currently {@link #selectedBookshelfProperty() selected} by this {@link Sidebar} instance.</p>
     *
     * @return The {@link StackPane} whose {@link StackPane#getChildren() contents} are bound to the {@link SidebarBookshelf#getButtonBox() Button Box} that contains the {@link ImageButton ImageButtons}
     * corresponding to each {@link UIBook} child in the parent {@link SidebarBookshelf} that is currently {@link #selectedBookshelfProperty() selected} by this {@link Sidebar} instance.
     */
    public StackPane getChildButtonPane()
    {
        return childButtonPane;
    }
    
    /**
     * <p>Returns the {@link StackPane} on which the {@link #getSelectedBookshelf() Selected} {@link SidebarBookshelf Bookshelf's} {@link SidebarBookshelf#getContentPane() content} is displayed.</p>
     *
     * @return The {@link StackPane} on which the {@link #getSelectedBookshelf() Selected} {@link SidebarBookshelf Bookshelf's} {@link SidebarBookshelf#getContentPane() content} is displayed.
     */
    public StackPane getContentPane()
    {
        return contentPane;
    }
    
    /**
     * <p>Returns the {@link ImageButton} that functions as the {@link #back() Back} {@link ImageButton Button} for turning the {@link SidebarBookshelf#selectionProperty() selected} {@link UIBook} back a {@link UIPage page}.</p>
     *
     * @return The {@link ImageButton} that functions as the {@link #back() Back} {@link ImageButton Button} for turning the {@link SidebarBookshelf#selectionProperty() selected} {@link UIBook} back a {@link UIPage page}.
     */
    public ImageButton getBackButton()
    {
        return backImageButton;
    }
    
    /**
     * <p>Returns the {@link ObservableList} containing the {@link SidebarBookshelf Bookshelves} in this {@link Sidebar}.</p>
     *
     * @return The {@link ObservableList} containing the {@link SidebarBookshelf Bookshelves} in this {@link Sidebar}.
     */
    public ObservableList<SidebarBookshelf> bookshelvesProperty()
    {
        return bookshelvesProperty;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty property} containing the {@link #getSelectedBookshelf() selected} {@link SidebarBookshelf}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty property} containing the {@link #getSelectedBookshelf() selected} {@link SidebarBookshelf}.
     *
     * @see #getSelectedBookshelf()
     * @see #setSelectedBookshelf(SidebarBookshelf)
     * @see #isBookshelfSelected(SidebarBookshelf)
     */
    public ReadOnlyObjectProperty<SidebarBookshelf> selectedBookshelfProperty()
    {
        return selectedBookshelfProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link #selectedBookshelfProperty() selected} {@link SidebarBookshelf}.</p>
     *
     * @return The {@link #selectedBookshelfProperty() selected} {@link SidebarBookshelf}.
     *
     * @see #selectedBookshelfProperty()
     * @see #setSelectedBookshelf(SidebarBookshelf)
     * @see #isBookshelfSelected(SidebarBookshelf)
     */
    public SidebarBookshelf getSelectedBookshelf()
    {
        return selectedBookshelfProperty.get();
    }
    
    /**
     * <p>Sets the {@link #selectedBookshelfProperty() selected} {@link SidebarBookshelf} to the specified value.</p>
     *
     * @param menu The {@link SidebarBookshelf} to be {@link #selectedBookshelfProperty() selected}.
     *
     * @see #selectedBookshelfProperty()
     * @see #getSelectedBookshelf()
     * @see #isBookshelfSelected(SidebarBookshelf)
     */
    protected void setSelectedBookshelf(SidebarBookshelf menu)
    {
        selectedBookshelfProperty.set(menu);
    }
    
    /**
     * <p>Checks if the specified {@link SidebarBookshelf} is currently {@link #selectedBookshelfProperty() selected} or not.</p>
     *
     * @param menu The {@link SidebarBookshelf} being checked.
     *
     * @return True if the specified {@link SidebarBookshelf} is currently {@link #selectedBookshelfProperty() selected}, false if it is not.
     *
     * @see #selectedBookshelfProperty()
     * @see #getSelectedBookshelf()
     * @see #setSelectedBookshelf(SidebarBookshelf)
     * @see Obj#equalsExcludeNull(Object, Object)
     */
    public boolean isBookshelfSelected(SidebarBookshelf menu)
    {
        return Obj.equalsExcludeNull(menu, getSelectedBookshelf());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
    
    /**
     * <p>{@link SidebarBookshelf#clearSelection() Clears} the {@link ImageButtonGroup#selectedButtonProperty() selection} of every {@link SidebarBookshelf SidebarBookshelves} in this {@link Sidebar}.</p>
     *
     * @see SidebarBookshelf#clearSelection()
     * @see SidebarBookshelf#getButtonGroup()
     * @see ImageButtonGroup#selectedButtonProperty()
     * @see ImageButtonGroup#clearSelection(ImageButton...)
     */
    protected void clearAllSelections()
    {
        bookshelvesProperty().forEach(menu -> menu.clearSelection());
    }
    
    /**
     * <p>{@link UIPageHandler#turnTo(UIPage) Turns} the {@link UIBook} that is currently {@link UIBookshelf#getBookDisplayer() selected} by the {@link UIBookshelf} that is currently {@link #selectedBookshelfProperty() selected} by this {@link Sidebar} instance {@link UIPageHandler#back() back} one {@link UIPage page}.</p>
     */
    public void back()
    {
        final UIBookshelf selectedBookshelf = getSelectedBookshelf();
        if (selectedBookshelf != null) {
            final UIBook selectedBook = selectedBookshelf.getBookDisplayer().getDisplay();
            if (selectedBook != null)
                selectedBook.getPageHandler().back();
        }
    }
}
