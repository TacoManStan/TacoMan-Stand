package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class ReadOnlyObservableList<T>
{
    private final ObservableList<T> list;
    
    public ReadOnlyObservableList()
    {
        this.list = FXCollections.observableArrayList();
    }
    
    public final int getSize()
    {
        return list.size();
    }
    
    public final boolean isEmpty()
    {
        return list.isEmpty();
    }
    
    public final boolean isSize(int size)
    {
        return getSize() == size;
    }
    
    //<editor-fold desc="--- LISTENERS ---">
    
    public final void addListener(ListChangeListener<? super T> listener)
    {
        list.addListener(ExceptionTools.nullCheck(listener, "ListChangeListener"));
    }
    
    public final void removeListener(ListChangeListener<? super T> listener)
    {
        list.removeListener(ExceptionTools.nullCheck(listener, "ListChangeListener"));
    }
    
    @SafeVarargs
    public final void addListeners(ListChangeListener<? super T>... listeners)
    {
        Arrays.stream(ExceptionTools.nullCheck(listeners, "ListChangeListener Array")).forEach(this::addListener);
    }
    
    @SafeVarargs
    public final void removeListeners(ListChangeListener<? super T>... listeners)
    {
        Arrays.stream(ExceptionTools.nullCheck(listeners, "ListChangeListener Array")).forEach(this::removeListener);
    }
    
    //
    
    public final void addListener(InvalidationListener listener)
    {
        list.addListener(ExceptionTools.nullCheck(listener, "InvalidationListener"));
    }
    
    public final void removeListener(InvalidationListener listener)
    {
        list.removeListener(ExceptionTools.nullCheck(listener, "InvalidationListener"));
    }
    
    public final void addListeners(InvalidationListener... listeners)
    {
        Arrays.stream(ExceptionTools.nullCheck(listeners, "InvalidationListener Array")).forEach(this::addListener);
    }
    
    public final void removeListeners(InvalidationListener... listeners)
    {
        Arrays.stream(ExceptionTools.nullCheck(listeners, "InvalidationListener Array")).forEach(this::removeListener);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ELEMENT OPERATIONS ---">
    
    public final int indexOf(T obj)
    {
        return list.indexOf(obj);
    }
    
    public final int lastIndexOf(T obj)
    {
        return list.lastIndexOf(obj);
    }
    
    //
    
    public final boolean contains(T obj)
    {
        return list.contains(obj);
    }
    
    public final boolean containsAll(Collection<T> c)
    {
        return list.containsAll(c);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TYPE CONVERSIONS ---">
    
    public final Object @NotNull [] toArray()
    {
        return list.toArray();
    }
    
    public final T @NotNull [] toArray(T[] a)
    {
        return list.toArray(a);
    }
    
    public final ObservableList<T> getCopy()
    {
        return list; // TODO: Return a copy
    }
    
    //</editor-fold>
}
