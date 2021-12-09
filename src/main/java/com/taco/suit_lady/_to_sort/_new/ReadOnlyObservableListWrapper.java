package com.taco.suit_lady._to_sort._new;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("SuspiciousToArrayCall")
public class ReadOnlyObservableListWrapper<T>
        implements ObservableList<T>
{
    private final ObservableList<T> backingList;
    private final ReadOnlyObservableList<T> readOnlyList;
    
    private boolean keepSorted;
    
    public ReadOnlyObservableListWrapper()
    {
        this(FXCollections.observableArrayList());
    }
    
    public ReadOnlyObservableListWrapper(@NotNull ObservableList<T> backingList)
    {
        this.backingList = backingList;
        this.readOnlyList = new ReadOnlyObservableList<>(this);
        
        this.keepSorted = false;
        
        //
        
        addListener((InvalidationListener) observable -> {
            if (keepSorted)
                sort(null);
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObservableList<T> getReadOnlyList()
    {
        return readOnlyList;
    }
    
    public final boolean isKeepSorted()
    {
        return keepSorted;
    }
    
    public final void setKeepSorted(boolean keepSorted)
    {
        this.keepSorted = keepSorted;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public void addListener(ListChangeListener<? super T> listener)
    {
        backingList.addListener(listener);
    }
    
    @Override
    public void removeListener(ListChangeListener<? super T> listener)
    {
        backingList.removeListener(listener);
    }
    
    @SafeVarargs @Override
    public final boolean addAll(T... elements)
    {
        return backingList.addAll(elements);
    }
    
    @SafeVarargs @Override
    public final boolean setAll(T... elements)
    {
        return backingList.setAll(elements);
    }
    
    @Override
    public boolean setAll(Collection<? extends T> col)
    {
        return backingList.setAll(col);
    }
    
    @SafeVarargs @Override
    public final boolean removeAll(T... elements)
    {
        return backingList.removeAll(elements);
    }
    
    @SafeVarargs @Override
    public final boolean retainAll(T... elements)
    {
        return backingList.retainAll(elements);
    }
    
    @Override
    public void remove(int from, int to)
    {
        backingList.remove(from, to);
    }
    
    @Override
    public int size()
    {
        return backingList.size();
    }
    
    @Override
    public boolean isEmpty()
    {
        return backingList.isEmpty();
    }
    
    @Override
    public boolean contains(Object o)
    {
        return backingList.contains(o);
    }
    
    @NotNull @Override
    public Iterator<T> iterator()
    {
        return backingList.iterator();
    }
    
    @NotNull @Override
    public Object @NotNull [] toArray()
    {
        return backingList.toArray();
    }
    
    @NotNull @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a)
    {
        return backingList.toArray(a);
    }
    
    @Override
    public boolean add(T t)
    {
        return backingList.add(t);
    }
    
    @Override
    public boolean remove(Object o)
    {
        return backingList.remove(o);
    }
    
    @Override
    public boolean containsAll(@NotNull Collection<?> c)
    {
        return backingList.containsAll(c);
    }
    
    @Override
    public boolean addAll(@NotNull Collection<? extends T> c)
    {
        return backingList.addAll(c);
    }
    
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c)
    {
        return backingList.addAll(index, c);
    }
    
    @Override
    public boolean removeAll(@NotNull Collection<?> c)
    {
        return backingList.removeAll(c);
    }
    
    @Override
    public boolean retainAll(@NotNull Collection<?> c)
    {
        return backingList.retainAll(c);
    }
    
    @Override
    public void clear()
    {
        backingList.clear();
    }
    
    @Override
    public T get(int index)
    {
        return backingList.get(index);
    }
    
    @Override
    public T set(int index, T element)
    {
        return backingList.set(index, element);
    }
    
    @Override
    public void add(int index, T element)
    {
        backingList.add(index, element);
    }
    
    @Override
    public T remove(int index)
    {
        return backingList.remove(index);
    }
    
    @Override
    public int indexOf(Object o)
    {
        return backingList.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(Object o)
    {
        return backingList.lastIndexOf(o);
    }
    
    @NotNull @Override
    public ListIterator<T> listIterator()
    {
        return backingList.listIterator();
    }
    
    @NotNull @Override
    public ListIterator<T> listIterator(int index)
    {
        return backingList.listIterator(index);
    }
    
    @NotNull @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return backingList.subList(fromIndex, toIndex);
    }
    
    @Override
    public void addListener(InvalidationListener listener)
    {
        backingList.addListener(listener);
    }
    
    @Override
    public void removeListener(InvalidationListener listener)
    {
        backingList.removeListener(listener);
    }
    
    //</editor-fold>
}
