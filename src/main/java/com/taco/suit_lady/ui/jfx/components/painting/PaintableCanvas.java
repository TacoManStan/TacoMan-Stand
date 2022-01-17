package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.util.Boundable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.tacository.obj_traits.common.Nameable;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@SuppressWarnings({"SuspiciousToArrayCall", "NullableProblems"})
public interface PaintableCanvas
        extends Springable, Lockable, Nameable, ObservableList<Paintable> {
    
    ObservableList<Paintable> paintables();
    
    IntegerBinding widthBinding();
    IntegerBinding heightBinding();
    
    //<editor-fold desc="--- DEFAULT IMPLEMENTATIONS ---">
    
    default int getWidth() {
        return widthBinding().get();
    }
    default int getHeight() {
        return heightBinding().get();
    }
    
    //<editor-fold desc="--- LIST IMPLEMENTATIONS ---">
    
    //List Properties
    
    @Override default int size() { return paintables().size(); }
    @Override default boolean isEmpty() { return paintables().isEmpty(); }
    
    //Element Properties
    
    @Override default boolean contains(Object o) { return paintables().contains(o); }
    @Override default boolean containsAll(@NotNull Collection<?> c) { return paintables().containsAll(c); }
    
    @Override default Paintable get(int index) { return paintables().get(index); }
    
    @Override default int indexOf(Object o) { return paintables().indexOf(o); }
    @Override default int lastIndexOf(Object o) { return paintables().lastIndexOf(o); }
    
    //Adding/Removing/Setting
    
    @Override default boolean add(Paintable paintable) { return paintables().add(paintable); }
    @Override default void add(int index, Paintable element) { paintables().add(index, element); }
    @Override default boolean addAll(Paintable... elements) { return paintables().addAll(elements); }
    @Override default boolean addAll(@NotNull Collection<? extends Paintable> c) { return paintables().addAll(c); }
    @Override default boolean addAll(int index, @NotNull Collection<? extends Paintable> c) { return paintables().addAll(index, c); }
    
    @Override default Paintable set(int index, Paintable element) { return paintables().set(index, element); }
    @Override default boolean setAll(Paintable... elements) { return paintables().setAll(elements); }
    @Override default boolean setAll(Collection<? extends Paintable> col) { return paintables().setAll(col); }
    
    
    @Override default boolean remove(Object o) { return paintables().remove(o); }
    @Override default Paintable remove(int index) { return paintables().remove(index); }
    @Override default void remove(int from, int to) { paintables().remove(from, to); }
    @Override default boolean removeAll(Paintable... elements) { return paintables().removeAll(elements); }
    @Override default boolean removeAll(@NotNull Collection<?> c) { return paintables().removeAll(c); }
    @Override default boolean removeIf(Predicate<? super Paintable> filter) { return paintables().removeIf(filter); }
    
    @Override default boolean retainAll(Paintable... elements) { return paintables().retainAll(elements); }
    @Override default boolean retainAll(@NotNull Collection<?> c) { return paintables().retainAll(c); }
    
    @Override default void replaceAll(UnaryOperator<Paintable> operator) { paintables().replaceAll(operator); }
    
    @Override default void clear() { paintables().clear(); }
    
    //Sorting/Filtering/Copying
    
    @Override default FilteredList<Paintable> filtered(Predicate<Paintable> predicate) { return paintables().filtered(predicate); }
    @Override default SortedList<Paintable> sorted(Comparator<Paintable> comparator) { return paintables().sorted(comparator); }
    @Override default SortedList<Paintable> sorted() { return paintables().sorted(); }
    @Override default void sort(Comparator<? super Paintable> c) { paintables().sort(c); }
    
    @NotNull @Override default Object[] toArray() { return paintables().toArray(); }
    @NotNull @Override default <T> T[] toArray(@NotNull T[] a) { return paintables().toArray(a); }
    @NotNull @Override default List<Paintable> subList(int fromIndex, int toIndex) { return paintables().subList(fromIndex, toIndex); }
    @Override default <T> T[] toArray(IntFunction<T[]> generator) { return paintables().toArray(generator); }
    
    //Listening
    
    @Override default void addListener(InvalidationListener listener) { paintables().addListener(listener); }
    @Override default void removeListener(InvalidationListener listener) { paintables().addListener(listener); }
    
    @Override default void addListener(ListChangeListener<? super Paintable> listener) { paintables().addListener(listener); }
    @Override default void removeListener(ListChangeListener<? super Paintable> listener) { paintables().removeListener(listener); }
    
    //Iteration/Streams
    
    @NotNull @Override default Iterator<Paintable> iterator() { return paintables().iterator(); }
    @Override default Spliterator<Paintable> spliterator() { return paintables().spliterator(); }
    @NotNull @Override default ListIterator<Paintable> listIterator() { return paintables().listIterator(); }
    @NotNull @Override default ListIterator<Paintable> listIterator(int index) { return paintables().listIterator(index); }
    
    
    @Override default Stream<Paintable> stream() { return paintables().stream(); }
    @Override default Stream<Paintable> parallelStream() { return paintables().parallelStream(); }
    
    @Override default void forEach(Consumer<? super Paintable> action) { paintables().forEach(action); }
    
    //</editor-fold>
    
    //</editor-fold>
}
