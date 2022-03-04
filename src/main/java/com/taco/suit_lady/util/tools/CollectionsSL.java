package com.taco.suit_lady.util.tools;

import com.taco.tacository.collections.ReadOnlySelectionList;
import com.taco.tacository.collections.SelectionList;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

public class CollectionsSL {
    
    //<editor-fold desc="--- ADD/REMOVE OPERATIONS ---">
    
    //<editor-fold desc="> Add Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@NotNull L list, @Nullable Predicate<E> filter, @NotNull E... elements) {
        return addOrRemoveAll(list, filter, true, elements);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Remove Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@NotNull L list, @Nullable Predicate<E> filter, @NotNull E... elements) {
        return addOrRemoveAll(list, filter, false, elements);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal: Add/Remove Operations">
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, @Nullable Predicate<E> filter, boolean add, @NotNull E... elements) {
        final ArrayList<E> toAddOrRemoveList = new ArrayList<>();
        list.stream().filter(filter != null ? filter : e -> true).forEach(toAddOrRemoveList::add);
        return addOrRemoveAll(list, toAddOrRemoveList, add);
    }
    
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, @NotNull List<E> toAddOrRemove, boolean add) {
        boolean result = add ? list.addAll(toAddOrRemove) : list.removeAll(toAddOrRemove);
        return list;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    
    //<editor-fold desc="--- LEGACY ---">
    
    private final Presets presets;
    
    CollectionsSL() {
        this.presets = new Presets();
    }
    
    public final Presets presets() {
        return this.presets;
    }
    
    //
    
    public <E> ReadOnlyListWrapper<E> boundList(ObservableMap<?, E> map, Lock lock) {
        Exceptions.nullCheck(map, "Map");
        
        ReadOnlyListWrapper<E> bound_list = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        if (lock != null)
            map.addListener(new MapChangeListener<Object, E>() {
                @Override public void onChanged(Change<?, ? extends E> change) {
                    if (change.wasAdded())
                        bound_list.add(change.getValueAdded());
                    if (change.wasRemoved())
                        bound_list.remove(change.getValueRemoved());
                }
            });
        else
            map.addListener(new MapChangeListener<Object, E>() {
                @Override public void onChanged(Change<?, ? extends E> change) {
                    if (change.wasAdded())
                        bound_list.add(change.getValueAdded());
                    if (change.wasRemoved())
                        bound_list.remove(change.getValueRemoved());
                }
            });
        
        return bound_list;
    }
    
    public static final class Presets {
        private Presets() { }
        
        public <E> ReadOnlySelectionList<E> readOnlySelectionArrayList() {
            return readOnlySelectionArrayList(null);
        }
        
        public <E> ReadOnlySelectionList<E> readOnlySelectionArrayList(E initialSelection) {
            return new ReadOnlySelectionList<>(new ArrayList<>(), initialSelection);
        }
        
        public <E> SelectionList<E> selectionArrayList() {
            return selectionArrayList(null);
        }
        
        public <E> SelectionList<E> selectionArrayList(E initialSelection) {
            return new SelectionList<>(new ArrayList<>(), initialSelection);
        }
        
    }
    
    //</editor-fold>
}
