package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class TriggerCondition<T extends TriggerEvent<T>>
        implements WrappedGameComponent, Predicate<T> {
    
    private final GameViewContent game;
    
    public TriggerCondition(@NotNull GameComponent gameComponent) {
        this.game = gameComponent.getGame();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return game; }
    
    //</editor-fold>
}
