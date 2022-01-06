package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.execution.Tickable;
import com.taco.suit_lady.logic.game.interfaces.Attributable;
import com.taco.suit_lady.util.springable.Springable;

public interface Entity
        extends Springable, Attributable, Tickable {
}