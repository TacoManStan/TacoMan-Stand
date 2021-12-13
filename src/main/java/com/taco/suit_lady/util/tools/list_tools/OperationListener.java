package com.taco.suit_lady.util.tools.list_tools;

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
     *     <li>If no {@link OperationType#PERMUTATION permutation} operations occurred in the {@link Change Change Event}, {@link #onPermutateBefore() this method} is not called.</li>
     * </ul>
     */
    void onPermutateBefore();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#PERMUTATION permutation} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#PERMUTATION permutation} operations occurred in the {@link Change Change Event}, {@link #onPermutateAfter() this method} is not called.</li>
     * </ul>
     */
    void onPermutateAfter();
    
    
    //TO-DOC
    void onAdd(Operation<E> op);
    
    /**
     * <p>Executed <u>prior</u> to triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#ADDITION addition} operations occurred in the {@link Change Change Event}, {@link #onAddBefore() this method} is not called.</li>
     * </ul>
     */
    void onAddBefore();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#ADDITION addition} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#ADDITION addition} operations occurred in the {@link Change Change Event}, {@link #onAddAfter() this method} is not called.</li>
     * </ul>
     */
    void onAddAfter();
    
    
    //TO-DOC
    void onRemove(Operation<E> op);
    
    /**
     * <p>Executed <u>prior</u> to triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#REMOVAL removal} operations occurred in the {@link Change Change Event}, {@link #onRemoveBefore() this method} is not called.</li>
     * </ul>
     */
    void onRemoveBefore();
    
    /**
     * <p>Executed <u>after</u> triggering all {@link OperationType#REMOVAL removal} operations that occurred in a single {@link Change Change Event} {@link Change#next() step}.</p>
     * <p><b>Details</b></p>
     * <ul>
     *     <li>If no {@link OperationType#REMOVAL removal} operations occurred in the {@link Change Change Event}, {@link #onRemoveBefore() this method} is not called.</li>
     * </ul>
     */
    void onRemoveAfter();
    
    
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