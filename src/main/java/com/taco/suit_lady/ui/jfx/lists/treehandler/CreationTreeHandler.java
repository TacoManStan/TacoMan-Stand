package com.taco.suit_lady.ui.jfx.lists.treehandler;

import javafx.scene.control.TreeView;

/**
 * Used to easily put elements into a {@link TreeView}, or to store elements for easy retrieval (or both).
 *
 * @param <T> The type of elements in the {@link CreationTreeHandler}.
 */
public class CreationTreeHandler<T> {

//	private final ReentrantLock lock;
//
//	private final HashMap<String, TreeItem<TreeCellData<T>>> folders;
//	private TreeItem<TreeCellData<T>> root;
//	private final TreeView<TreeCellData<T>> treeView;
//	private final V validator;
//	private final boolean showRoot;
//
//	private boolean hasSetRoot;
//
//	/**
//	 * Constructs a new {@link CreationTreeHandler} with the specified root, {@link TreeView}, and show root option.
//	 *
//	 * @param treeView The {@link TreeView} that is being added to.
//	 * @param showRoot True if the {@link TreeView} should show the root element, false otherwise.
//	 */
//	public CreationTreeHandler(TreeView<TreeCellData<T>> treeView, boolean showRoot) {
//		this(treeView, null, showRoot);
//	}
//
//	/**
//	 * Constructs a new {@link CreationTreeHandler} with the specified root, {@link TreeView}, and show root option.
//	 *
//	 * @param treeView  The {@link TreeView} that is being added to.
//	 * @param validator The {@link TreeItemValidator} that is being used to validate the elements of this {@link CreationTreeHandler}.
//	 * @param showRoot  True if the {@link TreeView} should show the root element, false otherwise.
//	 */
//	public CreationTreeHandler(TreeView<TreeCellData<T>> treeView, V validator, boolean showRoot) {
//		this(treeView, validator, showRoot, null);
//	}
//
//	/**
//	 * Constructs a new {@link CreationTreeHandler} with the specified root, {@link TreeView}, and show root option.
//	 *
//	 * @param treeView       The {@link TreeView} that is being added to.
//	 * @param validator      The {@link TreeItemValidator} that is being used to validate the elements of this {@link CreationTreeHandler}.
//	 * @param showRoot       True if the {@link TreeView} should show the root element, false otherwise.
//	 * @param treeCellLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated root {@link TreeCell}. Null if no settings need to be applied.
//	 */
//	public CreationTreeHandler(TreeView<TreeCellData<T>> treeView, V validator, boolean showRoot, TreeItemSettingsApplier treeCellLoader) {
//		this.lock = new ReentrantLock();
//
//		this.hasSetRoot = false;
//		this.folders = new HashMap<>();
//		this.treeView = treeView;
//		this.validator = validator;
//		this.showRoot = showRoot;
//		this.root = new TreeItem<>(generateFolderCell("root", null, this, validator, treeCellLoader));
//		this.root.setExpanded(true);
//		this.hasSetRoot = true;
//	}
//
//	//
//
//	//<editor-fold desc="Properties">
//
//	/**
//	 * Returns the folder elements of this {@link CreationTreeHandler}.
//	 * <p>
//	 * Returns a clone. No modifications to the structure of the returned {@link HashMap} will have an effect.
//	 *
//	 * @return The folder elements of this {@link CreationTreeHandler}.
//	 */
//	public final HashMap<String, TreeItem<TreeCellData<T>>> getFolders() {
//		synchronized (folders) {
//			return (HashMap<String, TreeItem<TreeCellData<T>>>) folders.clone();
//		}
//	}
//
//	/**
//	 * Returns the {@link TreeView} for this {@link CreationTreeHandler}.
//	 *
//	 * @return The {@link TreeView} for this {@link CreationTreeHandler}.
//	 */
//	public final TreeView getTreeView() {
//		return treeView;
//	}
//
//	/**
//	 * Returns the root element for this {@link CreationTreeHandler}.
//	 *
//	 * @return The root element for this {@link CreationTreeHandler}.
//	 */
//	public final TreeItem<TreeCellData<T>> getRootItem() {
//		return root;
//	}
//
//	/**
//	 * Returns the {@link TreeItemValidator TreeValidators} for this {@link CreationTreeHandler}.
//	 *
//	 * @return The {@link TreeItemValidator TreeValidators} for this {@link CreationTreeHandler}.
//	 */
//	public final V getValidator() {
//		return validator;
//	}
//
//	/**
//	 * Checks if the root element is being shown.
//	 *
//	 * @return True if the root element is being shown, false otherwise.
//	 */
//	public final boolean isShowRoot() {
//		return showRoot;
//	}
//
//	/**
//	 * Checks if the root element has been set, false otherwise.
//	 *
//	 * @return True if the root element has been set, false otherwise.
//	 */
//	public final boolean hasSetRoot() {
//		return hasSetRoot;
//	}
//
//	//</editor-fold>
//
//	//<editor-fold desc="Implementation">
//
//	@Override public final ReentrantLock getLock() {
//		return lock;
//	}
//
//	@Override public void interrupted(InterruptedException e) {}
//
//	//</editor-fold>
//
//	//
//
//	//<editor-fold desc="Helpers">
//
//	//<editor-fold desc="Cell/Value Helpers">
//
//	private T getItem(Predicate<T> filter, TreeItem<TreeCellData<T>> treeItem, Object... objs) {
//		if (treeItem != null) {
//			TreeCellData<T> cellData = treeItem.getValue();
//			if (cellData != null) {
//				T item = cellData.createWrappedInstance(objs);
//				if (item != null && (filter == null || filter.test(item)))
//					return item;
//			}
//			for (TreeItem<TreeCellData<T>> childTreeItem : treeItem.getChildren()) {
//				TreeCellData<T> childCell = childTreeItem.getValue();
//				if (childCell != null) {
//					T childItem = childCell.createWrappedInstance(objs);
//					if (childItem != null && (filter == null || filter.test(childItem)))
//						return childItem;
//				}
//			}
//		}
//		return null;
//	}
//
//	private ArrayList<T> addTo(Predicate<T> filter, TreeItem<TreeCellData<T>> treeItem, ArrayList<T> list, Object... objs) {
//		if (treeItem != null) {
//			TreeCellData<T> cellData = treeItem.getValue();
//			if (cellData != null) {
//				final T item = cellData.createWrappedInstance(objs);
//				if (item != null && (filter == null || filter.test(item)))
//					list.add(item);
//			}
//			for (TreeItem<TreeCellData<T>> childTreeItem : treeItem.getChildren()) {
//				TreeCellData<T> childCell = childTreeItem.getValue();
//				if (childCell != null) {
//					T childItem = childCell.createWrappedInstance(objs);
//					if (childItem != null && (filter == null || filter.test(childItem)))
//						list.add(childItem);
//				}
//			}
//		}
//		return list;
//	}
//
//	private TreeCellData<T> getCell(Predicate<TreeCellData<T>> filter, TreeItem<TreeCellData<T>> treeItem) {
//		if (treeItem != null) {
//			TreeCellData<T> cellData = treeItem.getValue();
//			if (cellData != null && (filter == null || filter.test(cellData)))
//				return cellData;
//			for (TreeItem<TreeCellData<T>> child : treeItem.getChildren()) {
//				TreeCellData<T> childValue = getCell(filter, child);
//				if (childValue != null)
//					return childValue;
//			}
//		}
//		return null;
//	}
//
//	private ArrayList<TreeCellData<T>> addTo(Predicate<TreeCellData<T>> filter, TreeItem<TreeCellData<T>> treeItem, ArrayList<TreeCellData<T>> list) {
//		if (treeItem != null) {
//			TreeCellData<T> cellData = treeItem.getValue();
//			if (cellData != null && !cellData.isFolder() && (filter == null || filter.test(cellData)))
//				list.add(cellData);
//			for (TreeItem<TreeCellData<T>> child : treeItem.getChildren())
//				addTo(filter, child, list);
//		}
//		return list;
//	}
//
//	//</editor-fold>
//
//	//<editor-fold desc="Clear Folders Helpers">
//
//	/**
//	 * Clears away all empty folders from the {@link TreeView} for this {@link CreationTreeHandler}.
//	 */
//	@SuppressWarnings("StatementWithEmptyBody")
//	private void clearEmptyFolders() {
//		while (clearEmptyFolders(root) > 0)
//			GeneralTools.sleepLoop();
//	}
//
//	/**
//	 * A recursive helper method used by the {@link #clearEmptyFolders()} method.
//	 *
//	 * @param item The {@link TreeItem}.
//	 */
//	private int clearEmptyFolders(TreeItem<TreeCellData<T>> item) {
//		ArrayList<TreeItem<TreeCellData<T>>> toRemove = new ArrayList<>();
//		int totalRemoved = 0;
//		for (TreeItem<TreeCellData<T>> child : item.getChildren())
//			if (child.getValue().isFolder())
//				if (child.getChildren().isEmpty())
//					toRemove.add(child);
//				else
//					totalRemoved += clearEmptyFolders(child);
//		if (item.getChildren().removeAll(toRemove))
//			totalRemoved += toRemove.size();
//		return totalRemoved;
//	}
//
//	/**
//	 * Clears away all parent folders that only have a single child until a parent folder with more than one child is found.
//	 */
//	private void clearUnnecessaryFolders() {
//		clearUnnecessaryFolders(root);
//	}
//
//	/**
//	 * A recursive helper method used by {@link #clearUnnecessaryFolders()}.
//	 *
//	 * @param item The {@link TreeItem}.
//	 */
//	private void clearUnnecessaryFolders(TreeItem<TreeCellData<T>> item) {
//		if (item.getChildren().size() == 1) {
//			TreeItem<TreeCellData<T>> child = item.getChildren().get(0);
//			root = item;
//			clearUnnecessaryFolders(child);
//			applyRoot();
//		}
//	}
//
//	//</editor-fold>
//
//	//<editor-fold desc="Misc. Helpers">
//
//	/**
//	 * Returns the parent element of the specified element.
//	 *
//	 * @param element The element.
//	 * @return The parent element of the specified element.
//	 */
//	private TreeItem<TreeCellData<T>> getParent(TreeCellData<T> element) {
//		return element.getParentName().equalsIgnoreCase("root") ? root : folders.get(element.getParentName());
//	}
//
//	/**
//	 * Puts the specified folder element into the {@link HashMap} of folders, creates a new {@link TreeItem} folder, and then returns the folder.
//	 *
//	 * @param element The element.
//	 * @return The {@link TreeItem} folder that was created by this method.
//	 */
//	private TreeItem<TreeCellData<T>> putFolder(TreeCellData<T> element) {
//		TreeItem<TreeCellData<T>> item = new TreeItem<>(element);
//		folders.put(element.getName(), item);
//		return item;
//	}
//
//	//</editor-fold>
//
//	//</editor-fold>
//
//	//<editor-fold desc="Static">
//
//	//<editor-fold desc="Generate">
//
//	/**
//	 * Generates a new {@link TreeCellData}, checks the generated element's validity, and then adds it to the specified {@link CreationTreeHandler} if the element is valid.
//	 *
//	 * @param <T>            The type of element contained within the {@link TreeCellData}.
//	 * @param <V>            The type of {@link TreeItemValidator}.
//	 * @param name           The name.
//	 * @param parentName     The parent name.
//	 * @param loader         The {@link CreationTreeHandler} that is doing the loading.
//	 * @param validator      The {@link TreeItemValidator} that is validating each element being added to the {@link TreeView}.
//	 * @param loadable       The {@link TreeItemValueProvider} that is loading each element to be put into the {@link TreeView}.
//	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated {@link TreeCell}. Null if no settings need to be applied.
//	 * @return A generated {@link TreeCellData} given the specified values.
//	 */
//	public static <T, V extends TreeItemValidator> TreeCellData<T> generateCell(String name,
//			String parentName,
//			CreationTreeHandler loader,
//			V validator,
//			TreeItemValueProvider<T> loadable,
//			TreeItemSettingsApplier treeItemLoader) {
//		return generateCell(name, parentName, false, loader, validator, loadable, treeItemLoader);
//	}
//
//	/**
//	 * Generates a new {@link TreeCellData}, checks the generated element's validity, and then adds it to the specified {@link CreationTreeHandler} if the element is valid.
//	 *
//	 * @param <T>            The type of element contained within the {@link TreeCellData}.
//	 * @param <V>            The type of {@link TreeItemValidator}.
//	 * @param name           The name.
//	 * @param parentName     The parent name.
//	 * @param loader         The {@link CreationTreeHandler} that is doing the loading.
//	 * @param validator      The {@link TreeItemValidator} that is validating each element being added to the {@link TreeView}.
//	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated {@link TreeCell}. Null if no settings need to be applied.
//	 * @return A generated {@link TreeCellData} given the specified values.
//	 */
//	public static <T, V extends TreeItemValidator> TreeCellData<T> generateFolderCell(String name, String parentName, CreationTreeHandler loader, V validator, TreeItemSettingsApplier treeItemLoader) {
//		return generateCell(name, parentName, true, loader, validator, null, treeItemLoader);
//	}
//
//	/**
//	 * Generates a new {@link TreeCellData}, checks the generated element's validity, and then adds it to the specified {@link CreationTreeHandler} if the element is valid.
//	 *
//	 * @param <T>            The type of element contained within the {@link TreeCellData}.
//	 * @param <V>            The type of {@link TreeItemValidator}.
//	 * @param name           The name.
//	 * @param parentName     The parent name.
//	 * @param folder         True if the element being generated is a folder element, false otherwise.
//	 * @param loader         The {@link CreationTreeHandler} that is doing the loading.
//	 * @param validator      The {@link TreeItemValidator} that is validating each element being added to the {@link TreeView}.
//	 * @param loadable       The {@link TreeItemValueProvider} that is loading each element to be put into the {@link TreeView}.
//	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated {@link TreeCell}. Null if no settings need to be applied.
//	 * @return A generated {@link TreeCellData} given the specified values.
//	 */
//	public static <T, V extends TreeItemValidator> TreeCellData<T> generateCell(String name,
//			String parentName,
//			boolean folder,
//			CreationTreeHandler loader,
//			V validator,
//			TreeItemValueProvider<T> loadable,
//			TreeItemSettingsApplier treeItemLoader) {
//		if (!folder && loadable != null) {
//			T instance = loadable.getLoadableValue();
//			if (validator != null && !validator.validate(instance))
//				return null;
//		}
//		final TreeCellData data = new TreeCellData(name, parentName, folder, loadable);
//		if (loader.hasSetRoot)
//			loader.addCellData(data, treeItemLoader);
//		return data;
//	}
//
//	/**
//	 * Generates a new {@link TreeCellData}, checks the generated element's validity, and then adds it to the specified {@link CreationTreeHandler} if the element is valid.
//	 *
//	 * @param <T>            The type of element contained within the {@link TreeCellData}.
//	 * @param <V>            The type of {@link TreeItemValidator}.
//	 * @param parentName     The parent name.
//	 * @param loader         The {@link CreationTreeHandler} that is doing the loading.
//	 * @param validator      The {@link TreeItemValidator} that is validating each element being added to the {@link TreeView}.
//	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated {@link TreeCell}. Null if no settings need to be applied.
//	 * @return A generated {@link TreeCellData} given the specified values.
//	 */
//	public static <T, V extends TreeItemValidator> TreeCellData<T> generateCell(CreationTreeHandler appendingLoader, String parentName, CreationTreeHandler loader, V validator, TreeItemSettingsApplier
//			treeItemLoader) {
//		final TreeItem<TreeCellData> tempRoot = appendingLoader.root;
//		final TreeCellData data = tempRoot.getValue();
//		if (loader.hasSetRoot)
//			loader.addCellData(data, treeItemLoader);
//		return data;
//	}
//
//	//</editor-fold>
//
//	//
//
//	/**
//	 * Creates and then returns a {@link TreeItemValueProvider} from the specified {@link Class}.
//	 *
//	 * @param clazz The {@link Class}.
//	 * @param <T>   The type of {@link Class}.
//	 * @return The {@link TreeItemValueProvider} created from the specified {@link Class}.
//	 */
//	@SuppressWarnings("Convert2Lambda")
//	public static <T> TreeItemValueProvider<T> toLoadableValue(Class<T> clazz) {
//		return new TreeItemValueProvider<T>() {
//			@Override public T getLoadableValue(Object... objs) {
//				return GeneralTools.newInstance(clazz, objs);
//			}
//		};
//	}
//
//	@SuppressWarnings("Convert2Lambda")
//	public static <T extends Copyable<T>> TreeItemValueProvider<T> toLoadableValue(T copyable) {
//		//noinspection Anonymous2MethodRef
//		return new TreeItemValueProvider<T>() {
//			@Override public T getLoadableValue(Object... objs) {
//				return copyable.newCopy(objs);
//			}
//		};
//	}
//
//	//</editor-fold>
//
//	//
//
//	//<editor-fold desc="Get Item/Cell">
//
//	//<editor-fold desc="Get Item">
//
//	public final T getItem(Predicate<T> filter, Object... objs) {
//		return getItem(filter, root, objs);
//	}
//
//	public final ArrayList<T> getAllItems(Object... objs) {
//		return getAllItems(null, objs);
//	}
//
//	public final ArrayList<T> getAllItems(Predicate<T> filter, Object... objs) {
//		return addTo(filter, root, new ArrayList<>(), objs);
//	}
//
//	/**
//	 * Returns the item with the specified {@link Class class name}, or null if no such item exists.
//	 *
//	 * @param className The {@link Class class name}.
//	 * @param objs      The data that are used to load the retrieved item.
//	 * @return The item with the specified {@link Class class name}, or null if no such item exists.
//	 */
//	public T getItemByClass(String className, Object... objs) {
//		final TreeCellData<T> cellData = getCellByClass(className);
//		if (cellData != null)
//			return cellData.createWrappedInstance(objs);
//		return null;
//	}
//
//	//</editor-fold>
//
//	//<editor-fold desc="Get Cell">
//
//	public final TreeCellData<T> getCell(Predicate<TreeCellData<T>> filter) {
//		return getCell(filter, root);
//	}
//
//	public final ArrayList<TreeCellData<T>> getAllCells() {
//		return getAllCells(null);
//	}
//
//	public final ArrayList<TreeCellData<T>> getAllCells(Predicate<TreeCellData<T>> filter) {
//		return addTo(filter, root, new ArrayList<>());
//	}
//
//	/**
//	 * Returns the first {@link TreeCellData} with the specified class name.
//	 * <p>
//	 * Note that this method searches branch by branch, so the returned {@link TreeCellData} will be the left-most matching element in the tree
//	 * (assuming traditional tree diagram/model).
//	 *
//	 * @param className The class name.
//	 * @return The first {@link TreeCellData} with the specified class name.
//	 */
//	public TreeCellData<T> getCellByClass(String className) {
//		return getCell(cell -> {
//			Class clazz = cell.getWrappedClass();
//			return cell.getWrappedClass() != null && cell.getWrappedClass().getSimpleName().equalsIgnoreCase(className);
//		});
//	}
//
//	//</editor-fold>
//
//	//</editor-fold>
//
//	//<editor-fold desc="Apply">
//
//	/**
//	 * Applies the settings of this {@link CreationTreeHandler} to the {@link TreeView}.
//	 */
//	public void apply() {
//		applyRoot();
//		clearEmptyFolders();
//		clearUnnecessaryFolders();
//	}
//
//	/**
//	 * Adds a new element to this {@link CreationTreeHandler}.
//	 *
//	 * @param element        The element being added.
//	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated root {@link TreeCell}. Null if no settings need to be applied.
//	 */
//	public void addCellData(TreeCellData<T> element, TreeItemSettingsApplier<T, V> treeItemLoader) {
//		if (element != null) {
//			TreeItem<TreeCellData<T>> treeItem = new TreeItem<>(element);
//			if (treeItemLoader != null)
//				treeItemLoader.apply(treeItem);
//			TreeItem<TreeCellData<T>> data = element.isFolder() ? putFolder(element) : new TreeItem<>(element);
//			try {
//				TreeItem<TreeCellData<T>> parent = getParent(element);
//				parent.getChildren().add(data);
//			} catch (Exception e) {
//				throw ExceptionTools.ex(e, element.getParentName() + " has not yet been added as a parent.");
//			}
//		}
//	}
//
//	//
//
//	/**
//	 * Applies the root for this {@link CreationTreeHandler} to the actual {@link TreeView} for this {@link CreationTreeHandler}.
//	 */
//	private void applyRoot() {
//		if (treeView != null) {
//			treeView.setRoot(root);
//			treeView.setShowRoot(showRoot);
//		}
//	}
//
//	//</editor-fold>
}

/*
 * TODO LIST:
 * [S] Make as many properties observable as possible. e.g.:
 *     [S] folders - HashMap
 *     [S] root - TreeItem
 *     [S] showRoot - boolean
 *     [S] etc.
 */