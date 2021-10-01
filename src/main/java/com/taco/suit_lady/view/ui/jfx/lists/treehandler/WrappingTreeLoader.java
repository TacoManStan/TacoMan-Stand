package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.util.Validatable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.function.Consumer;
import java.util.function.Function;

public class WrappingTreeLoader<T, C extends CellController<WrappingTreeCellData<T>>> extends TreeLoader<WrappingTreeCellData<T>, T, C> {

	public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView) {
		super(treeView);
	}

	public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView, Function<WrappingTreeCellData<T>, C> controllerSupplier) {
		super(treeView, controllerSupplier);
	}

	public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView,
			Function<WrappingTreeCellData<T>, C> controllerSupplier,
			Validatable<T> validator,
			ObservableValue revalidateObservable) {
		super(treeView, controllerSupplier, validator, revalidateObservable);
	}

	public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView,
			Function<WrappingTreeCellData<T>, C> controllerSupplier,
			Validatable<T> validator,
			ObservableValue revalidateObservable,
			String rootName) {
		super(treeView, controllerSupplier, validator, revalidateObservable, rootName);
	}

	public WrappingTreeLoader(TreeView<WrappingTreeCellData<T>> treeView,
			Function<WrappingTreeCellData<T>, C> controllerSupplier,
			Validatable<T> validator,
			ObservableValue revalidateObservable,
			Consumer<TreeItem<WrappingTreeCellData<T>>> settingsApplier, String rootName) {
		super(treeView, controllerSupplier, validator, revalidateObservable, settingsApplier, rootName);
	}

	//<editor-fold desc="Implementation">

	@Override protected WrappingTreeCellData<T> createTreeCellData(String name, String parentName, boolean isFolder, Function<Object[], T> provider) {
		T _obj = provider != null ? provider.apply(new Object[0]) : null;
		return new WrappingTreeCellData<>(name, parentName, isFolder, _obj);
	}

	//</editor-fold>
}
