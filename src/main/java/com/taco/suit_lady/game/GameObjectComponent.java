package com.taco.suit_lady.game;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

/**
 * <p>An implementation of {@link GameComponent} defining all implementations as being members/properties of a {@link GameObject} instance.</p>
 * <p><i>See {@link GameComponent} for additional information.</i></p>
 */
public interface GameObjectComponent
        extends SpringableWrapper, Lockable, GameComponent {
    
    @NotNull GameObject getOwner();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default @Override @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    default @Override @Nullable Lock getLock() { return getOwner().getLock(); }
    default @Override @NotNull Springable springable() { return getOwner(); }
    
    //</editor-fold>
}
