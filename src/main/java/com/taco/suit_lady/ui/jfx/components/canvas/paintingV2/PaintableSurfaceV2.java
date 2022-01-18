package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady._to_sort._new.Self;
import com.taco.suit_lady.ui.jfx.util.DimensionsBinding;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.property.ListProperty;
import org.jetbrains.annotations.NotNull;

public interface PaintableSurfaceV2<P extends PaintableV2<P, S>, S extends PaintableSurfaceV2<P, S>>
        extends Self<S>, SpringableWrapper, Lockable {
    
    @NotNull PaintableSurfaceDataContainerV2<P, S> data();
    
    @NotNull ListProperty<P> paintablesV2();
    @NotNull S repaintV2();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull DimensionsBinding dimensionsBinding() { return data().dimensionsBinding(); }
    
    //<editor-fold desc="> Operations">
    
    default @NotNull S init() {
        //TODO
        return self();
    }
    
    default boolean addPaintableV2(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ListProperty<P> paintables = paintablesV2();
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
    default boolean removePaintableV2(@NotNull P paintable) {
        return paintable != null && sync(() -> {
            final ListProperty<P> paintables = paintablesV2();
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
    
    //</editor-fold>
}
