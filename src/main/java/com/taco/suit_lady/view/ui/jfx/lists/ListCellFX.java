package com.taco.suit_lady.view.ui.jfx.lists;

import com.taco.suit_lady.view.ui.jfx.lists.treehandler.IndexedCellFXable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.scene.control.ListCell;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class ListCellFX<T, C extends CellController<T>> extends ListCell<T>
        implements IndexedCellFXable<T, C>
{
    
    private final Lock lock;
    private final CellControlManager<T, C> cellControlManager;
    
    public ListCellFX(Function<ListCellFX<T, C>, CellControlManager<T, C>> cellControlManagerFactory)
    {
        this.cellControlManager = cellControlManagerFactory.apply(this);
        this.lock = this.cellControlManager.getLock();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final Lock getLock()
    {
        return lock;
    }
    
    //</editor-fold>
    
    @Override
    protected void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);
        cellControlManager.doUpdateItem(item, empty);
    }
    
    @Override
    public CellControlManager<T, C> getCellControlManager()
    {
        return cellControlManager;
    }
}
