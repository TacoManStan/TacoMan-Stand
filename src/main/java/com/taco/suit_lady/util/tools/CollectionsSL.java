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
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CollectionsSL {
    
    //<editor-fold desc="--- ADD/REMOVE OPERATIONS ---">
    
    //<editor-fold desc="> Add Operations">
    
    //<editor-fold desc="> Add All Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Predicate<E> filter,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, passedElementOperation, failedElementOperation, true, elements);
    }
    
    //
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Predicate<E> filter,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Predicate<E> filter,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Predicate<E> filter,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable L targetList,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock,
                                                           @NotNull L list,
                                                           @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                           @NotNull E... elements) {
        return addOrRemoveAll(lock, list, passedElementOperation, failedElementOperation, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@Nullable Lock lock, @NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, true, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L addAll(@NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(list, true, elements);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Remove Operations">
    
    //<editor-fold desc="> Remove All Operations">
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Predicate<E> filter,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, passedElementOperation, failedElementOperation, false, elements);
    }
    
    //
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Predicate<E> filter,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Predicate<E> filter,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Predicate<E> filter,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, filter, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable L targetList,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock,
                                                              @NotNull L list,
                                                              @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                              @NotNull E... elements) {
        return addOrRemoveAll(lock, list, passedElementOperation, failedElementOperation, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@Nullable Lock lock, @NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, false, elements);
    }
    
    @SafeVarargs
    public static <E, L extends List<E>> @NotNull L removeAll(@NotNull L list, @NotNull E... elements) {
        return addOrRemoveAll(list, false, elements);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal: Add/Remove Operations">
    
    //<editor-fold desc="> Add or Remove All Operations">
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Predicate<E> filter,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return TasksSL.sync(lock, () -> {
            if (targetList != null && !targetList.isEmpty())
                throw Exceptions.unsupported("Target list must be null or empty.");
            
            final L zeList = targetList != null ? targetList : list;
            final ArrayList<E> passedList = new ArrayList<>();
            final ArrayList<E> failedList = new ArrayList<>();
            
            zeList.stream().filter(filter != null ? filter : e -> true).forEach(passedList::add);
            zeList.stream().filter(e -> !passedList.contains(e)).forEach(failedList::add);
            
            if (passedElementOperation != null)
                passedList.forEach(passedElementOperation);
            if (failedElementOperation != null)
                failedList.forEach(failedElementOperation);
            
            return addOrRemoveAll(zeList, passedList, add);
        }, true);
    }
    
    //
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Predicate<E> filter,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, filter, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Predicate<E> filter,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, filter, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Predicate<E> filter,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, filter, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable L targetList,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, targetList, null, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock,
                                                                    @NotNull L list,
                                                                    @Nullable Consumer<E> passedElementOperation, @Nullable Consumer<E> failedElementOperation,
                                                                    boolean add,
                                                                    @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, null, passedElementOperation, failedElementOperation, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@Nullable Lock lock, @NotNull L list, boolean add, @NotNull E... elements) {
        return addOrRemoveAll(lock, list, null, null, null, null, add, elements);
    }
    
    @SafeVarargs
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, boolean add, @NotNull E... elements) {
        return addOrRemoveAll(null, list, null, null, null, null, add, elements);
    }
    
    //
    
    private static <E, L extends List<E>> @NotNull L addOrRemoveAll(@NotNull L list, @NotNull List<E> toAddOrRemove, boolean add) {
        boolean result = add ? list.addAll(toAddOrRemove) : list.removeAll(toAddOrRemove);
        return list;
    }
    
    //</editor-fold>
    
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
