package com.taco.suit_lady._to_sort._new.initialization;

import com.taco.suit_lady.util.Lockable;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An {@code enum} defining how an {@link Initializer} instance will synchronize its {@link Initializer#init(Object...) initializion} process.</p>
 */
public enum LockMode {
    
    /**
     * <p>If the {@link Initializer#getOwner() owner} is {@link Lockable}, all synchronization will be passed to the {@link Initializer#getOwner() owner}.</p>
     * <p>If the {@link Initializer#getOwner() owner} is <i>not</i> {@link Lockable}, no synchronization will be performed.</p>
     */
    OWNER_ONLY,
    
    /**
     * <p>Synchronization will always be performed using a {@link ReentrantLock} object specific to the {@link Initializer}.</p>
     */
    NEW_LOCK_ONLY,
    
    /**
     * <p>If the {@link Initializer#getOwner() owner} is {@link Lockable}, all synchronization will be passed to the {@link Initializer#getOwner() owner}.</p>
     * <p>If the {@link Initializer#getOwner() owner} is <i>not</i> {@link Lockable}, synchronization will be performed using a {@link ReentrantLock} object specific to the {@link Initializer}.</p>
     */
    OWNER_OR_NEW_LOCK,
    
    /**
     * <p>No synchronization will be performed.</p>
     */
    DO_NOT_LOCK;
    
    //
    
    LockMode() {}
    
    public static LockMode getDefault() { return LockMode.OWNER_ONLY; }
}
