package com.taco.suit_lady.ui.jfx.components.painting.paintables;

import com.taco.suit_lady.ui.jfx.components.painting.surfaces.Surface;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class PaintableData<P extends Paintable<P, S>, S extends Surface<P, S>>
        implements SpringableWrapper, Lockable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    private final P owner;
    private final ObjectProperty<S> surfaceProperty;
    
    private final ObjectProperty<Predicate<S>> autoRemoveConditionProperty;
    private final BooleanProperty disabledProperty;
    private final BooleanProperty pausedProperty;
    private final IntegerProperty paintPriorityProperty;
    private final BooleanProperty repaintSurfaceDisabledProperty;
    
    private final BoundsBinding boundsBinding;
    private final BooleanBinding activeBinding;
    
    public PaintableData(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull P owner) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.owner = owner;
        this.surfaceProperty = new SimpleObjectProperty<>();
        
        this.autoRemoveConditionProperty = new SimpleObjectProperty<>();
        this.disabledProperty = new SimpleBooleanProperty(true);
        this.pausedProperty = new SimpleBooleanProperty(false);
        this.paintPriorityProperty = new SimpleIntegerProperty(1);
        this.repaintSurfaceDisabledProperty = new SimpleBooleanProperty(false);
        
        this.boundsBinding = new BoundsBinding();
        this.activeBinding = Bindings.and(Bindings.not(disabledProperty), Bindings.not(pausedProperty));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final P getOwner() { return owner; }
    
    public @NotNull ObjectProperty<S> surfaceProperty() { return surfaceProperty; }
    
    public @NotNull ObjectProperty<Predicate<S>> autoRemoveConditionProperty() { return autoRemoveConditionProperty; }
    public @NotNull BooleanProperty disabledProperty() { return disabledProperty; }
    public @NotNull BooleanProperty pausedProperty() { return pausedProperty; }
    public @NotNull IntegerProperty paintPriorityProperty() { return paintPriorityProperty; }
    public @NotNull BooleanProperty repaintSurfaceDisabledProperty() { return repaintSurfaceDisabledProperty; }
    
    public @NotNull BoundsBinding boundsBinding() { return boundsBinding; }
    public @NotNull BooleanBinding activeBinding() { return activeBinding; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return lock; }
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
}
