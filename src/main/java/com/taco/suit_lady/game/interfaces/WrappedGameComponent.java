package com.taco.suit_lady.game.interfaces;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

/**
 * <p>Wraps {@link GameComponent}, {@link Springable}, and {@link Lockable} interface implementations into a single {@link WrappedGameComponent interface} for convenience.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Both <i>{@link #springable()}</i> and <i>{@link #getLock()}</i> have {@code default} passthrough implementations.</li>
 *     <li>As a result, the only implementation required is the <i>{@link #getGame()}</i> method.</li>
 * </ol>
 */
public interface WrappedGameComponent
        extends GameComponent, SpringableWrapper, Lockable {
    
    default @Override @Nullable Lock getLock() { return getGame().getLock(); }
    default @Override @NotNull Springable springable() { return getGame(); }
}
