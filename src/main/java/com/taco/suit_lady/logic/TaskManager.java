package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public class TaskManager<E extends Entity>
        implements WrappedGameComponent {
    
    private final E owner;
    private final ListProperty<GameTask<E>> tasks;
    
    private final ReadOnlyBooleanWrapper enableSynchronizationProperty;
    
    public TaskManager(@NotNull E owner) {
        this.owner = owner;
        this.tasks = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.enableSynchronizationProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    void execute() {
        syncIf(() -> tasks.forEach(task -> {
            if (task.isDone())
                tasks.remove(task);
            else
                task.execute();
        }), this::isSynchronizationEnabled);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final E getOwner() { return owner; }
    public final ListProperty<GameTask<E>> tasks() { return tasks; }
    
    public final ReadOnlyBooleanProperty readOnlyEnableSynchronizationProperty() { return enableSynchronizationProperty.getReadOnlyProperty(); }
    public final boolean isSynchronizationEnabled() { return enableSynchronizationProperty.get(); }
    public final boolean setSynchronizationEnabled(boolean newValue) { return PropertiesSL.setProperty(enableSynchronizationProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
}
