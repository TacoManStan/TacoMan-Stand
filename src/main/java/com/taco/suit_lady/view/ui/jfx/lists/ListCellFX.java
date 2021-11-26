package com.taco.suit_lady.view.ui.jfx.lists;

import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.scene.control.ListCell;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class ListCellFX<T, U extends CellController<T>> extends ListCell<T>
{
    
    private final Lock lock;
    private final WrappingCell<T, U> wrapper;
    
    public ListCellFX(Function<ListCellFX<T, U>, WrappingCell<T, U>> wrappedCellFactory)
    {
        this.wrapper = wrappedCellFactory.apply(this);
        this.lock = this.wrapper.getLock();
    }
    
    //<editor-fold desc="Properties">
    
    protected final Lock getLock()
    {
        return lock;
    }
    
    protected final WrappingCell<T, U> getCellWrapper()
    {
        return wrapper;
    }
    
    //</editor-fold>
    
    //
    
    @Override
    protected void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);
        wrapper.doUpdateItem(item, empty);
    }
}
