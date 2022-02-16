package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public class TaskManager<E extends Entity>
        implements WrappedGameComponent {
    
    private final E owner;
    private final ListProperty<GameTask<E>> tasks;
    
    private final ReadOnlyBooleanWrapper enableSynchronizationProperty;
    private final ReadOnlyLongWrapper tickCountProperty;
    
    public TaskManager(@NotNull E owner) {
        this.owner = owner;
        this.tasks = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.enableSynchronizationProperty = new ReadOnlyBooleanWrapper(false);
        this.tickCountProperty = new ReadOnlyLongWrapper(0);
    }
    
    void execute() {
        syncIf(() -> tasks.forEach(task -> {
            if (task.isDone())
                tasks.remove(task);
            else
                task.execute();
            tickCountProperty.set(getTickCount() + 1);
        }), this::isSynchronizationEnabled);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    public final ListProperty<GameTask<E>> tasks() { return tasks; }
    
    
    public final ReadOnlyBooleanProperty readOnlyEnableSynchronizationProperty() { return enableSynchronizationProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return enableSynchronizationProperty.get(); }
    public final boolean setSynchronizationEnabled(boolean newValue) { return PropertiesSL.setProperty(enableSynchronizationProperty, newValue); }
    
    public final ReadOnlyLongProperty readOnlyTickCountProperty() { return tickCountProperty.getReadOnlyProperty(); }
    public final long getTickCount() { return tickCountProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
}
