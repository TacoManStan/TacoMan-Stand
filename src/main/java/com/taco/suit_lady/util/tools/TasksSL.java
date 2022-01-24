package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.UndefinedRuntimeException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Contains utility methods related to threading, including synchronization.
 * <p>
 * <i>It is recommended that you only use this class if you are experienced with synchronization.</i>
 */
public class TasksSL {
    private TasksSL() { }
    
    public static Thread start(Thread thread) {
        if (thread == null)
            throw new NullPointerException("Thread cannot be null.");
        thread.start();
        return thread;
    }
    
    public static Thread start(Thread thread, Lock lock) {
        throw new UndefinedRuntimeException(ExceptionsSL.nyi());
    }
    
    /**
     * Creates and then starts a {@link Thread} using the specified {@code Supplier}.
     * <p>
     * If the {@link Runnable} returned by the specified {@code Supplier} is itself a {@code Thread}, no additional {@code Thread} will be created.
     *
     * @param threadSupplier The {@code Supplier} that returns the {@code Runnable} that is to be created as a {@code Thread}.
     *
     * @return The {@code Thread} returned by the specified {@code Supplier}.
     *
     * @throws NullPointerException if the specified {@code Supplier} is null.
     */
    public static Thread start(Supplier<Runnable> threadSupplier) {
        Runnable runnable = ExceptionsSL.nullCheck(threadSupplier, "Thread Supplier").get();
        return start(runnable instanceof Thread ? (Thread) runnable : new Thread(runnable));
    }
    
    //
    
    //<editor-fold desc="Synchronization">
    
    /**
     * <p>Synchronizes the execution of the specified {@link Runnable}.</p>
     * <hr>
     * <p><b>Example Usage</b></p>
     * <blockquote><pre>
     * <code>String s = ThreadTools.run(lock, (){@code ->} sampleObject.sampleAction());</code></pre></blockquote>
     *
     * @param lock             The {@link Lock} being used for synchronization.
     * @param runnable         The {@link Runnable} that is executed synchronously using the specified {@link ReentrantLock}.
     * @param onFinallyActions Actions that are executed asynchronously upon the end of this method.
     *                         <ul>
     *                         <li>Actions are executed in a {@code finally block}, guaranteeing execution (unless {@code runnable} is {@code null}).</li>
     *                         <li>An {@code Exception} (or any {@code Throwable}) thrown during the execution of an action will be passed as the parameter of each action.</li>
     *                         <li>Actions will be executed <i>before</i> the {@code lock} is unlocked.</li>
     *                         <li>If an {@code Exception} is thrown during the execution of an action, remaining actions will <i>not</i> be executed.</li>
     *                         <li>Null actions are ignored.</li>
     *                         </ul>
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    @SafeVarargs
    public static void sync(Lock lock, Runnable runnable, Consumer<Throwable>... onFinallyActions) {
        ExceptionsSL.nullCheck(runnable, "Runnable");
        ExceptionsSL.nullCheck(onFinallyActions, "On-Finally Actions", "leave empty for no actions");
        
        TasksSL.sync(lock, runnable, false, onFinallyActions);
    }
    
    /**
     * <p>Synchronizes the execution of the specified {@link Runnable}.</p>
     * <br>
     * <p><b>Example Usage</b></p>
     * <blockquote><pre>
     * <code>String s = ThreadTools.run(lock, (){@code ->} sampleObject.sampleAction(), true);</code></pre></blockquote>
     *
     * @param lock             The {@link ReentrantLock} being used for synchronization.
     * @param runnable         The {@link Runnable} that is executed synchronously using the specified {@link ReentrantLock}.
     * @param allowNull        Determines what to do when the specified {@link ReentrantLock} is null.
     *                         <ol>
     *                         <li>When <i>true</i>: Executes the specified {@link Runnable} asynchronously.</li>
     *                         <li>When <i>false</i>: Throws a {@link NullPointerException}.</li>
     *                         </ol>
     * @param onFinallyActions Actions that are executed asynchronously upon the end of this method.
     *                         <ul>
     *                         <li>Actions are executed in a {@code finally block}, guaranteeing execution (unless {@code runnable} is {@code null}).</li>
     *                         <li>An {@code Exception} (or any {@code Throwable}) thrown during the execution of an action will be passed as the parameter of each action.</li>
     *                         <li>Actions will be executed <i>before</i> the {@code lock} is unlocked.</li>
     *                         <li>If an {@code Exception} is thrown during the execution of an action, remaining actions will <i>not</i> be executed.</li>
     *                         <li>Null actions are ignored.</li>
     *                         </ul>
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    @SafeVarargs
    public static void sync(Lock lock, Runnable runnable, boolean allowNull, Consumer<Throwable>... onFinallyActions) {
        ExceptionsSL.nullCheck(runnable, "Runnable");
        ExceptionsSL.nullCheck(onFinallyActions, "On-Finally Actions", "leave empty for no actions");
        
        TasksSL.sync(lock, ignored -> {
            runnable.run();
            return null;
        }, () -> null, allowNull);
    }
    
    /**
     * <p>Synchronizes the execution of the specified {@link Supplier}.</p>
     * <hr>
     * <p><b>Example Usage</b></p>
     * <blockquote><pre>
     * <code>String s = ThreadTools.run(lock, (){@code ->} sampleObject.calculateName(), true);</code></pre></blockquote>
     *
     * @param lock             The {@link ReentrantLock} being used for synchronization.
     * @param runnableSupplier The {@link Supplier} that is executed synchronously using the specified {@link ReentrantLock}.
     * @param onFinallyActions Actions that are executed asynchronously upon the end of this method.
     *                         <ul>
     *                         <li>Actions are executed in a {@code finally block}, guaranteeing execution (unless {@code runnableSupplier} is {@code null}).</li>
     *                         <li>An {@code Exception} (or any {@code Throwable}) thrown during the execution of an action will be passed as the parameter of each action.</li>
     *                         <li>Actions will be executed <i>before</i> the {@code lock} is unlocked.</li>
     *                         <li>If an {@code Exception} is thrown during the execution of an action, remaining actions will <i>not</i> be executed.</li>
     *                         <li>Null actions are ignored.</li>
     *                         </ul>
     * @param <R>              The type of Object returned by this method (and the specified {@link Supplier}.
     *
     * @return The value returned by the specified {@link Supplier}.
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    @SafeVarargs
    public static <R> R sync(Lock lock, Supplier<R> runnableSupplier, Consumer<Throwable>... onFinallyActions) {
        ExceptionsSL.nullCheck(runnableSupplier, "Runnable Supplier");
        ExceptionsSL.nullCheck(onFinallyActions, "On-Finally Actions", "leave empty for no actions");
        
        return TasksSL.sync(lock, runnableSupplier, false, onFinallyActions);
    }
    
    /**
     * Synchronizes the execution of the specified {@link Supplier}.
     * <p>
     * Example Usage:
     * <blockquote><pre>
     * <code>String s = ThreadTools.run(lock, (){@code ->} sampleObject.calculateName(), true);</code></pre></blockquote>
     *
     * @param lock             The {@link ReentrantLock} being used for synchronization.
     * @param runnableSupplier The {@link Supplier} that is executed synchronously using the specified {@link ReentrantLock}.
     * @param allowNull        Determines what to do when the specified {@link ReentrantLock} is null.
     *                         <ol>
     *                         <li>When <i>true</i>: Executes the specified {@link Supplier} asynchronously.</li>
     *                         <li>When <i>false</i>: Throws a {@link NullPointerException}.</li>
     *                         </ol>
     * @param <R>              The type of Object returned by this method (and the specified {@link Supplier}.
     * @param onFinallyActions Actions that are executed asynchronously upon the end of this method.
     *                         <ul>
     *                         <li>Actions are executed in a {@code finally block}, guaranteeing execution (unless {@code runnableSupplier} is {@code null}).</li>
     *                         <li>An {@code Exception} (or any {@code Throwable}) thrown during the execution of an action will be passed as the parameter of each action.</li>
     *                         <li>Actions will be executed <i>before</i> the {@code lock} is unlocked.</li>
     *                         <li>If an {@code Exception} is thrown during the execution of an action, remaining actions will <i>not</i> be executed.</li>
     *                         <li>Null actions are ignored.</li>
     *                         </ul>
     *
     * @return The value returned by the specified {@link Supplier}.
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    @SafeVarargs
    public static <R> R sync(Lock lock, Supplier<R> runnableSupplier, boolean allowNull, Consumer<Throwable>... onFinallyActions) {
        ExceptionsSL.nullCheck(runnableSupplier, "Runnable Supplier");
        ExceptionsSL.nullCheck(onFinallyActions, "On-Finally Actions", "leave empty for no actions");
        
        return TasksSL.sync(lock, ignored -> runnableSupplier.get(), () -> null, allowNull, onFinallyActions);
    }
    
    /**
     * Synchronizes the execution of the specified {@link Function runnableFunction} using the value returned by the specified {@link Supplier functionInputSupplier} as the function's input.
     * <p>
     *
     * <b>Note That...</b>
     * <ol>
     * <li>If the {@code onFinally} array is not empty, any thrown {@code Exception} is expected to be handled by the specified {@code onFinally} array.</li>
     * <li>If the {@code onFinally} array is empty, any {@code Exception} thrown during the execution of the specified {@code Supplier} will be internally caught and re-thrown.</li>
     * </ol>
     * <p>
     *
     * <b>Example Usage</b>
     * <blockquote><pre>
     * <code>public static double distanceToCursor(Point2D point) {
     *      {@link ExceptionsSL#nullCheck(Object, String) ExceptionTools.check}(point, "Point cannot be null.");
     *      return ThreadTools.run(new ReentrantLock(), paramPoint{@code ->} point.distanceTo(paramPoint), (){@code ->} Mouse.getLocation(), true);
     * }</code></pre></blockquote>
     *
     * @param lock                  The {@link ReentrantLock} being used for synchronization.
     * @param runnableFunction      The {@link Function} that is executed synchronously using the specified {@link ReentrantLock}.
     * @param functionInputSupplier The {@link Supplier} is used to supply the parameter for the {@link Function}.
     * @param allowNullLock         Determines what to do when the specified {@link ReentrantLock} is null.
     *                              <ol>
     *                              <li>When <i>true</i>: Executes the specified {@link Function} asynchronously.</li>
     *                              <li>When <i>false</i>: Throws a {@link NullPointerException}.</li>
     *                              </ol>
     * @param onFinally             Actions that are executed asynchronously upon the end of this method.
     *                              <ol>
     *                              <li>Actions are executed in a {@code finally block}, guaranteeing execution (unless {@code runnableFunction} or {@code functionInputSupplier} is {@code null}).</li>
     *                              <li>An {@code Exception} (or any {@code Throwable}) thrown during the execution of an action will be passed as the parameter of each action.</li>
     *                              <li>Actions will be executed <i>before</i> the {@code lock} is unlocked.</li>
     *                              <li>If an {@code Exception} is thrown during the execution of an action, remaining actions will <i>not</i> be executed.</li>
     *                              <li>Any null actions will result in a {@code NullPointerException} being thrown.</li>
     *                              </ol>
     * @param <R>                   The type of Object returned by this method (and the specified {@link Function}.
     *
     * @return The value returned by the specified {@code runnableFunction}.
     *
     * @throws NullPointerException If...
     *                              <ol>
     *                              <li>The specified {@code lock} is null <u>and</u> {@code allowNullLock} is specified as {@code false}.</li>
     *                              <li>The specified {@code runnableFunction} is null.</li>
     *                              <li>The specified {@code functionInputSupplier} is null.</li>
     *                              <li>The specified {@code onFinally} array is null.</li>
     *                              <li>Any of the elements within the {@code onFinally} array is null.</li>
     *                              </ol>
     *                              <p>
     *                              All null-checks are performed at the beginning of the method.
     */
    @SafeVarargs
    public static <T, R> R sync(Lock lock, Function<T, R> runnableFunction, Supplier<T> functionInputSupplier, boolean allowNullLock, Consumer<Throwable>... onFinally) {
        ExceptionsSL.nullCheck(runnableFunction, "Runnable Function");
        ExceptionsSL.nullCheck(functionInputSupplier, "Return Value Supplier");
        ExceptionsSL.nullCheck(onFinally, "On-Finally Actions", "leave empty for no actions, not null");
        
        if (ArraysSL.containsNull(onFinally))
            throw ExceptionsSL.ex(new NullPointerException("On-Finally Actions array cannot contain null elements."));
        
        final boolean locked = lock(lock, allowNullLock);
        Throwable thrown = null;
        
        try {
            return runnableFunction.apply(functionInputSupplier.get());
        } catch (Throwable t) {
            thrown = t;
            if (ArraysSL.isEmpty(onFinally))
                throw ExceptionsSL.ex(t, "");
        } finally {
            try {
                for (Consumer<Throwable> onFinallyAction: onFinally)
                    ExceptionsSL.nullCheck(onFinallyAction, "On-Finally Action").accept(thrown);
            } finally {
                if (locked)
                    lock.unlock();
            }
        }
        
        return null;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Lock/Unlock">
    
    /**
     * Locks the specified {@link ReentrantLock}.
     * <br>Throws a {@link NullPointerException} if the specified {@link ReentrantLock} is null.
     * <p>
     * <i>Calling this method is the same as calling {@link #lock(Lock, boolean) ThreadTools.lock(lock, false)}</i>
     *
     * @param lock The {@link ReentrantLock}.
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    public static void lock(Lock lock) {
        lock(lock, false);
    }
    
    /**
     * <p>Locks the specified {@link ReentrantLock}.</p>
     * <p>If this method returns true, {@link Lock#unlock()} <i>must</i> be called afterwards.</p>
     * <hr>
     * <p><b>Example Usage</b></p>
     * <blockquote><pre>
     * <code>//If lock is null and allowNull is false, this will throw a {@link NullPointerException}.
     * if (ThreadTools.lock(lock, allowNull)) {
     *      //Use a try-finally block to ensure that the {@link ReentrantLock} is always unlocked}.
     *      try {
     *          //Synchronized code
     *      } finally {
     *          ThreadTools.unlock(lock);
     *      }
     * //Only executed when allowNull is true (a {@link NullPointerException} will have already been thrown).
     * } else {
     *      //On-fail code - executed when lock is null.
     * }
     * </code></pre></blockquote>
     * <i>Generally speaking, it is a good idea to use a {@link #sync(Lock, Function, Supplier, boolean, Consumer...) sync} method instead of manually locking and unlocking.</i>
     *
     * @param lock      The {@link ReentrantLock} being locked.
     * @param allowNull Determines what to do when the specified {@link ReentrantLock} is null.
     *                  <ol>
     *                  <li>When <i>true</i>: Returns false immediately; does not perform any synchronization.</li>
     *                  <li>When <i>false</i>: Throws a {@link NullPointerException}.</li>
     *                  </ol>
     *
     * @return True if the lock was successful, false otherwise.
     *
     * @see #sync(Lock, Function, Supplier, boolean, Consumer...)
     */
    public static boolean lock(Lock lock, boolean allowNull) {
        if (!allowNull)
            ExceptionsSL.nullCheckMessage(lock, "Lock cannot be null if allowNull is false");
        if (lock != null) {
            lock.lock();
            return true;
        }
        return false;
    }
    
    //</editor-fold>
}
