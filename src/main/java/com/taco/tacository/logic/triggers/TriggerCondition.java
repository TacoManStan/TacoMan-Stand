package com.taco.tacository.logic.triggers;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

/**
 * <p>Assigned to a {@link Trigger} to define the {@link #test(Object) condition} that must be met for the {@link Trigger} to be {@link Trigger#trigger(TriggerEvent) triggered}.</p>
 * <p><i>See {@link Trigger} for additional information.</i></p>
 *
 * @param <T> The type of {@link TriggerEvent} this {@link TriggerCondition} {@link #test(Object) filters}.
 */
public abstract class TriggerCondition<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent, Predicate<T> {
    
    private final GameViewContent game;
    
    public TriggerCondition(@NotNull GameComponent gameComponent) {
        this.game = gameComponent.getGame();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getContent() { return game; }
    
    @Override public @NotNull Springable springable() { return getGame(); }
    @Override public @Nullable Lock getLock() { return getGame().getLock(); }
    
    //</editor-fold>
}
