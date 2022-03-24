package com.taco.tacository.logic;

import com.taco.tacository.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>Defines implementing {@link Object Objects} as being {@link #execute(Runnable) Executable} by the {@link LogiCore}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@code logic} for a {@link Tickable} is defined by the {@link TaskManager} returned by the abstract <i>{@link #taskManager()}</i> method.</li>
 *     <li>
 *         {@link Tickable} implementations contain a variety of convenient {@code passthrough} methods to the {@link #taskManager() Task Manager}:
 *         <ul>
 *             <li>
 *                 <p><b>Add/Remove {@link GameTask} Methods</b></p>
 *                 <ul>
 *                     <li><i>{@link #addTask(GameTask)}</i></li>
 *                     <li><i>{@link #addTaskAndGet(GameTask)}</i></li>
 *                     <li><i>{@link #removeTask(GameTask)}</i></li>
 *                     <li><i>{@link #removeTaskAndGet(GameTask)}</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <p><b>Execution Methods</b></p>
 *                 <ul>
 *                     <li>
 *                         <p><b>Execute Once Methods</b></p>
 *                         <ul>
 *                             <li><i>{@link #executeOnce(Supplier, Consumer)}</i></li>
 *                             <li><i>{@link #executeOnce(Runnable, Runnable)}</i></li>
 *                             <li><i>{@link #executeOnce(Runnable)}</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>
 *                         <p><b>Execute Once and Get Methods</b></p>
 *                         <ul>
 *                             <li><i>{@link #executeOnceAndGet(Supplier, Consumer)}</i></li>
 *                             <li><i>{@link #executeOnceAndGet(Runnable, Runnable)}</i></li>
 *                             <li><i>{@link #executeOnceAndGet(Runnable)}</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>
 *                         <p><b>Execute Persistent Methods</b></p>
 *                         <ul>
 *                             <li><i>{@link #execute(Runnable, Runnable, Supplier)}</i></li>
 *                             <li><i>{@link #execute(Runnable, Supplier)}</i></li>
 *                             <li><i>{@link #execute(Runnable, Runnable)}</i></li>
 *                             <li><i>{@link #execute(Runnable)}</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>
 *                         <p><b>Execute and Get Persistent Methods</b></p>
 *                         <ul>
 *                             <li><i>{@link #executeAndGet(Runnable, Runnable, Supplier)}</i></li>
 *                             <li><i>{@link #executeAndGet(Runnable, Supplier)}</i></li>
 *                             <li><i>{@link #executeAndGet(Runnable, Runnable)}</i></li>
 *                             <li><i>{@link #executeAndGet(Runnable)}</i></li>
 *                         </ul>
 *                     </li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 * <p><i>See {@link TaskManager} for additional information.</i></p>
 *
 * @param <E> The {@link Class} type of this {@link Tickable} implementation.
 */
public interface Tickable<E extends Tickable<E>>
        extends Springable {
    
    @NotNull TaskManager<E> taskManager();
    default boolean lockIfLockable() { return false; }
    
    //
    
    //<editor-fold desc="--- DEFAULT TASK METHODS ---">
    
    //<editor-fold desc="> Add/Remove Task Methods">
    
    default <T extends GameTask<E>> boolean addTask(@NotNull T task) { return taskManager().addTask(task); }
    default <T extends GameTask<E>> @Nullable T addTaskAndGet(@NotNull T task) { return taskManager().addTaskAndGet(task); }
    
    default <T extends GameTask<E>> boolean removeTask(@NotNull T task) { return taskManager().removeTask(task); }
    default <T extends GameTask<E>> @Nullable T removeTaskAndGet(@NotNull T task) { return taskManager().removeTaskAndGet(task); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Execution Methods">
    
    //<editor-fold desc=">> Execute Once Methods">
    
    default @NotNull OneTimeTask<E> executeOnceAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().executeOnceAndGet(action, onTerminateAction); }
    default @NotNull OneTimeTask<E> executeOnceAndGet(@NotNull Runnable action) { return taskManager().executeOnceAndGet(action); }
    
    default boolean executeOnce(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().executeOnce(action, onTerminateAction); }
    default boolean executeOnce(@NotNull Runnable action) { return taskManager().executeOnce(action); }
    
    default <T> OneTimeTask<E> executeOnceAndGet(@NotNull Supplier<T> action, @NotNull Consumer<T> resultResponder) { return taskManager().executeOnceAndGet(action, resultResponder); }
    default <T> boolean executeOnce(@NotNull Supplier<T> action, @NotNull Consumer<T> resultResponder) { return taskManager().executeOnce(action, resultResponder); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Execute Persistent Methods">
    
    //    default @NotNull GameTask<E> executeAndGet(@Nullable Lock lock, @NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().executeAndGet(action, onTerminateAction, terminateCondition); }
    //    default @NotNull GameTask<E> executeAndGet(@Nullable Lock lock, @NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().executeAndGet(action, onTerminateAction); }
    //    default @NotNull GameTask<E> executeAndGet(@Nullable Lock lock, @NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().executeAndGet(action, terminateCondition); }
    //    default @NotNull GameTask<E> executeAndGet(@Nullable Lock lock, @NotNull Runnable action) { return taskManager().executeAndGet(action); }
    //
    //    default boolean execute(@Nullable Lock lock, @NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().execute(action, onTerminateAction, terminateCondition); }
    //    default boolean execute(@Nullable Lock lock, @NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().execute(action, onTerminateAction); }
    //    default boolean execute(@Nullable Lock lock, @NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().execute(action, terminateCondition); }
    //    default boolean execute(@Nullable Lock lock, @NotNull Runnable action) { return taskManager().execute(action); }
    
    //
    
    default @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().executeAndGet(action, onTerminateAction, terminateCondition); }
    default @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().executeAndGet(action, onTerminateAction); }
    default @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().executeAndGet(action, terminateCondition); }
    default @NotNull GameTask<E> executeAndGet(@NotNull Runnable action) { return taskManager().executeAndGet(action); }
    
    default boolean execute(@NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().execute(action, onTerminateAction, terminateCondition); }
    default boolean execute(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return taskManager().execute(action, onTerminateAction); }
    default boolean execute(@NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return taskManager().execute(action, terminateCondition); }
    default boolean execute(@NotNull Runnable action) { return taskManager().execute(action); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
}
