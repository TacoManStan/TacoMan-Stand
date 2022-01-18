package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.Overlay;
import com.taco.suit_lady.ui.jfx.components.painting.Paintable;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class PaintCommand
        implements SpringableWrapper, Paintable<PaintCommand, BoundCanvas> {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    
    private final ObjectProperty<BoundCanvas> ownerProperty;
    
    private final ObjectProperty<Predicate<BoundCanvas>> autoRemoveConditionProperty;
    private final BooleanProperty disabledProperty;
    private final IntegerProperty paintPriorityProperty;
    
    private final BoundsBinding boundsBinding;
    
    public PaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.ownerProperty = new SimpleObjectProperty<>();
        
        this.autoRemoveConditionProperty = new ReadOnlyObjectWrapper<>();
        this.disabledProperty = new SimpleBooleanProperty(false);
        this.paintPriorityProperty = new SimpleIntegerProperty(1);
        
        this.boundsBinding = new BoundsBinding();
    }
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onPaint();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Paintable">
    
    @Override public ObjectProperty<BoundCanvas> ownerProperty() { return ownerProperty; }
    
    @Override public ObjectProperty<Predicate<BoundCanvas>> autoRemoveConditionProperty() { return autoRemoveConditionProperty; }
    @Override public BooleanProperty disabledProperty() { return disabledProperty; }
    @Override public IntegerProperty paintPriorityProperty() { return paintPriorityProperty; }
    
    @Override public BoundsBinding boundsBinding() { return boundsBinding; }
    
    
    @Override
    public void paint() {
        if (!isDisabled())
            FXTools.runFX(() -> sync(() -> {
                Predicate<BoundCanvas> autoRemoveCondition = getAutoRemoveCondition();
                if (autoRemoveCondition != null && autoRemoveCondition.test(getOwner()))
                    getOwner().removePaintable(this);
                else
                    onPaint();
            }), true);
    }
    
    
    @Override public void onAdd(BoundCanvas owner) { }
    @Override public void onRemove(BoundCanvas owner) { }
    
    
    //</editor-fold>
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @NotNull Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    @Override public void repaintOwner() {
        BoundCanvas owner = getOwner();
        if (owner != null)
            sync(() -> FXTools.runFX(() -> owner.repaint(), true));
    }
    
    //</editor-fold>
}
