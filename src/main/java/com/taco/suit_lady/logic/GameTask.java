package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class GameTask<E extends Tickable<E>>
        implements WrappedGameComponent {
    
    private final GameViewContent game;
    private final E owner;
    
    private final ReadOnlyLongWrapper tickCountProperty;
    private final ReadOnlyBooleanWrapper synchronizationEnabledProperty;
    
    public GameTask(@NotNull E owner) {
        if (owner instanceof GameComponent gameComponent)
            this.game = gameComponent.getGame();
        else
            throw ExceptionsSL.ex("Owner must be an implementation of GameComponent (" + owner + ")");
        this.owner = owner;
    
        this.tickCountProperty = new ReadOnlyLongWrapper(0);
        this.synchronizationEnabledProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    public GameTask(@NotNull GameComponent gameComponent, @NotNull E owner) {
        this.game = gameComponent.getGame();
        this.owner = owner;
        
        this.tickCountProperty = new ReadOnlyLongWrapper(0);
        this.synchronizationEnabledProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    
    
    public final ReadOnlyLongProperty readOnlyTickCountProperty() { return tickCountProperty.getReadOnlyProperty(); }
    public final long getTickCount() { return tickCountProperty.get(); }
    
    public final ReadOnlyBooleanProperty readOnlySynchronizationEnabledProperty() { return synchronizationEnabledProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return synchronizationEnabledProperty.get(); }
    protected final boolean setSynchronizationEnabled(boolean newValue) { return PropertiesSL.setProperty(synchronizationEnabledProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void tick();
    protected abstract void shutdown();
    
    protected abstract boolean isDone();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
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
