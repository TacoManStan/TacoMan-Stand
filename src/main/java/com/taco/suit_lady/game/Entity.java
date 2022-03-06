package com.taco.suit_lady.game;

import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;

import java.io.Serializable;

public interface Entity
        extends Springable, Lockable, Serializable, GameComponent {
}