package com.taco.suit_lady.view.ui.jfx.lists;

import com.taco.suit_lady.util.BindingTools;
import com.taco.suit_lady.util.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.lists.treehandler.TreeItemFX;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class TreeCellFX<T, C extends CellController<T>> extends TreeCell<T>
{
    
    private final Lock lock;
    private final WrappingCell<T, C> wrappingCell;
    
    private final ObjectBinding<TreeItemFX<T>> treeItemFXBinding;
    
    public TreeCellFX(Function<TreeCellFX<T, C>, WrappingCell<T, C>> wrappedCellFactory)
    {
        this.wrappingCell = wrappedCellFactory.apply(this);
        this.lock = this.wrappingCell.getLock();
        
        this.treeItemFXBinding = Bindings.createObjectBinding(() -> {
            TreeItem<T> _treeItem = getTreeItem();
            if (_treeItem != null)
                if (_treeItem instanceof TreeItemFX)
                    return (TreeItemFX<T>) _treeItem;
                else
                    throw ExceptionTools.ex(new ClassCastException(), "TreeCellFX objects must only contain TreeItemFX items.`");
            return null;
        }, treeItemProperty());
        
        initVisibleBinding();
    }
    
    private void initVisibleBinding()
    {
        BindingTools.RecursiveBinding<TreeItemFX<T>, Boolean> _visibleBinding = BindingTools.createRecursiveBinding(
                treeItemFX -> treeItemFX != null ? treeItemFX.visibleProperty() : BindingTools.createBooleanBinding(false), treeItemFXBinding());
        
        _visibleBinding.addListener(observable -> setDisable(!_visibleBinding.getValue()));
    }
    
    //<editor-fold desc="Properties">
    
    protected final Lock getLock()
    {
        return lock;
    }
    
    protected final WrappingCell<T, C> getCellWrapper()
    {
        return wrappingCell;
    }
    
    //
    
    public final ObjectBinding<TreeItemFX<T>> treeItemFXBinding()
    {
        return treeItemFXBinding;
    }
    
    public final TreeItemFX<T> getTreeItemFX()
    {
        return treeItemFXBinding.get();
    }
    
    //</editor-fold>
    
    //
    
    @Override protected void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);
        wrappingCell.doUpdateItem(item, empty);
    }
}
