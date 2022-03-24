package com.taco.tacository.ui.jfx.lists.treehandler;

import com.taco.tacository.util.Validatable;
import com.taco.tacository.ui.ui_internal.controllers.CellController;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.function.Consumer;
import java.util.function.Function;

public class WrappingTreeLoader<T, C extends CellController<WrappingTreeCellData<T>>> extends TreeLoader<WrappingTreeCellData<T>, T, C>
{
    
    public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView)
    {
        super(treeView);
    }
    
    public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView, Function<WrappingTreeCellData<T>, C> controllerSupplier)
    {
        super(treeView, controllerSupplier);
    }
    
    public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView, Function<WrappingTreeCellData<T>, C> controllerSupplier, Validatable<T> validator)
    {
        super(treeView, controllerSupplier, validator);
    }
    
    public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView, Function<WrappingTreeCellData<T>, C> controllerSupplier, Validatable<T> validator, String rootName)
    {
        super(treeView, controllerSupplier, validator, rootName);
    }
    
    public WrappingTreeLoader(
            TreeView<WrappingTreeCellData<T>> treeView,
            Function<WrappingTreeCellData<T>, C> controllerSupplier,
            Validatable<T> validator,
            Consumer<TreeItem<WrappingTreeCellData<T>>> settingsApplier, String rootName)
    {
        super(treeView, controllerSupplier, validator, settingsApplier, rootName);
    }
    
    //<editor-fold desc="Implementation">
    
    @Override
    protected WrappingTreeCellData<T> createTreeCellData(String name, String parentName, boolean isFolder, Function<Object[], T> provider)
    {
        T _obj = provider != null ? provider.apply(new Object[0]) : null;
        return new WrappingTreeCellData<>(name, parentName, isFolder, _obj);
    }
    
    //</editor-fold>
}
