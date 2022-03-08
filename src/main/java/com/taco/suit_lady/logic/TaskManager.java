package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TaskManager<E extends Tickable<E>>
        implements SpringableWrapper, Lockable, Tickable<E> {
    
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
        logiCore().submit(getOwner());
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    @Contract(pure = true) public final @Nullable GFXObject<E> getGfxOwner() { return (owner instanceof GFXObject<?>) ? (GFXObject<E>) owner : null; }
    public final boolean isGfxOwner() { return getGfxOwner() != null; }
    
    
    protected final @NotNull ListProperty<GameTask<E>> tasks() { return tasks; }
    
    
    public final ReadOnlyBooleanProperty readOnlyEnableSynchronizationProperty() { return enableSynchronizationProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return enableSynchronizationProperty.get(); }
    public final boolean setSynchronizationEnabled(boolean newValue) { return Props.setProperty(enableSynchronizationProperty, newValue); }
    
    public final ReadOnlyLongProperty readOnlyTickCountProperty() { return tickCountProperty.getReadOnlyProperty(); }
    public final long getTickCount() { return tickCountProperty.get(); }
    
    
    public final ReadOnlyBooleanProperty readOnlyShutdownProperty() { return shutdownProperty.getReadOnlyProperty(); }
    public final boolean isShutdown() { return shutdownProperty.get(); }
    public final void shutdown() { shutdownProperty.set(true); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Add/Remove Methods">
    
    @Override public final <T extends GameTask<E>> boolean addTask(@NotNull T task) { return addTaskAndGet(task) != null; }
    @Override public final <T extends GameTask<E>> @Nullable T addTaskAndGet(@NotNull T task) {
        return syncIf(() -> {
            if (tasks.add(task))
                return task;
            return null;
        }, this::isSynchronizationEnabled);
    }
    
    @Override public final <T extends GameTask<E>> boolean removeTask(@NotNull T task) { return removeTaskAndGet(task) != null; }
    @Override public final <T extends GameTask<E>> @Nullable T removeTaskAndGet(@NotNull T task) {
        return syncIf(() -> {
            if (tasks.remove(task))
                return task;
            return null;
        }, this::isSynchronizationEnabled);
    }
    
    public final void clear() { syncIf(tasks::clear, this::isSynchronizationEnabled); }
    
    //
    
    public final boolean addShutdownOperation(@NotNull Runnable operation) { return sync(() -> shutdownOperations.add(operation)); }
    public final boolean addGfxShutdownOperation(@NotNull Runnable operation) { return sync(() -> gfxShutdownOperations.add(operation)); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Execution Methods">
    
    //<editor-fold desc=">> Execute Once Methods">
    
    @Override public final @NotNull OneTimeTask<E> executeOnceAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction) {
        return addTaskAndGet(new OneTimeTask<>(getOwner()) {
            @Override protected void tick() { Obj.run(action); }
            @Override protected void shutdown() { Obj.run(onTerminateAction); }
            
            @Override public @Nullable Lock getLock() { return TaskManager.this.getLock(); }
        });
    }
    
    @Override public final @NotNull OneTimeTask<E> executeOnceAndGet(@NotNull Runnable action) { return executeOnceAndGet(action, null); }
    
    //
    
    @Override public final boolean executeOnce(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return executeOnceAndGet(action, onTerminateAction) != null; }
    @Override public final boolean executeOnce(@NotNull Runnable action) { return executeOnceAndGet(action) != null; }
    
    @Override public final <T> OneTimeTask<E> executeOnceAndGet(@NotNull Supplier<T> action, @NotNull Consumer<T> resultResponder) {
        return addTaskAndGet(new OneTimeTask<>(getOwner()) {
            private T value = null;
            
            @Override protected void tick() { value = Obj.get(action); }
            @Override protected void shutdown() { Obj.consume(resultResponder, value); }
            
            @Override public @Nullable Lock getLock() { return TaskManager.this.getLock(); }
        });
    }
    @Override public final <T> boolean executeOnce(@NotNull Supplier<T> action, @NotNull Consumer<T> resultResponder) { return executeOnceAndGet(action, resultResponder) != null; }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Execute Persistent Methods">
    
    @Override public final @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) {
        final Lock lock = getLock();
        return addTaskAndGet(new GameTask<>(getOwner()) {
            @Override protected void tick() { Obj.run(action); }
            @Override protected void shutdown() { Obj.run(onTerminateAction); }
            @Override protected boolean isDone() { return Obj.get(terminateCondition, () -> false); }
            
            @Override public @Nullable Lock getLock() { return lock; } //TODO: Give more control over this
        });
    }
    
    @Override public final @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return executeAndGet(action, onTerminateAction, null); }
    @Override public final @NotNull GameTask<E> executeAndGet(@NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return executeAndGet(action, null, terminateCondition); }
    @Override public final @NotNull GameTask<E> executeAndGet(@NotNull Runnable action) { return executeAndGet(action, null, null); }
    
    //
    
    @Override public final boolean execute(@NotNull Runnable action, @Nullable Runnable onTerminateAction, @Nullable Supplier<Boolean> terminateCondition) { return executeAndGet(action, onTerminateAction, terminateCondition) != null; }
    @Override public final boolean execute(@NotNull Runnable action, @Nullable Runnable onTerminateAction) { return executeAndGet(action, onTerminateAction) != null; }
    @Override public final boolean execute(@NotNull Runnable action, @Nullable Supplier<Boolean> terminateCondition) { return executeAndGet(action, terminateCondition) != null; }
    @Override public final boolean execute(@NotNull Runnable action) { return executeAndGet(action) != null; }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Internal">
    
    void execute() {
        if (!isShutdown()) {
            final ArrayList<GameTask<E>> toRemove = new ArrayList<>();
            syncIf(() -> {
                tasks.forEach(task -> {
                    if (task.isDone())
                        toRemove.add(task);
                    else
                        task.execute();
                });
                for (GameTask<E> eGameTask: toRemove) {
                    tasks.remove(eGameTask);
                    eGameTask.shutdown();
                }
                tickCountProperty.set(getTickCount() + 1);
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
                Printer.err("GFXObject is null  [" + getOwner() + "]");
        }
    }
    
    void shutdownOperations() {
        if (isShutdown())
            sync(() -> {
                //                if (getOwner() instanceof Tickable<?> tickableOwner && !tickableOwner.shutdown())
                //                        throw ExceptionsSL.ex("Shutdown operation failed for Tickable  [" + tickableOwner + "]");
                Printer.print("Running shutdown operation");
                logiCore().removeGfxObject(getGfxOwner());
                for (Runnable shutdownOperation: shutdownOperations)
                    Obj.run(shutdownOperation);
            });
    }
    
    void gfxShutdownOperations() {
        if (isShutdown())
            syncFX(() -> {
                for (Runnable gfxShutdownOperation: gfxShutdownOperations)
                    Obj.run(gfxShutdownOperation);
            });
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull TaskManager<E> taskManager() { return this; }
    
    //
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @Nullable Lock getLock() { return internalLock; }
    
    //</editor-fold>
}
