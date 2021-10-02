package com.taco.suit_lady.util.initializer;

import javafx.beans.value.ObservableValue;

import java.util.function.Supplier;

public abstract class ReadOnlyInitializer<T>
{
    
    protected ReadOnlyInitializer() { }
    
    //
    
    public abstract void checkInitialized();
    
    public abstract T checkInitialized(Supplier<T> supplier);
    
    public final T checkInitialized(T obj)
    {
        return checkInitialized(() -> checkInitialized(obj));
    }
    
    //
    
    public abstract void checkNotInitialized();
    
    public abstract T checkNotInitialized(Supplier<T> supplier);
    
    public final T checkNotInitialized(T obj)
    {
        return checkInitialized(() -> checkNotInitialized(obj));
    }
    
    //
    
    public abstract void blockUpdates(ObservableValue<T>[] observableValues);
    
    public abstract void unblockUpdates(ObservableValue<T>[] observableValues);
}
