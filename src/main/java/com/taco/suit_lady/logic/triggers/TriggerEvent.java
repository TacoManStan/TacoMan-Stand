package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public abstract class TriggerEvent<T extends TriggerEvent<T>>
        implements WrappedGameComponent {
    
    private final GameViewContent game;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerEvent(@NotNull GameComponent gameComponent) {
        this.game = gameComponent.getGame();
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ListProperty<Trigger<T>> triggers() { return triggers; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return game; }
    
    //</editor-fold>
}
