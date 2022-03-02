package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import com.taco.suit_lady.util.tools.list_tools.Operation.TriggerType;
import com.taco.suit_lady.util.values.ValuePair;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

/**
 * <p>The {@link ListsSL} utility class provides a wide variety of static factory methods for applying an {@link OperationListener} implementation to an {@link ObservableList}.</p>
 * <h2>Details</h2>
 * <p><i>TO-EXPAND</i></p>
 * <br><hr>
 * <h2>Example Usages</h2>
 * <p>
 * There are numerous default implementations of {@link OperationListener} that {@link ListsSL} provides.
 * Example usage of such implementations are shown below.
 * </p>
 * <br>
 * <h3>{@link OperationListener}</h3>
 * <p>The base {@link OperationListener} interface provides a separate abstract method for handling each type of {@link Change Change Event}.</p>
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
 * <h3>{@link SimpleOperationListener}</h3>
 * <p>The {@link SimpleOperationListener} interface provides empty default implementations of all pre and post as well as update event handling.</p>
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
 * <h3>{@link OperationResponder}</h3>
 * <p>The {@link OperationResponder} interface overrides the abstract methods provided by {@link OperationListener} and wraps them into a single abstract method.</p>
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
 * <h3>{@link SimpleOperationResponder}</h3>
 * <p>
 * The {@link SimpleOperationResponder} interface removes all but the first {@link Operation} input from {@link OperationResponder} for more basic event handling.
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
public final class ListsSL {
    
    private ListsSL() { } // No Instance
    
    //<editor-fold desc="--- LIST LISTENING ---">
    
    //<editor-fold desc="--- OPERATION HANDLER APPLY METHODS ---">
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull OperationListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull OperationListener<E> listener) {
        return ListsSL.wrap(lock, null, list, listener).apply();
    }
    
    //
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull SimpleOperationListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull SimpleOperationListener<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull OperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull OperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull UnlinkedOperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull UnlinkedOperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull SimpleOperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable Lock lock, @NotNull ObservableList<E> list, @NotNull SimpleOperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    //
    
    
    /**
     * <p>Wraps the specified {@link OperationListener} instance in an {@link OperationHandler} object.</p>
     * <br>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Keep in mind, {@link OperationHandler} is itself an implementation of {@link OperationListener}.</li>
     *     <li>The constructed {@link OperationHandler} instance passes its {@link OperationListener} method definitions to the specified {@link OperationListener} parameter.</li>
     * </ol>
     * <hr>
     * <p><b>Examples</b></p>
     * <br>
     * <h4>{@link OperationListener} Passthrough</h4>
     * <pre>{@code @Override
     * public void onPermutate(Operation<E> op, Operation<E> op2) {
     *     listener.onPermutate(op, op2);
     * }}</pre>
     */
    // TO-EXPAND
    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull OperationHandler<E> wrap(@Nullable Lock lock, @Nullable String name, @NotNull ObservableList<E> list, OperationListener<E> listener) {
        return new OperationHandler<>(lock, name, list) {
            
            @Override
            public void onPermutate(Operation<E> op, Operation<E> op2) {
                listener.onPermutate(op, op2);
            }
            
            @Override
            public void onAdd(Operation<E> op) {
                listener.onAdd(op);
            }
            
            @Override
            public void onRemove(Operation<E> op) {
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
    
    public interface SimpleOperationListener<E> extends OperationListener<E> {
        
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
    public interface OperationResponder<E> extends OperationListener<E> {
        
        void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        //<editor-fold desc="--- DEFAULT METHODS ---">
        
        //<editor-fold desc="--- CHANGE ---">
        
        @Override
        default void onPermutate(Operation<E> op, Operation<E> op2) {
            respond(op, op2, OperationType.PERMUTATION, TriggerType.CHANGE);
        }
        
        @Override
        default void onAdd(Operation<E> op) {
            respond(op, null, OperationType.ADDITION, TriggerType.CHANGE);
        }
        
        @Override
        default void onRemove(Operation<E> op) {
            respond(op, null, OperationType.REMOVAL, TriggerType.CHANGE);
        }
        
        @Override
        default void onUpdate(int from, int to) {
            respond(Operation.empty(from, to), null, OperationType.UPDATION, TriggerType.CHANGE);
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
    public interface UnlinkedOperationResponder<E> extends OperationResponder<E> {
        
        void respond(@Nullable Operation<E> op, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        @Override
        default void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            respond(op1, opType, triggerType);
        }
    }
    
    @FunctionalInterface
    public interface SimpleOperationResponder<E> extends OperationResponder<E> {
        
        void respond(@Nullable Operation<E> op);
        
        @Override
        default void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            if (triggerType == TriggerType.CHANGE)
                respond(op1);
        }
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    @SafeVarargs
    public static <K, V> @NotNull HashMap<K, V> map(@Nullable Predicate<ValuePair<K, V>> filter, @NotNull ValuePair<K, V>... contents) {
        filter = filter != null ? filter : valuePair -> true;
        final HashMap<K, V> map = new HashMap<>();
        Arrays.stream(contents)
              .filter(Objects::nonNull)
              .filter(filter)
              .forEach(valuePair -> map.put(valuePair.a(), valuePair.b()));
        return map;
    }
    
    @SafeVarargs
    public static <K, V> @NotNull HashMap<K, V> map(@NotNull ValuePair<K, V>... contents) {
        return map(null, contents);
    }
}
