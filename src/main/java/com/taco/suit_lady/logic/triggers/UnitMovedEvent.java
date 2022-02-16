package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.interfaces.GameComponent;
import org.jetbrains.annotations.NotNull;

public class UnitMovedEvent extends TriggerEvent<UnitMovedEvent> {
    
    public UnitMovedEvent(@NotNull GameComponent gameComponent) {
        super(gameComponent);
    }
}
