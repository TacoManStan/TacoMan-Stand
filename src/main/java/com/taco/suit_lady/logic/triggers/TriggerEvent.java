package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public abstract class TriggerEvent<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
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
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final void submit() { triggers().submit((T) this); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return source.getGame(); }
    
    @Override public @NotNull Springable springable() { return getGame(); }
    @Override public @Nullable Lock getLock() { return getGame().getLock(); }
    
    //</editor-fold>
}
