package com.taco.suit_lady.view.ui.jfx.lists;

import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class WrappingCell<T, C extends CellController<T>> {

	private final Lock lock;

	private final TreeView<T> treeView;
	private final ListView<T> listView;
	private final IndexedCell<T> wrappedCell;

	private final Function<T, C> controllerFactory;
	private final ReadOnlyObjectWrapper<C> controllerProperty;

	public WrappingCell(TreeView<T> treeView, IndexedCell<T> wrappedCell, Function<T, C> controllerFactory) {
		this(treeView, null, wrappedCell, controllerFactory);
	}

	public WrappingCell(ListView<T> listView, IndexedCell<T> wrappedCell, Function<T, C> controllerFactory) {
		this(null, listView, wrappedCell, controllerFactory);
	}

	private WrappingCell(TreeView<T> treeView, ListView<T> listView, IndexedCell<T> wrappedCell, Function<T, C> controllerFactory) {
		this.lock = new ReentrantLock();

		this.treeView = treeView;
		this.listView = listView;
		this.wrappedCell = wrappedCell;

		this.controllerFactory = controllerFactory;
		this.controllerProperty = new ReadOnlyObjectWrapper<>();
	}

	//<editor-fold desc="Properties">

	protected final Lock getLock() {
		return lock;
	}

	//

	public final IndexedCell<T> getWrappedCell() {
		return wrappedCell;
	}

	//

	public final ReadOnlyObjectProperty<C> controllerProperty() {
		return controllerProperty.getReadOnlyProperty();
	}

	public final C getController() {
		return controllerProperty.get();
	}

	//</editor-fold>

	//

	protected void doUpdateItem(T item, boolean empty) {
		FXTools.get().runFX(() -> {
			if (!empty) {
				C _controller = getController();
				if (item != null) {
					if (_controller == null) { // Lazy initialization
//						ConsoleBB.CONSOLE.print("Lazy Initializing Controller Factory for Item: " + item);
						_controller = controllerFactory.apply(item);
						wrappedCell.setMaxWidth(Integer.MAX_VALUE);
					}
					
					_controller.setContents(item);

					// TODO - Add support for setting/changing cell text in addition to graphic
					// The main reason for this is that cell editing is natively supported via cell text
					wrappedCell.setGraphic(_controller.root());
					wrappedCell.setOpaqueInsets(Insets.EMPTY);
					wrappedCell.setPadding(Insets.EMPTY);
				}
			} else {
				wrappedCell.setGraphic(null);
				wrappedCell.setText(null);
			}
		}, true);
	}
}
