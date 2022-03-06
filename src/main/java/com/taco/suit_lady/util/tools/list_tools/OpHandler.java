package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Exe;
import com.taco.suit_lady.util.tools.list_tools.Op.OperationType;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>An abstract implementation of both {@link ListChangeListener} and {@link OpListener}.</p>
 * <h2>Details</h2>
 * <ol>
 *     <li>
 *         {@link OpHandler} objects are constructed by <i>{@link L}<b>.</b>{@link L#wrap(Lock, String, ObservableList, OpListener) wrap(...)}</i> and other factory methods in the {@link L} utility class.
 *         <ul>
 *             <li>Most factory methods return a {@link OpHandler} that was constructed using <i>{@link L}<b>.</b>{@link L#wrap(Lock, String, ObservableList, OpListener) wrap(...)}</i>.</li>
 *         </ul>
 *     </li>
 *     <li>The primary function of {@link OpHandler} is to streamline the event data provided by <i>{@link ListChangeListener#onChanged(Change) ListChangeListener#onChanged(Change)}</i>.</li>
 *     <li>{@link OpHandler} uses {@link Op} records to communicate a {@link Change Change Event}.</li>
 * </ol>
 * <hr>
 * <h2>Auto Conversion</h2>
 * <p>
 *     By default, an {@link OpHandler} will attempt to detect {@link ObservableList} {@link #onAdd(Op) add} and {@link #onRemove(Op) remove} operations that are functionally {@link OperationType#PERMUTATION permutations}
 *     — e.g. <i>{@link Collections}<b>.</b>{@link Collections#shuffle(List) shuffle}<b>(</b>{@link List}<b>)</b></i>.
 * </p>
 * <ol>
 *     <li>If such an {@link Op} exists, the {@link OperationType#ADDITION add} and {@link OperationType#REMOVAL remove} operations that comprise the inferred {@link OperationType#PERMUTATION permutation} are no longer triggered as {@link OperationType#ADDITION add} or {@link OperationType#REMOVAL remove} events, but rather converted and submitted as a single {@link OperationType#PERMUTATION permutation} event.</li>
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
 *     <li>{@link A#sort(List)}</li>
 * </ol>
 * <h3>Example Output</h3>
 * <p>Refer to {@link ListToolsDemo}.</p>
 *
 * @param <E> The type of element contained within the {@link ObservableList list} that has been assigned to this {@link OpHandler}.
 */
// TO-EXPAND
public abstract class OpHandler<E>
        implements OpListener<E>, ListChangeListener<E>, Lockable, Nameable, UIDProcessable {
    
    private final Lock lock;
    private final String name;
    
    private final ObservableList<E> list;
    private final ReadOnlyObjectWrapper<List<E>> backingListProperty;
    
    private final BooleanProperty smartConvertProperty;
    
    protected OpHandler(@Nullable Lock lock, @Nullable String name, @NotNull ObservableList<E> list) {
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
        Exe.sync(lock, () -> {
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
                                new Op<>(oldIndex, newIndex, newElement),
                                new Op<>(oldElementOldIndex, oldElementNewIndex, oldElement));
                    });
                    
                    onPermutateOperationInternal(false);
                } else if (change.wasUpdated())
                    onUpdateInternal(change.getFrom(), change.getTo());
                else {
                    ArrayList<Op<E>> allOps = list.stream().map(element -> new Op<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    
                    ArrayList<Op<E>> removedOps = change.getRemoved().stream().<Op<E>>map(element -> new Op<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Op<E>> addedOps = change.getAddedSubList().stream().<Op<E>>map(element -> new Op<>(
                            backingListProperty.get().indexOf(element),
                            list.indexOf(element),
                            element)).collect(Collectors.toCollection(ArrayList::new));
                    
                    if (isSmartConvert()) {
                        ArrayList<Op<E>> ops = allOps.stream().filter(
                                o -> addedOps.contains(o) && removedOps.contains(o)).collect(
                                Collectors.toCollection(ArrayList::new));
                        
                        ops.forEach(p -> {
                            removedOps.remove(p);
                            addedOps.remove(p);
                        });
                        
                        
                        if (!ops.isEmpty()) {
                            onPermutateOperationInternal(true);
                            ops.forEach(operation -> onPermutateInternal(operation, getByIndex(operation.movedToIndex(), false, allOps)));
                            onPermutateOperationInternal(false);
                        }
                    }
                    
                    
                    if (!removedOps.isEmpty()) {
                        onAddOrRemoveOperationInternal(true, false);
                        removedOps.forEach(operation -> onRemoveInternal(operation));
                        onAddOrRemoveOperationInternal(false, false);
                    }
                    
                    if (!addedOps.isEmpty()) {
                        onAddOrRemoveOperationInternal(true, true);
                        addedOps.forEach(operation -> onAddInternal(operation));
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
    
    private void onPermutateInternal(Op<E> op1, Op<E> op2) {
        Exe.sync(lock, () -> onPermutate(op1, op2), true);
    }
    
    private void onUpdateInternal(int from, int to) {
        Exe.sync(lock, () -> onUpdate(from, to), true);
    }
    
    private void onAddInternal(Op<E> op) {
        Exe.sync(lock, () -> onAdd(op), true);
    }
    
    private void onRemoveInternal(Op<E> op) {
        Exe.sync(lock, () -> onRemove(op), true);
    }
    
    //
    
    private void onPermutateOperationInternal(boolean before) {
        Exe.sync(lock, () -> {
            if (before) onPrePermutate();
            else onPostPermutate();
        }, true);
    }
    
    private void onAddOrRemoveOperationInternal(boolean before, boolean add) {
        Exe.sync(lock, () -> {
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
        Exe.sync(lock, () -> backingListProperty.set(A.copy(list)), true);
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns the first {@link Op} matching the specified {@code index} of the specified {@link Boolean index type} located in any of the specified {@link List lists}.</p>
     *
     * @param index The {@code index} being searched for.
     * @param to    True if the {@link Op#movedToIndex() moved to} {@code index} should be searched for, false if the {@link Op#movedFromIndex() moved from} index should be searched for instead.
     * @param lists The array of {@link List lists} to be scanned for an {@link Op} that matches the specified {@code index}.
     *
     * @return The first {@link Op} matching the specified {@code index} of the specified {@link Boolean index type} located in any of the specified {@link List lists}.
     */
    @SafeVarargs
    public final @Nullable Op<E> getByIndex(int index, boolean to, List<Op<E>> @NotNull ... lists) {
        for (List<Op<E>> list: lists) {
            Op<E> p = list.stream().filter(
                    operation -> (!to && operation.movedFromIndex() == index) || (to && operation.movedToIndex() == index)).findFirst().orElse(null);
            if (p != null)
                return p;
        }
        return null;
    }
    
    /**
     * <p>Applies this {@link OpHandler} as a {@link ListChangeListener} to the {@link ObservableList} assigned to this {@link OpHandler} object.</p>
     *
     * @return This {@link OpHandler}. Functionally void.
     */
    public final OpHandler<E> apply() {
        refresh();
        Exc.nullCheck(list, "Observable List").addListener(Exc.nullCheck(this, "List Listener"));
        return this;
    }
}
