package com.taco.suit_lady.util;

import com.taco.suit_lady.util.tools.TaskTools;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
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
     * See {@link TaskTools#sync(Lock, Runnable, Consumer[])}.
     *
     * @param action           See {@link TaskTools#sync(Lock, Runnable, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TaskTools#sync(Lock, Runnable, boolean, Consumer[])}.
     */
    default void sync(Runnable action, Consumer<Throwable>... onFinallyActions)
    {
        TaskTools.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link TaskTools#sync(Lock, Supplier, Consumer[])}.
     *
     * @param action           See {@link TaskTools#sync(Lock, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TaskTools#sync(Lock, Supplier, boolean, Consumer[])}.
     */
    default <R> R sync(Supplier<R> action, Consumer<Throwable>... onFinallyActions)
    {
        return TaskTools.sync(getLock(), action, isNullableLock(), onFinallyActions);
    }
    
    /**
     * See {@link TaskTools#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     *
     * @param action           See {@link TaskTools#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param actionSupplier   See {@link TaskTools#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     * @param onFinallyActions See {@link TaskTools#sync(Lock, Function, Supplier, boolean, Consumer[])}.
     */
    default <T, R> R sync(Function<T, R> action, Supplier<T> actionSupplier, Consumer<Throwable>... onFinallyActions)
    {
        return TaskTools.sync(getLock(), action, actionSupplier, isNullableLock(), onFinallyActions);
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
}
