package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class GameTask<E extends Entity>
        implements WrappedGameComponent {
    
    private final E owner;
    private final ReadOnlyBooleanWrapper doneProperty;
    
    public GameTask(@NotNull E owner) {
        this.owner = owner;
        this.doneProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    
    public final ReadOnlyBooleanProperty readOnlyDoneProperty() { return doneProperty.getReadOnlyProperty(); }
    public final boolean isDone() { return doneProperty.get(); }
    public final void complete() { doneProperty.set(true); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void execute();
    protected abstract void shutdown();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
}
