package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.Lockable;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("SuspiciousToArrayCall")
public class ReadOnlyObservableListWrapper<E extends Comparable<? super E>>
        implements ObservableList<E>, Lockable {
    
    private final ReentrantLock lock;
    
    private final ObservableList<E> backingList;
    private final ReadOnlyObservableList<E> readOnlyList;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    @SafeVarargs
    public ReadOnlyObservableListWrapper(@NotNull E... initialContents) {
        this(FXCollections.observableArrayList(initialContents));
    }
    
    public ReadOnlyObservableListWrapper(@NotNull ObservableList<E> backingList) {
        this.lock = new ReentrantLock();
        
        this.backingList = backingList;
        this.readOnlyList = new ReadOnlyObservableList<>(this);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObservableList<E> readOnlyList() {
        return readOnlyList;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="--- OBSERVABLE LIST ---">
    
    @Override
    public void addListener(ListChangeListener<? super E> listener) {
        sync(() -> backingList.addListener(listener));
    }
    
    @Override
    public void removeListener(ListChangeListener<? super E> listener) {
        sync(() -> backingList.removeListener(listener));
    }
    
    @SafeVarargs
    @Override
    public final boolean addAll(E... elements) {
        return sync(() -> backingList.addAll(elements));
    }
    
    @SafeVarargs
    @Override
    public final boolean setAll(E... elements) {
        return sync(() -> backingList.setAll(elements));
    }
    
    @Override
    public boolean setAll(Collection<? extends E> col) {
        return sync(() -> backingList.setAll(col));
    }
    
    @SafeVarargs @Override
    public final boolean removeAll(E... elements) {
        return sync(() -> backingList.removeAll(elements));
    }
    
    @SafeVarargs @Override
    public final boolean retainAll(E... elements) {
        return sync(() -> backingList.retainAll(elements));
    }
    
    @Override
    public void remove(int from, int to) {
        sync(() -> backingList.remove(from, to));
    }
    
    @Override
    public int size() {
        return sync(() -> backingList.size());
    }
    
    @Override
    public boolean isEmpty() {
        return sync(() -> backingList.isEmpty());
    }
    
    @Override
    public boolean contains(Object o) {
        return sync(() -> backingList.contains(o));
    }
    
    @NotNull @Override
    public Iterator<E> iterator() {
        return sync(() -> backingList.iterator());
    }
    
    @NotNull @Override
    public Object @NotNull [] toArray() {
        return sync(() -> backingList.toArray());
    }
    
    @NotNull @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        return sync(() -> backingList.toArray(a));
    }
    
    @SuppressWarnings("Contract")
    @Override
    public boolean add(E e) {
        return sync(() -> backingList.add(e));
    }
    
    @Override
    public boolean remove(Object o) {
        return sync(() -> backingList.remove(o));
    }
    
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return sync(() -> backingList.containsAll(c));
    }
    
    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return sync(() -> backingList.addAll(c));
    }
    
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return sync(() -> backingList.addAll(index, c));
    }
    
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return sync(() -> backingList.removeAll(c));
    }
    
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return sync(() -> backingList.retainAll(c));
    }
    
    @Override
    public void clear() {
        sync(() -> backingList.clear());
    }
    
    @Override
    public E get(int index) {
        return sync(() -> backingList.get(index));
    }
    
    @Override
    public E set(int index, E element) {
        return sync(() -> backingList.set(index, element));
    }
    
    @Override
    public void add(int index, E element) {
        sync(() -> backingList.add(index, element));
    }
    
    @Override
    public E remove(int index) {
        return sync(() -> backingList.remove(index));
    }
    
    @Override
    public int indexOf(Object o) {
        return sync(() -> backingList.indexOf(o));
    }
    
    @Override
    public int lastIndexOf(Object o) {
        return sync(() -> backingList.lastIndexOf(o));
    }
    
    @NotNull @Override
    public ListIterator<E> listIterator() {
        return sync(() -> backingList.listIterator());
    }
    
    @NotNull @Override
    public ListIterator<E> listIterator(int index) {
        return sync(() -> backingList.listIterator(index));
    }
    
    @NotNull @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return sync(() -> backingList.subList(fromIndex, toIndex));
    }
    
    @Override
    public void addListener(InvalidationListener listener) {
        sync(() -> backingList.addListener(listener));
    }
    
    @Override
    public void removeListener(InvalidationListener listener) {
        sync(() -> backingList.removeListener(listener));
    }
    
    //</editor-fold>
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    //</editor-fold>
}
