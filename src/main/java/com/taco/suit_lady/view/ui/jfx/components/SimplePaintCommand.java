package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SimplePaintCommand
        implements PaintCommandable, Lockable
{
    private final ReentrantLock lock;
    
    private final ReadOnlyObjectWrapper<Predicate<BoundCanvas>> autoRemoveConditionProperty;
    private final BooleanProperty activeProperty;
    
    private final ObservableList<BoundCanvas> owners;
    
    public SimplePaintCommand(@NotNull Predicate<BoundCanvas> autoRemoveCondition, @Nullable ReentrantLock lock)
    {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.autoRemoveConditionProperty = new ReadOnlyObjectWrapper<>(ExceptionTools.nullCheck(autoRemoveCondition, "Auto-Remove Condition Input"));
        this.activeProperty = new SimpleBooleanProperty(true);
        
        this.owners = FXCollections.observableArrayList();
        this.activeProperty.addListener((observable, oldValue, newValue) -> repaintOwners());
    }
    
    protected void repaintOwners()
    {
        owners.forEach(canvas -> FXTools.get().runFX(() -> canvas.repaint(), true));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyObjectProperty<Predicate<BoundCanvas>> autoRemoveConditionProperty()
    {
        return autoRemoveConditionProperty.getReadOnlyProperty();
    }
    
    public final @NotNull Predicate<BoundCanvas> getAutoRemoveCondition()
    {
        return autoRemoveConditionProperty.get();
    }
    
    public final void setAutoRemoveCondition(@NotNull Predicate<BoundCanvas> condition)
    {
        autoRemoveConditionProperty.set(ExceptionTools.nullCheck(condition, "Condition"));
    }
    
    public final @NotNull BooleanProperty activeProperty()
    {
        return activeProperty;
    }
    
    public final boolean isActive()
    {
        return activeProperty.get();
    }
    
    public void setActive(boolean isActive)
    {
        activeProperty.set(isActive);
    }
    
    //</editor-fold>
    
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public void onAdded(BoundCanvas canvas)
    {
        sync(() -> { owners.add(canvas); });
    }
    
    @Override
    public void onRemoved(BoundCanvas canvas)
    {
        sync(() -> { owners.remove(canvas); });
    }
    
    @Override
    public final void paint(BoundCanvas canvas)
    {
        if (isActive())
            FXTools.get().runFX(() -> sync(() -> {
                if (getAutoRemoveCondition().test(canvas))
                    canvas.removePaintCommand(this);
                else
                    onPaint(canvas);
            }), true);
    }
    
    //
    
    @Override
    public final @NotNull ReentrantLock getLock()
    {
        return lock;
    }
    
    //</editor-fold>
    
    protected abstract void onPaint(BoundCanvas canvas);
}