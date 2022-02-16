package com.taco.suit_lady.game.interfaces;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public interface WrappedGameComponent
        extends GameComponent, SpringableWrapper, Lockable {
    default @Override @Nullable Lock getLock() { return getGame().getLock(); }
    default @Override @NotNull Springable springable() { return getGame(); }
}
