package com.taco.tacository.ui;

import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository._to_sort.collections.ObservableLinkedList;
import com.taco.tacository.util.tools.Exc;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

// TODO - All Javadocs require rewriting/reformatting to match current conventions.

// TO-DOC
public class UIPageHandler
        implements Lockable {
    
    private final ReentrantLock lock;
    private final UIBook owner;
    
    private final ObjectProperty<UIPage<?>> coverPageProperty;
    private final ObservableLinkedList<UIPage<?>> pages;
    
    
    private final ObjectBinding<UIPage<?>> visiblePageBinding;
    private final BooleanBinding isEmptyBinding;
    
    /**
     * <p>Constructs a new {@link UIPageHandler} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Construction is done internally in the {@link UIBook} {@link UIBook#UIBook(FxWeaver, ConfigurableApplicationContext, String, String, Function, Runnable, StackPane) constructor}.</li>
     * </ol>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <b>Lock</b>
     *         <ol>
     *             <li>Used to synchronize all operations performed by this {@link UIPageHandler} instance.</li>
     *             <li>If {@code null}, no synchronization is performed.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Owner</b> — Refer to <code><i>{@link #getOwner()}</i></code> for additional information.
     *         <ol>
     *             <li>A backwards reference to the {@link UIBook} containing this {@link UIPageHandler} instance.</li>
     *             <li>In the {@link UIBook} {@link UIBook#UIBook(FxWeaver, ConfigurableApplicationContext, String, String, Function, Runnable, StackPane) constructor}, the {@link #getOwner() owner} is always set to {@code this}.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Cover Page</b> — Refer to <code><i>{@link #coverPageProperty()}</i></code> for additional information.
     *         <ol>
     *             <li>The {@link UIPage} that is {@link UIBook#getDisplayer() displayed} if no {@link UIPage pages} have been added to this {@link UIPageHandler}.</li>
     *             <li>A {@link UIPageHandler} is considered {@link #isEmptyBinding() empty} when its {@link #getPages() Page List} is {@link ObservableLinkedList#isEmpty() empty}.</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @param lock      The {@link ReentrantLock} used to synchronize functionality of this {@link UIPageHandler}.
     * @param owner     The {@link UIBook owner} of this {@link UIPageHandler}.
     * @param coverPage The {@link #getCoverPage() Cover Page} of this {@link UIPageHandler}.
     */
    UIPageHandler(ReentrantLock lock, UIBook owner, UIPage<?> coverPage) {
        this.lock = lock;
        this.owner = owner;
        
        this.coverPageProperty = new SimpleObjectProperty<>(coverPage);
        this.pages = new ObservableLinkedList<>();
        
        this.visiblePageBinding = Bindings.createObjectBinding(this::calcVisiblePage, pages);
        this.isEmptyBinding = Bindings.createBooleanBinding(() -> pages.isEmpty(), pages);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link UIBook} containing this {@link UIPage}.</p>
     *
     * @return The {@link UIBook} containing this {@link UIPage}.
     */
    public UIBook getOwner() {
        return owner;
    }
    
    /**
     * <p>Returns the {@link ObjectProperty property} containing the {@link UIPage Cover Page} assigned to this {@link UIPageHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link UIPage} that is {@link UIBook#getDisplayer() displayed} if no {@link UIPage pages} have been added to this {@link UIPageHandler}.</li>
     *     <li>A {@link UIPageHandler} is considered {@link #isEmptyBinding() empty} when its {@link #getPages() Page List} is {@link ObservableLinkedList#isEmpty() empty}.</li>
     *     <li>If this {@link UIPageHandler} does not have a {@link #coverPageProperty() Cover Page}, the returned {@link ObjectProperty property} will contain {@code null}.</li>
     * </ol>
     *
     * @return The {@link ObjectProperty property} containing the {@link UIPage Cover Page} assigned to this {@link UIPageHandler}.
     */
    public @NotNull ObjectProperty<UIPage<?>> coverPageProperty() {
        return coverPageProperty;
    }
    
    /**
     * <p>Returns the {@link UIPage Cover Page} assigned to this {@link UIPageHandler}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #coverPageProperty()}<b>.</b>{@link ObjectProperty#get() get()}</code></i></blockquote>
     *
     * @return The {@link UIPage Cover Page} assigned to this {@link UIPageHandler}.
     */
    public @Nullable UIPage<?> getCoverPage() {
        return coverPageProperty.get();
    }
    
    /**
     * <p>Sets the {@link UIPage Cover Page} assigned to this {@link UIPageHandler} to the specified value.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #coverPageProperty()}<b>.</b>{@link ObjectProperty#set(Object) set}<b>(</b>coverPage<b>)</b></code></i></blockquote>
     *
     * @param coverPage The {@link UIPage} to be the new {@link #coverPageProperty() Cover Page} assigned to this {@link UIPageHandler}.
     */
    public void setCoverPage(UIPage<?> coverPage) {
        coverPageProperty.set(coverPage);
    }
    
    /**
     * <p>Returns the {@link ObservableLinkedList Observable List} of {@link UIPage UIPages} managed and displayed by this {@link UIPageHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link ObservableLinkedList list} returned by {@link #getPages() this method} is functionally {@code read-only} and must <i>not</i> be modified by external sources. // TODO - Make Read-Only</li>
     * </ol>
     *
     * @return The {@link ObservableLinkedList Observable List} of {@link UIPage UIPages} managed and displayed by this {@link UIPageHandler}.
     */
    public ObservableLinkedList<UIPage<?>> getPages() {
        return pages;
    }
    
    /**
     * <p>Returns a convenience {@link ObjectBinding binding} containing the {@link UIPage} that is currently displayed by this {@link UIPageHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If available, the {@link ObservableLinkedList#getLast() last} {@link UIPage page} in this {@link UIPageHandler UIPageHandlers} {@link #getPages() Page List} is displayed.</li>
     *     <li>If this {@link UIPageHandler UIPageHandlers} {@link #getPages() Page List} is {@link #isEmpty() empty}, the {@link #getCoverPage() Cover Page} is displayed instead.</li>
     *     <li>If this {@link UIPageHandler UIPageHandlers} {@link ObservableLinkedList Page List} is {@link #isEmptyBinding() empty} <u>AND</u> this {@link UIPageHandler} has no {@link #getCoverPage() Cover Page}, then no {@link UIPage page} is displayed, and the {@link ObjectBinding} returned by {@link #visiblePageBinding() this method} will contain {@code null}.</li>
     *     <li>The logic for calculating the contents of the {@link BooleanBinding} returned by {@link #visiblePageBinding() this method} is contained within <code><i>{@link #calcVisiblePage()}</i></code>.</li>
     * </ol>
     *
     * @return A convenience {@link ObjectBinding binding} containing the {@link UIPage} that is currently displayed by this {@link UIPageHandler}.
     */
    public @NotNull ObjectBinding<UIPage<?>> visiblePageBinding() {
        return visiblePageBinding;
    }
    
    /**
     * <p>Returns the {@link UIPage} that is currently displayed by this {@link UIPageHandler}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #visiblePageBinding()}<b>.</b>{@link ObjectBinding#get() get()}</code></i></blockquote>
     *
     * @return The {@link UIPage} that is currently displayed by this {@link UIPageHandler}.
     */
    public @Nullable UIPage<?> getVisiblePage() {
        return visiblePageBinding.get();
    }
    
    /**
     * <p>A {@link BooleanBinding} that reflects whether this {@link UIPageHandler} is {@link ObservableLinkedList#isEmpty() empty} or not.</p>
     * <blockquote><b>Binding Passthrough Definition:</b> <i><code>{@link #getPages()}<b>.</b>{@link ObservableLinkedList#isEmpty() isEmpty()}</code></i></blockquote>
     *
     * @return A {@link BooleanBinding} that reflects whether this {@link UIPageHandler} is {@link ObservableLinkedList#isEmpty() empty} or not.
     */
    public BooleanBinding isEmptyBinding() {
        return isEmptyBinding;
    }
    
    /**
     * <p>Checks if this {@link UIPageHandler} is {@link ObservableLinkedList#isEmpty() empty} or not.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #isEmptyBinding()}<b>.</b>{@link BooleanBinding#get() get()}</code></i></blockquote>
     *
     * @return True if this {@link UIPageHandler} is {@link ObservableLinkedList#isEmpty() empty}, false if it is not.
     */
    public boolean isEmpty() {
        return isEmptyBinding.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PAGE ACTIONS ---">
    
    /**
     * <p>Checks if this {@link UIPageHandler} contains the specified {@link UIPage} or not.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>This method is synchronized.</li>
     * </ol>
     * <p><b>Return Definition</b></p>
     * <ol>
     *     <li>If the specified {@link UIPage} is {@code null}, return {@code false}.</li>
     *     <li>If this {@link UIPageHandler UIPageHandlers} {@link #getPages() Page List} contains the specified {@link UIPage}, return {@code true}.</li>
     *     <li>If the specified {@link UIPage} is this {@link UIPageHandler UIPageHandlers} {@link #coverPageProperty() Cover Page}, return {@code true}.</li>
     *     <li>Otherwise, return {@code false}.</li>
     * </ol>
     *
     * @param page The {@link UIPage} being checked.
     *
     * @return True if this {@link UIPageHandler} contains the specified {@link UIPage}, false if it does not.
     */
    public boolean containsPage(UIPage<?> page) {
        lock.lock();
        try {
            return page != null && getCoverPage() != page && !pages.contains(page);
        } finally {
            lock.unlock();
        }
    }
    
    
    /**
     * <p>Turns this {@link UIPageHandler} to the specified {@link UIPage}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@link UIPage} is {@code null}, this method does nothing and returns {@code false} silently.</li>
     *     <li>If the specified {@link UIPage} is in the {@link #getPages() Page List}, all {@link UIPage pages} <i>after</i> the specified {@link UIPage} are {@link ObservableLinkedList#remove(Object) removed}.</li>
     *     <li>If the specified {@link UIPage} is the {@link #getCoverPage() Cover Page}, the {@link #getPages() Page List} is {@link ObservableLinkedList#clear() cleared}.</li>
     *     <li>Otherwise, the specified {@link UIPage} is instead passed to <code><i>{@link #turnToNew(UIPage)}</i></code>.</li>
     * </ol>
     *
     * @param page The {@link UIPage} being turned to.
     *
     * @return True if the specified {@link UIPage} was successfully turned to, false if it was not.
     */
    // TO-TEST
    public boolean turnTo(@Nullable UIPage<?> page) {
        lock.lock();
        try {
            if (page != null) {
                if (getPages().contains(page)) {
                    while (!getVisiblePage().equals(page))
                        back();
                    if (getVisiblePage().equals(page))
                        return true;
                    else
                        throw Exc.ex("Visible page should equal the specified page given prior loop, but it does not. This should not be possible; extensive debugging is necessary.");
                }
            }
        } finally {
            lock.unlock();
        }
        return false;
    }
    
    /**
     * <p>Adds the specified {@link UIPage} to this {@link UIPageHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@link UIPage} is {@code null}, a {@link NullPointerException} is thrown.</li>
     *     <li>If the specified {@link UIPage} is the {@link #getCoverPage() Cover Page}, an {@link UnsupportedOperationException} is thrown.</li>
     *     <li>If the specified {@link UIPage} has already been added to the {@link #getPages() Page List}, an {@link UnsupportedOperationException} is thrown.</li>
     *     <li>Otherwise, the specified {@link UIPage} is {@link ObservableLinkedList#addLast(Object) added} to the {@link #getPages() Page List}.</li>
     * </ol>
     *
     * @param page The {@link UIPage} being turned to.
     */
    // TO-UPDATE
    public void turnToNew(@NotNull UIPage<?> page) {
        lock.lock();
        try {
            Exc.nullCheck(page, "Page cannot be null");
            if (Objects.equals(page, getCoverPage()))
                throw Exc.unsupported("Cannot add the cover page to the page list.");
            else if (pages.contains(page))
                throw Exc.unsupported("Page has already been added to this PageHandler.");
            pages.addLast(page);
        } finally {
            lock.unlock();
        }
    }
    
    
    /**
     * <p>{@link #turnTo(UIPage) Turns} this {@link UIPageHandler} back one {@link UIPage page}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getPages()}<b>.</b>{@link ObservableLinkedList#pollLast() pollLast()}</code></i></blockquote>
     *
     * @return The {@link UIPage} that was previously displayed before this {@link UIPageHandler} was {@link #turnTo(UIPage) turned} {@link #back() back}.
     *
     * @see #backUnchecked()
     */
    public UIPage<?> back() { return FX.callFX(() -> getPages().pollLast()); }
    
    /**
     * <p>Identical to <code><i>{@link #back()}</i></code>, except an {@link RuntimeException exception} is thrown if the {@link #getPages() Page List} is {@link #isEmpty() empty}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #getPages()}<b>.</b>{@link ObservableLinkedList#removeLast() removeLast()}</code></i></blockquote>
     *
     * @return The {@link UIPage} that was previously displayed before this {@link UIPageHandler} was {@link #turnTo(UIPage) turned} {@link #backUnchecked() back}.
     *
     * @see #back()
     */
    public UIPage<?> backUnchecked() { return pages.removeLast(); }
    
    // TO-DOC
    private UIPage<?> calcVisiblePage() {
        UIPage<?> pagedPage;
        if ((pagedPage = getPages().peekLast()) != null)
            return pagedPage;
        return getCoverPage();
    }
    
    //</editor-fold>
}