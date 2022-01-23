package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.SLArrays;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.SLTasks;
import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import com.taco.tacository.obj_traits.common.Nameable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>An abstract implementation of both {@link ListChangeListener} and {@link OperationListener}.</p>
 * <h2>Details</h2>
 * <ol>
 *     <li>
 *         {@link OperationHandler} objects are constructed by <i>{@link ListTools}<b>.</b>{@link ListTools#wrap(ReentrantLock, String, ObservableList, OperationListener) wrap(...)}</i> and other factory methods in the {@link ListTools} utility class.
 *         <ul>
 *             <li>Most factory methods return a {@link OperationHandler} that was constructed using <i>{@link ListTools}<b>.</b>{@link ListTools#wrap(ReentrantLock, String, ObservableList, OperationListener) wrap(...)}</i>.</li>
 *         </ul>
 *     </li>
 *     <li>The primary function of {@link OperationHandler} is to streamline the event data provided by <i>{@link ListChangeListener#onChanged(Change) ListChangeListener#onChanged(Change)}</i>.</li>
 *     <li>{@link OperationHandler} uses {@link Operation} records to communicate a {@link Change Change Event}.</li>
 * </ol>
 * <hr>
 * <h2>Auto Conversion</h2>
 * <p>
 *     By default, an {@link OperationHandler} will attempt to detect {@link ObservableList} {@link #onAdd(Operation) add} and {@link #onRemove(Operation) remove} operations that are functionally {@link OperationType#PERMUTATION permutations}
 *     — e.g. <i>{@link Collections}<b>.</b>{@link Collections#shuffle(List) shuffle}<b>(</b>{@link List}<b>)</b></i>.
 * </p>
 * <ol>
 *     <li>If such an {@link Operation} exists, the {@link OperationType#ADDITION add} and {@link OperationType#REMOVAL remove} operations that comprise the inferred {@link OperationType#PERMUTATION permutation} are no longer triggered as {@link OperationType#ADDITION add} or {@link OperationType#REMOVAL remove} events, but rather converted and submitted as a single {@link OperationType#PERMUTATION permutation} event.</li>
 *     <li>To disable automatic {@link OperationType#PERMUTATION permutation} conversions, set <i>{@link #isSmartConvertProperty() Smart Convert}</i> to {@code false}.</li>
 * </ol>
 * <h3>Known Functions Behavior</h3>
 * <p>Below outline {@link List} manipulation functions and thir permutation behavior.</p>
 * <h4>With <u>Smart Convert</u> Permutation Event Support</h4>
 * <ol>
 *     <li>{@link FXCollections#shuffle(ObservableList)}</li>
 * </ol>
 * <h4>Without <u>Smart Convert</u> Permutation Event Support</h4>
 * <ol>
 *     <li>{@link Collections#shuffle(List)}</li>
 * </ol>
 * <h4>With <u>Native</u> Permutation Event Support</h4>
 * <ol>
 *     <li>{@link SLArrays#sort(List)}</li>
 * </ol>
 * <h3>Example Output</h3>
 * <p>Refer to {@link ListToolsDemo}.</p>
 *
 * @param <E> The type of element contained within the {@link ObservableList list} that has been assigned to this {@link OperationHandler}.
 */
// TO-EXPAND
public abstract class OperationHandler<E>
        implements OperationListener<E>, ListChangeListener<E>, Lockable, Nameable, UIDProcessable {
    
    private final ReentrantLock lock;
    private final String name;
    
    private final ObservableList<E> list;
    private final ReadOnlyObjectWrapper<List<E>> backingListProperty;
    
    private final BooleanProperty smartConvertProperty;
    
    protected OperationHandler(@Nullable ReentrantLock lock, @Nullable String name, @NotNull ObservableList<E> list) {
        this.lock = lock;
        this.name = name;
        
        this.list = list;
        this.backingListProperty = new ReadOnlyObjectWrapper<>();
        
        this.smartConvertProperty = new SimpleBooleanProperty(true);
    }
    
    //<editor-fold desc="--- PROPERTIES  ---">
    
    public final ObservableList<E> getList() {
        return list;
    }
    
    
    public final BooleanProperty isSmartConvertProperty() {
        return smartConvertProperty;
    }
    
    public final boolean isSmartConvert() {
        return smartConvertProperty.get();
    }
    
    public final void setSmartConvert(boolean smartConvert) {
        smartConvertProperty.set(smartConvert);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    @Override
    public final void onChanged(Change<? extends E> change) {
        SLTasks.sync(lock, () -> {
            while (change.next())
                if (change.wasPermutated()) {
                    onPermutateOperationInternal(true);
                    
                    IntStream.range(change.getFrom(), change.getTo()).forEach(i -> {
                        final E newElement = list.get(i);
                        final int oldIndex = backingListProperty.get().indexOf(newElement);
                        @SuppressWarnings("UnnecessaryLocalVariable") final int newIndex = i;
                        
                        final E oldElement = list.get(change.getPermutation(newIndex));
                        final int oldElementOldIndex = backingListProperty.get().indexOf(oldElement);
                        final int oldElementNewIndex = change.getPermutation(newIndex);
                        
                        onPermutateInternal(
                                new Operation<>(oldIndex, newIndex, newElement),
                                new Operation<>(oldElementOldIndex, oldElementNewIndex, oldElement));
                    });
                    
                    onPermutateOperationInternal(false);
                } else if (change.wasUpdated())
                    onUpdateInternal(change.getFrom(), change.getTo());
                else {
                    ArrayList<Operation<E>> allOperations = list.stream().map(element -> new Operation<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    
                    ArrayList<Operation<E>> removedOperations = change.getRemoved().stream().<Operation<E>>map(element -> new Operation<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Operation<E>> addedOperations = change.getAddedSubList().stream().<Operation<E>>map(element -> new Operation<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    
                    if (isSmartConvert()) {
                        ArrayList<Operation<E>> operations = allOperations.stream().filter(
                                o -> addedOperations.contains(o) && removedOperations.contains(o)).collect(
                                Collectors.toCollection(ArrayList::new));
                        
                        operations.forEach(p -> {
                            removedOperations.remove(p);
                            addedOperations.remove(p);
                        });
                        
                        
                        if (!operations.isEmpty()) {
                            onPermutateOperationInternal(true);
                            operations.forEach(operation -> onPermutateInternal(operation, getByIndex(operation.movedToIndex(), false, allOperations)));
                            onPermutateOperationInternal(false);
                        }
                    }
                    
                    
                    if (!removedOperations.isEmpty()) {
                        onAddOrRemoveOperationInternal(true, false);
                        removedOperations.forEach(operation -> onRemoveInternal(operation));
                        onAddOrRemoveOperationInternal(false, false);
                    }
                    
                    if (!addedOperations.isEmpty()) {
                        onAddOrRemoveOperationInternal(true, true);
                        addedOperations.forEach(operation -> onAddInternal(operation));
                        onAddOrRemoveOperationInternal(false, true);
                    }
                }
            
            refresh();
        }, true);
    }
    
    //<editor-fold desc="--- EVENT RESPONSE ---">
    
    @Override
    public void onPrePermutate() { }
    
    @Override
    public void onPostPermutate() { }
    
    //
    
    @Override
    public void onPreAdd() { }
    
    @Override
    public void onPostAdd() { }
    
    //
    
    @Override
    public void onPreRemove() { }
    
    @Override
    public void onPostRemove() { }
    
    //
    
    @Override
    public void onUpdate(int from, int to) { }
    
    //</editor-fold>
    
    @Override
    public String getName() {
        return name;
    }
    
    private UIDProcessor uIDContainer;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uIDContainer == null) // Lazy Initialization
            uIDContainer = new UIDProcessor("group-name");
        return uIDContainer;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void onPermutateInternal(Operation<E> op1, Operation<E> op2) {
        SLTasks.sync(lock, () -> onPermutate(op1, op2), true);
    }
    
    private void onUpdateInternal(int from, int to) {
        SLTasks.sync(lock, () -> onUpdate(from, to), true);
    }
    
    private void onAddInternal(Operation<E> op) {
        SLTasks.sync(lock, () -> onAdd(op), true);
    }
    
    private void onRemoveInternal(Operation<E> op) {
        SLTasks.sync(lock, () -> onRemove(op), true);
    }
    
    //
    
    private void onPermutateOperationInternal(boolean before) {
        SLTasks.sync(lock, () -> {
            if (before) onPrePermutate();
            else onPostPermutate();
        }, true);
    }
    
    private void onAddOrRemoveOperationInternal(boolean before, boolean add) {
        SLTasks.sync(lock, () -> {
            if (add)
                if (before)
                    onPreAdd();
                else
                    onPostAdd();
            if (!add)
                if (before)
                    onPreRemove();
                else
                    onPostRemove();
        }, true);
    }
    
    //
    
    private void refresh() {
        SLTasks.sync(lock, () -> backingListProperty.set(SLArrays.copy(list)), true);
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns the first {@link Operation} matching the specified {@code index} of the specified {@link Boolean index type} located in any of the specified {@link List lists}.</p>
     *
     * @param index The {@code index} being searched for.
     * @param to    True if the {@link Operation#movedToIndex() moved to} {@code index} should be searched for, false if the {@link Operation#movedFromIndex() moved from} index should be searched for instead.
     * @param lists The array of {@link List lists} to be scanned for an {@link Operation} that matches the specified {@code index}.
     *
     * @return The first {@link Operation} matching the specified {@code index} of the specified {@link Boolean index type} located in any of the specified {@link List lists}.
     */
    @SafeVarargs
    public final @Nullable Operation<E> getByIndex(int index, boolean to, List<Operation<E>> @NotNull ... lists) {
        for (List<Operation<E>> list: lists) {
            Operation<E> p = list.stream().filter(
                    operation -> (!to && operation.movedFromIndex() == index) || (to && operation.movedToIndex() == index)).findFirst().orElse(null);
            if (p != null)
                return p;
        }
        return null;
    }
    
    /**
     * <p>Applies this {@link OperationHandler} as a {@link ListChangeListener} to the {@link ObservableList} assigned to this {@link OperationHandler} object.</p>
     *
     * @return This {@link OperationHandler}. Functionally void.
     */
    public final OperationHandler<E> apply() {
        refresh();
        SLExceptions.nullCheck(list, "Observable List").addListener(SLExceptions.nullCheck(this, "List Listener"));
        return this;
    }
}
