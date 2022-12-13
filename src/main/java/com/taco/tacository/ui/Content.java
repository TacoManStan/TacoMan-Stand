package com.taco.tacository.ui;

import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.logic.LogiCore;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page.MandelbrotContentHandler;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.list_tools.A;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.ui.jfx.components.painting.surfaces.OverlaySurface;
import com.taco.tacository.ui.jfx.components.painting.surfaces.OverlayHandler;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>The primary backbone defining a {@link Content} module.</p>
 * <blockquote><i>See {@link ContentManager} for {@link Content} Framework details.</i></blockquote>
 * <br><hr><br>
 * <p><b>{@link Content} Basics</b></p>
 * <ol>
 *     <li>{@link Content} defines all components of the {@link ContentManager#getContent() Currently Active} {@link Content} implementation.</li>
 *     <li>
 *         {@link Content} is defined primarily by {@code 5} generic modules:
 *         <ol>
 *             <li><i>{@link Content} of type {@link T}</i></li>
 *             <li><i>{@link ContentData} of type {@link TD}</i></li>
 *             <li><i>{@link ContentController} of type {@link TC}</i></li>
 *             <li><i>{@link Footer} of type {@link F}</i></li>
 *             <li><i>{@link FooterController} of type {@link FC}</i></li>
 *         </ol>
 *     </li>
 *     <li>Use <i>{@link ContentManager#setContent(Content)}</i> to change the {@link ContentManager#getContent() Active Content} for this  {@link AppController Application}.</li>
 *     <li>
 *         Before a {@link Content} object can be used, the <i>{@link Content#init()}</i> method must first be called.
 *         <ul>
 *             <li>The <i>{@link #init()}</i> method returns a self-reference to this {@link Content} instance.</li>
 *             <li>
 *                 Chaining {@link Content} {@link Content#Content(Springable) Construction} and {@link #init() Initializing} in a single statement is a common strategy for constructing a new {@link Content}.
 *                 <ul>
 *                     <li><i><u>Example</u>: {@link GameViewContent#GameViewContent(Springable) new} {@link GameViewContent}<b>(</b>springable<b>).</b>{@link GameViewContent#init() init()}<b>;</b></i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 * <hr><br>
 * <p><b>Abstract Methods</b></p>
 * <p><i>The {@link Content} defines a variety of abstract methods that require implementation.</i></p>
 * <ol>
 *     <li>
 *         <p><b>Module Accessor Methods</b></p>
 *         <ul>
 *             <li><b>{@link #loadData()}:</b> Defines and returns the {@link ContentData} instance assigned to this {@link Content} implementation.</li>
 *             <li><b>{@link #controllerDefinition()}:</b> Defines and returns the {@link ContentController} instance assigned to handle the {@code JavaFX Components} of this {@link Content} implementation.</li>
 *             <li>
 *                 <b>{@link #constructFooter()}:</b> Defines and returns the {@link Footer} instance assigned to this {@link Content} implementation.
 *                 <ul>
 *                     <li>By default, {@link Content} implementations do not have a {@link Footer}, and the value of <i>{@link #constructFooter()}</i> is ignored.</li>
 *                     <li>To enable the {@link Footer} for a {@link Content} implementation, the default <i>{@link ContentController#hasFooter()}</i> implementation for the {@link ContentController} assigned to this {@link Content} implementation must overridden to return {@code true}.</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         <p><b>Peripheral Input Handling</b></p>
 *         <ul>
 *             <li>
 *                 <p><b>Mouse Input Handling</b></p>
 *                 <ul>
 *                     <li><i><b>{@link #handleMousePressEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_PRESSED Mouse Pressed} {@link MouseEvent Event}.</i></li>
 *                     <li><i><b>{@link #handleMouseReleaseEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_RELEASED Mouse Released} {@link MouseEvent Event}.</i></li>
 *                     <li><i><b>{@link #handleMouseMoveEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_MOVED Mouse Moved} {@link MouseEvent Event}.</i></li>
 *                     <li><i><b>{@link #handleMouseDragEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_DRAGGED Mouse Drag} {@link MouseEvent Event}.</i></li>
 *                     <li><i><b>{@link #handleMouseEnterEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_ENTERED Mouse Entered} {@link MouseEvent Event}.</i></li>
 *                     <li><i><b>{@link #handleMouseExitEvent(MouseEvent, boolean)}:</b> Defines the response to a {@link MouseEvent#MOUSE_EXITED Mouse Exited} {@link MouseEvent Event}.</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <p><b>Key Input Handling</b></p>
 *                 <ul>
 *                     <li><i><b>{@link #handleKeyEvent(KeyEvent, boolean)}:</b> Defines the response to all {@link KeyEvent} input.</i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         <p><b>{@link Content} Event Handling</b></p>
 *         <ul>
 *             <li>
 *                 <p><b>Activation Response Methods</b></p>
 *                 <ul>
 *                     <li><i><b>{@link #onActivate()}:</b> Defines any necessary setup operations required to {@link ContentManager#setContent(Content) Activate} this {@link Content} implementation.</i></li>
 *                     <li><i><b>{@link #onDeactivate()}:</b> Defines any necessary setup operations required to {@link ContentManager#setContent(Content) Deactivate} this {@link Content} implementation.</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <p><b>{@link #getBookshelves() Bookshelves} {@link ListChangeListener Change} Response Methods</b></p>
 *                 <ul>
 *                     <li><i><b>{@link #onBookshelfAdded(SidebarBookshelf)}:</b> Defines any necessary operations required when a new {@link UIBookshelf} is {@code added} to this {@link Content} implementation.</i></li>
 *                     <li><i><b>{@link #onBookshelfRemoved(SidebarBookshelf)}:</b> Defines any necessary operations required when a new {@link UIBookshelf} is {@code removed} from this {@link Content} implementation.</i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 * <hr><br>
 * <p><b>Other {@link Content} Components</b></p>
 * <ol>
 *     <li>
 *         <b>{@link #getOverlayHandler() Overlay Handler}</b>
 *         <ul>
 *             <li>Returns the {@link OverlayHandler} for this {@link Content} instance.</li>
 *             <li>The {@link OverlayHandler} accepts any number of {@link OverlaySurface Overlay Surface} objects containing supplemental graphical information for this {@link Content} instance.</li>
 *         </ul>
 *         <p><i>See {@link OverlayHandler} for additional information.</i></p>
 *     </li>
 *     <li>
 *         <b>{@link #getBookshelves() Bookshelves}</b>
 *         <ul>
 *             <li>Returns the {@link List} of {@link SidebarBookshelf Bookshelves} assigned to this {@link Content} instance.</li>
 *             <li>The {@link Sidebar} contains a number of universal, {@code Application Scope} {@link UIBookshelf Bookshelves}.</li>
 *             <li>Every {@link SidebarBookshelf Bookshelf} in the aforementioned {@link #getBookshelves() Bookshelves List} is {@code Content Scope} and therefore only available when the owning {@link Content} is currently {@link ContentManager#contentProperty() Active}.</li>
 *             <li>Use the abstract <i>{@link #onBookshelfAdded(SidebarBookshelf)}</i> and <i>{@link #onBookshelfRemoved(SidebarBookshelf)}</i> event handler methods to respond to a {@link SidebarBookshelf} being added or removed from this {@link Content} object.</li>
 *             <li>
 *                 The <i>{@link #getBookshelves()}</i> method is used to access the {@link List} of {@link SidebarBookshelf Bookshelves} assigned to this {@link Content} instance.
 *                 <ul>
 *                     <li><i>{@link #getBookshelves()}</i> returns a {@link ReadOnlyListProperty} and can therefore not be modified directly.</li>
 *                     <li>
 *                         Instead, use <i>{@link #injectBookshelf(String, UIBook...)}</i> to add a {@link SidebarBookshelf Bookshelf} to a {@link Content} instance.
 *                         <ul>
 *                             <li><i>Note that <i>{@link #injectBookshelf(String, UIBook...)}</i> is {@code protected} and can therefore only be accessed by the internal framework or, more importantly, implementing {@link Content} types.</i></li>
 *                         </ul>
 *                     </li>
 *                     <li><i>{@link #getBookshelves()}</i> does not return a copy and therefore any operations performed on the result must be properly {@code synchronized}. Otherwise, a {@link ConcurrentModificationException} is likely to eventually be {@code thrown}.</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T>  The {@link Content} implementation of this {@link Content}.
 * @param <TD> The {@link ContentData} implementation of this {@link Content}.
 * @param <TC> The {@link ContentController} implementation of this {@link Content}.
 * @param <F>  The {@link Footer} implementation of this {@link Content}.
 * @param <FC> The {@link FooterController} implementation of this {@link Content}.
 */
//TO-EXPAND
public abstract class Content<T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>,
        F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>>
        implements Springable {
    
    private final Springable strictSpringable;
    
    private final TD data;
    private final TC controller;
    private final F footer;
    
    private final OverlayHandler overlayHandler;
    
    private final ReadOnlyListWrapper<SidebarBookshelf> bookshelves;
    
    public Content(@NotNull Springable springable) {
        this.strictSpringable = Exc.nullCheck(springable, "Springable Parent").asStrict();
        
        this.data = Exc.nullCheck(loadData(), "SLContentData");
        this.controller = Exc.nullCheckMessage(
                weaver().loadController(Exc.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file.");
        
        this.overlayHandler = new OverlayHandler(this, null);
        this.overlayHandler.addOverlay(new OverlaySurface(this, null, "default", 1));
        
        this.bookshelves = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.footer = constructFooter();
    }
    
    /**
     * <p>Initializes this {@link Content} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Executes any additional initialization operations necessary to wrap up {@link Content} {@link Content#Content(Springable) Construction}.</li>
     *     <li>The primary purpose of <i>{@link #init()}</i> is to define initialization logic such that all standard {@link Content#Content(Springable) Construction} has been completed.</li>
     *     <li>The separation of the {@link Content} {@link Content#Content(Springable) Constructor} and the <i>{@link #init()}</i> method allows dynamic control over the {@link Content} {@link Content#Content(Springable) Construction} process.</li>
     *     <li>Finally, <i>{@link #init()}</i> guarantees that all universal {@link Content} properties have been initialized, further improving control over the {@link Content} initialization process.</li>
     * </ol>
     *
     * @return This {@link Content} instance, cast to and returned as type {@link T}.
     */
    public T init() {
        //        footer = constructFooter();
        getController().init((T) this);
        return (T) this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ContentData} implementation of type {@link TD} assigned to this {@link Content} implementation.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The abstract <i>{@link #loadData()}</i> method is used to construct the unique {@link ContentData} implementation for this {@link Content} instance.</li>
     * </ol>
     *
     * @return The {@link ContentData} implementation of type {@link TD} assigned to this {@link Content} implementation.
     */
    public final @NotNull TD getData() { return data; }
    
    /**
     * <p>Returns the {@link ContentController} implementation of type {@link TC} assigned to handle the {@code JavaFX Graphics} for this {@link Content} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The abstract <i>{@link #controllerDefinition()}</i> method is used to define the {@link Class Class Type} of the {@link ContentController} assigned to this {@link Content} instance.</li>
     *     <li>
     *         Unlike <i>{@link #loadData()}</i>, <i>{@link #controllerDefinition()}</i> does not construct a new {@link ContentController} instance.
     *         <ul>
     *             <li>Rather, <i>{@link #controllerDefinition()}</i> returns the {@link Class Class Type} of the {@link ContentController}.</li>
     *             <li>The aforementioned {@link Class Class Type} is then passed to the {@link FxWeaver} for {@link FxWeaver#loadController(Class) Auto-Construction}.</li>
     *         </ul>
     *     </li>
     *     <li>Rather, <i>{@link #controllerDefinition()}</i> returns the {@link Class Type} of the {@link ContentController}, which is then processed and auto-constructed by {@link FxWeaver}.</li>
     * </ol>
     *
     * @return The {@link ContentController} implementation of type {@link TC} assigned to handle the {@code JavaFX Graphics} for this {@link Content} instance.
     */
    public final @NotNull TC getController() { return controller; }
    
    /**
     * <p>Returns the {@link Footer} implementation of type {@link F} assigned to this {@link Content} instance.</p>
     * <blockquote><i>See {@link Footer} for additional information.</i></blockquote>
     *
     * @return The {@link Footer} implementation of type {@link F} assigned to this {@link Content} instance.
     */
    public final F getFooter() { return footer; }
    
    /**
     * <p>Returns the {@link OverlayHandler} for this {@link Content} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link OverlayHandler} is used to add supplemental graphics to the {@link Content} instance.</li>
     *     <p><i>See {@link OverlayHandler} for details.</i></p>
     * </ol>
     *
     * @return The {@link OverlayHandler} for this {@link Content} instance.
     */
    //TO-EXPAND
    public final @NotNull OverlayHandler getOverlayHandler() { return overlayHandler; }
    
    /**
     * <p>Returns the {@link ReadOnlyListProperty} containing the {@link SidebarBookshelf Bookshelves} assigned to this {@link Content} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The returned {@link List} object is {@link ReadOnlyListProperty Read Only} and cannot be directly modified.</li>
     *     <li>To add a {@link SidebarBookshelf Bookshelf} to a {@link Content} instance, use the <i>{@link #injectBookshelf(String, UIBook...)}</i> method.</li>
     *     <li>
     *         Note that <i>{@link #injectBookshelf(String, UIBook...)}</i> is {@code protected} and designed to be called only from within a {@link Content} implementation.
     *         <ul>
     *             <li><i>See the {@link MandelbrotContent} {@link MandelbrotContent#MandelbrotContent(MandelbrotContentHandler) Constructor} for example usage.</i></li>
     *         </ul>
     *     </li>
     *     <li><i>Note that {@link Content} {@link #getBookshelves() Bookshelves} are of {@code Content Scope} and therefore only visible when this {@link Content} is {@link ContentManager#setContent(Content) Active}.</i></li>
     * </ol>
     *
     * @return The {@link ReadOnlyListProperty} containing the {@link SidebarBookshelf Bookshelves} assigned to this {@link Content} instance.
     */
    protected final @NotNull ReadOnlyListProperty<SidebarBookshelf> getBookshelves() { return bookshelves.getReadOnlyProperty(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CLASS BODY ---">
    
    /**
     * <p>Constructs a new {@link SidebarBookshelf} instance with the specified {@link String Name} and {@link UIBook UIBook Contents}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>Once constructed, the new {@link SidebarBookshelf} instance is added to this {@link Content Contents} {@link #getBookshelves() Bookshelves List}.</li>
     *     <li>Currently, a {@link Content} {@link SidebarBookshelf} cannot be removed once it has been {@link #injectBookshelf(String, UIBook...) injected}.</li>
     *     <li>Use abstract <i>{@link #onBookshelfAdded(SidebarBookshelf)}</i> and <i>{@link #onBookshelfRemoved(SidebarBookshelf)}</i> methods to define the {@link #getBookshelves() Bookshelves List} change response logic specific to this {@link Content Content} implementation.</li>
     * </ul>
     *
     * @param name  The {@link SidebarBookshelf#nameProperty() Name} of the new {@link SidebarBookshelf} instance.
     * @param books The {@link Array} of {@link UIBook UIBooks} the returned {@link SidebarBookshelf} instance is to {@link SidebarBookshelf#getBooks() Contain}.
     *
     * @return The auto-constructed {@link SidebarBookshelf} instance with the specified {@link String Name} and {@link UIBook UIBook Contents}.
     */
    protected SidebarBookshelf injectBookshelf(String name, UIBook... books) {
        if (A.isEmpty(Exc.nullCheck(books, "Book Array")))
            throw Exc.ex("Bookshelf Contents Cannot Be Empty");
        if (A.containsNull(books))
            throw Exc.ex("Bookshelf Contents Cannot Contain Null Elements: [" + java.util.Arrays.asList(books) + "]");
        
        final SidebarBookshelf bookshelf = new SidebarBookshelf(sidebar(), name, true);
        
        bookshelf.getBooks().addAll(books);
        bookshelf.getButtonGroup().selectFirst();
        
        onBookshelfAddedInternal(bookshelf);
        bookshelves.add(bookshelf);
        
        return bookshelf;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT (TO-DOC) ---">
    
    protected abstract @NotNull TD loadData();
    protected abstract @NotNull Class<TC> controllerDefinition();
    protected abstract F constructFooter();
    
    //
    
    protected boolean handleKeyEvent(@NotNull KeyEvent keyEvent, boolean fx) { return false; }
    
    protected boolean handleMousePressEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    protected boolean handleMouseReleaseEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    
    protected boolean handleMouseMoveEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    protected boolean handleMouseDragEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    
    protected boolean handleMouseEnterEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    protected boolean handleMouseExitEvent(@NotNull MouseEvent event, boolean fx) { return false; }
    
    //<editor-fold desc="--- EVENTS ---">
    
    /**
     * <p>Abstract method that is executed <i>after</i> this {@link Content} is {@link ContentManager#setContent(Content) set} as the active {@link Content}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #onActivate() This method} is {@link ThreadPoolExecutor#execute(Runnable) executed} by the {@link LogiCore} {@link ThreadPoolExecutor Executor}.</li>
     *     <li>
     *         The <i>{@link #onDeactivate()}</i> implementation of the <i>{@link ContentManager#getContent() previous content}</i> is executed <i>prior to</i> {@link #onActivate() this method}.
     *         <ul>
     *             <li>That said, because both operations are executed in a separate {@link ThreadPoolExecutor#execute(Runnable) execution}, the operations are very likely to execute at least partially concurrent.</li>
     *             <li>The only exception to this rule is if the {@link ThreadPoolExecutor Executor} is limited to a single background thread.</li>
     *             <li>However, even then, both operations will always be executed concurrently with remaining {@code JavaFX} operations taking place in the <code><i>{@link ContentManager#onChange(Content, Content)}</i></code> method.</li>
     *         </ul>
     *     </li>
     *     <li>{@link #onActivate() This method} is wrapped in the <i>{@link #onSetInternal()}</i> method.</li>
     * </ol>
     *
     * @see #onDeactivate()
     */
    //TO-EXPAND TO-UPDATE
    protected abstract void onActivate();
    
    /**
     * <p>Abstract method that is executed <i>after</i> this {@link Content} is {@link ContentManager#setContent(Content) set} as the active {@link Content}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #onActivate() This method} is {@link ThreadPoolExecutor#execute(Runnable) executed} by the {@link LogiCore} {@link ThreadPoolExecutor Executor}.</li>
     *     <li>The <i>{@link #onActivate()}</i> implementation of the <i>{@link ContentManager#getContent() new content}</i> is executed <i>after</i> {@link #onDeactivate() this method}.</li>
     *     <li>{@link #onDeactivate() This method} is wrapped in the <i>{@link #onRemovedInternal()}</i> method.</li>
     *     <p><i>See {@link #onActivate()}</i> for in-depth details.</p>
     * </ol>
     *
     * @see #onActivate()
     */
    //TO-EXPAND TO-UPDATE
    protected abstract void onDeactivate();
    
    //TO-DOC
    protected void onBookshelfRemoved(SidebarBookshelf bookshelf) { }
    
    //TO-DOC
    protected void onBookshelfAdded(SidebarBookshelf bookshelf) { }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return strictSpringable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return strictSpringable.ctx();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected final void onSetInternal() {
        getBookshelves().forEach(bookshelf -> sidebar().bookshelvesProperty().add(bookshelf));
        onActivate();
    }
    
    protected final void onRemovedInternal() {
        getBookshelves().forEach(bookshelf -> sidebar().bookshelvesProperty().remove(bookshelf));
        onDeactivate();
    }
    
    private void onBookshelfAddedInternal(@NotNull SidebarBookshelf bookshelf) {
        bookshelf.initialize();
        onBookshelfAdded(bookshelf);
    }
    
    private void onBookshelfRemovedInternal(SidebarBookshelf bookshelf) { onBookshelfRemoved(bookshelf); }
    
    //</editor-fold>
}
