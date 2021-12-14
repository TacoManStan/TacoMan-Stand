package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.util.tools.list_tools.ListTools.SimpleOperationListener;
import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;

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
 *     <li>
 *         Keep in mind that {@link OperationListener} is bound by the same limitations as {@link ListChangeListener}.
 *         <ul>
 *             <li>This is primarily relevant for {@code multi-threaded} applications that might trigger concurrency problems if the list is modified in the middle of {@link Change Change Event} handling.</li>
 *             <li>
 *                 This is exceptionally important when using an {@link OperationListener} to track {@link Change changes} made to an {@link ObservableList} that is a member of a {@code JavaFX} {@link Node} object
 *                 — This is because {@link OperationListener} events should be handled in a {@code background thread}, whereas the {@code user} is still able to make {@link Change changes} to the {@link ObservableList list}
 *                 via {@code JavaFX UI} input, which must always be immediately responsive.
 *             </li>
 *             <li>
 *                 The two solutions to the aforementioned concurrency problem are as follows:
 *                 <ol>
 *                     <li>
 *                         Perform all {@link OperationListener} event handling on the {@link FXTools#runFX(Runnable, boolean) JavaFX Thread}.
 *                         <ul>
 *                             <li>This is a viable solution <i>only</i> if <i>all</i> event handling operations are bound by {@link Contract contract} to complete execution instantaneously.</li>
 *                             <li>
 *                                 When using this strategy, it is recommended to use a {@link Timer} to automatically break out of the event handling operation if the {@link Timer} is active for too long.
 *                                 Should the {@link Timer} expire, a {@link RuntimeException} should be thrown to indicate the event handling has been done incorrectly.
 *                             </li>
 *                         </ul>
 *                     </li>
 *                     <li>Listen to a <i>copy</i> of the {@code JavaFX} {@link ObservableList} and then synchronize the copy to the original using a {@link ReentrantLock}. TO-EXPAND</li>
 *                 </ol>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 * <br><hr>
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
 * <br><hr>
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
 * <br><hr>
 * <h2>Example Usage</h2>
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
 *
 *     @Override
 *     public void onUpdate(int from, int to) {
 *         // Handle generic updation logic (rarely used)
 *     }
 * };
 *
 * ListTools.applyListener(list, opListener);
 * }</pre>
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