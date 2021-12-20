package com.taco.suit_lady.util.initializer;

import com.taco.tacository.quick.ConsoleBB;
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

public final class Initializer<T> extends ReadOnlyInitializer<T>
{
    
    private ReadOnlyInitializer<T> readOnlyInitializer;
    
    private final Runnable onInitializeAction;
    private final ReadOnlyBooleanWrapper initializedProperty;
    
    private final ChangeListener<T> propertyUpdateBlock;
    private final ReadOnlyListWrapper<ObservableValue<T>> blockedProperties;
    
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
    
    public ReadOnlyBooleanProperty initializedProperty()
    {
        return initializedProperty.getReadOnlyProperty();
    }
    
    public boolean isInitialized()
    {
        return initializedProperty.get();
    }
    
    //
    
    public ReadOnlyListProperty<ObservableValue<T>> blockedProperties()
    {
        return blockedProperties.getReadOnlyProperty();
    }
    
    //
    
    public ReadOnlyInitializer<T> getReadOnlyInitializer()
    {
        if (readOnlyInitializer == null) // Lazy initialization
            readOnlyInitializer = new ReadOnlyInitializerImpl();
        return readOnlyInitializer;
    }
    
    //</editor-fold>
    
    //
    
    public void initialize()
    {
        checkNotInitialized();
        initializeUnchecked();
    }
    
    public void initializeUnchecked()
    {
        initializedProperty.set(true);
        if (onInitializeAction != null)
            onInitializeAction.run();
    }
    
    //
    
    @Override
    public void checkInitialized()
    {
        checkInitialized(() -> null);
    }
    
    @Override
    public T checkInitialized(Supplier<T> supplier)
    {
        if (!isInitialized())
            throw new IllegalStateException("The initializer has not yet been initialized.");
        return supplier == null ? null : supplier.get();
    }
    
    @Override
    public void checkNotInitialized()
    {
        checkNotInitialized(() -> null);
    }
    
    @Override
    public T checkNotInitialized(Supplier<T> supplier)
    {
        if (isInitialized())
            throw new IllegalStateException("The initializer has already been initialized.");
        return supplier == null ? null : supplier.get();
    }
    
    //
    
    @SafeVarargs
    @Override
    public final void blockUpdates(ObservableValue<T>... observableValues)
    {
        for (ObservableValue<T> observableValue: observableValues)
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
    
    @SafeVarargs
    @Override
    public final void unblockUpdates(ObservableValue<T>... observableValues)
    {
        for (ObservableValue<T> observableValue: observableValues)
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
    
    private class ReadOnlyInitializerImpl extends ReadOnlyInitializer<T>
    {
        @Override
        public void checkInitialized()
        {
            Initializer.this.checkInitialized();
        }
        
        @Override
        public T checkInitialized(Supplier<T> supplier)
        {
            return Initializer.this.checkInitialized(supplier);
        }
        
        @Override
        public void checkNotInitialized()
        {
            Initializer.this.checkNotInitialized();
        }
        
        @Override
        public T checkNotInitialized(Supplier<T> supplier)
        {
            return Initializer.this.checkNotInitialized(supplier);
        }
        
        @SafeVarargs
        @Override
        public final void blockUpdates(ObservableValue<T>... observableValues)
        {
            Initializer.this.blockUpdates(observableValues);
        }
        
        @SafeVarargs
        @Override
        public final void unblockUpdates(ObservableValue<T>... observableValues)
        {
            Initializer.this.blockUpdates(observableValues);
        }
    }
}
