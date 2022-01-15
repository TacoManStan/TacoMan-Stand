package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.execution.AutoManagedTickable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

//TODO: You're going to have to implement either A* or Dijkstra pathing for this to work for AI-controlled units.
public class TargetedMoveCommand extends AutoManagedTickable<GameObject> {
    
    public TargetedMoveCommand(@Nullable GameObject owner) {
        super(owner);
    }
    
    public TargetedMoveCommand(@Nullable GameObject owner, @NotNull Predicate<GameObject> autoCancelCondition) {
        super(owner, autoCancelCondition);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected void step() {
    
    }
    
    //</editor-fold>
}
