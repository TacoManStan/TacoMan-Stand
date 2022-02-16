package com.taco.suit_lady.game.galaxy.events.triggers;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public class TriggerGroup<T extends TriggerEvent<T>>
        implements WrappedGameComponent {
    
    private final TriggerManager manager;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerGroup(@NotNull TriggerManager manager) {
        this.manager = manager;
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    protected void trigger(@NotNull T event) {
        triggers.forEach(trigger -> trigger.trigger(event));
    }
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected boolean register(@NotNull Trigger<T> trigger) { return triggers.add(trigger); }
    protected boolean unregister(@NotNull Trigger<T> trigger) { return triggers.remove(trigger); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return manager.getGame(); }
    
    //</editor-fold>
}
