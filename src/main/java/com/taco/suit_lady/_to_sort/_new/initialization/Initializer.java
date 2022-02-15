package com.taco.suit_lady._to_sort._new.initialization;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.TasksSL;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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
            throw ExceptionsSL.ex("Initializer (" + this + ") has already been initialized.");
        operation(true, params);
        return owner;
    }
    
    T shutdown(@NotNull Object... params) {
        if (!isInitialized())
            throw ExceptionsSL.ex("Cannot shutdown Initializer that has not been initialized — [ " + getOwner() + " ]");
        operation(false, params);
        return owner;
    }
    
    
    void throwInitException() { throw ExceptionsSL.ex("Initializer has not been initialized — [ " + owner + " ]"); }
    
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
                if (getOwner() instanceof Lockable lockableOwner)
                    lockableOwner.sync(() -> doOperation(init, params));
                else
                    doOperation(init, params);
            }
            
            case NEW_LOCK_ONLY -> TasksSL.sync(getLock(), () -> doOperation(init, params));
            
            case OWNER_OR_NEW_LOCK -> {
                if (getOwner() instanceof Lockable lockableOwner)
                    lockableOwner.sync(() -> doOperation(init, params));
                else
                    TasksSL.sync(getLock(), () -> doOperation(init, params));
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
