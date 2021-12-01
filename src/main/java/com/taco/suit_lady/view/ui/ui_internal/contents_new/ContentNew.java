package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class ContentNew
        implements Springable
{
    private final Springable strictSpringable;
    
    private final ReadOnlyListWrapper<SidebarBookshelf> bookshelves;
    
    public ContentNew(@NotNull Springable springable)
    {
        ExceptionTools.nullCheck(springable, "Springable Parent");
        this.strictSpringable = springable.asStrict();
        
        this.bookshelves = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    public @NotNull ReadOnlyListProperty<SidebarBookshelf> bookshelves()
    {
        return bookshelves.getReadOnlyProperty();
    }
    
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
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract Region getRoot();
    
    /**
     * <p>Abstract method that is executed <i>after</i> this {@link ContentNew} is {@link ContentManagerNew#setContent(ContentNew) set} as the active {@link ContentNew contnet}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link #onSet() This method} is {@link ThreadPoolExecutor#execute(Runnable) executed} by the {@link TB Toolbox} {@link ThreadPoolExecutor Executor}.</li>
     *     <li>
     *         The <code><i>{@link #onRemoved()}</i></code> implementation of the <i>{@link ContentManagerNew#getContent() previous content}</i> is executed <i>prior to</i> {@link #onSet() this method}.
     *         <ul>
     *             <li>That said, because both operations are executed in a separate {@link ThreadPoolExecutor#execute(Runnable) execution}, the operations are very likely to occur concurrently.</li>
     *             <li>The only exception to this rule is if the {@link ThreadPoolExecutor Executor} is limited to a single background thread.</li>
     *             <li>However, even then, both operations will always be executed concurrently with remaining {@code JavaFX} operations taking place in the <code><i>{@link ContentManagerNew#onChange(ContentNew, ContentNew)}</i></code> method.</li>
     *         </ul>
     *     </li>
     *     <li>{@link #onSet() This method} is wrapped in the <code><i>{@link #onSetInternal()}</i></code> method.</li>
     * </ol>
     */
    // TO-UPDATE
    protected abstract void onSet();
    
    // TO-UPDATE
    protected abstract void onRemoved();
    
    //</editor-fold>
    
    protected final void onSetInternal()
    {
        final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
        bookshelves().forEach(bookshelf -> sidebar.bookshelvesProperty().add(bookshelf));
        
        onSet();
    }
    
    protected final void onRemovedInternal()
    {
        final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
        bookshelves().forEach(bookshelf -> sidebar.bookshelvesProperty().remove(bookshelf));
        
        onRemoved();
    }
}
