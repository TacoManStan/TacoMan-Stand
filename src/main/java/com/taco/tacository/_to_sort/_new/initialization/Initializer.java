package com.taco.tacository._to_sort._new.initialization;

import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Exe;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * <p>Defines the {@link Initializer#init(Object...) initialization} process for an {@link Initializable} object of type <{@link T}></p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>An {@link Initializer} defines two {@link Consumer} functions: {@link #getInitOperation() Init Operations} and {@link #getShutdownOperation() Shtdown Operations}.</li>
 *     <li>The {@link Consumer} objects accept an {@code array} of {@link Object Objects} containing the {@code data} required to {@link #init(Object...) Initialize} and {@link #shutdown(Object...) Shutdown} this {@link Initializer} and wrapped {@link Initializable} object it contains.</li>
 *     <li>
 *         An {@link Initializer} can only be {@link #init(Object...) initialized} and {@link #shutdown(Object...) shutdown} once each.
 *         <ul>
 *             <li><i>Upon calling {@link #init(Object...)}, the value of {@link #initializedProperty()} is updated accordingly.</i></li>
 *             <li><i>Upon calling {@link #shutdown(Object...)}, the value of {@link #shutdownProperty()} is updated accordingly.</i></li>
 *             <li><i>Subsequent calls to {@link #init(Object...)} will throw a {@link RuntimeException} if {@link #isInitialized()} is {@code true}.</i></li>
 *             <li><i>Subsequent calls to {@link #shutdown(Object...)} will throw a {@link RuntimeException} if {@link #isShutdown()} is {@code true}.</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         The internal {@link Lock#lock() synchronization} logic used by this {@link Initializer} can be customized by specifying a {@link LockMode}.
 *         <ul>
 *             <li><i>Note that the {@link #getLockMode() Lock Mode} is {@code final} and can only be set by an {@link Initializer} {@link Initializer#Initializer(Initializable, Consumer, Consumer, LockMode) Constructor}.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The {@link Initializable} implementation assigned to this {@link Initializer} instance.
 *
 * @see Initializable
 */
public final class Initializer<T extends Initializable<T>> {
    
    private final T owner;
    
    private final ReadOnlyBooleanWrapper initializedProperty;
    private final ReadOnlyBooleanWrapper shutdownProperty;
    
    //
    
    private final Consumer<Object[]> initOperation;
    private final Consumer<Object[]> shutdownOperation;
    
    private final LockMode lockMode;
    
    public Initializer(@NotNull T owner, @NotNull Consumer<Object[]> initOperation, @Nullable Consumer<Object[]> shutdownOperation, @Nullable LockMode lockMode) {
        this.owner = owner;
        
        this.initializedProperty = new ReadOnlyBooleanWrapper(false);
        this.shutdownProperty = new ReadOnlyBooleanWrapper(false);
        
        //
        
        this.initOperation = initOperation;
        this.shutdownOperation = shutdownOperation != null ? shutdownOperation : objects -> { };
        
        this.lockMode = lockMode != null ? lockMode : LockMode.getDefault();
    }
    
    T init(@NotNull Object... params) {
        if (isInitialized())
            throw Exc.ex("Initializer (" + this + ") has already been initialized.");
        operation(true, params);
        return owner;
    }
    
    T shutdown(@NotNull Object... params) {
        if (!isInitialized())
            throw Exc.ex("Cannot shutdown Initializer that has not been initialized — [ " + getOwner() + " ]");
        operation(false, params);
        return owner;
    }
    
    
    void throwInitException() { throw Exc.ex("Initializer has not been initialized — [ " + owner + " ]"); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public T getOwner() { return owner; }
    
    
    public ReadOnlyBooleanProperty initializedProperty() { return initializedProperty.getReadOnlyProperty(); }
    public boolean isInitialized() { return initializedProperty.get(); }
    
    public ReadOnlyBooleanProperty shutdownProperty() { return shutdownProperty.getReadOnlyProperty(); }
    public boolean isShutdown() { return shutdownProperty.get(); }
    
    //
    
    public Consumer<Object[]> getInitOperation() { return initOperation; }
    public Consumer<Object[]> getShutdownOperation() { return shutdownOperation; }
    
    public LockMode getLockMode() { return lockMode; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void operation(boolean init, @NotNull Object[] params) {
        switch (lockMode) {
            case OWNER_ONLY -> {
                if (getOwner() instanceof Lockable lockableOwner && (lockableOwner.isNullableLock() || lockableOwner.getLock() != null))
                    lockableOwner.sync(() -> doOperation(init, params));
                else
                    doOperation(init, params);
            }
            
            case NEW_LOCK_ONLY -> {
                if (getLock() != null)
                    Exe.sync(getLock(), () -> doOperation(init, params));
                else
                    doOperation(init, params);
            }
            
            case OWNER_OR_NEW_LOCK -> {
                if (getOwner() instanceof Lockable lockableOwner && (lockableOwner.isNullableLock() || lockableOwner.getLock() != null))
                    lockableOwner.sync(() -> doOperation(init, params));
                else if (getLock() != null)
                    Exe.sync(getLock(), () -> doOperation(init, params));
                else
                    doOperation(init, params);
            }
            
            case DO_NOT_LOCK -> doOperation(init, params);
        }
    }
    
    private void doOperation(boolean init, @NotNull Object[] params) {
        if (init) {
            initializedProperty.set(true);
            initOperation.accept(params);
        } else {
            shutdownProperty.set(true);
            shutdownOperation.accept(params);
        }
    }
    
    
    private ReentrantLock lock;
    private ReentrantLock getLock() {
        if (lock == null)
            lock = new ReentrantLock();
        return lock;
    }
    
    //</editor-fold>
}
