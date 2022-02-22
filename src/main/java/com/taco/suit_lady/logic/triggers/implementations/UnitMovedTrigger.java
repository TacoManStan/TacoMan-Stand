package com.taco.suit_lady.logic.triggers.implementations;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.triggers.Trigger;
import com.taco.suit_lady.logic.triggers.TriggerCondition;
import org.jetbrains.annotations.NotNull;

public abstract class UnitMovedTrigger extends Trigger<UnitMovedEvent> {
    
    public UnitMovedTrigger(@NotNull GameObject owner, @NotNull TriggerCondition<UnitMovedEvent> condition) {
        super(owner, condition);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getOwner() { return (GameObject) super.getOwner(); }
    @Override protected Class<UnitMovedEvent> type() { return UnitMovedEvent.class; }
    
    //</editor-fold>
}
