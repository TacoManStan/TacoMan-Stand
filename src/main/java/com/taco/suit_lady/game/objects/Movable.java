package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.GameComponent;
import org.jetbrains.annotations.NotNull;

public interface Movable
        extends GameComponent {
    @NotNull Mover mover();
}
