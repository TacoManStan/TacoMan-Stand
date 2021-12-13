package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.list_tools.ListTools.SimpleOperationListener;
import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An interface used to help streamline {@link ListChangeListener} events to be more easily understandable.</p>
 * <h2>Details</h2>
 * <p>The primary usage pattern for {@link OperationListener} is as follows:</p>
 * <ol>
 *     <li>The primary implementation of {@link OperationListener} is the {@link OperationHandler} class.</li>
 *     <li>
 *         {@link OperationListener OperationListeners} are used as the input to {@link OperationHandler} static factory methods located in {@link ListTools}
 *         <ul>
 *             <li>Root Factory Method: <i>{@link ListTools#applyListener(ReentrantLock, ObservableList, OperationListener) ListTools.applyListener(... OperationListener)}</i></li>
 *         </ul>
 *     </li>
 *     <li>Various interface extensions of {@link OperationListener} exist to allow varying amounts and types of lambda factory input parameters.</li>
 * </ol>
 * <hr>
 * <h2>Operation Event Response Functions</h2>
 * <p><i>See individual method documentation for additional information.</i></p>
 * <h3>Permutation</h3>
 * <ol>
 *     <li><b>{@link #onPrePermutate() On Pre-Permutate}:</b> Executed immediately <i>prior to</i> triggering all {@link OperationType#PERMUTATION permutation} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onPostPermutate() On Post-Permutate}:</b> Executed immediately <i>after</i> triggering all {@link OperationType#PERMUTATION permutation} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onPermutate(Operation, Operation) On Permutate}:</b> Executed for each {@link OperationType#PERMUTATION permutation} operation that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 * </ol>
 * <h3>Addition</h3>
 * <ol>
 *     <li><b>{@link #onPreAdd() On Pre-Add}:</b> Executed immediately <i>prior to</i> triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onPostAdd() On Post-Add}:</b> Executed immediately <i>after</i> triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onAdd(Operation) On Add}:</b> Executed for each {@link OperationType#ADDITION addition} operation that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 * </ol>
 * <h3>Removal</h3>
 * <ol>
 *     <li><b>{@link #onPreRemove() On Pre-Remove}:</b> Executed immediately <i>prior to</i> triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onPostRemove() On Post-Remove}:</b> Executed immediately <i>after</i> triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 *     <li><b>{@link #onRemove(Operation) On Remove}:</b> Executed for each {@link OperationType#REMOVAL removal} operation that occurred in a single {@link Change Change Event} {@link Change#next() step}.</li>
 * </ol>
 * <hr>
 * <h2>Implementation</h2>
 * <ol>
 *     <li>The most concrete implementation of {@link OperationListener} is the {@link OperationHandler} class.</li>
 *     <li>
 *         However, the main purpose of the {@link OperationListener} interface is to allow for anonymous implementations to be passed to the static {@link ListTools#wrap(ReentrantLock, String, ObservableList, OperationListener) wrap} factory method for use with a wrapping {@link OperationHandler} instance.
 *         <ul>
 *             <li>The {@link ListTools#wrap(ReentrantLock, String, ObservableList, OperationListener) wrap} method is used by most other factory methods located in the {@link ListTools} utility class.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         Sub-implementations exist in {@link ListTools} — e.g., {@link SimpleOperationListener} — that provide more streamlined implementations of the event response methods present in {@link OperationListener this interface}.
 *         <ul>
 *             <li>Multiple static factory methods corresponding to each sub-implementation are available for use in the {@link ListTools} utility class as well.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <E> The type of element in the {@link ObservableList} this {@link OperationListener} is listening to.
 */
// TO-EXPAND
public interface OperationListener<E> {
    
    /**
     * <p>Executed for each {@link OperationType#PERMUTATION permutation} operation that is performed on the {@link ObservableList}.</p>
     * <h4>Details</h4>
     * <ol>
     *     <li>
     *         In most cases, only the first parameter — the {@code primary operation} — is needed.
     *         <ul>
     *             <li>The second parameter — the {@code secondary operation} — contains data pertaining to the element that was previously located at the index the {@code primary operation} was {@link Operation#movedToIndex() moved to}.</li>
     *             <li>Put simply, the value of <i>op<b>.</b>{@link Operation#movedToIndex() movedToIndex()}</i> will always be equal to the value of <i>op2<b>.</b>{@link Operation#movedFromIndex() movedFromIndex()}</i>.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param op  The primary {@link Operation}.
     * @param op2 The secondary {@link Operation}.
     */
    //TO-EXPAND
    void onPermutate(Operation<E> op, Operation<E> op2);
    
    /**
     * <p>Executed <u>prior</u> to triggering all {@link OperationType#PERMUTATION permutation} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#PERMUTATION permutation} operations occurred in the {@link Change Change Event}, {@link #onPrePermutate() this method} is not called.</li>
     * </ul>
     */
    void onPrePermutate();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#PERMUTATION permutation} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#PERMUTATION permutation} operations occurred in the {@link Change Change Event}, {@link #onPostPermutate() this method} is not called.</li>
     * </ul>
     */
    void onPostPermutate();
    
    
    //TO-DOC
    void onAdd(Operation<E> op);
    
    /**
     * <p>Executed <u>prior</u> to triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#ADDITION addition} operations occurred in the {@link Change Change Event}, {@link #onPreAdd() this method} is not called.</li>
     * </ul>
     */
    void onPreAdd();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#ADDITION addition} operations occurred in the {@link Change Change Event}, {@link #onPostAdd() this method} is not called.</li>
     * </ul>
     */
    void onPostAdd();
    
    
    //TO-DOC
    void onRemove(Operation<E> op);
    
    /**
     * <p>Executed <u>prior</u> to triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#REMOVAL removal} operations occurred in the {@link Change Change Event}, {@link #onPreRemove() this method} is not called.</li>
     * </ul>
     */
    void onPreRemove();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#REMOVAL removal} operations occurred in the {@link Change Change Event}, {@link #onPreRemove() this method} is not called.</li>
     * </ul>
     */
    void onPostRemove();
    
    
    /**
     * <p>Executed once in response to a {@link OperationType#UPDATION updation} operation that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>List operations that utilize this event type are rare, and therefore this method typically be ignored in favor of {@link #onPermutate(Operation, Operation) permutate}, {@link #onAdd(Operation) add}, and {@link #onRemove(Operation) remove} event responses.</li>
     * </ul>
     *
     * @param from
     * @param to
     */
    void onUpdate(int from, int to);
}