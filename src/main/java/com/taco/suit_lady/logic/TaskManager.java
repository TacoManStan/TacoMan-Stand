package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager<E extends Entity>
        implements WrappedGameComponent {
    
    private final ReentrantLock internalLock;
    
    private final E owner;
    private final ListProperty<GameTask<E>> tasks;
    
    private final ReadOnlyBooleanWrapper enableSynchronizationProperty;
    private final ReadOnlyLongWrapper tickCountProperty;
    
    private final ReadOnlyBooleanWrapper shutdownProperty;
    private final ListProperty<Runnable> shutdownOperations;
    private final ListProperty<Runnable> gfxShutdownOperations;
    
    public TaskManager(@NotNull E owner) {
        this.internalLock = new ReentrantLock();
        
        this.owner = owner;
        this.tasks = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.enableSynchronizationProperty = new ReadOnlyBooleanWrapper(true);
        this.tickCountProperty = new ReadOnlyLongWrapper(0);
        
        this.shutdownProperty = new ReadOnlyBooleanWrapper(false);
        this.shutdownOperations = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.gfxShutdownOperations = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    void execute() {
        if (!isShutdown())
            syncIf(() -> tasks.forEach(task -> {
                if (task.isDone())
                    tasks.remove(task);
                else
                    task.execute();
                tickCountProperty.set(getTickCount() + 1);
            }), this::isSynchronizationEnabled);
    }
    
    void shutdownOperations() {
        if (isShutdown())
            sync(() -> {
                //                if (getOwner() instanceof Tickable<?> tickableOwner && !tickableOwner.shutdown())
                //                        throw ExceptionsSL.ex("Shutdown operation failed for Tickable  [" + tickableOwner + "]");
                Print.print("Running shutdown operation");
                for (Runnable shutdownOperation: shutdownOperations)
                    shutdownOperation.run();
            });
    }
    
    void gfxShutdownOperations() {
        if (isShutdown())
            syncFX(() -> {
                for (Runnable gfxShutdownOperation: gfxShutdownOperations)
                    gfxShutdownOperation.run();
            });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    public final ListProperty<GameTask<E>> tasks() { return tasks; }
    
    
    public final ReadOnlyBooleanProperty readOnlyEnableSynchronizationProperty() { return enableSynchronizationProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return enableSynchronizationProperty.get(); }
    public final boolean setSynchronizationEnabled(boolean newValue) { return PropertiesSL.setProperty(enableSynchronizationProperty, newValue); }
    
    public final ReadOnlyLongProperty readOnlyTickCountProperty() { return tickCountProperty.getReadOnlyProperty(); }
    public final long getTickCount() { return tickCountProperty.get(); }
    
    
    public final ReadOnlyBooleanProperty readOnlyShutdownProperty() { return shutdownProperty.getReadOnlyProperty(); }
    public final boolean isShutdown() { return shutdownProperty.get(); }
    public final void shutdown() { shutdownProperty.set(true); }
    
    public final boolean addShutdownOperation(@NotNull Runnable operation) { return sync(() -> shutdownOperations.add(operation)); }
    public final boolean addGfxShutdownOperation(@NotNull Runnable operation) { return sync(() -> gfxShutdownOperations.add(operation)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    @Override public @Nullable Lock getLock() { return internalLock; }
    //</editor-fold>
}
