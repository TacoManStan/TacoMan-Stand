package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public abstract class AStarNodeV2<T>
        implements Comparable<AStarNodeV2<T>>, NumExpr2D<AStarNodeV2<T>> {
    
    private AStarPathfinderV2<T> pathfinder;
    private final T wrappedData;
    
    private final ReadOnlyObjectWrapper<AStarNodeV2<T>> previousNodeProperty;
    private final ReadOnlyBooleanWrapper pathableProperty;
    
    private double hCost;
    private double gCost;
    
    public AStarNodeV2(@NotNull T wrappedData) {
        this.wrappedData = wrappedData;
        
        this.previousNodeProperty = new ReadOnlyObjectWrapper<>();
        this.pathableProperty = new ReadOnlyBooleanWrapper(true);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull AStarPathfinderV2<T> pathfinder() { return pathfinder; }
    public final @NotNull T data() { return wrappedData; }
    
    //
    
    public final @NotNull ReadOnlyObjectProperty<AStarNodeV2<T>> readOnlyPreviousNodeProperty() { return previousNodeProperty.getReadOnlyProperty(); }
    public final @Nullable AStarNodeV2<T> previousNode() { return previousNodeProperty.get(); }
    protected final @Nullable AStarNodeV2<T> setPreviousNode(@Nullable AStarNodeV2<T> newValue) { return Props.setProperty(previousNodeProperty, newValue); }
    
    public final @NotNull ReadOnlyBooleanProperty readOnlyPathableProperty() { return pathableProperty.getReadOnlyProperty(); }
    public final boolean isPathable() { return pathableProperty.get(); }
    protected final boolean setPathable(boolean newValue) { return Props.setProperty(pathableProperty, newValue); }
    
    //
    
    protected final double getCostH() { return hCost; }
    protected final double refreshCostH() {
        double oldValue = this.hCost;
        this.hCost = hCost();
        return oldValue;
    }
    
    protected final double getCostG() { return hCost; }
    protected final double setCostG(@NotNull Number newValue) {
        double oldValue = this.gCost;
        this.gCost = newValue.doubleValue();
        return oldValue;
    }
    protected final double refreshCostG(@NotNull AStarNodeV2<T> other) {
        double oldValue = this.gCost;
        this.gCost = gCost(other);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected double edgeCost(@NotNull AStarNodeV2<T> other) { return distance(other); }
    
    
    protected abstract @NotNull List<AStarNodeV2<T>> pathableNeighbors();
    
    protected abstract void onInit(@NotNull AStarPathfinderV2<T> pathfinder);
    protected final void init(@NotNull AStarPathfinderV2<T> pathfinder) {
        this.pathfinder = pathfinder;
        onInit(pathfinder);
    }
    
    protected abstract @NotNull Num2D matrixIndex(@NotNull T wrappedData);
    
    //<editor-fold desc="> Default Abstract Methods">
    
    protected double hCost() { return distance(pathfinder.goal()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    protected final double gCost() { return previousNode() != null ? (edgeCost(previousNode()) + previousNode().gCost()) : 0; }
    protected final double gCost(@Nullable AStarNodeV2<T> other) { return gCost() + edgeCost(other); }
    
    protected double fCost() { return hCost() + gCost(); }
    
    protected boolean isStart() { throw Exc.nyi(); }
    protected boolean isGoal() { throw Exc.nyi(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Foundational">
    
    @Override public final @Nullable Number a() { return matrixIndex(data()).a(); }
    @Override public final @Nullable Number b() { return matrixIndex(data()).b(); }
    
    //
    
    @Override public int compareTo(@NotNull AStarNodeV2<T> o) { return Double.compare(fCost(), o.fCost()); }
    
    //</editor-fold>
    
    //</editor-fold>
}
