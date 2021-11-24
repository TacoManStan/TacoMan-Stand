package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.Validatable;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.lists.TreeCellFX;
import com.taco.suit_lady.view.ui.jfx.lists.WrappingCell;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * TODO [S]: Write documentation.
 *
 * @param <E> The type of {@link TreeCellData} objects contained within the {@link TreeView} managed by this {@code TreeLoader}.
 * @param <T> The type of elements wrapped by the {@code TreeCellData} objects contained within the {@code TreeView} managed by this {@code TreeLoader}.
 * @param <C> The type of {@link CellController} used for each {@link TreeCell} of the {@code TreeView} managed by this {@code TreeLoader}.
 */
public abstract class TreeLoader<E extends TreeCellData<T>, T, C extends CellController<E>>
        implements Serializable
{
    
    //<editor-fold desc="Static">
    
    private static final String DEFAULT_ROOT_NAME;
    
    static
    {
        DEFAULT_ROOT_NAME = "Root";
    }
    
    //</editor-fold>
    
    //
    
    private final ReentrantLock lock;
    
    private final TreeView<E> treeView; // TODO [S]: Turn into list of TreeViews?
    private TreeItemFX<E> rootItem; // TODO [S]: Turn into property?
    
    private final ReadOnlyMapWrapper<String, TreeItemFX<E>> folders; // TODO [S]: Make observable.
    private final ReadOnlyListWrapper<TreeItemFX<E>> items;
    
    private final ReadOnlyObjectWrapper<Function<E, C>> controllerSupplierProperty;
    
    private final Validatable<T> validator; // TODO [S]: Change into property, then reload the TreeView when the property value changes?
    
    private final String rootName;
    private boolean hasSetRoot; // TODO [S]: Could likely be more elegantly/reliably implemented via synchronization.
    
    public TreeLoader(TreeView<E> treeView)
    {
        this(treeView, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier)
    {
        this(treeView, controllerSupplier, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier, Validatable<T> validator)
    {
        this(treeView, controllerSupplier, validator, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier, Validatable<T> validator, String rootName)
    {
        this(treeView, controllerSupplier, validator, null, rootName);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier, Validatable<T> validator, Consumer<TreeItem<E>> settingsApplier, String rootName)
    {
        this.lock = new ReentrantLock();
        
        this.hasSetRoot = false;
        
        this.treeView = treeView;
        
        this.folders = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
        this.items = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.controllerSupplierProperty = new ReadOnlyObjectWrapper<>(controllerSupplier);
        
        this.validator = validator;
        
        this.rootName = rootName != null ? rootName : TreeLoader.DEFAULT_ROOT_NAME;
        this.rootItem = new TreeItemFX<>(generateFolderCell(this.rootName, null, settingsApplier));
        
        this.rootItem.setExpanded(true);
        this.hasSetRoot = true;
    }
    
    //<editor-fold desc="Properties">
    
    public final TreeView getTreeView()
    {
        return treeView;
    }
    
    //
    
    public final TreeItemFX<E> getRootItem()
    {
        return rootItem;
    }
    
    private boolean hasSetRoot()
    {
        return hasSetRoot;
    }
    
    //
    
    public final ReadOnlyMapProperty<String, TreeItemFX<E>> folders()
    {
        return folders.getReadOnlyProperty();
    }
    
    public final ReadOnlyListProperty<TreeItemFX<E>> items()
    {
        return items.getReadOnlyProperty();
    }
    
    //
    
    public final Validatable getValidator()
    {
        return validator;
    }
    
    //
    
    public final ReadOnlyObjectProperty<Function<E, C>> controllerSupplierProperty()
    {
        return controllerSupplierProperty.getReadOnlyProperty();
    }
    
    public final Function<E, C> getControllerSupplier()
    {
        return controllerSupplierProperty.get();
    }
    
    public final void setControllerSupplier(Function<E, C> controllerSupplier)
    {
        controllerSupplierProperty.set(controllerSupplier);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Initialize">
    
    public void initialize()
    {
        initializeRoot();
        apply();
        applyCellFactory();
    }
    
    public <Z extends TreeLoader> Z initializeAndGet()
    {
        initialize();
        return (Z) this;
    }
    
    public void apply()
    {
        clearEmptyFolders();
        clearUnnecessaryFolders();
    }
    
    private void initializeRoot()
    {
        treeView.setRoot(rootItem);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Helpers">
    
    // Cell/Value Helpers
    
    private T getItem(Predicate<T> filter, TreeItem<E> treeItem, Object... objs)
    {
        if (treeItem != null)
        {
            E cellData = treeItem.getValue();
            if (cellData != null)
            {
                T item = cellData.createWrappedInstance(objs);
                if (item != null && (filter == null || filter.test(item)))
                    return item;
            }
            for (TreeItem<E> childTreeItem: treeItem.getChildren())
            {
                E childCell = childTreeItem.getValue();
                if (childCell != null)
                {
                    T childItem = childCell.createWrappedInstance(objs);
                    if (childItem != null && (filter == null || filter.test(childItem)))
                        return childItem;
                }
            }
        }
        return null;
    }
    
    private ArrayList<T> addTo(Predicate<T> filter, TreeItem<E> treeItem, ArrayList<T> list, Object... objs)
    {
        if (treeItem != null)
        {
            E cellData = treeItem.getValue();
            if (cellData != null)
            {
                final T item = cellData.createWrappedInstance(objs);
                if (item != null && (filter == null || filter.test(item)))
                    list.add(item);
            }
            for (TreeItem<E> childTreeItem: treeItem.getChildren())
            {
                E childCell = childTreeItem.getValue();
                if (childCell != null)
                {
                    T childItem = childCell.createWrappedInstance(objs);
                    if (childItem != null && (filter == null || filter.test(childItem)))
                        list.add(childItem);
                }
            }
        }
        return list;
    }
    
    private E getCell(Predicate<E> filter, TreeItem<E> treeItem)
    {
        if (treeItem != null)
        {
            E cellData = treeItem.getValue();
            if (cellData != null && (filter == null || filter.test(cellData)))
                return cellData;
            for (TreeItem<E> child: treeItem.getChildren())
            {
                E childValue = getCell(filter, child);
                if (childValue != null)
                    return childValue;
            }
        }
        return null;
    }
    
    private ArrayList<E> addTo(Predicate<E> filter, TreeItem<E> treeItem, ArrayList<E> list)
    {
        if (treeItem != null)
        {
            E cellData = treeItem.getValue();
            if (cellData != null && !cellData.isFolder() && (filter == null || filter.test(cellData)))
                list.add(cellData);
            for (TreeItem<E> child: treeItem.getChildren())
                addTo(filter, child, list);
        }
        return list;
    }
    
    // Clear Folders Helpers
    
    private void clearEmptyFolders()
    {
        while (clearEmptyFolders(rootItem) > 0)
            TB.general().sleepLoop();
    }
    
    private int clearEmptyFolders(TreeItem<E> item)
    {
        ArrayList<TreeItem<E>> toRemove = new ArrayList<>();
        int totalRemoved = 0;
        for (TreeItem<E> child: item.getChildren())
            if (child.getValue().isFolder())
                if (child.getChildren().isEmpty())
                    toRemove.add(child);
                else
                    totalRemoved += clearEmptyFolders(child);
        if (item.getChildren().removeAll(toRemove))
            totalRemoved += toRemove.size();
        return totalRemoved;
    }
    
    /**
     * Clears away all parent folders that only have a single child until a parent folder with more than one child is found.
     */
    private void clearUnnecessaryFolders()
    {
        clearUnnecessaryFolders(rootItem);
    }
    
    /**
     * A recursive helper method used by {@link #clearUnnecessaryFolders()}.
     *
     * @param newRootItem The {@link TreeItem}.
     */
    private void clearUnnecessaryFolders(TreeItemFX<E> newRootItem)
    {
        if (newRootItem.getChildren().size() == 1)
        {
            TreeItemFX<E> _childItem = (TreeItemFX) newRootItem.getChildren().get(0);
            rootItem = newRootItem;
            clearUnnecessaryFolders(_childItem);
            initializeRoot();
        }
    }
    
    // Get/Put Folder Helpers
    
    private TreeItemFX<E> getFolderFor(E element)
    {
        return element.getParentName().equalsIgnoreCase(rootName) ? rootItem : folders.get(element.getParentName());
    }
    
    /**
     * Puts the specified folder element into the {@link HashMap} of folders, creates a new {@link TreeItem} folder, and then returns the folder.
     *
     * @param element The element.
     * @return The {@link TreeItem} folder that was created by this method.
     */
    private TreeItemFX<E> putFolder(E element)
    {
        TreeItemFX<E> item = new TreeItemFX<>(element);
        folders.put(element.getName(), item);
        return item;
    }
    
    // Misc. Helpers
    
    private void applyCellFactory()
    {
        // TODO [S]: Make sure resources are cleared when the cell is destroyed.
        // NOTE [S]: This might not be applicable for CreationTreeHandlers?
        Function<E, C> _controllerSupplier = getControllerSupplier();
        treeView.setCellFactory(_treeView -> new TreeCellFX<>(
                _wrappedIndexedCell -> new WrappingCell<>(
                        _treeView,
                        _wrappedIndexedCell,
                        _cellData -> TB.resources().get(
                                _cellData,
                                () -> _controllerSupplier.apply(_cellData), // CHANGE-HERE
                                _treeView.hashCode()
                        )
                )));
        
//        treeView.setCellFactory(_treeView -> new TreeCellFX<>(
//                _wrappedIndexedCell -> new WrappingCell<>(
//                        _treeView, _wrappedIndexedCell, _cellData -> TB.resources().get(
//                        _cellData, () -> {
//                            C _controller = _controllerSupplier.apply(_cellData);
//                        }, _treeView.hashCode()))));
    }
    
    //</editor-fold>
    
    //	//<editor-fold desc="Static"> // CHANGE-HERE
    //
    //	public static <T> Function<Object[], T> toLoadableValue(Class<T> clazz) {
    //		return objs -> GeneralTools.newInstance(clazz, objs); // NOTE [S]: Issues with lambda serialization.
    //	}
    //
    //	public static <T extends Copyable<T>> Function<Object[], T> toLoadableValue(T copyable) {
    //		return objs -> copyable.newCopy(objs); // NOTE [S]: Issues with lambda serialization.
    //	}
    //
    //	//</editor-fold>
    
    // Generate Cell
    
    public E generateCell(String folder, Function<Object[], T> provider)
    {
        return generateCell(null, folder, provider, null, false);
    }
    
    public E generateCell(String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier)
    {
        return generateCell(null, folder, provider, settingsApplier, false);
    }
    
    // TODO [S]: Figure out why there is a validator parameter and a validator instance variable.
    // TODO [S]: The name of a cell should only be a thing for the CreationTreeHandler.
    
    public E generateCell(String name, String folder, Function<Object[], T> provider)
    {
        return generateCell(name, folder, provider, null, false);
    }
    
    public E generateCell(String name, String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier)
    {
        return generateCell(name, folder, provider, settingsApplier, false);
    }
    
    public E generateFolderCell(String name, String folder, Consumer<TreeItem<E>> settingsApplier)
    {
        return generateCell(name, folder, null, settingsApplier, true);
    }
    
    private E generateCell(String name, String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier, boolean isFolder)
    {
        // TODO [S]: Add synchronization here?
        if (!isFolder)
        {
            // If isFolder is false, ensure that there is a non-null TreeItemValueProvider specified.
            ExceptionTools.nullCheck(provider, "Provider", "Provider cannot be null when isFolder is false");
            //			Validatable<T> _validator = getValidator(); // Create temp variable in case the validator is ever turned into a property.
            //			if (_validator != null) {
            //				// If the TreeItemValidator is non-null, validate an instance returned by the specified TreeItemValueProvider.
            //				T _instance = provider.apply(new Object[0]); // TODO [S]: Figure out what creation parameters aren't necessary here.
            //				if (!_validator.validate(_instance))
            //					// If the instance returned by the specified TreeItemValueProvider is not valid, return null.
            //					return null;
            //			}
        }
        
        E _cellData = createTreeCellData(name, folder, isFolder, provider);
        //TODO [S]: Figure out what happens when the root hasn't been set.
        if (hasSetRoot())
            addCellData(_cellData, settingsApplier);
        return _cellData;
    }
    
    // Get Item
    
    public final T getItem(Predicate<T> filter, Object... objs)
    {
        return getItem(filter, rootItem, objs);
    }
    
    public final ArrayList<T> getAllItems(Object... objs)
    {
        return getAllItems(null, objs);
    }
    
    public final ArrayList<T> getAllItems(Predicate<T> filter, Object... objs)
    {
        return addTo(filter, rootItem, new ArrayList<>(), objs);
    }
    
    public T getItemByClass(String className, Object... objs)
    {
        E _cellData = getCellByClass(className);
        if (_cellData != null)
            return _cellData.createWrappedInstance(objs);
        return null;
    }
    
    // Get Cell
    
    public final E getCell(Predicate<E> filter)
    {
        return getCell(filter, rootItem);
    }
    
    public final ArrayList<E> getAllCells()
    {
        return getAllCells(null);
    }
    
    public final ArrayList<E> getAllCells(Predicate<E> filter)
    {
        return addTo(filter, rootItem, new ArrayList<>());
    }
    
    public E getCellByClass(String className)
    {
        return getCell(_cell -> {
            Class _class = _cell.getWrappedClass();
            return _class != null && _class.getSimpleName().equalsIgnoreCase(className);
        });
    }
    
    // Misc.
    
    public boolean addCellData(E cellData, Consumer<TreeItem<E>> settingsApplier)
    {
        // TODO [S]: Add synchronization here?
        if (cellData != null)
        {
            TreeItemFX<E> _treeItem;
            if (!cellData.isFolder())
            {
                _treeItem = new TreeItemFX<>(cellData);
                if (settingsApplier != null)
                    settingsApplier.accept(_treeItem);
            }
            else
                _treeItem = putFolder(cellData);
            try
            {
                boolean _added = getFolderFor(cellData).getChildren().add(_treeItem);
                items.add(_treeItem);
                revalidate(_treeItem);
                return _added;
            }
            catch (Exception e)
            {
                throw ExceptionTools.ex(e, cellData.getParentName() + " has not yet been added as a parent.");
            }
        }
        return false;
    }
    
    public void revalidate()
    {
        FXTools.get().runFX(() -> items.forEach(this::revalidate), true);
    }
    
    private void revalidate(TreeItemFX<E> item)
    {
        if (validator != null)
        {
            E _cellData = item.getValue();
            if (_cellData != null)
            {
                boolean _valid = validator.validate(_cellData.getWrappedObject());
                item.setVisible(_valid);
            }
        }
    }
    
    // Abstract
    
    protected abstract E createTreeCellData(String name, String parentName, boolean isFolder, Function<Object[], T> provider);
    
    // Old
    
    //<editor-fold desc="Old">
    
    //	/**
    //	 * Generates a new {@link TreeCellData}, checks the generated element's validity, and then adds it to the specified {@link TreeLoader} if the element is valid.
    //	 *
    //	 * @param <T>            The type of element contained within the {@link TreeCellData}.
    //	 * @param <T>            The type of {@link TreeItemValidator}.
    //	 * @param parentName     The parent name.
    //	 * @param loader         The {@link TreeLoader} that is doing the loading.
    //	 * @param validator      The {@link TreeItemValidator} that is validating each element being added to the {@link TreeView}.
    //	 * @param treeItemLoader The {@link TreeItemSettingsApplier} that is being used to apply any needed settings to the generated {@link TreeCell}. Null if no settings need to be applied.
    //	 * @return A generated {@link TreeCellData} given the specified values.
    //	 */
    //	public E generateCell(TreeLoader appendingLoader, String parentName, T validator, TreeItemSettingsApplier<T> treeItemLoader) {
    //		final TreeItem<E> tempRoot = appendingLoader.getRootItem();
    //		final E data = tempRoot.getValue();
    //		if (hasSetRoot())
    //			addCellData(data, treeItemLoader);
    //		return data;
    //	} // TODO [S]: Figure out what this is.
    
    //</editor-fold>
}


/*
 * TODO LIST:
 * [S] Make as many properties observable as possible. e.g.:
 *     [S] folders - HashMap
 *     [S] root - TreeItem
 *     [S] showRoot - boolean
 *     [S] etc.
 * [S] Make a method that retrieves a (String?) representation of the tree's structure.
 *     [S] Most likely going to need to make an information wrapper class for this that keeps track of its children.
 * [S] Write another way of retrieving elements specifically for WrappingTreeHandlers.
 *     [S] UID?
 *     [S] Nameable?
 * [S] Potentially have a mirrored data structure that allows for elements to be retrieved more easily based on a key?
 *     [S] At least for CreationTreeHandlers, I'm thinking nested ObservableHashMaps that uses the TreeCellData names as keys?
 * [S] Potentially make custom Iterable?
 * [S] Remove items from observable list when they are removed from the TreeView.
 * [S] Make validation process internal and automated using properties.
 *     [S] Maybe also keep the revalidate() method for things that don't use properties?
 */