package com.taco.suit_lady.util.initializer;

import com.taco.util.quick.ConsoleBB;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public final class Initializer extends ReadOnlyInitializer
{
    
    private ReadOnlyInitializer readOnlyInitializer;
    
    private final Runnable onInitializeAction;
    private final ReadOnlyBooleanWrapper initializedProperty;
    
    private final ChangeListener propertyUpdateBlock;
    private final ReadOnlyListWrapper<ObservableValue> blockedProperties;
    
    public Initializer()
    {
        this(new ReentrantLock(), () -> { });
    }
    
    public Initializer(Runnable onInitializeAction)
    {
        this(new ReentrantLock(), onInitializeAction);
    }
    
    public Initializer(Lock lock)
    {
        this(lock, () -> { });
    }
    
    public Initializer(Lock lock, Runnable onInitializeAction)
    {
        this.readOnlyInitializer = null;
        
        this.initializedProperty = new ReadOnlyBooleanWrapper(false);
        this.onInitializeAction = onInitializeAction;
        
        this.propertyUpdateBlock = (observable, oldValue, newValue) -> { throw new UnsupportedOperationException(); };
        this.blockedProperties = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="Properties">
    
    public final ReadOnlyBooleanProperty initializedProperty()
    {
        return initializedProperty.getReadOnlyProperty();
    }
    
    public final boolean isInitialized()
    {
        return initializedProperty.get();
    }
    
    //
    
    public final ReadOnlyListProperty<ObservableValue> blockedProperties()
    {
        return blockedProperties.getReadOnlyProperty();
    }
    
    //
    
    public final ReadOnlyInitializer getReadOnlyInitializer()
    {
        if (readOnlyInitializer == null) // Lazy initialization
            readOnlyInitializer = new ReadOnlyInitializerImpl();
        return readOnlyInitializer;
    }
    
    //</editor-fold>
    
    //
    
    public final void initialize()
    {
        checkNotInitialized();
        initializeUnchecked();
    }
    
    public final void initializeUnchecked()
    {
        initializedProperty.set(true);
        if (onInitializeAction != null)
            onInitializeAction.run();
    }
    
    //
    
    @Override public final void checkInitialized()
    {
        checkInitialized(null);
    }
    
    @Override public final <T> T checkInitialized(Supplier<T> supplier)
    {
        if (!isInitialized())
            throw new IllegalStateException("The initializer has not yet been initialized.");
        return supplier == null ? null : supplier.get();
    }
    
    @Override public final void checkNotInitialized()
    {
        checkNotInitialized(null);
    }
    
    @Override public final <T> T checkNotInitialized(Supplier<T> supplier)
    {
        if (isInitialized())
            throw new IllegalStateException("The initializer has already been initialized.");
        return supplier == null ? null : supplier.get();
    }
    
    //
    
    @Override public void blockUpdates(ObservableValue... observableValues)
    {
        for (ObservableValue observableValue: observableValues)
            if (observableValue != null)
            {
                if (!blockedProperties.contains(observableValue))
                {
                    blockedProperties.add(observableValue);
                    observableValue.addListener(propertyUpdateBlock);
                }
                else
                    ConsoleBB.CONSOLE.dev("WARNING: Observable Value has already been added to this Initializer."); // TODO - Change to printDev
            }
            else
                ConsoleBB.CONSOLE.dev("WARNING: Observable Value is null.");
    }
    
    @Override public void unblockUpdates(ObservableValue... observableValues)
    {
        for (ObservableValue observableValue: observableValues)
            if (observableValue != null)
            {
                if (blockedProperties.contains(observableValue))
                {
                    observableValue.removeListener(propertyUpdateBlock);
                    blockedProperties.remove(observableValue);
                }
                else
                    ConsoleBB.CONSOLE.dev("WARNING: Attempting to remove Observable Value not in list.");
            }
            else
                ConsoleBB.CONSOLE.dev("WARNING: Observable Value is null.");
    }
    
    //
    
    private class ReadOnlyInitializerImpl extends ReadOnlyInitializer
    {
        
        @Override public void checkInitialized()
        {
            Initializer.this.checkInitialized();
        }
        
        @Override public <T> T checkInitialized(Supplier<T> supplier)
        {
            return Initializer.this.checkInitialized(supplier);
        }
        
        @Override public void checkNotInitialized()
        {
            Initializer.this.checkNotInitialized();
        }
        
        @Override public <T> T checkNotInitialized(Supplier<T> supplier)
        {
            return Initializer.this.checkNotInitialized(supplier);
        }
        
        @Override public void blockUpdates(ObservableValue... observableValues)
        {
            Initializer.this.blockUpdates(observableValues);
        }
        
        @Override public void unblockUpdates(ObservableValue... observableValues)
        {
            Initializer.this.blockUpdates(observableValues);
        }
    }
}
