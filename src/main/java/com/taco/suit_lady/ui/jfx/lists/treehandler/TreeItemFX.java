package com.taco.suit_lady.ui.jfx.lists.treehandler;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

public class TreeItemFX<T> extends TreeItem<T> {

	private final BooleanProperty visibleProperty;

	public TreeItemFX(T element) {
		super(element);
		this.visibleProperty = new SimpleBooleanProperty(true);
	}

	//<editor-fold desc="Properties">

	public final BooleanProperty visibleProperty() {
		return visibleProperty;
	}

	public final boolean isVisible() {
		return visibleProperty.get();
	}

	public final void setVisible(boolean visible) {
		visibleProperty.set(visible);
	}

	//</editor-fold>
}
