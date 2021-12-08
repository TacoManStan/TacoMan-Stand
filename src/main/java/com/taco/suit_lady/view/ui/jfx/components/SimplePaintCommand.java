package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class SimplePaintCommand
        implements PaintCommandable
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
    
    public final @NotNull ReentrantLock getLock()
    {
        return lock;
    }
    
    public final @NotNull ReadOnlyObjectProperty<Predicate<BoundCanvas>> autoRemoveConditionProperty()
    {
        return autoRemoveConditionProperty.getReadOnlyProperty();
    }
    
    public final @NotNull Predicate<BoundCanvas> getAutoRemoveCondition()
    {
        return autoRemoveConditionProperty.get();
    }
    
    public final void setAutoRemoveCondition(@NotNull Predicate<BoundCanvas> autoRemoveCondition)
    {
        autoRemoveConditionProperty.set(ExceptionTools.nullCheck(autoRemoveCondition, "Auto-Remove Condition Input"));
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
    
    
    @Override
    public void onAdded(BoundCanvas canvas)
    {
        TaskTools.sync(lock, () -> { owners.add(canvas); });
    }
    
    @Override
    public void onRemoved(BoundCanvas canvas)
    {
        TaskTools.sync(lock, () -> { owners.remove(canvas); });
    }
    
    @Override
    public final void paint(BoundCanvas canvas)
    {
        if (isActive())
            FXTools.get().runFX(() -> {
                lock.lock();
                try {
                    if (getAutoRemoveCondition().test(canvas))
                        canvas.removePaintCommand(this);
                    else
                        onPaint(canvas);
                } finally {
                    lock.unlock();
                }
            }, true);
    }
    
    protected abstract void onPaint(BoundCanvas canvas);
}