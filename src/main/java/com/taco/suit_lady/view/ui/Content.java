package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.painting.Overlay;
import com.taco.suit_lady.view.ui.painting.OverlayHandler;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Content<D extends ContentData, C extends ContentController>
        implements Springable
{
    private final Springable strictSpringable;
    
    private final D data;
    private final C controller;
    
    private final OverlayHandler overlayHandler;
    
    private final ReadOnlyListWrapper<SidebarBookshelf> bookshelves;
    
    public Content(@NotNull Springable springable)
    {
        this.strictSpringable = ExceptionTools.nullCheck(springable, "Springable Parent").asStrict();
        
        this.data = ExceptionTools.nullCheck(loadData(), "SLContentData");
        this.controller = ExceptionTools.nullCheckMessage(
                weaver().loadController(ExceptionTools.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file."
        );
        
        this.overlayHandler = new OverlayHandler(this, null);
        this.overlayHandler.addOverlay(new Overlay(this, null, "default", 1));
        
        this.bookshelves = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
//        ArrayTools.applyChangeHandler(
//                bookshelves,
//                bookshelf -> onBookshelfAddedInternal(bookshelf),
//                bookshelf -> onBookshelfRemovedInternal(bookshelf)
//        );
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull D getData()
    {
        return data;
    }
    
    public final @NotNull C getController()
    {
        return controller;
    }
    
    public final @NotNull OverlayHandler getOverlayHandler() {
        return overlayHandler;
    }
    
    protected final @NotNull ReadOnlyListProperty<SidebarBookshelf> getBookshelves()
    {
        return bookshelves.getReadOnlyProperty();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CLASS BODY ---">
    
    protected SidebarBookshelf injectBookshelf(String name, UIBook... books)
    {
        if (ArrayTools.isEmpty(ExceptionTools.nullCheck(books, "Book Array"))) throw ExceptionTools.ex("Bookshelf Contents Cannot Be Empty");
        if (ArrayTools.containsNull(books)) throw ExceptionTools.ex("Bookshelf Contents Cannot Contain Null Elements: [" + Arrays.asList(books) + "]");
        
        final SidebarBookshelf bookshelf = new SidebarBookshelf(getSidebar(), name);
        
        bookshelf.getBooks().addAll(books);
        bookshelf.getButtonGroup().selectFirst();
        
        onBookshelfAddedInternal(bookshelf);
        bookshelves.add(bookshelf);
        
        return bookshelf;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract @NotNull D loadData();
    
    protected abstract @NotNull Class<C> controllerDefinition();
    
    //<editor-fold desc="--- EVENTS ---">
    
    /**
     * <p>Abstract method that is executed <i>after</i> this {@link Content} is {@link ContentManager#setContent(Content) set} as the active {@link Content contnet}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #onActivate() This method} is {@link ThreadPoolExecutor#execute(Runnable) executed} by the {@link TB Toolbox} {@link ThreadPoolExecutor Executor}.</li>
     *     <li>
     *         The <code><i>{@link #onDeactivate()}</i></code> implementation of the <i>{@link ContentManager#getContent() previous content}</i> is executed <i>prior to</i> {@link #onActivate() this method}.
     *         <ul>
     *             <li>That said, because both operations are executed in a separate {@link ThreadPoolExecutor#execute(Runnable) execution}, the operations are very likely to occur concurrently.</li>
     *             <li>The only exception to this rule is if the {@link ThreadPoolExecutor Executor} is limited to a single background thread.</li>
     *             <li>However, even then, both operations will always be executed concurrently with remaining {@code JavaFX} operations taking place in the <code><i>{@link ContentManager#onChange(Content, Content)}</i></code> method.</li>
     *         </ul>
     *     </li>
     *     <li>{@link #onActivate() This method} is wrapped in the <code><i>{@link #onSetInternal()}</i></code> method.</li>
     * </ol>
     */
    // TO-UPDATE
    protected abstract void onActivate();
    
    protected abstract void onDeactivate();
    
    protected void onBookshelfRemoved(SidebarBookshelf bookshelf) { }
    
    protected void onBookshelfAdded(SidebarBookshelf bookshelf) { }
    
    protected abstract void onShutdown();
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return strictSpringable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return strictSpringable.ctx();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PASSTHROUGH METHODS ---">
    
    protected final Sidebar getSidebar()
    {
        return ctx().getBean(AppUI.class).getSidebar();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected final void onSetInternal()
    {
        final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
        getBookshelves().forEach(bookshelf -> sidebar.bookshelvesProperty().add(bookshelf));
        
        onActivate();
    }
    
    protected final void onRemovedInternal()
    {
        final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
        getBookshelves().forEach(bookshelf -> sidebar.bookshelvesProperty().remove(bookshelf));
        
        onDeactivate();
    }
    
    private void onBookshelfAddedInternal(SidebarBookshelf bookshelf)
    {
        bookshelf.initialize();
        onBookshelfAdded(bookshelf);
    }
    
    private void onBookshelfRemovedInternal(SidebarBookshelf bookshelf)
    {
        onBookshelfRemoved(bookshelf);
    }
    
    //</editor-fold>
}
