package com.taco.suit_lady.logic;

import com.taco.suit_lady.game.Entity;
import org.jetbrains.annotations.NotNull;

public interface Tickable<E extends Entity> {
    
    @NotNull TaskManager<E> taskManager();
}
