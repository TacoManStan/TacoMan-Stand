package com.taco.suit_lady.game;

import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.util.springable.Springable;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;

public interface Entity
        extends Springable, Serializable, Tickable, GameComponent {
}