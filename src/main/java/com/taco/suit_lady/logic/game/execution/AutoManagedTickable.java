package com.taco.suit_lady.logic.game.execution;

import com.taco.suit_lady.util.tools.SLExceptions;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

// TODO: Make stateful?
public abstract class AutoManagedTickable<T>
        implements Tickable {
    
    private final T owner;
    private final Predicate<T> autoCancelCondition;
    private long tickCount;
    
    private final ReadOnlyBooleanWrapper cancelledProperty;
    private final ReadOnlyIntegerWrapper priorityProperty;
    
    private final ReadOnlyIntegerWrapper maxTickSkipsProperty;
    private final IntegerBinding maxTickSkipsBinding;
    
    public AutoManagedTickable(@Nullable T owner) {
        this(owner, t -> false);
    }
    
    public AutoManagedTickable(@Nullable T owner, @NotNull Predicate<T> autoCancelCondition) {
        this.owner = owner;
        this.autoCancelCondition = autoCancelCondition;
        this.tickCount = 0;
        
        this.cancelledProperty = new ReadOnlyBooleanWrapper();
        this.priorityProperty = new ReadOnlyIntegerWrapper(0);
        
        this.maxTickSkipsProperty = new ReadOnlyIntegerWrapper(0);
        this.maxTickSkipsBinding = Bindings.createIntegerBinding(() -> getPriority() != 0 ? getMaxTickSkips() : 0, priorityProperty, maxTickSkipsProperty);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final T getOwner() {
        return owner;
    }
    
    public final Predicate<T> getAutoCancelCondition() {
        return autoCancelCondition;
    }
    
    public final long getTickCount() {
        return tickCount;
    }
    
    
    public final ReadOnlyBooleanProperty readOnlyCancelledProperty() {
        return cancelledProperty.getReadOnlyProperty();
    }
    
    public final boolean isCancelled() {
        return cancelledProperty.get();
    }
    
    public final void cancel() {
        cancelledProperty.set(true);
    }
    
    
    /**
     * <p>The priority determines in what order the {@link Ticker} executes {@link Tickable Tickables}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link Tickable Tickables} with a priority closer to 0 are executed first.</li>
     *     <li>{@link Tickable Tickables} with a priority of zero must be executed every tick.</li>
     *     <li>{@link Tickable Tickables} with a priority larger than zero but that has already skipped its specified max number of skips must be executed.</li>
     * </ol>
     */
    public final ReadOnlyIntegerProperty readOnlyPriorityProperty() {
        return priorityProperty.getReadOnlyProperty();
    }
    
    public final int getPriority() {
        return priorityProperty.get();
    }
    
    
    public final ReadOnlyIntegerProperty readOnlyMaxTickSkipsProperty() {
        return maxTickSkipsProperty.getReadOnlyProperty();
    }
    
    public final int getMaxTickSkips() {
        return maxTickSkipsProperty.get();
    }
    
    public final IntegerBinding maxTickSkipsBinding() {
        return maxTickSkipsBinding;
    }
    
    public final int getBoundMaxTickSkips() {
        return maxTickSkipsBinding.get();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void step();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public void tick() {
        if (isCancelled())
            throw SLExceptions.ex("Tickable has already been cancelled!");
        
        if (autoCancelCondition.test(owner))
            cancel();
        else {
            step();
            tickCount++;
        }
    }
    
    //</editor-fold>
}
