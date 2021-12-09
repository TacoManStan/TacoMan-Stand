package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.Lockable;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.property.*;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SLPaintCommand<N extends Node>
        implements Lockable, Nameable, Comparable<SLPaintCommand<N>>
{
    private final ReentrantLock lock;
    private final String name;
    
    private final ReadOnlyObjectWrapper<Overlay> ownerProperty; // Try to decouple this if you can.
    private final ReadOnlyObjectWrapper<N> nodeProperty;
    
    private final Predicate<? super SLPaintCommand<N>> autoRemoveCondition;
    private final BooleanProperty activeProperty;
    
    private final IntegerProperty paintPriorityProperty;
    
    private final boolean scaleToParent;
    
    public SLPaintCommand(@Nullable ReentrantLock lock, @NotNull String name, @Nullable Predicate<? super SLPaintCommand<N>> autoRemoveCondition, boolean scaleToParent, int priority)
    {
        this.lock = lock;
        this.name = name;
        
        this.ownerProperty = new ReadOnlyObjectWrapper<>();
        this.nodeProperty = new ReadOnlyObjectWrapper<>();
        
        this.autoRemoveCondition = autoRemoveCondition != null ? autoRemoveCondition : paintCommand -> true;
        this.activeProperty = new SimpleBooleanProperty(false);
        
        this.paintPriorityProperty = new SimpleIntegerProperty();
        
        this.scaleToParent = scaleToParent;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Overlay> ownerProperty()
    {
        return ownerProperty.getReadOnlyProperty();
    }
    
    protected final Predicate<? super SLPaintCommand<N>> getAutoRemoveCondition()
    {
        return autoRemoveCondition;
    }
    
    public final BooleanProperty activeProperty()
    {
        return activeProperty;
    }
    
    public final SLPaintCommand<N> activate()
    {
        activeProperty.set(true);
        return this;
    }
    
    public final SLPaintCommand<N> deactivate()
    {
        activeProperty.set(false);
        return this;
    }
    
    public final boolean isScaleToParent()
    {
        return scaleToParent;
    }
    
    public final IntegerProperty paintPriorityProperty()
    {
        return paintPriorityProperty;
    }
    
    public final int getPaintPriority()
    {
        return paintPriorityProperty.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract N getPaintNode();
    
    protected abstract void onAdded(@NotNull OverlayHandler owner);
    
    protected abstract void onRemoved(@NotNull OverlayHandler owner);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull Lock getLock()
    {
        return lock != null ? lock : new ReentrantLock();
    }
    
    @Override
    public final String getName()
    {
        return name;
    }
    
    //
    
    @Override
    public int compareTo(@NotNull SLPaintCommand<N> o)
    {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
}
