package com.taco.suit_lady.ui.jfx.components.painting.surfaces;

import com.taco.suit_lady._to_sort._new.Self;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.Paintable;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ListProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public interface Surface<P extends Paintable<P, S>, S extends Surface<P, S>>
        extends Self<S>, SpringableWrapper, Lockable {
    
    @NotNull SurfaceData<P, S> data();
    
    @NotNull S repaint();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull ListProperty<P> paintables() { return data().paintables(); }
    
    default @NotNull IntegerBinding widthBinding() { return data().widthBinding(); }
    default @NotNull IntegerBinding heightBinding() { return data().heightBinding(); }
    
    //<editor-fold desc="> Implementations">
    
    default @Override @NotNull Lock getLock() { return data().getLock(); }
    default @Override @NotNull Springable springable() { return data().springable(); }
    
    //</editor-fold>
    
    default @NotNull S init() {
        //TODO
        return self();
    }
    
    default boolean addPaintable(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ListProperty<P> paintables = paintables();
            if (paintable.getSurface() != null) {
                if (paintable.getSurface().equals(this))
                    return true;
                else {
                    System.out.println("WARNING: Paintable has owner but owner is not this PaintableCanvas!  [" + paintable.getSurface() + "]");
                    return false;
                }
            } else if (paintables.contains(paintable))
                return true;
            else {
                final boolean added = paintables.add(paintable);
                
                paintable.setSurface(self());
                paintable.onAdd(self());
                
                return added;
            }
        });
    }
    default boolean removePaintable(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ListProperty<P> paintables = paintables();
            if (paintables.contains(paintable)) {
                final boolean removed = paintables.remove(paintable);
                
                paintable.setSurface(null);
                paintable.onRemove(self());
                
                return removed;
            } else
                return false;
        });
    }
    
    //</editor-fold>
}
