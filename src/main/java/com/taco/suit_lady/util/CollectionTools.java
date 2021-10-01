package com.taco.suit_lady.util;

import com.taco.suit_lady.util.collections.ReadOnlySelectionList;
import com.taco.suit_lady.util.collections.SelectionList;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class CollectionTools
{
    private final Presets presets;
    
    CollectionTools()
    {
        this.presets = new Presets();
    }
    
    public final Presets presets()
    {
        return this.presets;
    }
    
    //
    
    public <E> ReadOnlyListWrapper<E> boundList(ObservableMap<?, E> map, Lock lock)
    {
        ExceptionTools.nullCheck(map, "Map");
        
        ReadOnlyListWrapper<E> bound_list = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        if (lock != null)
            map.addListener(new MapChangeListener<Object, E>()
            {
                @Override public void onChanged(Change<?, ? extends E> change)
                {
                    if (change.wasAdded())
                        bound_list.add(change.getValueAdded());
                    if (change.wasRemoved())
                        bound_list.remove(change.getValueRemoved());
                }
            });
        else
            map.addListener(new MapChangeListener<Object, E>()
            {
                @Override public void onChanged(Change<?, ? extends E> change)
                {
                    if (change.wasAdded())
                        bound_list.add(change.getValueAdded());
                    if (change.wasRemoved())
                        bound_list.remove(change.getValueRemoved());
                }
            });
        
        return bound_list;
    }
    
    public static final class Presets
    {
        private Presets() { }
        
        public <E> ReadOnlySelectionList<E> readOnlySelectionArrayList()
        {
            return readOnlySelectionArrayList(null);
        }
        
        public <E> ReadOnlySelectionList<E> readOnlySelectionArrayList(E initialSelection)
        {
            return new ReadOnlySelectionList<>(new ArrayList<>(), initialSelection);
        }
        
        public <E> SelectionList<E> selectionArrayList()
        {
            return selectionArrayList(null);
        }
        
        public <E> SelectionList<E> selectionArrayList(E initialSelection)
        {
            return new SelectionList<>(new ArrayList<>(), initialSelection);
        }
        
    }
}
