package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <p>An {@code abstract} class used by {@link AStarPathfinder} to define and construct a {@link AStarPathfinder#aStar() traversable} {@link AStarPathfinder#readOnlyMapMatrixProperty() Node Map} representation of a specified {@code matrix} of type <{@link T}>.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link AStarNode} instances are constructed by a {@link AStarPathfinder#readOnlyNodeFactoryProperty() Node Factory} within an {@link AStarPathfinder} instance.</li>
 *     <li>The primary purpose of an {@link AStarNode} is to wrap and process an object of type <{@link T}> into an object that is readable by an {@link AStarPathfinder}.</li>
 *     <li>
 *         To customize the {@code heuristic} function used to calculate the {@code pathing cost} between two {@link AStarNode} objects, overwrite the default implementation of {@link #hCost()}.
 *         <ul>
 *             <li><i>The default {@code heuristic} function is defined as the {@link #distance(NumExpr2D) distance} between the calling {@link AStarNode} and the {@link AStarPathfinder#readOnlyGoalIndexProperty() Goal Index} of the {@link #pathfinder() Pathfinder} object for this {@link AStarNode}.</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         To customize the {@code cost} function used to calculate the cost of crossing the {@code edge} between this {@link AStarNode} and one of its {@link #pathableNeighbors() neighbors}, overwrite the default implementation of {@link #edgeCost(AStarNode)}.
 *         <ul>
 *             <li><i>The default {@code cost} function is defined as the {@link #distance(NumExpr2D) distance} between the calling {@link AStarNode} and the specified {@link AStarNode} {@code parameter}.</i></li>
 *             <li><i>In most cases, the value returned by {@link #hCost()} should follow the same logic pattern as {@link #edgeCost(AStarNode)}.</i></li>
 *             <li><i>Typically, unless the {@link AStarPathfinder#readOnlyMapMatrixProperty() Map Matrix} defined by the {@link AStarPathfinder} containing this {@link AStarNode} uses {@code orientation-dependent pathing data}, both {@link #hCost()} and {@link #edgeCost(AStarNode)} methods can be left as default.</i></li>
 *         </ul>
 *     </li>
 *     <li>The {@link CachedAStarNode} implementation of {@link AStarNode} offers an internally-cached {@link #pathableNeighbors()} {@link List}, eliminating the need to repeatedly re-calculate the value for {@link #pathableNeighbors()}.</li>
 * </ol>
 *
 * @param <T>
 */
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
    protected boolean pathableFrom(@NotNull AStarNode<T> other) { return pathable() && other.pathable(); }
    protected abstract boolean pathable();
    
    protected abstract void onInit(@NotNull AStarPathfinder<T> pathfinder);
    protected final void init(@NotNull AStarPathfinder<T> pathfinder) {
        this.pathfinder = pathfinder;
        onInit(pathfinder);
    }
    
    protected abstract @NotNull Num2D matrixIndex();
    
    //<editor-fold desc="> Default Abstract Methods">
    
    protected double hCost() { return distance(pathfinder.getGoalIndex()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    protected final double gCost() { return previousNode() != null ? (edgeCost(previousNode()) + previousNode().gCost()) : 0; }
    protected final double gCost(@Nullable AStarNode<T> other) { return gCost() + edgeCost(other); }
    
    protected double fCost() { return hCost() + gCost(); }
    
    protected boolean isStart() { return matrixIndex().equalTo(pathfinder().getStartIndex()); }
    protected boolean isGoal() { return matrixIndex().equalTo(pathfinder().getGoalIndex()); }
    
    //
    
    final boolean isPathableFrom(@Nullable AStarNode<T> other) {
        return pathable() && (other == null || (other.pathable() && pathableFrom(other)));
    }
    
    //
    
    public final boolean isInMap(@Nullable AStarPathfinder<?> other) { return Obj.equalsExcludeNull(pathfinder(), other); }
    public final boolean sharesMapWith(@Nullable AStarNode<?> other) { return other != null && Obj.equalsExcludeNull(pathfinder(), other.pathfinder()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Foundational">
    
    @Override public final @Nullable Number a() { return matrixIndex().a(); }
    @Override public final @Nullable Number b() { return matrixIndex().b(); }
    
    //
    
    /**
     * <p>Compares this {@link AStarNode} to the specified {@link AStarNode} parameter.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Internally, this method returns the value of <i>{@link Double#compare(double, double)}.</i></li>
     *     <li>Specifically, the {@link Double#compare(double, double) Compared} value of <i>{@link #fCost() this.fCost()}</i> and <i>{@link #fCost() ((AStarNode) o).fCost()} is used as the {@code result} of {@link #compareTo(AStarNode) this method}.</i></li>
     * </ol>
     *
     * @param o
     *
     * @return
     */
    @Override public final int compareTo(@NotNull AStarNode<T> o) { return Double.compare(fCost(), o.fCost()); }
    
    //</editor-fold>
    
    //</editor-fold>
}
