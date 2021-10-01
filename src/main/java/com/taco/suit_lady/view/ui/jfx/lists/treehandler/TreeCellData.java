package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.io.Serializable;

public abstract class TreeCellData<T>
		implements Nameable, Serializable, UIDProcessable
{
	private final String name;
	private final String parentName;
	private final boolean isFolder;

	private final ReadOnlyObjectWrapper<T> wrappedObjectProperty;

	protected TreeCellData(String name, String parentName, boolean isFolder) {
		this.name = name;
		this.parentName = parentName;
		this.isFolder = isFolder;

		this.wrappedObjectProperty = new ReadOnlyObjectWrapper<>();
	}

	//<editor-fold desc="Properties">

	/**
	 * Returns the name of this {@link TreeCellData}.
	 *
	 * @return The name of this {@code TreeCellData}.
	 */
	@Override public final String getName() {
		return name;
	}

	//

	/**
	 * Returns the name of this {@link TreeCellData TreeCellData's} parent, or null if this {@link TreeCellData} does not have a parent.
	 *
	 * @return The name of this {@link TreeCellData TreeCellData's} parent, or null if this {@link TreeCellData} does not have a parent.
	 */
	public final String getParentName() {
		return parentName;
	}

	/**
	 * Checks to see if this {@link TreeCellData} is a folder or not.
	 *
	 * @return True if this {@link TreeCellData} is a folder, false otherwise.
	 */
	public final boolean isFolder() {
		return isFolder;
	}

	//

	public final ReadOnlyObjectProperty<T> wrappedObjectProperty() {
		return wrappedObjectProperty.getReadOnlyProperty();
	}

	public final T getWrappedObject() {
		return wrappedObjectProperty.get();
	}

	protected final void setWrappedObject(T wrappedObject) {
		wrappedObjectProperty.set(wrappedObject);
	}

	//</editor-fold>

	//<editor-fold desc="Implementation">

	private UIDProcessor uIDContainer;

	@Override
	public UIDProcessor getUIDProcessor() {
		if (uIDContainer == null) // Lazy initialization
			uIDContainer = new UIDProcessor("tree-cell-data");
		return uIDContainer;
	}

	//</editor-fold>

	//

	//<editor-fold desc="Abstract">

	public abstract T createWrappedInstance(Object... creationParams);

	//</editor-fold>

	//

	/**
	 * Returns the {@link Class} that is to be used to compare elements for this {@link CreationTreeCellData}.
	 *
	 * @return The {@link Class} that is to be used to compare elements for this {@link CreationTreeCellData}.
	 */
	public Class<T> getWrappedClass(Object... creationParams) {
		T wrappedObject = getWrappedObject();
		if (wrappedObject != null)
			return (Class<T>) wrappedObject.getClass();
		return null;
	}
}

/*
 * TODO LIST:
 * [S] Make the name into a property.
 *     [S] For this to work properly, you're going to need to rebuild the folders HashMap in the TreeLoader that owns this TreeCellData.
 */