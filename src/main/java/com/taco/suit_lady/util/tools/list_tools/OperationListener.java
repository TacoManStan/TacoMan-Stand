package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import javafx.collections.ListChangeListener;
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
    void onPermutate(Operation<E> op, Operation<E> op2);
    
    void onPermutateBefore();
    
    void onPermutateAfter();
    
    
    void onAdd(Operation<E> op);
    
    void onAddBefore();
    
    void onAddAfter();
    
    
    void onRemove(Operation<E> op);
    
    void onRemoveBefore();
    
    void onRemoveAfter();
    
    
    void onUpdate(int from, int to);
}