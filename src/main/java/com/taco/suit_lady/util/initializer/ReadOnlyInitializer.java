package com.taco.suit_lady.util.initializer;

import javafx.beans.value.ObservableValue;

import java.util.function.Supplier;

public abstract class ReadOnlyInitializer
{
    
    protected ReadOnlyInitializer() { }
    
    //
    
    public abstract void checkInitialized();
    
    public abstract <T> T checkInitialized(Supplier<T> supplier);
    
    public final <T> T checkInitialized(T obj) { return checkInitialized(() -> checkInitialized(obj)); }
    
    //
    
    public abstract void checkNotInitialized();
    
    public abstract <T> T checkNotInitialized(Supplier<T> supplier);
    
    public final <T> T checkNotInitialized(T obj) { return checkInitialized(() -> checkNotInitialized(obj)); }
    
    //
    
    public abstract void blockUpdates(ObservableValue... observableValues);
    
    public abstract void unblockUpdates(ObservableValue... observableValues);
}
