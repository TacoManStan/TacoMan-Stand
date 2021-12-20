package com.taco.util.collections;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableListBase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReadOnlySelectionList<E> extends ObservableListBase<E> {

	private final List<E> backingList;
	protected final ReadOnlyObjectWrapper<E> selectedValueProperty;

	public ReadOnlySelectionList(List<E> backingList, E initialSelection) {
		this.backingList = backingList;
		this.selectedValueProperty = new ReadOnlyObjectWrapper<>(initialSelection);

		//

		this.selectedValueProperty.addListener((observable, oldValue, newValue) -> validateSelection());
		this.validateSelection();
	}

	//<editor-fold desc="Properties">

	protected List<E> getBackingList() {
		return backingList;
	}

	//

	public ReadOnlyObjectProperty<E> selectedValueProperty() {
		return selectedValueProperty.getReadOnlyProperty();
	}

	public final E getSelectedValue() {
		return selectedValueProperty.get();
	}

	//</editor-fold>

	//<editor-fold desc="List Methods">

	@Override public int size() {
		return backingList.size();
	}

	@Override public boolean isEmpty() {
		return backingList.isEmpty();
	}

	@Override public boolean contains(Object obj) {
		return backingList.contains(obj);
	}

	@Override public Iterator<E> iterator() {
		return backingList.iterator();
	}

	@Override public Object[] toArray() {
		return backingList.toArray();
	}

	@Override public <T> T[] toArray(T[] array) {
		return backingList.toArray(array);
	}

	@Override public boolean add(E element) {
		return backingList.add(element);
	}

	@Override public boolean remove(Object obj) {
		return backingList.remove(obj);
	}

	@Override public boolean containsAll(Collection<?> collection) {
		return backingList.containsAll(collection);
	}

	@Override public boolean addAll(Collection<? extends E> collection) {
		return backingList.addAll(collection);
	}

	@Override public boolean addAll(int index, Collection<? extends E> collection) {
		return backingList.addAll(index, collection);
	}

	@Override public boolean removeAll(Collection<?> collection) {
		return backingList.removeAll(collection);
	}

	@Override public boolean retainAll(Collection<?> collection) {
		return backingList.retainAll(collection);
	}

	@Override public void clear() {
		backingList.clear();
	}

	@Override public E get(int index) {
		return backingList.get(index);
	}

	@Override public E set(int index, E element) {
		return backingList.set(index, element);
	}

	@Override public void add(int index, E element) {
		backingList.add(index, element);
	}

	@Override public E remove(int index) {
		return backingList.remove(index);
	}

	@Override public int indexOf(Object obj) {
		return backingList.indexOf(obj);
	}

	@Override public int lastIndexOf(Object obj) {
		return backingList.lastIndexOf(obj);
	}

	@Override public ListIterator<E> listIterator() {
		return backingList.listIterator();
	}

	@Override public ListIterator<E> listIterator(int index) {
		return backingList.listIterator(index);
	}

	@Override public List<E> subList(int fromIndex, int toIndex) {
		return backingList.subList(fromIndex, toIndex);
	}

	//</editor-fold>

	private void validateSelection() {
		E selectedValue = getSelectedValue();
		if (selectedValue != null)
			if (!backingList.contains(selectedValue))
				backingList.add(selectedValue);
	}
}
