package com.taco.suit_lady.view.ui.ui_internal.contents_sl;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class SLContent<D extends SLContentData, C extends SLContentController>
        implements Springable
{
    private final Springable strictSpringable;
    
    private final D data;
    private final C controller;
    
    private final ReadOnlyListWrapper<SidebarBookshelf> bookshelves;
    
    public SLContent(@NotNull Springable springable)
    {
        this.strictSpringable = ExceptionTools.nullCheck(springable, "Springable Parent").asStrict();
        
        this.data = ExceptionTools.nullCheck(loadData(), "SLContentData");
        this.controller = ExceptionTools.nullCheckMessage(
                weaver().loadController(ExceptionTools.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file."
        );
        
        this.bookshelves = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final @NotNull D getData()
    {
        return data;
    }
    
    protected final @NotNull C getController()
    {
        return controller;
    }
    
    protected final @NotNull ReadOnlyListProperty<SidebarBookshelf> getBookshelves()
    {
        return bookshelves.getReadOnlyProperty();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract @NotNull D loadData();
    
    protected abstract @NotNull Class<C> controllerDefinition();
    
    //<editor-fold desc="--- EVENTS ---">
    
    /**
     * <p>Abstract method that is executed <i>after</i> this {@link SLContent} is {@link SLContentManager#setContent(SLContent) set} as the active {@link SLContent contnet}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #onActivate() This method} is {@link ThreadPoolExecutor#execute(Runnable) executed} by the {@link TB Toolbox} {@link ThreadPoolExecutor Executor}.</li>
     *     <li>
     *         The <code><i>{@link #onDeactivate()}</i></code> implementation of the <i>{@link SLContentManager#getContent() previous content}</i> is executed <i>prior to</i> {@link #onActivate() this method}.
     *         <ul>
     *             <li>That said, because both operations are executed in a separate {@link ThreadPoolExecutor#execute(Runnable) execution}, the operations are very likely to occur concurrently.</li>
     *             <li>The only exception to this rule is if the {@link ThreadPoolExecutor Executor} is limited to a single background thread.</li>
     *             <li>However, even then, both operations will always be executed concurrently with remaining {@code JavaFX} operations taking place in the <code><i>{@link SLContentManager#onChange(SLContent, SLContent)}</i></code> method.</li>
     *         </ul>
     *     </li>
     *     <li>{@link #onActivate() This method} is wrapped in the <code><i>{@link #onSetInternal()}</i></code> method.</li>
     * </ol>
     */
    // TO-UPDATE
    protected abstract void onActivate();
    
    protected abstract void onDeactivate();
    
    protected abstract void onShutdown();
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return strictSpringable.weaver();
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return strictSpringable.ctx();
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
    
    //</editor-fold>
}
