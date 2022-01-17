package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.tacository.obj_traits.common.Nameable;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

public interface PaintableCanvas<P extends Paintable<P, O>, O extends PaintableCanvas<P, O>>
        extends Springable, Lockable, Nameable {
    
    ObservableList<P> paintables();
    
    default boolean addPaintable(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ObservableList<P> paintables = paintables();
            if (paintable.hasOwner()) {
                if (paintable.getOwner().equals(this))
                    return true;
                else {
                    System.out.println("WARNING: Paintable has owner but owner is not this PaintableCanvas!  [" + paintable.getOwner() + "]");
                    return false;
                }
            } else if (paintables.contains(paintable))
                return true;
            else {
                final boolean added = paintables.add(paintable);
                
                paintable.setOwner((O) this);
                paintable.onAdd((O) this);
                
                return added;
            }
        });
    }
    default boolean removePaintable(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ObservableList<P> paintables = paintables();
            if (paintables.contains(paintable)) {
                final boolean removed = paintables.remove(paintable);
                
                paintable.setOwner(null);
                paintable.onRemove((O) this);
                
                return removed;
            } else
                return false;
        });
    }
    
    IntegerBinding widthBinding();
    IntegerBinding heightBinding();
    
    
    void repaint();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default O init() { return (O) this; }
    
    default ObservableList<P> sortAndGet() {
        return sync(() -> {
            FXCollections.sort(paintables());
            return paintables();
        });
    }
    
    //<editor-fold desc="--- LIST IMPLEMENTATIONS (UNUSED) ---">
    
    //List Properties
    //
    //    @Override default int size() { return paintables().size(); }
    //    @Override default boolean isEmpty() { return paintables().isEmpty(); }
    //
    //    //Element Properties
    //
    //    @Override default boolean contains(Object o) { return paintables().contains(o); }
    //    @Override default boolean containsAll(@NotNull Collection<?> c) { return paintables().containsAll(c); }
    //
    //    @Override default P get(int index) { return paintables().get(index); }
    //
    //    @Override default int indexOf(Object o) { return paintables().indexOf(o); }
    //    @Override default int lastIndexOf(Object o) { return paintables().lastIndexOf(o); }
    //
    //    //Adding/Removing/Setting
    //
    //    @Override default boolean add(P paintable) { return paintables().add(paintable); }
    //    @Override default void add(int index, P element) { paintables().add(index, element); }
    //    @Override default boolean addAll(P... elements) { return paintables().addAll(elements); }
    //    @Override default boolean addAll(@NotNull Collection<? extends P> c) { return paintables().addAll(c); }
    //    @Override default boolean addAll(int index, @NotNull Collection<? extends P> c) { return paintables().addAll(index, c); }
    //
    //    @Override default P set(int index, P element) { return paintables().set(index, element); }
    //    @Override default boolean setAll(P... elements) { return paintables().setAll(elements); }
    //    @Override default boolean setAll(Collection<? extends P> col) { return paintables().setAll(col); }
    //
    //
    //    @Override default boolean remove(Object o) { return paintables().remove(o); }
    //    @Override default P remove(int index) { return paintables().remove(index); }
    //    @Override default void remove(int from, int to) { paintables().remove(from, to); }
    //    @Override default boolean removeAll(P... elements) { return paintables().removeAll(elements); }
    //    @Override default boolean removeAll(@NotNull Collection<?> c) { return paintables().removeAll(c); }
    //    @Override default boolean removeIf(Predicate<? super P> filter) { return paintables().removeIf(filter); }
    //
    //    @Override default boolean retainAll(P... elements) { return paintables().retainAll(elements); }
    //    @Override default boolean retainAll(@NotNull Collection<?> c) { return paintables().retainAll(c); }
    //
    //    @Override default void replaceAll(UnaryOperator<P> operator) { paintables().replaceAll(operator); }
    //
    //    @Override default void clear() { paintables().clear(); }
    //
    //    //Sorting/Filtering/Copying
    //
    //    @Override default FilteredList<P> filtered(Predicate<P> predicate) { return paintables().filtered(predicate); }
    //    @Override default SortedList<P> sorted(Comparator<P> comparator) { return paintables().sorted(comparator); }
    //    @Override default SortedList<P> sorted() { return paintables().sorted(); }
    //    @Override default void sort(Comparator<? super P> c) { paintables().sort(c); }
    //
    //    @NotNull @Override default Object[] toArray() { return paintables().toArray(); }
    //    @NotNull @Override default <T> T[] toArray(@NotNull T[] a) { return paintables().toArray(a); }
    //    @NotNull @Override default List<P> subList(int fromIndex, int toIndex) { return paintables().subList(fromIndex, toIndex); }
    //    @Override default <T> T[] toArray(IntFunction<T[]> generator) { return paintables().toArray(generator); }
    //
    //    //Listening
    //
    //    @Override default void addListener(InvalidationListener listener) { paintables().addListener(listener); }
    //    @Override default void removeListener(InvalidationListener listener) { paintables().addListener(listener); }
    //
    //    @Override default void addListener(ListChangeListener<? super P> listener) { paintables().addListener(listener); }
    //    @Override default void removeListener(ListChangeListener<? super P> listener) { paintables().removeListener(listener); }
    //
    //    //Iteration/Streams
    //
    //    @NotNull @Override default Iterator<P> iterator() { return paintables().iterator(); }
    //    @Override default Spliterator<P> spliterator() { return paintables().spliterator(); }
    //    @NotNull @Override default ListIterator<P> listIterator() { return paintables().listIterator(); }
    //    @NotNull @Override default ListIterator<P> listIterator(int index) { return paintables().listIterator(index); }
    //
    //
    //    @Override default Stream<P> stream() { return paintables().stream(); }
    //    @Override default Stream<P> parallelStream() { return paintables().parallelStream(); }
    //
    //    @Override default void forEach(Consumer<? super P> action) { paintables().forEach(action); }
    
    //</editor-fold>
    
    //</editor-fold>
}
