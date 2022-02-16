package com.taco.suit_lady.game.galaxy.events.triggers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public class TriggerGroup<T extends TriggerEvent> {
    
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerGroup() {
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    protected void trigger(@NotNull T event) {
        triggers.forEach(trigger -> trigger.trigger(event));
    }
    
    protected boolean register(@NotNull Trigger<T> trigger) { return triggers.add(trigger); }
    protected boolean unregister(@NotNull Trigger<T> trigger) { return triggers.remove(trigger); }
}
