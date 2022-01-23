package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.tools.SLExceptions;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ReadOnlyObservableList<T>
{
    private final ObservableList<T> list;
    
    public ReadOnlyObservableList()
    {
        this(FXCollections.observableArrayList());
    }
    
    public ReadOnlyObservableList(@NotNull ObservableList<T> backingList)
    {
        this.list = SLExceptions.nullCheck(backingList, "Backing List");
    }
    
    public final int getSize()
    {
        return list.size();
    }
    
    public final boolean isSize(int size)
    {
        return getSize() == size;
    }
    
    public final boolean isEmpty()
    {
        return list.isEmpty();
    }
    
    //<editor-fold desc="--- LISTENERS ---">
    
    public final void addListener(ListChangeListener<? super T> listener)
    {
        list.addListener(SLExceptions.nullCheck(listener, "ListChangeListener"));
    }
    
    public final void removeListener(ListChangeListener<? super T> listener)
    {
        list.removeListener(SLExceptions.nullCheck(listener, "ListChangeListener"));
    }
    
    @SafeVarargs
    public final void addListeners(ListChangeListener<? super T>... listeners)
    {
        Arrays.stream(SLExceptions.nullCheck(listeners, "ListChangeListener Array")).forEach(this::addListener);
    }
    
    @SafeVarargs
    public final void removeListeners(ListChangeListener<? super T>... listeners)
    {
        Arrays.stream(SLExceptions.nullCheck(listeners, "ListChangeListener Array")).forEach(this::removeListener);
    }
    
    //
    
    public final void addListener(InvalidationListener listener)
    {
        list.addListener(SLExceptions.nullCheck(listener, "InvalidationListener"));
    }
    
    public final void removeListener(InvalidationListener listener)
    {
        list.removeListener(SLExceptions.nullCheck(listener, "InvalidationListener"));
    }
    
    public final void addListeners(InvalidationListener... listeners)
    {
        Arrays.stream(SLExceptions.nullCheck(listeners, "InvalidationListener Array")).forEach(this::addListener);
    }
    
    public final void removeListeners(InvalidationListener... listeners)
    {
        Arrays.stream(SLExceptions.nullCheck(listeners, "InvalidationListener Array")).forEach(this::removeListener);
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
    
    //
    
    public final @NotNull ObservableList<T> getObservableCopy()
    {
        return getObservableCopy(e -> e);
    }
    
    public final @NotNull ObservableList<T> getObservableCopy(@NotNull Callback<T, T> copier)
    {
        return FXCollections.observableArrayList(getCopy(copier));
    }
    
    public final @NotNull ArrayList<T> getCopy()
    {
        return getCopy(e -> e);
    }
    
    public final @NotNull ArrayList<T> getCopy(@NotNull Callback<T, T> copier)
    {
        // TODO: Synchronize?
        final ArrayList<T> newList = new ArrayList<>();
        list.forEach(e -> newList.add(copier.call(e)));
        return newList;
    }
    
    //</editor-fold>
}
