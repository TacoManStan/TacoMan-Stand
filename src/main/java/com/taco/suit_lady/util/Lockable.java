package com.taco.suit_lady.util;

import com.taco.suit_lady.util.tools.SLTasks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked") public interface Lockable
        extends Lock
{
    
    @NotNull Lock getLock();
    
    /* *************************************************************************** *
     *                                                                             *
     * Default Implementation                                                      *
     *                                                                             *
     * *************************************************************************** */
    
    default void interrupted(InterruptedException e) { }
    
    default boolean isNullableLock()
    {
        return false;
    }
    
    // Sync
    
    /**
     * See {@link SLTasks#sync(Lock, Runnable, Consumer[])}.
     *
     * @param action           See {@link SLTasks#sync(Lock, Runnable, boolean, Consumer[])}.
     * @param onFinallyActions See {@link SLTasks#sync(Lock, Runnable, boolean, Consumer[])}.
     */
    default void sync(Runnable action, Consumer<Throwable>... onFinallyActions)
    {
        SLTasks.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link SLTasks#sync(Lock, Supplier, Consumer[])}.
     *
     * @param action           See {@link SLTasks#sync(Lock, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link SLTasks#sync(Lock, Supplier, boolean, Consumer[])}.
     */
    default <R> R sync(Supplier<R> action, Consumer<Throwable>... onFinallyActions)
    {
        return SLTasks.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link SLTasks#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     *
     * @param action           See {@link SLTasks#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param actionSupplier   See {@link SLTasks#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link SLTasks#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     */
    default <T, R> R sync(Function<T, R> action, Supplier<T> actionSupplier, Consumer<Throwable>... onFinallyActions)
    {
        return SLTasks.sync(getLock(), action, actionSupplier, isNullableLock(), onFinallyActions);
    }
    
    // Lock Methods
    
    //<editor-fold desc="Lock Methods">
    
    @Override
    default void lock()
    {
        getLock().unlock();
    }
    
    @Override
    default void lockInterruptibly()
    {
        try
        {
            getLock().lockInterruptibly();
        }
        catch (InterruptedException e)
        {
            interrupted(e);
        }
    }
    
    @Override
    default boolean tryLock()
    {
        return getLock().tryLock();
    }
    
    @Override
    default boolean tryLock(long time, @NotNull TimeUnit unit)
    {
        try
        {
            return getLock().tryLock(time, unit);
        }
        catch (InterruptedException e)
        {
            interrupted(e);
            return false;
        }
    }
    
    @Override
    default void unlock()
    {
        getLock().unlock();
    }
    
    @Override
    default Condition newCondition()
    {
        return getLock().newCondition();
    }
    
    //</editor-fold>
    
    @Contract(value = "!null -> param1; null -> new", pure = true) static @NotNull Lock setLock(@Nullable Lock lock) { return lock != null ? lock : new ReentrantLock(); }
}
