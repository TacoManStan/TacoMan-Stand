package com.taco.suit_lady.ui.jfx.lists.treehandler;

import com.taco.suit_lady.ui.jfx.lists.CellControlManager;
import com.taco.suit_lady.ui.jfx.lists.TreeCellFX;
import com.taco.suit_lady.util.Validatable;
import com.taco.suit_lady.util.tools.SLArrays;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.TB;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
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
 * TO-EXPAND
 *
 * @param <E> The type of {@link TreeCellData} objects contained within the {@link TreeView} managed by this {@code TreeLoader}.
 * @param <T> The type of elements wrapped by the {@code TreeCellData} objects contained within the {@code TreeView} managed by this {@code TreeLoader}.
 * @param <C> The type of {@link CellController} used for each {@link TreeCell} of the {@code TreeView} managed by this {@code TreeLoader}.
 */
public abstract class TreeLoader<E extends TreeCellData<T>, T, C extends CellController<E>>
        implements Serializable {
    //<editor-fold desc="--- STATIC ---">
    
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
    
    // TO-DOC
    public TreeLoader(TreeView<E> treeView) {
        this(treeView, null);
    }
    
    // TO-DOC
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory) {
        this(treeView, cellControllerFactory, null);
    }
    
    // TO-DOC
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory, Validatable<T> validator) {
        this(treeView, cellControllerFactory, validator, null);
    }
    
    // TO-DOC
    public TreeLoader(TreeView<E> treeView, Function<E, C> cellControllerFactory, Validatable<T> validator, String rootName) {
        this(treeView, cellControllerFactory, validator, null, rootName);
    }
    
    // TO-DOC
    public TreeLoader(TreeView<E> treeView, Function<E, C> controllerSupplier, Validatable<T> validator, Consumer<TreeItem<E>> settingsApplier, String rootName) {
        this.lock = new ReentrantLock();
        
        this.isFinishedConstructing = false;
        
        this.treeView = treeView;
        
        this.folders = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
        this.items = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.cellControllerFactoryProperty = new ReadOnlyObjectWrapper<>(controllerSupplier);
        
        this.validator = validator;
        
        this.rootName = rootName != null ? rootName : TreeLoader.DEFAULT_ROOT_NAME;
        this.rootItem = new TreeItemFX<>(generateFolderCellData(this.rootName, null, settingsApplier));
        
        this.rootItem.setExpanded(true);
        this.isFinishedConstructing = true;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link TreeView} loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link TreeView} loaded by this {@link TreeLoader}.
     */
    public final TreeView<E> getTreeView() {
        return treeView;
    }
    
    /**
     * <p>Returns the {@link TreeItemFX} set as the {@link TreeView#rootProperty() root} of the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link TreeItemFX} set as the {@link TreeView#rootProperty() root} of the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.
     */
    public final TreeItemFX<E> getRootItem() {
        return rootItem;
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
    public final ReadOnlyMapProperty<String, TreeItemFX<E>> folders() {
        return folders.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link ReadOnlyListProperty List} containing the {@link TreeItemFX items} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.</p>
     *
     * @return The {@link ReadOnlyListProperty List} containing the {@link TreeItemFX items} in the {@link #getTreeView() TreeView} that is loaded by this {@link TreeLoader}.
     */
    public final ReadOnlyListProperty<TreeItemFX<E>> items() {
        return items.getReadOnlyProperty();
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
    private boolean isFinishedConstructing() {
        return isFinishedConstructing;
    }
    
    /**
     * <p>Returns the {@link ObjectProperty} containing the {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input.</p>
     *
     * @return The {@link ObjectProperty} containing the {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input.
     */
    public final ReadOnlyObjectProperty<Function<E, C>> cellControllerFactoryProperty() {
        return cellControllerFactoryProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input.</p>
     *
     * @return The {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input.
     */
    public final Function<E, C> getCellControllerFactory() {
        return cellControllerFactoryProperty.get();
    }
    
    /**
     * <p>Sets the {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input to the specified {@code value}./p>
     *
     * @param controllerSupplier The new {@link Function} to be set as the {@link Function} used to construct a {@link CellController} for a provided {@link TreeCellData} input
     */
    public final void setCellControllerFactory(Function<E, C> controllerSupplier) {
        cellControllerFactoryProperty.set(controllerSupplier);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    // TO-DOC
    public void initialize() {
        treeView.setRoot(rootItem);
        
        while (clearEmptyFolders(rootItem) > 0)
            TB.general().sleepLoop();
        
        applyCellFactory();
    }
    
    // TO-DOC
    public <Z extends TreeLoader<E, T, C>> Z initializeAndGet() {
        initialize();
        return (Z) this;
    }
    
    //
    
    /**
     * <p>Recursively clears all empty folders, starting from the specified {@link TreeItem}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Used in {@link TreeLoader} {@link #initialize() initialization}.</li>
     * </ol>
     *
     * @param item The {@link TreeItem} to iterate from.
     *
     * @return The total number of {@link TreeItem TreeItems} removed, including those removed by recursive calls.
     */
    private int clearEmptyFolders(TreeItem<E> item) {
        final ArrayList<TreeItem<E>> toRemove = new ArrayList<>();
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
    
    // TO-DOC
    private void applyCellFactory() {
        // TODO [S]: Make sure resources are cleared when the cell is destroyed.
        // NOTE [S]: This might not be applicable for CreationTreeHandlers?
        final Function<E, C> controllerSupplier = getCellControllerFactory();
        treeView.setCellFactory(treeView -> new TreeCellFX<>(
                treeCellFX -> new CellControlManager<>(
                        treeCellFX,
                        cellData -> TB.resources().get(
                                cellData,
                                () -> controllerSupplier.apply(cellData), // CHANGE-HERE
                                treeView.hashCode()))));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GET OBJS ---">
    
    /**
     * <p>Returns the first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #getObj(Predicate, TreeItem, Object...) getObj}<b>(</b>filter<b>,</b> {@link #getRootItem()}<b>,</b> wrappedObjConstructorParams<b>)</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return The first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.
     */
    public final T getObj(Predicate<T> filter, Object... wrappedObjConstructorParams) {
        return getObj(filter, getRootItem(), wrappedObjConstructorParams);
    }
    
    /**
     * <p>Returns the first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link SLArrays}<b>.</b>{@link SLArrays#getAt(int, List) getAt}<b>(</b><u>{@code 0}</u><b>,</b> {@link #getObjs(Predicate, TreeItem, ArrayList, int, Object...) getObjs}<b>(</b>filter<b>,</b> treeItem<b>,</b> <u>{@code null}</u><b>,</b> <u>{@code 1}</u><b>,</b> wrappedObjConstructorParams<b>))</b>
     * </code></i></blockquote>
     *
     * @param filter                      The {@link Predicate filter} used to filter through the {@link TreeItemFX items} in this {@link TreeLoader}.
     * @param treeItem                    The {@link TreeItem} to be used as the starting point of the iteration.
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return The first {@link TreeCellData#getWrappedObject() Wrapped Value} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the specified {@link TreeItem} as the iteration {@code root} -- that matches the specified {@link Predicate filter}.
     */
    public T getObj(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable Object... wrappedObjConstructorParams) {
        return SLArrays.getAt(0, getObjs(filter, treeItem, null, 1, wrappedObjConstructorParams));
    }
    
    /**
     * <p>Returns all {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #getObjs(Predicate, Object...) getObjs}<b>(</b><u>{@code null}</u><b>,</b> wrappedObjConstructorParams<b>)</b>
     * </code></i></blockquote>
     *
     * @param wrappedObjConstructorParams Passed to the <code><i>{@link TreeCellData#createWrappedInstance(Object...)}</i></code> as the optional parameters to use upon {@link T item} construction.
     *
     * @return All {@link TreeCellData#getWrappedObject() Wrapped Values} in the {@link TreeView} loaded by this {@link TreeLoader} -- starting with the {@link #getRootItem() Root Item} as the iteration {@code root} -- that match the specified {@link Predicate filter}.
     */
    public final ArrayList<T> getAllObjs(Object... wrappedObjConstructorParams) {
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
    public final ArrayList<T> getObjs(Predicate<T> filter, Object... wrappedObjConstructorParams) {
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
    public @NotNull ArrayList<T> getObjs(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable ArrayList<T> list, @Nullable Object... wrappedObjConstructorParams) {
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
    public @NotNull ArrayList<T> getObjs(@Nullable Predicate<T> filter, @Nullable TreeItem<E> treeItem, @Nullable ArrayList<T> list, int maxSize, @Nullable Object... wrappedObjConstructorParams) {
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
    
    //
    
    // TO-DOC
    private boolean testTreeItem(int maxSize, @NotNull ArrayList<T> list, @NotNull Predicate<T> filter, @NotNull TreeItem<E> treeItem, @NotNull Object[] wrappedObjConstructorParams) {
        final E childCell = treeItem.getValue();
        if (childCell != null) {
            final T wrappedInstance = childCell.createWrappedInstance(wrappedObjConstructorParams);
            if (wrappedInstance != null && filter.test(wrappedInstance))
                list.add(wrappedInstance);
        }
        
        // Set to >= instead of == in case the specified ArrayList is already over the limit.
        return maxSize != -1 && list.size() >= maxSize;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CELL DATA ---">
    
    //TO-DOC
    protected abstract E createTreeCellData(String name, String parentName, boolean isFolder, Function<Object[], T> provider);
    
    //TO-DOC
    public E generateCellData(String folder, Function<Object[], T> provider) {
        return generateCellData(null, folder, provider, null, false);
    }
    
    //TO-DOC
    public E generateCellData(String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier) {
        return generateCellData(null, folder, provider, settingsApplier, false);
    }
    
    //TO-DOC
    public E generateCellData(String name, String folder, Function<Object[], T> provider) {
        return generateCellData(name, folder, provider, null, false);
    }
    
    //TO-DOC
    public E generateCellData(String name, String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier) {
        return generateCellData(name, folder, provider, settingsApplier, false);
    }
    
    //TO-DOC
    public E generateFolderCellData(String name, String folder, Consumer<TreeItem<E>> settingsApplier) {
        return generateCellData(name, folder, null, settingsApplier, true);
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
     *         <b>Is Folder:</b> {@literal boolean}
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
    // TO-EXPAND
    private E generateCellData(String name, String folder, Function<Object[], T> provider, Consumer<TreeItem<E>> settingsApplier, boolean isFolder) {
        // TODO [S]: Add synchronization here?
        if (!isFolder) {
            // If isFolder is false, ensure that there is a non-null TreeItemValueProvider specified.
            SLExceptions.nullCheck(provider, "Provider", "Provider cannot be null when isFolder is false");
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
    
    //
    
    //TO-DOC
    private boolean addCellData(E cellData, Consumer<TreeItem<E>> settingsApplier) {
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
                throw SLExceptions.ex(e, cellData.getParentName() + " has not yet been added as a parent.");
            }
        }
        return false;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- FOLDERS ---">
    
    //TO-DOC
    public TreeItemFX<E> getFolderFor(E element) {
        final String parentName = SLExceptions.nullCheck(SLExceptions.nullCheck(element, "Element").getParentName(), "Parent Name");
        return parentName.equalsIgnoreCase(rootName) ? rootItem : folders.get(element.getParentName());
    }
    
    /**
     * Puts the specified folder element into the {@link HashMap} of folders, creates a new {@link TreeItem} folder, and then returns the folder.
     *
     * @param element The element.
     *
     * @return The {@link TreeItem} folder that was created by this method.
     */
    // TO-EXPAND
    private TreeItemFX<E> putFolder(E element) {
        final TreeItemFX<E> item = new TreeItemFX<>(element);
        folders.put(element.getName(), item);
        return item;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- VALIDATION ---">
    
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
    public final Validatable<T> getValidator() {
        return validator;
    }
    
    //TO-DOC
    public void revalidate() {
        FXTools.runFX(() -> items.forEach(this::revalidate), true);
    }
    
    //TO-DOC
    private TreeItemFX<E> revalidate(TreeItemFX<E> item) {
        final Validatable<T> tempValidator = getValidator();
        if (tempValidator != null) {
            final E cellData = item.getValue();
            if (cellData != null)
                item.setVisible(tempValidator.validate(cellData.getWrappedObject()));
        }
        return item;
    }
    
    //</editor-fold>
}