package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.collections.ObservableLinkedList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

// TODO - All Javadocs require rewriting/reformatting to match current conventions.

/**
 * Contains and handles all {@link UIPage pages} for a {@link UINode}.
 */
public class UIPageHandler
{
    
    private final ReentrantLock lock;
    private final UINode owner;
    
    private final ObjectProperty<UIPage<?>> coverPageProperty;
    private final ObservableLinkedList<UIPage<?>> pages;
    
    private final ObjectBinding<UIPage<?>> visiblePageBinding;
    private final BooleanBinding hasPagedContentBinding;
    
    /**
     * Creates a new {@code UIPageHandler} with the specified {@code owner} and {@link #coverPageProperty() cover page}, using the specified {@link ReentrantLock lock} for synchronization.
     *
     * @param lock      The {@code ReentrantLock} to be used for synchronization. Specify null for no synchronization.
     *                  <br>Typically the same lock used by the {@code owner}.
     * @param owner     The {@code UINode} in which this {@code UIPageHandler} belongs to.
     * @param coverPage The {@code UIPage} to be displayed as the cover page.
     *                  See {@link #coverPageProperty()} for details.
     */
    public UIPageHandler(ReentrantLock lock, UINode owner, UIPage<?> coverPage)
    {
        this.lock = lock;
        this.owner = owner;
        
        this.coverPageProperty = new SimpleObjectProperty<>(coverPage);
        this.pages = new ObservableLinkedList<>();
        
        this.visiblePageBinding = Bindings.createObjectBinding(this::calcVisiblePage, pages);
        this.hasPagedContentBinding = Bindings.createBooleanBinding(() -> !pages.isEmpty(), pages);
    }
    
    //<editor-fold desc="Properties">
    
    /**
     * Returns the owner of this {@code UIPageHandler}.
     *
     * @return The owner of this {@code UIPageHandler}.
     */
    public UINode getOwner()
    {
        return owner;
    }
    
    //
    
    /**
     * Returns the cover page property for this {@code UIPageHandler}.
     * <p>
     * The cover page is displayed when no additional pages have been added to this {@code UIPageHandler} (specifically, when the page list is empty).
     *
     * @return The cover page property for this {@code UIPageHandler}.
     *
     * @see #getCoverPage()
     * @see #setCoverPage(UIPage)
     */
    public ObjectProperty<UIPage<?>> coverPageProperty()
    {
        return coverPageProperty;
    }
    
    /**
     * Returns the cover page of this {@code UIPageHandler}.
     *
     * @return The cover page of this {@code UIPageHandler}.
     *
     * @see #coverPageProperty()
     */
    public UIPage<?> getCoverPage()
    {
        return coverPageProperty.get();
    }
    
    /**
     * Sets the cover page of this {@code UIPageHandler} to the specified value.
     *
     * @param coverPage The {@code UIPage} to be set as the cover page.
     *
     * @see #coverPageProperty()
     */
    public void setCoverPage(UIPage<?> coverPage)
    {
        coverPageProperty.set(coverPage);
    }
    
    //
    
    /**
     * Returns the list of pages that represent this {@code UIPageHandler}. Does not include the {@link #coverPageProperty() cover page}.
     * <p>
     * The last element of the page list is the page to be displayed by this {@code UIPageHandler}.
     * If the page list is empty, the cover page is displayed instead.
     *
     * @return All pages that are part of this {@code UIPageHandler}, excluding the cover page.
     */
    public ObservableLinkedList<UIPage<?>> getPages()
    {
        return pages;
    } //TODO - Make read-only
    
    //
    
    /**
     * Returns an {@code ObjectBinding} bound to the currently displayed page of this {@code UIPageHandler}.
     * <p>
     * The visible page is determined by the following:
     * <ol>
     * <li>If the {@link #getPages() page list} is not empty, the last page in the page list is returned.</li>
     * <li>If the page list is empty, the {@link #coverPageProperty() cover page} is returned.</li>
     * </ol>
     *
     * @return An {@code ObjectBinding} bound to the currently displayed {@code UIPage} of this {@code UIPageHandler}.
     *
     * @see #getVisiblePage()
     */
    public ObjectBinding<UIPage<?>> visiblePageBinding()
    {
        return visiblePageBinding;
    }
    
    /**
     * Returns the page currently being displayed by this UIPageHandler.
     * <p>
     * The visible page is determined by the following:
     * <ol>
     * <li>If the {@link #getPages() page list} is not empty, the last page in the page list is returned.</li>
     * <li>If the page list is empty, the {@link #coverPageProperty() cover page} is returned.</li>
     * </ol>
     *
     * @return The {@code UIPage} currently being displayed by this {@code UIPageHandler}.
     *
     * @see #visiblePageBinding()
     */
    public UIPage<?> getVisiblePage()
    {
        return visiblePageBinding.get();
    }
    
    //
    
    /**
     * Returns a {@code BooleanBinding} that checks if this {@code UIPageHandler} has paged content.
     * <p>
     * {@code Paged content} is defined as a page that is part of this {@code UIPageHandler}, but is not currently being displayed.
     * <br>
     * The value of {@code pageData.hasPagedContentBinding().get()} will always be equal to {@code getPages().isEmpty()}.
     *
     * @return A {@code BooleanBinding} that checks if this {@code UIPageHandler} has paged content.
     *
     * @see #hasPagedContent()
     */
    public BooleanBinding hasPagedContentBinding()
    {
        return hasPagedContentBinding;
    }
    
    /**
     * Returns true if this {@code UIPageHandler} has paged content, false if it does not.
     *
     * @return True if this {@code UIPageHandler} has paged content, false if it does not.
     *
     * @see #hasPagedContentBinding()
     */
    public boolean hasPagedContent()
    {
        return hasPagedContentBinding.get();
    }
    
    //</editor-fold>
    
    //
    
    /**
     * Checks if this {@code UIPageHandler} contains the specified page.
     * <p>
     * A page is defined as being contained within a {@code UIPageHandler} if any of the following conditions are true:
     * <ol>
     * <li>The page is contained within the {@link #getPages() page list}.</li>
     * <li>The page is the {@link #coverPageProperty() cover page}.
     * </ol>
     * <p>
     * <i>Synchronized.</i>
     *
     * @param page The {@code UIPage} being searched for.
     *
     * @return True if this {@code UIPageHandler} contains the specified page, false otherwise.
     *
     * @see #coverPageProperty()
     * @see #getPages()
     */
    public boolean containsPage(UIPage<?> page)
    {
        lock.lock();
        try {
            return page != null && getCoverPage() != page && !pages.contains(page);
        } finally {
            lock.unlock();
        }
    }
    
    //
    
    /**
     * Turns this {@code UIPageHandler} to the specified page.
     * <ol>
     * <li>All pages that are after the specified page are removed from the {@link #getPages() page list}.</li>
     * <li>If the specified page is the {@link #coverPageProperty() cover page}, the page list is cleared.</li>
     * <li>If this {@code UIPageHandler} does not contain the specified  page, {@link #turnToNew(UIPage)} is called.</li>
     * </ol>
     * <p>
     * <i>Synchronized.</i>
     *
     * @param page The {@code UIPage} being made the next page in the page list (that is, the page being turned to).
     *
     * @throws NullPointerException If the specified page is null.
     * @see #turnToNew(UIPage)
     */
    public void turnTo(UIPage<?> page)
    {
        throw ExceptionTools.nyi();
    } //TODO
    
    /**
     * Adds the specified page to this {@code UIPageHandler}, and then turns to that {@code UIPageHandler}.
     * <p>
     * Unlike {@link #turnTo(UIPage) turnTo}, this method throws an {@code UnsupportedOperationException} if this {@code UIPageHandler} already contains the specified page.
     * <p>
     * <i>Synchronized.</i>
     *
     * @param page The {@code UIPage} being turned to.
     *
     * @throws NullPointerException          If the specified page is null.
     * @throws UnsupportedOperationException If this {@code UIPageHandler} already contains the specified page (see {@link #containsPage(UIPage) containsPage}).
     * @see #turnTo(UIPage)
     */
    public void turnToNew(UIPage<?> page)
    {
        lock.lock();
        try {
            ExceptionTools.nullCheck(page, "Page cannot be null");
            if (Objects.equals(page, getCoverPage()))
                throw ExceptionTools.unsupported("Cannot add the cover page to the page list.");
            else if (pages.contains(page))
                throw ExceptionTools.unsupported("Page has already been added to this PageHandler.");
            //			ConsoleBB.CONSOLE.dev("Setting paged item: " + page);
            pages.addLast(page);
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Turns this {@code UIPageHandler} back one page.
     * <p>
     * Equivalent to {@code getPages().pollLast()}.
     * <p>
     * <i>Synchronized.</i>
     *
     * @see #turnTo(UIPage)
     * @see #turnToNew(UIPage)
     * @see #backUnchecked()
     */
    public void back()
    {
        lock.lock();
        try {
            pages.pollLast();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Turns this {@code UIPageHandler} back one page.
     * <p>
     * Equivalent to {@code getPages().getLast()}.
     * This method differs from {@link #back()} only in that it throws an exception if the page list is empty.
     * <p>
     * <i>Synchronized.</i>
     *
     * @see #turnTo(UIPage)
     * @see #turnToNew(UIPage)
     * @see #back()
     */
    public void backUnchecked()
    {
        lock.lock();
        try {
            pages.removeLast();
        } finally {
            lock.unlock();
        }
    }
    
    //
    
    /**
     * Calculates the currently visible page.
     * <p>
     * The visible page is determined by the following:
     * <ol>
     * <li>If the {@link #getPages() page list} is not empty, the last page in the page list is returned.</li>
     * <li>If the page list is empty, the {@link #coverPageProperty() cover page} is returned.</li>
     * </ol>
     * <p>
     * <i>Synchronized.</i>
     *
     * @return The currently visible {@code UIPage}.
     */
    private UIPage<?> calcVisiblePage()
    {
        lock.lock();
        try {
            UIPage<?> pagedPage;
            if ((pagedPage = getPages().peekLast()) != null)
                return pagedPage;
            return getCoverPage();
        } finally {
            lock.unlock();
        }
    }
}