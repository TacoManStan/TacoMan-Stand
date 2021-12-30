package com.taco.suit_lady.ui.pages.impl.content_selector;

import com.taco.suit_lady._to_sort._new.Debugger;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ContentHandler<
        D extends ContentData,
        P extends ContentSelectorPage<D, P, SC, EC, H, T>,
        SC extends ContentSelectorPageController<D, P, SC, EC, H, T>,
        EC extends ContentElementController<D, P, SC, EC, H, T>,
        H extends ContentHandler<D, P, SC, EC, H, T>,
        T extends ListableContent<D, ?, P, SC, EC, H, T>>
        implements Springable, Lockable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private P contentSelectorPage;
    
    private final ReadOnlyListWrapper<T> contentList;
    private final ObjectProperty<T> selectedContentProperty;
    
    public ContentHandler(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        this.lock = new ReentrantLock();
        
        this.contentList = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        this.selectedContentProperty = new SimpleObjectProperty<>();
        
        
        this.selectedContentProperty.addListener((observable, oldValue, newValue) -> ui().getContentManager().setContent(newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final P getContentSelectorPage() {
        return contentSelectorPage;
    }
    
    public final void setContentSelectorPage(P contentSelectorPage) {
        this.contentSelectorPage = contentSelectorPage;
    }
    
    
    public final ReadOnlyListProperty<T> contentList() {
        return contentList.getReadOnlyProperty();
    }
    
    public final ObjectProperty<T> selectedContentProperty() {
        return selectedContentProperty;
    }
    
    public final T getSelectedContent() {
        return selectedContentProperty.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public final @NotNull ReentrantLock getLock() {
        return lock;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract @NotNull T newInstance();
    
    protected abstract void onShutdown(T t);
    
    //</editor-fold>
    
    public final @NotNull T addInstance() {
        T content = newInstance();
        sync(() -> contentList.add(content));
        return content;
    }
    
    public void shutdown() {
        // TODO: Move to separate, synchronized task threads for each DummyClient instance.
        logiCore().execute(() -> {
            TaskTools.sync(lock, () -> contentList.forEach(client -> onShutdown(client)));
            if (!contentList.isEmpty())
                throw ExceptionTools.ex("Mandelbrot Content List should be empty! (" + contentList + ")");
        });
    }
    
    /**
     * <p>Performs the following actions:</p>
     * <ol>
     *     <li>Removes the specified {@link T content} from this {@link ContentHandler}.</li>
     *     <li>Calls the <i>{@link ListableContent#onShutdown()}</i> method of the specified {@link T content}.</li>
     *     <li>Calls <i>{@link #onShutdown(T)}</i> on the specified {@link T content}.</li>
     * </ol>
     * <p><b>Shutdown Implementation</b></p>
     * <ul>
     *     <li>
     *         <i>{@link ListableContent#onShutdown()}</i> should perform all instance-specific shutdown operations.
     *         After the method has been called, the {@link ListableContent} instance should be ready for garbage collection.
     *     </li>
     *     <li><i>{@link ContentHandler#onShutdown(T)}</i> should perform all shutdown operations that are always relevant to every {@link ListableContent} instance located in this {@link ContentHandler}.</li>
     *     <li>
     *         In most cases, the {@link ListableContent} should be able to handle all shutdown operations.
     *         <i>{@link ContentHandler#onShutdown(T)}</i> is most useful for performing any necessary reset/refresh operations on the entire {@link ContentHandler} when such additional operations are necessary for proper functionality post element removal.
     *     </li>
     * </ul>
     *
     * @param content The {@link ListableContent} to be shutdown.
     */
    // TO-EXPAND
    public final void shutdown(T content) {
        debugger().print("Shutting Down.");
        sync(() -> {
            contentList.remove(content);
            content.onShutdown();
            onShutdown(content);
        });
    }
}
