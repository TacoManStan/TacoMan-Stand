package com.taco.suit_lady.util;

import com.taco.suit_lady.util.tools.TasksSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked") public interface Lockable
        extends Lock {
    
    @NotNull Lock getLock();
    
    /* *************************************************************************** *
     *                                                                             *
     * Default Implementation                                                      *
     *                                                                             *
     * *************************************************************************** */
    
    default void interrupted(InterruptedException e) { }
    
    default boolean isNullableLock() {
        return false;
    }
    
    // Sync
    
    /**
     * See {@link TasksSL#sync(Lock, Runnable, Consumer[])}.
     *
     * @param action           See {@link TasksSL#sync(Lock, Runnable, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TasksSL#sync(Lock, Runnable, boolean, Consumer[])}.
     */
    default void sync(Runnable action, Consumer<Throwable>... onFinallyActions) {
        TasksSL.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link TasksSL#sync(Lock, Supplier, Consumer[])}.
     *
     * @param action           See {@link TasksSL#sync(Lock, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TasksSL#sync(Lock, Supplier, boolean, Consumer[])}.
     */
    default <R> R sync(Supplier<R> action, Consumer<Throwable>... onFinallyActions) {
        return TasksSL.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link TasksSL#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     *
     * @param action           See {@link TasksSL#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param actionSupplier   See {@link TasksSL#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TasksSL#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     */
    default <T, R> R sync(Function<T, R> action, Supplier<T> actionSupplier, Consumer<Throwable>... onFinallyActions) {
        return TasksSL.sync(getLock(), action, actionSupplier, isNullableLock(), onFinallyActions);
    }
    
    
    default void syncFX(Runnable action, Consumer<Throwable>... onFinallyActions) {
        TasksSL.sync(getLock(), () -> ToolsFX.runFX(action, true), isNullableLock(), onFinallyActions);
    }
    
    default <R> R syncFX(Supplier<R> action, Consumer<Throwable>... onFinallyActions) {
        return TasksSL.sync(getLock(), () -> ToolsFX.runFX(() -> action.get()), isNullableLock(), onFinallyActions);
    }
    
    /**
     * <p>Identical to <i>{@link #sync(Function, Supplier, Consumer[])}</i> except the specified {@link Function} is executed on the {@link ToolsFX#runFX(Callable) Java FX Thread}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Only the specified {@link Function} operation is executed on the {@link ToolsFX#runFX(Callable) Java FX Thread}.</li>
     * </ol>
     */
    //TO-EXPAND
    default <T, R> R syncFX(Function<T, R> action, Supplier<T> actionSupplier, Consumer<Throwable>... onFinallyActions) {
        return TasksSL.sync(getLock(), t -> ToolsFX.runFX(() -> action.apply(t)), actionSupplier, isNullableLock(), onFinallyActions);
    }
    
    // Lock Methods
    
    //<editor-fold desc="Lock Methods">
    
    @Override
    default void lock() {
        getLock().unlock();
    }
    
    @Override
    default void lockInterruptibly() {
        try {
            getLock().lockInterruptibly();
        } catch (InterruptedException e) {
            interrupted(e);
        }
    }
    
    @Override
    default boolean tryLock() {
        return getLock().tryLock();
    }
    
    @Override
    default boolean tryLock(long time, @NotNull TimeUnit unit) {
        try {
            return getLock().tryLock(time, unit);
        } catch (InterruptedException e) {
            interrupted(e);
            return false;
        }
    }
    
    @Override
    default void unlock() {
        getLock().unlock();
    }
    
    @Override
    default Condition newCondition() {
        return getLock().newCondition();
    }
    
    //</editor-fold>
    
    @Contract(value = "!null -> param1; null -> new", pure = true) static @NotNull Lock setLock(@Nullable Lock lock) { return lock != null ? lock : new ReentrantLock(); }
}
