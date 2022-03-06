package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

public abstract class TriggerCondition<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent, Predicate<T> {
    
    private final GameViewContent game;
    
    public TriggerCondition(@NotNull GameComponent gameComponent) {
        this.game = gameComponent.getGame();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return game; }
    
    @Override public @NotNull Springable springable() { return getGame(); }
    @Override public @Nullable Lock getLock() { return getGame().getLock(); }
    
    //</editor-fold>
}
