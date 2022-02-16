package com.taco.suit_lady.game;

import com.taco.suit_lady.logic.legacy.TickableMk1;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.util.springable.Springable;

import java.io.Serializable;

public interface Entity
        extends Springable, Serializable, TickableMk1, GameComponent {
}