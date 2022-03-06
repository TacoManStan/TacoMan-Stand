package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class GameTask<E extends Tickable<E>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameViewContent game;
    private final E owner;
    
    private final ReadOnlyLongWrapper tickCountProperty;
    private final ReadOnlyBooleanWrapper synchronizationEnabledProperty;
    
    private final ReadOnlyObjectWrapper<TaskState> stateProperty;
    
    public GameTask(@NotNull E owner) { this(null, owner); }
    
    public GameTask(@Nullable GameComponent gameComponent, @NotNull E owner) {
        if (gameComponent != null)
            this.game = gameComponent.getGame();
        else if (owner instanceof GameComponent gameComponentOwner)
            this.game = gameComponentOwner.getGame();
        else
            throw Exc.ex("GameComponent param is null and owner is not implementation of GameComponent (" + owner + ")");
        this.owner = owner;
        
        this.tickCountProperty = new ReadOnlyLongWrapper(0);
        this.synchronizationEnabledProperty = new ReadOnlyBooleanWrapper(false);
        
        this.stateProperty = new ReadOnlyObjectWrapper<>(TaskState.PRE_EXECUTION);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull E getOwner() { return owner; }
    
    
    public final ReadOnlyLongProperty readOnlyTickCountProperty() { return tickCountProperty.getReadOnlyProperty(); }
    public final long getTickCount() { return tickCountProperty.get(); }
    
    public final ReadOnlyBooleanProperty readOnlySynchronizationEnabledProperty() { return synchronizationEnabledProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return synchronizationEnabledProperty.get(); }
    protected final boolean setSynchronizationEnabled(boolean newValue) { return Props.setProperty(synchronizationEnabledProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<TaskState> readOnlyStateProperty() { return stateProperty.getReadOnlyProperty(); }
    public final @NotNull TaskState getState() { return stateProperty.get(); }
    protected final @NotNull TaskState setState(@NotNull TaskState newValue) { return Props.setProperty(stateProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void tick();
    protected abstract void shutdown();
    
    protected abstract boolean isDone();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return game; }
    
    private Lock lock;
    @Override public @Nullable Lock getLock() {
        if (lock == null)
            lock = new ReentrantLock();
        return lock;
    }
    
    @Override public final @NotNull GameViewContent getGame() { return game; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    final void execute() {
        syncIf(() -> {
            tick();
            tickCountProperty.set(getTickCount() + 1);
        }, this::isSynchronizationEnabled);
    }
    
    //</editor-fold>
}
