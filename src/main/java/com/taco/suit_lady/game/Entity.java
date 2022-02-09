package com.taco.suit_lady.game;

import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.util.springable.Springable;

public interface Entity
        extends Springable, Tickable, GameComponent {
}