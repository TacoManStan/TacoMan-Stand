package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.values.enums.CardinalDirection;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AStarNode
implements Comparable<AStarNode>{
    
    private final AStarPathfinder owner;
    private final Num2D wrappedData;
    
    private AStarNode previous;
    
    double hCost;
    double gCost;
    
    private boolean pathable;
    
    public AStarNode(@NotNull AStarPathfinder owner, @NotNull Num2D wrappedData, boolean pathable) {
        this.owner = owner;
        this.wrappedData = wrappedData;
        
        this.previous = null;
        
        this.pathable = pathable;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final AStarPathfinder getOwner() { return owner; }
    public final Num2D getData() { return wrappedData; }
    
    public final @Nullable AStarNode getPrevious() { return previous; }
    public final @Nullable AStarNode setPrevious(@Nullable AStarNode newValue) {
        AStarNode oldValue = getPrevious();
        this.previous = newValue;
        return oldValue;
    }
    
    public final boolean isPathable() { return pathable; }
    public final boolean setPathable(boolean newValue) {
        boolean oldValue = isPathable();
        this.pathable = newValue;
        return oldValue;
    }
    
    //</editor-fold>
    
    public final List<AStarNode> neighbors() {
        throw Exc.nyi();
    }
    
    public final @Nullable AStarNode getNeighbor(@NotNull CardinalDirection direction) {
        return getOwner().getNeighbor(getData(), direction);
    }
    
    public final double hCost() {
        return Math.abs(getData().distance(getOwner().getGoal().getData()));
    }
    
    public final double gCost() {
        return Math.abs(getOwner().getStart().getData().distance(getData()));
    }
    
    public final double gCost(@NotNull AStarNode other) {
        return gCost() + cost(other);
    }
    
    public final double fCost() {
        return gCost() + hCost();
    }
    
    public final double cost(@NotNull AStarNode other) {
//        if (!isNeighbor(other))
//            throw Exc.unsupported("Input Node is not a Neighbor [" + this + "  |  " + other + "]");
        return other.isPathable() ? 10 : 10000000; //TODO
    }
    
    public final boolean isStart() { return getData().equalTo(getOwner().getStart().getData()); }
    public final boolean isGoal() { return getData().equalTo(getOwner().getGoal().getData()); }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public int compareTo(@NotNull AStarNode o) {
        final double fCost = fCost();
        final double fCostOther = o.fCost();
        if (fCost < fCostOther)
            return -1;
        else if (fCost > fCostOther)
            return 1;
        else
            return 0;
    }
    @Override public String toString() {
        return getData().a() + ", " + getData().b();
    }
    @Override public boolean equals(Object obj) {
        return getData().equalTo(((AStarNode) obj).getData());
    }
    
    //</editor-fold>
}
