package com.taco.tacository.util.values.formulas.pathfinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AStarNodeable {
    
    @NotNull AStarNode aStarNode();
    @NotNull List<AStarNode> aStarNeighbors();
}
