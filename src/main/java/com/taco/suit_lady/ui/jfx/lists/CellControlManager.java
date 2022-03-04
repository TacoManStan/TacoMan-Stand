package com.taco.suit_lady.ui.jfx.lists;

import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * <p>Used by an {@link IndexedCell FX IndexedCell} to help create, update, manage, and use a {@link CellController} to display its {@link IndexedCell#getItem() content}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link CellControlManager CellControlManagers} are always immutable.</li>
 *     <li>
 *         {@link CellControlManager} is used to manage properties and functionality that is present in both {@link ListCellFX} and {@link TreeCellFX}.
 *         <ul>
 *             <li>{@link CellControlManager} cannot be abstracted out into parent because {@link ListCellFX} must extend {@link ListCell}, and {@link TreeCellFX} must extend {@link TreeCell}.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The type of element contained in the {@link IndexedCell}.
 * @param <C> The type of {@link CellController} assigned to the {@link IndexedCell}.
 */
// TO-EXPAND
public class CellControlManager<T extends Serializable, C extends CellController<T>> {
    
    private final Lock lock;
    
    private final IndexedCell<T> wrappedCell;
    
    private final Function<T, C> controllerFactory;
    private final ReadOnlyObjectWrapper<C> controllerProperty;
    
    /**
     * <p>Constructs a new {@link CellControlManager} for the specified {@link IndexedCell}.</p>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <b>Wrapped Cell:</b> {@literal IndexedCell<T>}
     *         <blockquote><i>Refer to <code>{@link #getWrappedCell()}</code> for additional information.</i></blockquote>
     *         <ol>
     *             <li>Can be either a {@link TreeCellFX} or {@link ListCellFX} object.</li>
     *             <li>Immutable.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Controller Factory:</b> {@literal Function<T, C>}
     *         <blockquote><i>Refer to <code>{@link #getControllerFactory()}</code> for additional information.</i></blockquote>
     *         <ol>
     *             <li>Used to retrieve a new {@link CellController} instance to assign to the {@link #getWrappedCell() Wrapped Cell}.</li>
     *             <li>The {@link CellController} is automatically {@link #doUpdateItem(Serializable, boolean)}  updated} in the {@link TreeCellFX} and {@link ListCellFX} implementations of <code><i>{@link IndexedCell#updateItem(Object, boolean)}</i></code>.</li>
     *             <li>The {@link #controllerProperty() Controller Property} is automatically {@link #updateController(Serializable) updated} upon aforementioned changes.</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @param wrappedCell       The wrapped {@link IndexedCell}.
     * @param controllerFactory The {@link Function} used to retrieve a new {@link CellController} instance for the {@link #getWrappedCell() Wrapped Cell} whenever <code><i>{@link #doUpdateItem(Serializable, boolean)}</i></code> is called.
     */
    public CellControlManager(IndexedCell<T> wrappedCell, Function<T, C> controllerFactory) {
        this.lock = new ReentrantLock();
        
        this.wrappedCell = wrappedCell;
        
        this.controllerFactory = controllerFactory;
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link Lock} used to synchronize this {@link CellControlManager}.</p>
     *
     * @return The {@link Lock} used to synchronize this {@link CellControlManager}.
     */
    protected final Lock getLock() {
        return lock;
    }
    
    /**
     * <p>Returns the immutable {@link IndexedCell} object wrapped by this {@link CellControlManager}.</p>
     *
     * @return The immutable {@link IndexedCell} object wrapped by this {@link CellControlManager}.
     */
    public final IndexedCell<T> getWrappedCell() {
        return wrappedCell;
    }
    
    /**
     * <p>Returns the {@link Function} used to retrieve the appropriate {@link CellController} for the {@link #getWrappedCell() Wrapped Cell} whenever <code><i>{@link #doUpdateItem(Serializable, boolean)}</i></code> is called.</p>
     *
     * @return The {@link Function} used to retrieve the appropriate {@link CellController} for the {@link #getWrappedCell() Wrapped Cell} whenever <code><i>{@link #doUpdateItem(Serializable, boolean)}</i></code> is called.
     */
    protected final Function<T, C> getControllerFactory() {
        return controllerFactory;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the current {@link CellController} assigned to the {@link #getWrappedCell() Wrapped Cell}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the current {@link CellController} assigned to the {@link #getWrappedCell() Wrapped Cell}.
     */
    public final ReadOnlyObjectProperty<C> controllerProperty() {
        return controllerProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>The current {@link CellController} assigned to the {@link #getWrappedCell() Wrapped Cell}.</p>
     *
     * @return The current {@link CellController} assigned to the {@link #getWrappedCell() Wrapped Cell}.
     */
    public final C getController() {
        return controllerProperty.get();
    }
    
    /**
     * <p>Sets the {@link CellController} assigned to the {@link #getWrappedCell() Wrapped Cell} to the specified value.</p>
     *
     * @param controller The {@link CellController} being assigned to the {@link #getWrappedCell() Wrapped Cell}.
     */
    private void setController(C controller) {
        controllerProperty.set(controller);
    }
    
    //</editor-fold>
    
    /**
     * <p>Helper method called by the {@link TreeCellFX} and {@link ListCellFX} implementations of <code><i>{@link IndexedCell#updateItem(Object, boolean)}</i></code>.</p>
     *
     * @param item  The new {@link T item} for the {@link IndexedCell}.
     * @param empty Whether this {@link IndexedCell} represents data from the list. If it is empty, then it does not represent any domain data, but is a cell being used to render an "empty" row. *Copied from IndexedCell*
     */
    protected void doUpdateItem(T item, boolean empty) {
        FX.runFX(() -> {
            if (!empty) {
                if (item != null) {
                    final C controller = updateController(item);
                    wrappedCell.setMaxWidth(Integer.MAX_VALUE);
                    wrappedCell.setGraphic(controller.root());
                    wrappedCell.setOpaqueInsets(Insets.EMPTY);
                    wrappedCell.setPadding(Insets.EMPTY);
                }
            } else {
                wrappedCell.setGraphic(null);
                wrappedCell.setText(null);
            }
        }, true);
    }
    
    /**
     * <p>Helper method that retrieves and updates the {@link CellController} of the {@link #getWrappedCell() Wrapped Cell} by passing the specified {@link T item} to the {@link #getControllerFactory() Controller Factory}.</p>
     *
     * @param item The new {@link T item} assigned to the {@link #getWrappedCell() Wrapped Cell}.
     *
     * @return The newly retrieved and assigned {@link CellController} instance as defined by the {@link #getControllerFactory() Controller Factory}.
     */
    private @NotNull C updateController(T item) {
        final C controller = controllerFactory.apply(item);
        
        controller.setContents(item);
        setController(controller);
        
        return controller;
    }
}
