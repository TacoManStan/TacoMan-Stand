package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Exe;
import com.taco.suit_lady.util.tools.list_tools.Op.OperationType;
import com.taco.suit_lady.util.tools.list_tools.Op.TriggerType;
import com.taco.suit_lady.util.values.ValuePair;
import com.taco.tacository.collections.ReadOnlySelectionList;
import com.taco.tacository.collections.SelectionList;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>The {@link L} utility class provides a wide variety of static factory methods for applying an {@link OpListener} implementation to an {@link ObservableList}.</p>
 * <h2>Details</h2>
 * <p><i>TO-EXPAND</i></p>
 * <br><hr>
 * <h2>Example Usages</h2>
 * <p>
 * There are numerous default implementations of {@link OpListener} that {@link L} provides.
 * Example usage of such implementations are shown below.
 * </p>
 * <br>
 * <h3>{@link OpListener}</h3>
 * <p>The base {@link OpListener} interface provides a separate abstract method for handling each type of {@link Change Change Event}.</p>
 * <pre>{@code
 * ObservableList<E> list = ...;
 *
 * OperationListener<E> opListener = new OperationListener<>() {
 *
 *     @Override
 *     public void onPermutate(Operation<E> op, Operation<E> op2) {
 *         // Handle permutation logic
 *     }
 *
 *     @Override
 *     public void onAdd(Operation<E> op) {
 *         // Handle addition logic
 *     }
 *
 *     @Override
 *     public void onRemove(Operation<E> op) {
 *         // Handle removal logic
 *     }
 *
 *     @Override
 *     public void onUpdate(int from, int to) {
 *         // Handle generic updation logic (rarely used)
 *     }
 *
 *
 *     @Override
 *     public void onPrePermutate() {
 *         // Handle pre-permutation logic
 *     }
 *
 *     @Override
 *     public void onPostPermutate() {
 *         // Handle post-permutation logic
 *     }
 *
 *     @Override
 *     public void onPreAdd() {
 *         // Handle pre-addition logic
 *     }
 *
 *     @Override
 *     public void onPostAdd() {
 *         // Handle post-addition logic
 *     }
 *
 *     @Override
 *     public void onPreRemove() {
 *         // Handle pre-removal logic
 *     }
 *
 *     @Override
 *     public void onPostRemove() {
 *         // Handle post-removal logic
 *     }
 * };
 *
 * ListTools.applyListener(list, opListener);
 * }</pre>
 * <br>
 * <h3>{@link SimpleOpListener}</h3>
 * <p>The {@link SimpleOpListener} interface provides empty default implementations of all pre and post as well as update event handling.</p>
 * <pre>{@code
 * ObservableList<E> list = ...;
 *
 * SimpleOperationListener<E> opListener = new SimpleOperationListener<>() {
 *
 *     @Override
 *     public void onPermutate(Operation<E> op, Operation<E> op2) {
 *         // Handle permutation logic
 *     }
 *
 *     @Override
 *     public void onAdd(Operation<E> op) {
 *         // Handle addition logic
 *     }
 *
 *     @Override
 *     public void onRemove(Operation<E> op) {
 *         // Handle removal logic
 *     }
 * };
 *
 * ListTools.applyListener(list, opListener);
 * }</pre>
 * <br>
 * <h3>{@link OpResponder}</h3>
 * <p>The {@link OpResponder} interface overrides the abstract methods provided by {@link OpListener} and wraps them into a single abstract method.</p>
 * <pre>{@code
 * ObservableList<E> list = ...;
 *
 * ListTools.applyListener(list, (op1, op2, opType, triggerType) -> {
 *     // Check Operation Type
 *     // Check Trigger Type
 *     // Check Inferred (optional)
 *     // Respond to event
 * });
 * }</pre>
 * <br>
 * <h3>{@link SimpleOpResponder}</h3>
 * <p>
 * The {@link SimpleOpResponder} interface removes all but the first {@link Op} input from {@link OpResponder} for more basic event handling.
 * <br>Only responds to {@link TriggerType#CHANGE Change} triggers.
 * </p>
 * <pre>{@code
 * ObservableList<E> list = ...;
 *
 * ListTools.applyListener(list, op -> {
 *     // Respond to event
 * });
 * }</pre>
 */
// TO-EXPAND
public final class L {
    
    private L() { } // No Instance
    
    //<editor-fold desc="--- LIST LISTENING ---">
    
    //<editor-fold desc="--- OPERATION HANDLER APPLY METHODS ---">
    
    public static <E> OpHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull OpListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OpHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull OpListener<E> listener) {
        return L.wrap(lock, null, list, listener).apply();
    }
    
    //
    
    public static <E> OpHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull L.SimpleOpListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OpHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull L.SimpleOpListener<E> listener) {
        return applyListener(lock, list, (OpListener<E>) listener);
    }
    
    
    public static <E> OpHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull L.OpResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OpHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull L.OpResponder<E> listener) {
        return applyListener(lock, list, (OpListener<E>) listener);
    }
    
    
    public static <E> OpHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull L.UnlinkedOpResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OpHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull L.UnlinkedOpResponder<E> listener) {
        return applyListener(lock, list, (OpListener<E>) listener);
    }
    
    
    public static <E> OpHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull L.SimpleOpResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OpHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull L.SimpleOpResponder<E> listener) {
        return applyListener(lock, list, (OpListener<E>) listener);
    }
    
    
    //
    
    
    /**
     * <p>Wraps the specified {@link OpListener} instance in an {@link OpHandler} object.</p>
     * <br>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Keep in mind, {@link OpHandler} is itself an implementation of {@link OpListener}.</li>
     *     <li>The constructed {@link OpHandler} instance passes its {@link OpListener} method definitions to the specified {@link OpListener} parameter.</li>
     * </ol>
     * <hr>
     * <p><b>Examples</b></p>
     * <br>
     * <h4>{@link OpListener} Passthrough</h4>
     * <pre>{@code @Override
     * public void onPermutate(Operation<E> op, Operation<E> op2) {
     *     listener.onPermutate(op, op2);
     * }}</pre>
     */
    // TO-EXPAND
    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull OpHandler<E> wrap(@Nullable Lock lock, @Nullable String name, @NotNull ObservableList<E> list, OpListener<E> listener) {
        return new OpHandler<>(lock, name, list) {
            
            @Override
            public void onPermutate(Op<E> op, Op<E> op2) {
                listener.onPermutate(op, op2);
            }
            
            @Override
            public void onAdd(Op<E> op) {
                listener.onAdd(op);
            }
            
            @Override
            public void onRemove(Op<E> op) {
                listener.onRemove(op);
            }
            
            //
            
            @Override
            public void onPrePermutate() {
                listener.onPrePermutate();
            }
            
            @Override
            public void onPostPermutate() {
                listener.onPostPermutate();
            }
            
            @Override
            public void onPreAdd() {
                listener.onPreAdd();
            }
            
            @Override
            public void onPostAdd() {
                listener.onPostAdd();
            }
            
            @Override
            public void onPreRemove() {
                listener.onPreRemove();
            }
            
            @Override
            public void onPostRemove() {
                listener.onPostRemove();
            }
            
            @Override
            public void onUpdate(int from, int to) {
                listener.onUpdate(from, to);
            }
        };
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CLASSES ---">
    
    //<editor-fold desc="--- INTERFACES ---">
    
    public interface SimpleOpListener<E> extends OpListener<E> {
        
        @Override
        default void onPrePermutate() { }
        
        @Override
        default void onPostPermutate() { }
        
        
        @Override
        default void onPreAdd() { }
        
        @Override
        default void onPostAdd() { }
        
        
        @Override
        default void onPreRemove() { }
        
        @Override
        default void onPostRemove() { }
        
        @Override
        default void onUpdate(int from, int to) { }
    }
    
    //
    
    @FunctionalInterface
    public interface OpResponder<E> extends OpListener<E> {
        
        void respond(@Nullable Op<E> op1, @Nullable Op<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        //<editor-fold desc="--- DEFAULT METHODS ---">
        
        //<editor-fold desc="--- CHANGE ---">
        
        @Override
        default void onPermutate(Op<E> op, Op<E> op2) {
            respond(op, op2, OperationType.PERMUTATION, TriggerType.CHANGE);
        }
        
        @Override
        default void onAdd(Op<E> op) {
            respond(op, null, OperationType.ADDITION, TriggerType.CHANGE);
        }
        
        @Override
        default void onRemove(Op<E> op) {
            respond(op, null, OperationType.REMOVAL, TriggerType.CHANGE);
        }
        
        @Override
        default void onUpdate(int from, int to) {
            respond(Op.empty(from, to), null, OperationType.UPDATION, TriggerType.CHANGE);
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- BEFORE/AFTER ---">
        
        @Override
        default void onPrePermutate() {
            respond(null, null, OperationType.PERMUTATION, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onPostPermutate() {
            respond(null, null, OperationType.PERMUTATION, TriggerType.POST_CHANGE);
        }
        
        @Override
        default void onPreAdd() {
            respond(null, null, OperationType.ADDITION, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onPostAdd() {
            respond(null, null, OperationType.ADDITION, TriggerType.POST_CHANGE);
        }
        
        @Override
        default void onPreRemove() {
            respond(null, null, OperationType.REMOVAL, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onPostRemove() {
            respond(null, null, OperationType.REMOVAL, TriggerType.POST_CHANGE);
        }
        
        //</editor-fold>
        
        //</editor-fold>
    }
    
    @FunctionalInterface
    public interface UnlinkedOpResponder<E> extends OpResponder<E> {
        
        void respond(@Nullable Op<E> op, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        @Override
        default void respond(@Nullable Op<E> op1, @Nullable Op<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            respond(op1, opType, triggerType);
        }
    }
    
    @FunctionalInterface
    public interface SimpleOpResponder<E> extends OpResponder<E> {
        
        void respond(@Nullable Op<E> op);
        
        @Override
        default void respond(@Nullable Op<E> op1, @Nullable Op<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            if (triggerType == TriggerType.CHANGE)
                respond(op1);
        }
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- MAP OPERATIONS ---">
    
    //<editor-fold desc="> Factory & Conversion Methods">
    
    @SafeVarargs public static <K, V> @NotNull HashMap<K, V> map(@Nullable Predicate<ValuePair<K, V>> filter, @NotNull ValuePair<K, V>... contents) {
        filter = filter != null ? filter : valuePair -> true;
        final HashMap<K, V> map = new HashMap<>();
        Arrays.stream(contents)
              .filter(Objects::nonNull)
              .filter(filter)
              .forEach(valuePair -> map.put(valuePair.a(), valuePair.b()));
        return map;
    }
    
    @SafeVarargs public static <K, V> @NotNull HashMap<K, V> map(@NotNull ValuePair<K, V>... contents) { return map(null, contents); }
    
    //</editor-fold>
    
    public static <K, V> @Nullable V get(@NotNull K key, @NotNull Class<V> returnType, @NotNull Map<K, Object> map) { return (V) map.get(key); }
    public static <K, V> @Nullable V get(@NotNull K key, @NotNull Map<K, Object> map) { return (V) map.get(key); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CollectionsSL Ported Content ---">
    
    //<editor-fold desc="--- ADD/REMOVE OPERATIONS ---">
    
    //<editor-fold desc="> Add Operations">
    
    //<editor-fold desc="> Add All Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Predicate<E> filter,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, passedElementOperation, failedElementOperation, true, elements);
    }
    
    //
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Predicate<E> filter,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Predicate<E> filter,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Predicate<E> filter,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock, @NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(list, true, elements);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Remove Operations">
    
    //<editor-fold desc="> Remove All Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Predicate<E> filter,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, passedElementOperation, failedElementOperation, false, elements);
    }
    
    //
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Predicate<E> filter,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Predicate<E> filter,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Predicate<E> filter,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock, @NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(list, false, elements);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal: Add/Remove Operations">
    
    //<editor-fold desc="> Add or Remove All Operations">
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Predicate<E> filter,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return Exe.sync(lock, () -> {
            if (targetList != null && !targetList.isEmpty())
                throw Exc.unsupported("Target list must be null or empty.");
            
            final L zeList = targetList != null ? targetList : list;
            final ArrayList<E> passedList = new ArrayList<>();
            final ArrayList<E> failedList = new ArrayList<>();
            
            zeList.stream().filter(filter != null ? filter : e -> true).forEach(passedList::add);
            zeList.stream().filter(e -> !passedList.contains(e)).forEach(failedList::add);
            
            if (passedElementOperation != null)
                passedList.forEach(passedElementOperation);
            if (failedElementOperation != null)
                failedList.forEach(failedElementOperation);
            
            return addOrRemoveAll(zeList, passedList, add);
        }, true);
    }
    
    //
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Predicate<E> filter,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Predicate<E> filter,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, filter, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Predicate<E> filter,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, filter, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, null, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, null, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock, @NotNull L list, boolean add, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, null, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, boolean add, @NotNull E... elements) {
        return addOrRemoveAll(null, list, null, null, null, null, add, elements);
    }
    
    //
    
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, @NotNull List<E> toAddOrRemove, boolean add) {
        boolean result = add ? list.addAll(toAddOrRemove) : list.removeAll(toAddOrRemove);
        return list;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- LEGACY ---">
    
    private static final Presets presets = new Presets();
    public static Presets presets() { return presets; }
    
    //
    
    public static <E> @NotNull ReadOnlyListWrapper<E> boundList(@Nullable Lock lock, @NotNull ObservableMap<?, E> map) {
        return Exe.sync(lock, () -> {
            final ReadOnlyListWrapper<E> bound_list = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
            map.addListener((MapChangeListener<Object, E>) change -> Exe.sync(lock, () -> {
                if (change.wasAdded())
                    bound_list.add(change.getValueAdded());
                if (change.wasRemoved())
                    bound_list.remove(change.getValueRemoved());
            }, true));
            return bound_list;
        }, true);
    }
    
    public static final class Presets {
        private Presets() { }
        
        @Contract(" -> new") public <E> @NotNull ReadOnlySelectionList<E> readOnlySelectionArrayList() { return readOnlySelectionArrayList(null); }
        @Contract("_ -> new") public <E> @NotNull ReadOnlySelectionList<E> readOnlySelectionArrayList(E initialSelection) { return new ReadOnlySelectionList<>(new ArrayList<>(), initialSelection); }
        @Contract(" -> new") public <E> @NotNull SelectionList<E> selectionArrayList() { return selectionArrayList(null); }
        @Contract("_ -> new") public <E> @NotNull SelectionList<E> selectionArrayList(E initialSelection) { return new SelectionList<>(new ArrayList<>(), initialSelection); }
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
