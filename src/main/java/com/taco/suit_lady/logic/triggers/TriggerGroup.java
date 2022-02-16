package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public final class TriggerGroup<T extends TriggerEvent<T>>
        implements WrappedGameComponent {
    
    private final TriggerManager manager;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerGroup(@NotNull TriggerManager manager) {
        this.manager = manager;
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- LOGIC ---">
    
    void trigger(@NotNull T event) { triggers.forEach(trigger -> process(event, trigger)); }
    
    boolean register(@NotNull Trigger<T> trigger) { return triggers.add(trigger); }
    boolean unregister(@NotNull Trigger<T> trigger) { return triggers.remove(trigger); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return manager.getGame(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void process(@NotNull T event, @NotNull Trigger<T> trigger) {
        if (trigger.test(event))
            trigger.trigger(event);
    }
    
    //</editor-fold>
}