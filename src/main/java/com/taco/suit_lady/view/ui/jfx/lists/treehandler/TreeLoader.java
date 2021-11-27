package com.taco.suit_lady.view.ui.jfx.lists.treehandler;

import com.taco.suit_lady.util.Validatable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.view.ui.jfx.lists.TreeCellFX;
import com.taco.suit_lady.view.ui.ui_internal.controllers.CellController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
    
    static {
        DEFAULT_ROOT_NAME = "Root";
    }
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private final TreeView<E> treeView; // TODO [S]: Turn into list of TreeViews?
    private TreeItemFX<E> rootItem; // TODO [S]: Turn into property?
    
    private final ReadOnlyMapWrapper<String, TreeItemFX<E>> folders; // TODO [S]: Make observable.
    private final ReadOnlyListWrapper<TreeItemFX<E>> items;
    
    private final ReadOnlyObjectWrapper<Function<E, C>> cellControllerFactoryProperty;
    
    private final Validatable<T> validator; // TODO [S]: Change into property, then reload the TreeView when the property value changes?
    
    private final String rootName;
    private boolean isFinishedConstructing;
    
    public TreeLoader(TreeView<E> treeView)
    {
        this(treeView, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory)
    {
        this(treeView, cellControllerFactory, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory, Validatable<T> validator)
    {
        this(treeView, cellControllerFactory, validator, null);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory, Validatable<T> validator, String rootName)
    {
        this(treeView, cellControllerFactory, validator, null, rootName);
    }
    
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier, Validatable<T> validator, Consumer<TreeItem<E>> settingsApplier, String rootName)
    {
        this.lock = new ReentrantLock();
        
        this.isFinishedConstructing = false;
        
        this.treeView = treeView;
        
        this.folders = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
        this.items = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.cellControllerFactoryProperty = new ReadOnlyObjectWrapper<>(controllerSupplier);
        
        this.validator = validator;
        
        this.rootName = rootName != null ? rootName : TreeLoader.DEFAULT_ROOT_NAME;
        this.rootItem = new TreeItemFX<>(generateFolderCell(this.rootName, null, settingsApplier));
        
        this.rootItem.setExpanded(true);
        this.isFinishedConstructing = true;
    }
    
    //<editor-fold desc="Properties">
    
    /**
     * <p>Returns the {@link TreeView} loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link TreeView} loaded by this {@link TreeLoader}.
     */
    public final TreeView<E> getTreeView()
    {
        return treeView;
    }
    
    /**
     * <p>Returns the {@link TreeItemFX} set as the {@link TreeView#rootProperty() root} of the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link TreeItemFX} set as the {@link TreeView#rootProperty() root} of the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.
     */
    public final TreeItemFX<E> getRootItem()
    {
        return rootItem;
    }
    
    /**
     * <p>Returns whether this {@link TreeLoader} has finished {@link #TreeLoader(TreeView, Function, Validatable, Consumer, String) construction} or not.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Used to ensure this {@link TreeLoader} is not asynchronously accessed prior to being fully {@link #TreeLoader(TreeView, Function, Validatable, Consumer, String) constructed}.</li>
     *     <li>TODO - Eventually, the functionality of this method should be done via actual synchronization.</li>
     * </ol>
     *
     * @return True if this {@link TreeLoader} has finished {@link TreeLoader#TreeLoader(TreeView, Function, Validatable, Consumer, String) construction}, false if it has not.
     */
    private boolean isFinishedConstructing()
    {
        return isFinishedConstructing;
    }
    
    /**
     * <p>Returns the {@link ReadOnlyMapProperty Map} containing the {@link TreeItemFX folders} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link TreeCellData#getName() name} of the {@link TreeCellData} assigned to the {@link TreeItemFX folder} is the {@link String key} used to {@link ReadOnlyMapWrapper#put(Object, Object) put} the {@link TreeItemFX folder} in the {@link ReadOnlyMapProperty Folders Map}.</li>
     * </ol>
     *
     * @return The {@link ReadOnlyMapProperty Map} containing the {@link TreeItemFX folders} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.
     */
    public final ReadOnlyMapProperty<String, TreeItemFX<E>> folders()
    {
        return folders.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyListProperty List} containing the {@link TreeItemFX items} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link ReadOnlyListProperty List} containing the {@link TreeItemFX items} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.
     */
    public final ReadOnlyListProperty<TreeItemFX<E>> items()
    {
        return items.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Validatable validator} used to ensure all {@link TreeItemFX items} added to the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader} adhere to the conditions set by the {@link Validatable validator}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link Validatable validator} is automatically called when a new {@link TreeItemFX item} is {@link #addCellData(TreeCellData, Consumer)  added} to the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</li>
     *     <li>The primary usage of the {@link Validatable validator} is located in the <code><i>{@link #revalidate(TreeItemFX)}</i></code> method.</li>
     * </ol>
     *
     * @return The {@link Validatable validator} used to ensure all {@link TreeItemFX items} added to the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader} adhere to the conditions set by the {@link Validatable validator}.
     */
    public final Validatable<T> getValidator()
    {
        return validator;
    }
    
    public final ReadOnlyObjectProperty<Function<E, C>> cellControllerFactoryProperty()
    {
        return cellControllerFactoryProperty.getReadOnlyProperty();
    }
    
    public final Function<E, C> getCellControllerFactory()
    {
        return cellControllerFactoryProperty.get();
    }
    
    public final void setCellControllerFactory(Function<E, C> controllerSupplier)
    {
        cellControllerFactoryProperty.set(controllerSupplier);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Initialize">
    
    public void initialize()
    {
        initializeRoot();
        apply();
        applyCellFactory();
    }
    
    public <Z extends TreeLoader<E, T, C>> Z initializeAndGet()
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
    
    public final T getObj(Predicate<T> filter, Object... objs)
    {
        return getObj(filter, rootItem, objs);
    }
    
    /**
     * <p>Returns the first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link ArrayTools}<b>.</b>{@link ArrayTools#getAt(int, List) getAt}<b>(</b><u>{@code 0}</u><b>,</b> {@link #getObjs(Predicate, TreeItem, ArrayList, int, Object...) getObjs}<b>(</b>filter<b>,</b> treeItem<b>,</b> <u>{@code null}</u><b>,</b> <u>{@code 1}</u><b>,</b> wrappedObjConstructorParams<b>))</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param treeItem                    The {@link TreeItem} to be used as the starting point of the iteration.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return The first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.
     */
    public T getObj(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable Object... wrappedObjConstructorParams)
    {
        return ArrayTools.getAt(0, getObjs(filter, treeItem, null, 1, wrappedObjConstructorParams));
    }
    
    //
    
    /**
     * <p>Returns all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #getObjs(Predicate, Object...) getObjs}<b>(</b><u>{@code null}</u><b>,</b> wrappedObjConstructorParams<b>)</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return All {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that match the specified {@link Predicate filter}.
     */
    public final ArrayList<T> getAllObjs(Object... wrappedObjConstructorParams)
    {
        return getObjs(null, wrappedObjConstructorParams);
    }
    
    /**
     * <p>Returns all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that match the specified {@link Predicate filter}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #getObjs(Predicate, TreeItem, ArrayList, Object...) getObjs}<b>(</b>filter<b>,</b> <u>{@link #getRootItem()}</u><b>,</b> <u>{@code null}</u><b>,</b> wrappedObjConstructorParams<b>)</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return All {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that match the specified {@link Predicate filter}.
     */
    public final ArrayList<T> getObjs(Predicate<T> filter, Object... wrappedObjConstructorParams)
    {
        return getObjs(filter, getRootItem(), null, wrappedObjConstructorParams);
    }
    
    /**
     * <p>Returns all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that match the specified {@link Predicate filter}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #getObjs(Predicate, TreeItem, ArrayList, int, Object...) getObjs}<b>(</b>filter<b>,</b> treeItem<b>,</b> list<b>,</b> <u>{@code -1}</u><b>,</b> wrappedObjConstructorParams<b>)</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param treeItem                    The {@link TreeItem} to be used as the starting point of the iteration.
     * @param list                        The {@link ArrayList} the {@link Predicate filtered} {@link TreeCellData#getWrappedObject() Values} are added to.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return All {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that match the specified {@link Predicate filter}.
     */
    public @NotNull ArrayList<T> getObjs(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable ArrayList<T> list, @Nullable Object... wrappedObjConstructorParams)
    {
        return getObjs(filter, treeItem, list, -1, wrappedObjConstructorParams);
    }
    
    /**
     * <p>Returns all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that match the specified {@link Predicate filter}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Each tested {@link T Wrapped Value} is retrieved by <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code>.</li>
     *     <li>Only the {@link TreeItem#getChildren() children} of the specified {@link TreeItem} -- as well as the {@link TreeItem item} itself -- are {@link Predicate#test(Object) tested}.</li>
     *     <li>If {@code wrappedObjConstructorParams} is {@code null}, an empty array is used instead.</li>
     *     <li>If the specified {@link Predicate filter} is {@code null}, <code><i>wrappedInstance -> true</i></code> is used instead.</li>
     *     <li>If the specified {@link TreeItem item} is {@code null}, {@link #getObj(Predicate, TreeItem, Object...) this method} does nothing and silently returns {@code null}.</li>
     *     <li>
     *         As soon as the {@link ArrayList} is greater than or equal to the specified {@code maxSize}, this method returns the {@link ArrayList list}.
     *         <ul>
     *             <li><i>This means if the specified {@link ArrayList} is already {@link ArrayList#size() larger} than the specified {@code maxSize}, {@link #getObjs(Predicate, TreeItem, ArrayList, int, Object...) this method} returns immediately.</i></li>
     *         </ul>
     *     </li>
     *     <li>If the specified {@code maxSize} is {@code -1}, the {@link ArrayList#size() size} limit is ignored.</li>
     * </ol>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param treeItem                    The {@link TreeItem} to be used as the starting point of the iteration.
     * @param list                        The {@link ArrayList} the {@link Predicate filtered} {@link TreeCellData#getWrappedObject() Values} are added to.
     * @param maxSize                     The maximum {@link ArrayList#size() size} of the returned {@link ArrayList}
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return The specified {@link ArrayList} with all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} that match the specified {@link Predicate filter} {@link ArrayList#add(Object) added} to it.
     */
    public @NotNull ArrayList<T> getObjs(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable ArrayList<T> list, int maxSize, @Nullable Object... wrappedObjConstructorParams)
    {
        final ArrayList<T> tempList = list != null ? list : new ArrayList<>();
        final Predicate<T> tempFilter = Objects.requireNonNullElseGet(filter, () -> (wrappedInstance -> true));
        final Object[] tempObjParams = wrappedObjConstructorParams != null ? wrappedObjConstructorParams : new Object[0];
        
        if (treeItem == null)
            return tempList;
        
        if (testTreeItem(maxSize, tempList, tempFilter, treeItem, tempObjParams))
            return tempList;
        
        for (TreeItem<E> childTreeItem: treeItem.getChildren())
            if (testTreeItem(maxSize, tempList, tempFilter, childTreeItem, tempObjParams))
                return tempList;
        
        return tempList;
    }
    
    private boolean testTreeItem(int maxSize, @NotNull ArrayList<T> list, @NotNull Predicate<T> filter, @NotNull TreeItem<E> treeItem, @NotNull Object[] wrappedObjConstructorParams)
    {
        final E childCell = treeItem.getValue();
        if (childCell != null) {
            final T wrappedInstance = childCell.createWrappedInstance(wrappedObjConstructorParams);
            if (wrappedInstance != null && filter.test(wrappedInstance))
                list.add(wrappedInstance);
        }
        
        // Set to >= instead of == in case the specified ArrayList is already over the limit.
        return maxSize != -1 && list.size() >= maxSize;
    }
    
    //
    
    private ArrayList<E> getCells(Predicate<E> filter, TreeItem<E> treeItem, ArrayList<E> list)
    {
        if (treeItem != null) {
            final E cellData = treeItem.getValue();
            if (cellData != null && !cellData.isFolder() && (filter == null || filter.test(cellData)))
                list.add(cellData);
            for (TreeItem<E> child: treeItem.getChildren())
                getCells(filter, child, list);
        }
        return list;
    }
    
    private E getCell(Predicate<E> filter, TreeItem<E> treeItem)
    {
        if (treeItem != null) {
            final E cellData = treeItem.getValue();
            if (cellData != null && (filter == null || filter.test(cellData)))
                return cellData;
            for (TreeItem<E> child: treeItem.getChildren()) {
                final E childValue = getCell(filter, child);
                if (childValue != null)
                    return childValue;
            }
        }
        return null;
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
        if (newRootItem.getChildren().size() == 1) {
            final TreeItemFX<E> childItemFX = (TreeItemFX<E>) newRootItem.getChildren().get(0);
            rootItem = newRootItem;
            clearUnnecessaryFolders(childItemFX);
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
     *
     * @return The {@link TreeItem} folder that was created by this method.
     */
    private TreeItemFX<E> putFolder(E element)
    {
        final TreeItemFX<E> item = new TreeItemFX<>(element);
        folders.put(element.getName(), item);
        return item;
    }
    
    // Misc. Helpers
    
    private void applyCellFactory()
    {
        // TODO [S]: Make sure resources are cleared when the cell is destroyed.
        // NOTE [S]: This might not be applicable for CreationTreeHandlers?
        final Function<E, C> controllerSupplier = getCellControllerFactory();
        treeView.setCellFactory(treeView -> new TreeCellFX<>(
                treeCellFX -> new CellControlManager<>(
                        treeView,
                        treeCellFX,
                        cellData -> TB.resources().get(
                                cellData,
                                () -> controllerSupplier.apply(cellData), // CHANGE-HERE
                                treeView.hashCode()
                        )
                )));
    }
    
    //</editor-fold>
    
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
    
    /**
     * <p>Generates a new {@link TreeCellData} instance of implementation-type {@link E}.</p>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <b>Name:</b> {@literal String}
     *         <ol>
     *             <li>The {@code name} of the generated {@link TreeCellData} object.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Folder:</b> String
     *         <ol>
     *             <li>The {@code name} of the {@link #folders() folder} the generated {@link TreeCellData} object is added to.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Provider:</b> {@literal Function<Object[], T>}
     *         <ol>
     *             <li>The {@link Function} used to provide the {@link T object} contained in the generated {@link TreeCellData} object.</li>
     *             <li>An optional {@link Object} {@link Array} can be used to provide additional parameter data that is required to retrieve the {@link T object}.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Settings Applier:</b> {@literal Consumer<TreeItem<E>>}
     *         <ol>
     *             <li>An optional {@link Consumer} that allows additional operations to be performed on the {@link TreeItem} for the generated {@link TreeCellData} object.</li>
     *             <li>If the {@link Consumer} is {@code null}, no additional operations will be performed.</li>
     *         </ol>
     *     </li>
     *     <li>
     *         <b>Is Folder:</b> {@literal boolean}</b>
     *         <ol>
     *             <li>Indicates whether the generated {@link TreeCellData} object represents a {@code folder} or a standard {@code element}.</li>
     *         </ol>
     *     </li>
     * </ol>
     *
     * @param name
     * @param folder
     * @param provider
     * @param settingsApplier
     * @param isFolder
     *
     * @return
     */
    private E generateCell(String name, String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier, boolean isFolder)
    {
        // TODO [S]: Add synchronization here?
        if (!isFolder) {
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
        
        final E cellData = createTreeCellData(name, folder, isFolder, provider);
        //TODO [S]: Figure out what happens when the root hasn't been set.
        if (isFinishedConstructing())
            addCellData(cellData, settingsApplier);
        return cellData;
    }
    
    // Get Item
    
    // Get Cell
    
    /**
     * <p>Returns the first {@link TreeCellData} contained within this {@link TreeLoader} that matches the specified {@link Predicate filter}.</p>
     *
     * @param filter The {@link Predicate} used to filter the {@link TreeCellData} objects within this {@link TreeLoader}.
     *
     * @return The first {@link TreeCellData} contained within this {@link TreeLoader} that matches the specified {@link Predicate filter}.
     */
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
        return getCells(filter, rootItem, new ArrayList<>());
    }
    
    public E getCellByClass(String className)
    {
        return getCell(cell -> {
            final Class<T> clazz = cell.getWrappedClass();
            return clazz != null && clazz.getSimpleName().equalsIgnoreCase(className);
        });
    }
    
    // Misc.
    
    public boolean addCellData(E cellData, Consumer<TreeItem<E>> settingsApplier)
    {
        // TODO [S]: Add synchronization here?
        if (cellData != null) {
            TreeItemFX<E> treeItem;
            if (!cellData.isFolder()) {
                treeItem = new TreeItemFX<>(cellData);
                if (settingsApplier != null)
                    settingsApplier.accept(treeItem);
            } else
                treeItem = putFolder(cellData);
            try {
                final boolean added = getFolderFor(cellData).getChildren().add(treeItem);
                items.add(revalidate(treeItem));
                return added;
            } catch (Exception e) {
                throw ExceptionTools.ex(e, cellData.getParentName() + " has not yet been added as a parent.");
            }
        }
        return false;
    }
    
    public void revalidate()
    {
        FXTools.get().runFX(() -> items.forEach(this::revalidate), true);
    }
    
    private TreeItemFX<E> revalidate(TreeItemFX<E> item)
    {
        final Validatable<T> tempValidator = getValidator();
        if (tempValidator != null) {
            final E cellData = item.getValue();
            if (cellData != null)
                item.setVisible(tempValidator.validate(cellData.getWrappedObject()));
        }
        return item;
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