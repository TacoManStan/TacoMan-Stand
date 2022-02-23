package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;

public interface Tickable<E extends Tickable<E>>
        extends Springable {
    
    @NotNull TaskManager<E> taskManager();
}
