package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

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
