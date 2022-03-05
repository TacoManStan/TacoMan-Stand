package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public abstract class TriggerEvent<T extends TriggerEvent<T>>
        implements WrappedGameComponent {
    
    private final GameComponent source;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerEvent(@NotNull GameComponent source) {
        this.source = source;
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public GameComponent getSource() { return source; }
    public final ListProperty<Trigger<T>> triggerList() { return triggers; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return source.getGame(); }
    
    //</editor-fold>
}
