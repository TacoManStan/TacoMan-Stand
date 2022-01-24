package com.taco.suit_lady.ui.jfx.lists;

import com.taco.suit_lady.util.tools.SLBindings;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.ui.jfx.lists.treehandler.IndexedCellFXable;
import com.taco.suit_lady.ui.jfx.lists.treehandler.TreeItemFX;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.Cell;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * <p>Implementation of {@link TreeCell} that {@link CellControlManager manages} the display of its {@link #getItemFX() contents} using a {@link CellController}.</p>
 *
 * @param <T> The type of object contained within this {@link TreeCellFX}.
 * @param <C> The type of {@link CellController} used to display the {@link #getItemFX() contents} of this {@link TreeCellFX} object.
 */
// TO-EXPAND
public class TreeCellFX<T, C extends CellController<T>> extends TreeCell<T>
        implements IndexedCellFXable<T, C> {
    
    private final Lock lock;
    
    private final CellControlManager<T, C> cellControlManager;
    private final ObjectBinding<TreeItemFX<T>> treeItemFXBinding;
    
    private final BooleanBinding contentVisibleBinding;
    
    /**
     * <p>Constructs a new {@link TreeCellFX} instance managed by the {@link CellControlManager} constructed by the specified {@link Function cellControlManagerFactory} object.</p>
     *
     * @param cellControlManagerFactory The {@link Function} called to construct a new {@link CellControlManager} to display and manage the new the {@link #getItemFX() content} of this {@link TreeCellFX} whenever the {@link #getItemFX() content} is {@link #updateItem(Object, boolean) changed}.
     *
     * @throws NullPointerException If the specified {@link Function cellControlManagerFactory} is {@code null}.
     */
    public TreeCellFX(@NotNull Function<TreeCellFX<T, C>, CellControlManager<T, C>> cellControlManagerFactory) {
        SLExceptions.nullCheck(cellControlManagerFactory, "Cell Control Manager Factory Function");
        
        this.cellControlManager = cellControlManagerFactory.apply(this);
        this.lock = this.cellControlManager.getLock();
        
        this.treeItemFXBinding = Bindings.createObjectBinding(() -> {
            final TreeItem<T> treeItem = getTreeItem();
            if (treeItem != null)
                if (treeItem instanceof TreeItemFX)
                    return (TreeItemFX<T>) treeItem;
                else
                    throw SLExceptions.ex(new ClassCastException(), "TreeCellFX objects must only contain TreeItemFX items.`");
            return null;
        }, treeItemProperty());
        
        final SLBindings.RecursiveBinding<TreeItemFX<T>, Boolean> recursiveVisibleBinding = SLBindings.bindRecursive(
                treeItemFX -> treeItemFX != null ? treeItemFX.visibleProperty() : SLBindings.bindBoolean(false), treeItemFXBinding());
        this.contentVisibleBinding = Bindings.createBooleanBinding(() -> recursiveVisibleBinding.getValue(), recursiveVisibleBinding);
        this.contentVisibleBinding.addListener(observable -> setDisable(!recursiveVisibleBinding.getValue()));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link Lock} object used to provide synchronization to this {@link TreeCellFX} object.</p>
     *
     * @return The {@link Lock} object used to provide synchronization to this {@link TreeCellFX} object.
     */
    protected Lock getLock() {
        return lock;
    }
    
    /**
     * <p>Returns an {@link ObjectBinding} that reflects the parent {@link #itemProperty() Item Property} cast to {@link TreeItemFX}.</p>
     * <blockquote><b>Binding Passthrough Definition:</b> <i><code><b>(</b>{@link TreeItemFX}<b>)</b> {@link #getItem()}</code></i></blockquote>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Convenience binding that eliminates the need to cast the {@link TreeItem} {@link #getItem() content} of this {@link TreeCellFX} by storing its value as a pre-casted {@link TreeItemFX}.</li>
     *     <li>If the {@link #getItem() content} of this {@link TreeCellFX} is not an instance of {@link TreeItemFX}, a {@link ClassCastException} is thrown.</li>
     * </ol>
     *
     * @return An {@link ObjectBinding} that reflects the parent {@link #itemProperty() Item Property} cast to {@link TreeItemFX}.
     */
    public ObjectBinding<TreeItemFX<T>> treeItemFXBinding() {
        return treeItemFXBinding;
    }
    
    /**
     * <p>Returns the {@link #getItem() content} of this {@link TreeCellFX} pre-cast to a {@link TreeItemFX}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #treeItemFXBinding()}<b>.</b>{@link ObjectBinding#get() get()}</code></i></blockquote>
     *
     * @return The {@link #getItem() content} of this {@link TreeCellFX} pre-cast to a {@link TreeItemFX}.
     */
    public TreeItemFX<T> getItemFX() {
        return treeItemFXBinding.get();
    }
    
    /**
     * <p>A {@link BooleanBinding} that reflects whether the current {@link #getItemFX() content} of this {@link TreeCellFX} is currently {@link TreeItemFX#visibleProperty() visible} or not.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the {@link #getItemFX() content} of this {@link TreeCellFX} is {@code null}, the {@link BooleanBinding} returned by {@link #contentVisibleBinding() this method} will reflect {@code false}.</li>
     *     <li>The {@link BooleanBinding} returned by {@link #contentVisibleBinding() this method} is bound to a {@link SLBindings.RecursiveBinding RecursiveBinding}, therefore, the {@link BooleanBinding binding} will reflect accurate results, even when the {@link #getItemFX() content} of this {@link TreeCellFX} changes or is {@code null}.</li>
     *     <li>The {@link BooleanBinding} returned by {@link #contentVisibleBinding() this method} is defined in the {@link TreeCellFX} {@link TreeCellFX#TreeCellFX(Function) constructor}.</li>
     * </ol>
     *
     * @return A {@link BooleanBinding} that reflects whether the current {@link #getItemFX() content} of this {@link TreeCellFX} is currently {@link TreeItemFX#visibleProperty() visible} or not.
     */
    public BooleanBinding contentVisibleBinding() {
        return contentVisibleBinding;
    }
    
    /**
     * <p>Returns whether the current {@link #getItemFX() content} of this {@link TreeCellFX} is currently {@link TreeItemFX#visibleProperty() visible} or not.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>{@link #contentVisibleBinding()}<b>.</b>{@link }</code></i></blockquote>
     *
     * @return Whether the current {@link #getItemFX() content} of this {@link TreeCellFX} is currently {@link TreeItemFX#visibleProperty() visible} or not.
     */
    public boolean isContentVisible() {
        return contentVisibleBinding.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /**
     * <p>Returns the {@link CellControlManager} used by this {@link TreeCellFX} to manage the {@link CellController} used to display and manage the {@link #getItemFX() content} of this {@link TreeCellFX}.</p>
     *
     * @return The {@link CellControlManager} used by this {@link TreeCellFX} to manage the {@link CellController} used to display and manage the {@link #getItemFX() content} of this {@link TreeCellFX}.
     */
    @Override
    public CellControlManager<T, C> getCellControlManager() {
        return cellControlManager;
    }
    
    /**
     * <p>Overridden implementation of <code><i>{@link Cell#updateItem(Object, boolean)}</i></code>.</p>
     * <p><b>Updation Process</b></p>
     * <ol>
     *     <li>The {@link TreeCell parent} implementation of <code><i>{@link Cell#updateItem(Object, boolean)}</i></code> is executed.</li>
     *     <li>
     *         Passes specified <i>{@link T item}</i> and <i>{@link Boolean empty}</i> parameters to...
     *         <blockquote><code><i>{@link #getCellControlManager()}<b>.</b>{@link CellControlManager#doUpdateItem(Object, boolean) doUpdateItem}<b>(</b>{@link T item}<b>, </b>{@link Boolean empty}<b>)</b></i></code></blockquote>
     *     </li>
     * </ol>
     *
     * @param item  {@inheritDoc}
     * @param empty {@inheritDoc}
     *
     * @see Cell#updateItem(Object, boolean)
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        cellControlManager.doUpdateItem(item, empty);
    }
    
    //</editor-fold>
}
