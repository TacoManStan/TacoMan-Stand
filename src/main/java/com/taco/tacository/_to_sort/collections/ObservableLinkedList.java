package com.taco.tacository._to_sort.collections;

import com.taco.tacository.util.tools.Exc;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An {@link ObservableList} backed by a {@link LinkedList}.</p>
 *
 * @param <E> The type of elements held in the backing {@code LinkedList}.
 */
public class ObservableLinkedList<E> extends ObservableListBase<E>
        implements Deque<E>, Externalizable {
    
    private final ReentrantLock lock;
    private LinkedList<E> backingList;
    
    public ObservableLinkedList() {
        this(new LinkedList<>());
    }
    
    public ObservableLinkedList(LinkedList<E> backingList) {
        Exc.nullCheck(backingList, "Backing list cannot be null.");
        
        this.lock = new ReentrantLock();
        this.backingList = backingList;
    }
    
    //<editor-fold desc="--- LOGIC ---">
    
    /**
     * Removes the <i>first</i> occurrence of the specified element from this {@code ObservableLinkedList}.
     * <p>
     * This method is identical in functionality to {@link #removeFirstOccurrence(Object)}.
     *
     * @param e The element being removed.
     *
     * @return True if the element was successfully removed, false otherwise.
     *
     * @see #removeFirstOccurrence(Object)
     */
    public boolean removeFirst(E e) {
        int index = backingList.indexOf(e);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Removes the <i>last</i> occurrence of the specified element from this {@code ObservableLinkedList}.
     * <p>
     * This method is identical in functionality to {@link #removeLastOccurrence(Object)}.
     *
     * @param e The element being removed.
     *
     * @return True if the element was successfully removed, false otherwise.
     *
     * @see #removeLastOccurrence(Object)
     */
    public boolean removeLast(E e) {
        int index = backingList.lastIndexOf(e);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> List/Deque">
    
    /**
     * {@inheritDoc}
     */
    @Override public void addFirst(E e) {
        backingList.addFirst(e);
        beginChange();
        nextAdd(0, 1);
        ++modCount;
        endChange();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public void addLast(E e) {
        backingList.addLast(e);
        int size = backingList.size();
        beginChange();
        nextAdd(size - 1, size);
        ++modCount;
        endChange();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public boolean offerLast(E e) {
        addLast(e);
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E removeFirst() {
        E old = backingList.removeFirst();
        beginChange();
        nextRemove(0, old);
        ++modCount;
        endChange();
        return old;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E removeLast() {
        E old = backingList.removeLast();
        beginChange();
        nextRemove(backingList.size(), old);
        ++modCount;
        endChange();
        return old;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E pollFirst() {
        E result = backingList.pollFirst();
        if (result != null) {
            beginChange();
            nextRemove(0, result);
            ++modCount;
            endChange();
        }
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E pollLast() {
        E result = backingList.pollLast();
        if (result != null) {
            beginChange();
            nextRemove(backingList.size(), result);
            ++modCount;
            endChange();
        }
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E getFirst() {
        return backingList.getFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E getLast() {
        return backingList.getLast();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E peekFirst() {
        return backingList.peekFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E peekLast() {
        return backingList.peekLast();
    }
    
    /**
     * {@inheritDoc}
     *
     * @see #removeFirst(Object)
     */
    @Override public boolean removeFirstOccurrence(Object o) {
        return removeFirst((E) o);
    }
    
    /**
     * {@inheritDoc}
     *
     * @see #removeLast(Object)
     */
    @Override public boolean removeLastOccurrence(Object o) {
        return removeLast((E) o);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public boolean offer(E e) {
        return offerLast(e);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E remove() {
        return removeFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E poll() {
        return pollFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E element() {
        return getFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E peek() {
        return peekFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public void push(E e) {
        addFirst(e);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E pop() {
        return removeFirst();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public Iterator<E> descendingIterator() {
        return backingList.descendingIterator();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public E get(int index) {
        return backingList.get(index);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override public int size() {
        return backingList.size();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Externalizable">
    
    /**
     * {@inheritDoc}
     *
     * @throws IOException {@inheritDoc}
     */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(backingList);
    }
    
    /**
     * {@inheritDoc}
     *
     * @throws IOException            {@inheritDoc}
     * @throws ClassNotFoundException {@inheritDoc}
     */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        backingList = (LinkedList<E>) in.readObject();
    }
    
    //</editor-fold>
    
    //</editor-fold>
}