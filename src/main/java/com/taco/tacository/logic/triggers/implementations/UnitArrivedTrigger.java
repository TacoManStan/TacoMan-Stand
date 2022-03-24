package com.taco.tacository.logic.triggers.implementations;

import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.logic.triggers.Trigger;
import com.taco.tacository.logic.triggers.TriggerCondition;
import org.jetbrains.annotations.NotNull;

public abstract class UnitArrivedTrigger extends Trigger<UnitArrivedEvent> {
    
    public UnitArrivedTrigger(@NotNull GameObject owner, @NotNull TriggerCondition<UnitArrivedEvent> condition) {
        super(owner, condition);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public GameObject getOwner() { return (GameObject) super.getOwner(); }
    @Override protected Class<UnitArrivedEvent> type() { return UnitArrivedEvent.class; }
    
    //</editor-fold>
}
