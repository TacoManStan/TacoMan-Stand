package com.taco.tacository.game;

import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

/**
 * <p>An implementation of {@link GameComponent} defining all implementations as being members/properties of a {@link GameObject} instance.</p>
 * <blockquote><i>See {@link GameComponent} for additional information.</i></blockquote>
 */
public interface GameObjectComponent
        extends SpringableWrapper, Lockable, GameComponent {
    
    /**
     * <p>Returns the {@link GameObject} owner containing this {@link GameObjectComponent}.</p>
     *
     * @return The {@link GameObject} owner containing this {@link GameObjectComponent}.
     */
    @NotNull GameObject getOwner();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #getOwner()}<b>.</b>{@link GameObject#getGame() getGame()}</i></blockquote>
     */
    default @Override @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #getOwner()}<b>.</b>{@link GameObject#getLock() getLock()}</i></blockquote>
     */
    default @Override @Nullable Lock getLock() { return getOwner().getLock(); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #getOwner()}</i></blockquote>
     */
    default @Override @NotNull Springable springable() { return getOwner(); }
    
    //</editor-fold>
}
