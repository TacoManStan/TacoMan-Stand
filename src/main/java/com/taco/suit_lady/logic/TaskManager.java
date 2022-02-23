package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager<E extends Tickable<E>>
        implements SpringableWrapper, Lockable {
    
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
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public TaskManager<E> init() {
        Print.print(getGfxOwner() + ":  " + isGfxOwner());
        logiCore().submit(getOwner());
        //        logiCore().addGfxObject(getGfxOwner());
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    void execute() {
        if (!isShutdown()) {
            final ArrayList<GameTask<E>> toRemove = new ArrayList<>();
            syncIf(() -> {
                tasks.forEach(task -> {
                    if (task.isDone())
                        toRemove.add(task);
                    else
                        task.execute();
                    tickCountProperty.set(getTickCount() + 1);
                });
                toRemove.forEach(tasks::remove);
            }, this::isSynchronizationEnabled);
        }
    }
    
    void executeGfx() {
        if (!isShutdown() && isGfxOwner()) {
            final GFXObject<E> gfxObject = getGfxOwner();
            if (gfxObject != null) {
                gfxObject.updateGfx();
                //                    Print.print("Updating Gfx");
            } else
                Print.err("GFXObject is null  [" + getOwner() + "]");
        }
    }
    
    void shutdownOperations() {
        if (isShutdown())
            sync(() -> {
                //                if (getOwner() instanceof Tickable<?> tickableOwner && !tickableOwner.shutdown())
                //                        throw ExceptionsSL.ex("Shutdown operation failed for Tickable  [" + tickableOwner + "]");
                Print.print("Running shutdown operation");
                logiCore().removeGfxObject(getGfxOwner());
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
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    @Contract(pure = true) public final @Nullable GFXObject<E> getGfxOwner() { return (owner instanceof GFXObject<?>) ? (GFXObject<E>) owner : null; }
    public final boolean isGfxOwner() { return getGfxOwner() != null; }
    
    
    //    public final ListProperty<GameTask<E>> tasks() { return tasks; }
    public final boolean addTask(@NotNull GameTask<E> task) { return syncIf(() -> tasks.add(task), this::isSynchronizationEnabled); }
    public final boolean removeTask(@NotNull GameTask<E> task) { return syncIf(() -> tasks.remove(task), this::isSynchronizationEnabled); }
    public final void clearTasks() { syncIf(tasks::clear, this::isSynchronizationEnabled); }
    
    
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
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @Nullable Lock getLock() { return internalLock; }
    
    //</editor-fold>
}
