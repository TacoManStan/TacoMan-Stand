package com.taco.suit_lady.util.collections;

import javafx.beans.property.ObjectProperty;

import java.util.List;

public class SelectionList<E> extends ReadOnlySelectionList<E> {

	public SelectionList(List<E> backingList, E initialSelection) {
		super(backingList, initialSelection);
	}

	@Override public ObjectProperty<E> selectedValueProperty() {
		return selectedValueProperty;
	}

	public void setSelectedValue(E value) {
		selectedValueProperty.set(value);
	}
}
