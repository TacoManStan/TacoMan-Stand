package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.enums.CardinalDirectionType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class AStarPathfinder<T> {
    
    private final CardinalDirectionType directionType;
    
    private Num2D start;
    private Num2D goal;
    
    private final PriorityQueue<AStarNode<T>> openSet;
    private final PriorityQueue<AStarNode<T>> closedSet;
    
    private final AStarNode<T>[][] nodeMatrix;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public AStarPathfinder(@Nullable CardinalDirectionType directionType, @NotNull BiFunction<Num2D, T, AStarNode<T>> nodeFactory, @NotNull T[][] rawMatrix) {
        this.directionType = directionType != null ? directionType : CardinalDirectionType.ALL_BUT_CENTER;
        
        this.openSet = new PriorityQueue<>();
        this.closedSet = new PriorityQueue<>();
        
        this.nodeMatrix = generateMatrix(nodeFactory, rawMatrix);
    }
    
    public AStarPathfinder(@NotNull BiFunction<Num2D, T, AStarNode<T>> nodeFactory, @NotNull T[][] rawMatrix) {
        this(null, nodeFactory, rawMatrix);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected @NotNull AStarPathfinder<T> init() {
        A.iterateMatrix((matrixIndex, node) -> {
            node.init(this);
            return null;
        }, matrix());
        
        return this;
    }
    
    //</editor-fold>
    
    private @NotNull AStarNode<T>[][] matrix() { return nodeMatrix; }
    
    protected final @NotNull Num2D mapSize() { return A.matrixDimensions(matrix()); }
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull NumExpr2D<?> matrixIndex) { return A.grab(matrixIndex, matrix()); }
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull Number indexX, @NotNull Number indexY) { return getNodeAt(new Num2D(indexX, indexY)); }
    
    protected final @NotNull List<AStarNode<T>> neighbors(@NotNull NumExpr2D<?> matrixIndex) {
        return A.grabNeighbors(matrixIndex, getDirectionType(), matrix());
    }
    
    @SafeVarargs protected final @NotNull List<AStarNode<T>> neighbors(@NotNull NumExpr2D<?> matrixIndex, @Nullable FilterType filterType, @NotNull Predicate<AStarNode<T>>... filters) {
        return A.grabNeighbors(matrixIndex, getDirectionType(), matrix(), filterType, filters);
    }
    
    
    public final @NotNull CardinalDirectionType getDirectionType() { return directionType; }
    
    public @NotNull AStarNode<T>[][] generateMatrix(@NotNull BiFunction<Num2D, T, AStarNode<T>> nodeFactory, @NotNull T[][] inputMap) {
        final Num2D inputDimensions = A.matrixDimensions(inputMap);
        final AStarNode<T>[][] outputMap = new AStarNode[inputDimensions.aI()][inputDimensions.bI()];
        return A.fillMatrix(nodeFactory, inputMap, outputMap);
    }
    
    protected final @NotNull AStarNode<T> startNode() { return A.grab(start(), matrix()); }
    protected final @NotNull AStarNode<T> goalNode() { return A.grab(goal(), matrix()); }
    
    protected final @NotNull Num2D start() { return start; }
    protected final @NotNull Num2D goal() { return goal; }
    
    //
    
    public @NotNull List<AStarNode<T>> aStar(@NotNull Num2D start, @NotNull Num2D goal) {
        this.start = start;
        this.goal = goal;
        this.openSet.add(startNode());
        
        while (!openSet.isEmpty()) {
            AStarNode<T> current = openSet.poll();
            closedSet.add(current);
            
            if (current.isGoal())
                return formPath();
            
            for (AStarNode<T> neighbor: current.pathableNeighbors()) {
                if (neighbor != null && !closedSet.contains(neighbor))
                    if (!openSet.contains(neighbor)) {
                        //                        if (neighbor.isPathable()) {
                        neighbor.setPreviousNode(current);
                        neighbor.setCostH(neighbor.hCost());
                        neighbor.setCostG(current.gCost(neighbor));
                        //                            neighbor.hCost = neighbor.hCost();
                        //                            neighbor.gCost = current.gCost(neighbor);
                        openSet.add(neighbor);
                        //                        }
                    } else {
                        double gCostCalc = current.gCost(neighbor);
                        if (neighbor.getCostG() >= gCostCalc) {
                            neighbor.setPreviousNode(current);
                            neighbor.setCostG(gCostCalc);
                            openSet.remove(neighbor);
                        }
                    }
            }
            
            if (openSet.isEmpty())
                return new ArrayList<>();
        }
        
        return null;
    }
    
    private @NotNull List<AStarNode<T>> formPath() {
        final ArrayList<AStarNode<T>> path = new ArrayList<>();
        AStarNode<T> current = goalNode();
        while (current != null) {
            path.add(current);
            current = current.previousNode();
        }
        return path;
    }
    
    
}
