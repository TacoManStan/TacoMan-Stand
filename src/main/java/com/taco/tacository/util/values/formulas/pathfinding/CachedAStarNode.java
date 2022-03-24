package com.taco.tacository.util.values.formulas.pathfinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CachedAStarNode<T>
        extends AStarNode<T> {
    
    private ArrayList<AStarNode<T>> pathableNeighbors;
    
    public CachedAStarNode(@NotNull T wrappedData) {
        super(wrappedData);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void onInit(@NotNull AStarPathfinder<T> pathfinder) { this.pathableNeighbors = new ArrayList<>(pathfinder.getNeighbors(true, this)); }
    @Override protected @NotNull List<AStarNode<T>> pathableNeighbors() { return pathableNeighbors; }
    
    //</editor-fold>
}
