package com.taco.suit_lady.ui.jfx.components.canvas.painting;

import com.taco.suit_lady.ui.jfx.components.canvas.painting.surface.Surface;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
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
    private final IntegerProperty paintPriorityProperty;
    
    private final BoundsBinding boundsBinding;
    
    public PaintableData(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull P owner) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.owner = owner;
        this.surfaceProperty = new SimpleObjectProperty<>();
        
        this.autoRemoveConditionProperty = new SimpleObjectProperty<>();
        this.disabledProperty = new SimpleBooleanProperty(false);
        this.paintPriorityProperty = new SimpleIntegerProperty(1);
        
        this.boundsBinding = new BoundsBinding();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final P getOwner() { return owner; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    public @NotNull ObjectProperty<S> surfaceProperty() { return surfaceProperty; }
    
    public @NotNull ObjectProperty<Predicate<S>> autoRemoveConditionProperty() { return autoRemoveConditionProperty; }
    public @NotNull BooleanProperty disabledProperty() { return disabledProperty; }
    public @NotNull IntegerProperty paintPriorityProperty() { return paintPriorityProperty; }
    
    public @NotNull BoundsBinding boundsBinding() { return boundsBinding; }
    
    //
    
    public @NotNull Lock getLock() { return lock; }
    public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
}
