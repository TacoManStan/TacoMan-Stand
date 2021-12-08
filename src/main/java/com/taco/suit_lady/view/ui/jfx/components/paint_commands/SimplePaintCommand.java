package com.taco.suit_lady.view.ui.jfx.components.paint_commands;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimplePaintCommand
        implements PaintCommandable
{
    private final ReentrantLock lock;
    
    private final ReadOnlyObjectWrapper<Consumer<BoundCanvas>> paintCommandProperty;
    private final ReadOnlyObjectWrapper<Predicate<BoundCanvas>> autoRemoveConditionProperty;
    
    public SimplePaintCommand(@NotNull Consumer<BoundCanvas> paintCommand, @NotNull Predicate<BoundCanvas> autoRemoveCondition, @Nullable ReentrantLock lock)
    {
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.paintCommandProperty = new ReadOnlyObjectWrapper<>(ExceptionTools.nullCheck(paintCommand, "Paint Command Input"));
        this.autoRemoveConditionProperty = new ReadOnlyObjectWrapper<>(ExceptionTools.nullCheck(autoRemoveCondition, "Auto-Remove Condition Input"));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReentrantLock getLock()
    {
        return lock;
    }
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<BoundCanvas>> paintCommandProperty()
    {
        return paintCommandProperty.getReadOnlyProperty();
    }
    
    public final @NotNull Consumer<BoundCanvas> getPaintCommand()
    {
        return paintCommandProperty.get();
    }
    
    public final void setPaintCommand(@NotNull Consumer<BoundCanvas> paintCommand)
    {
        paintCommandProperty.set(ExceptionTools.nullCheck(paintCommand, "Paint Command Input"));
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
    
    //</editor-fold>
    
    @Override
    public final void paint(BoundCanvas canvas)
    {
        FXTools.get().runFX(() -> {
            lock.lock();
            try {
                if (getAutoRemoveCondition().test(canvas))
                    canvas.removePaintCommand(this);
                else
                    getPaintCommand().accept(canvas);
            } finally {
                lock.unlock();
            }
        }, true);
    }
}