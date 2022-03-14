package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AStarNode<T>
        implements Comparable<AStarNode<T>>, NumExpr2D<AStarNode<T>> {
    
    private AStarPathfinder<T> pathfinder;
    private final T wrappedData;
    
    private final ReadOnlyObjectWrapper<AStarNode<T>> previousNodeProperty;
    
    private double hCost;
    private double gCost;
    
    public AStarNode(@NotNull T wrappedData) {
        this.wrappedData = wrappedData;
        
        this.previousNodeProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull AStarPathfinder<T> pathfinder() { return pathfinder; }
    public final @NotNull T data() { return wrappedData; }
    
    //
    
    public final @NotNull ReadOnlyObjectProperty<AStarNode<T>> readOnlyPreviousNodeProperty() { return previousNodeProperty.getReadOnlyProperty(); }
    public final @Nullable AStarNode<T> previousNode() { return previousNodeProperty.get(); }
    protected final @Nullable AStarNode<T> setPreviousNode(@Nullable AStarNode<T> newValue) { return Props.setProperty(previousNodeProperty, newValue); }
    
    //
    
    protected final double getCostH() { return hCost; }
    protected final double setCostH(@NotNull Number newValue) {
        double oldValue = getCostH();
        this.hCost = newValue.doubleValue();
        return oldValue;
    }
    
    protected final double getCostG() { return gCost; }
    protected final double setCostG(@NotNull Number newValue) {
        double oldValue = getCostG();
        this.gCost = newValue.doubleValue();
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected double edgeCost(@NotNull AStarNode<T> other) { return distance(other); }
    
    
    protected abstract @NotNull List<AStarNode<T>> pathableNeighbors();
    protected abstract boolean pathableFrom(@NotNull AStarNode<T> other);
    protected abstract boolean pathable();
    
    protected abstract void onInit(@NotNull AStarPathfinder<T> pathfinder);
    protected final void init(@NotNull AStarPathfinder<T> pathfinder) {
        this.pathfinder = pathfinder;
        onInit(pathfinder);
    }
    
    protected abstract @NotNull Num2D matrixIndex();
    
    //<editor-fold desc="> Default Abstract Methods">
    
    protected double hCost() { return distance(pathfinder.goal()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    protected final double gCost() { return previousNode() != null ? (edgeCost(previousNode()) + previousNode().gCost()) : 0; }
    protected final double gCost(@Nullable AStarNode<T> other) { return gCost() + edgeCost(other); }
    
    protected double fCost() { return hCost() + gCost(); }
    
    protected boolean isStart() { return matrixIndex().equalTo(pathfinder().start()); }
    protected boolean isGoal() { return matrixIndex().equalTo(pathfinder().goal()); }
    
    //
    
    final boolean isPathableFrom(@NotNull AStarNode<T> other) {
        return pathable() && other.pathable() && pathableFrom(other);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Foundational">
    
    @Override public final @Nullable Number a() { return matrixIndex().a(); }
    @Override public final @Nullable Number b() { return matrixIndex().b(); }
    
    //
    
    @Override public int compareTo(@NotNull AStarNode<T> o) { return Double.compare(fCost(), o.fCost()); }
    
    //</editor-fold>
    
    //</editor-fold>
}
