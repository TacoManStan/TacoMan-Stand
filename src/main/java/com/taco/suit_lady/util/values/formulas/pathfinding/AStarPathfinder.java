package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.enums.CardinalDirectionType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>Used to construct a {@code matrix} of {@link AStarNode nodes}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@code matrix} of {@link AStarNode nodes} can then be used to calculate the {@link AStarNode#fCost() best} path from {@link #start() start} and {@link #goal() goal} coordinates.</li>
 * </ol>
 * <hr>
 * <p><b>How to Use</b></p>
 * <ol>
 *     <li></li>
 * </ol>
 *
 * @param <T>
 */
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
    
    protected final @NotNull Num2D getMapSize() { return A.matrixDimensions(matrix()); }
    
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull NumExpr2D<?> matrixIndex) { return matrixIndex instanceof AStarNode indexNode ? indexNode : A.grab(matrixIndex, matrix()); }
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull Number indexX, @NotNull Number indexY) { return getNodeAt(new Num2D(indexX, indexY)); }
    
    //<editor-fold desc="> Node Neighbor Methods">
    
    @SafeVarargs protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex, @Nullable FilterType filterType, @NotNull Predicate<AStarNode<T>>... filters) {
        return A.grabNeighbors(matrixIndex, getDirectionType(), matrix(), filterType,
                               checkPathing ? A.concat(filters, neighbor -> neighbor.isPathableFrom(getNodeAt(matrixIndex))) : filters);
    }
    
    protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex) { return getNeighbors(checkPathing, matrixIndex, null, new Predicate[0]); }
    @SafeVarargs protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex, @NotNull Predicate<AStarNode<T>>... filters) { return getNeighbors(checkPathing, matrixIndex, null, filters); }
    
    //</editor-fold>
    
    
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
    
    public @NotNull List<AStarNode<T>> aStar(@NotNull Num2D start, @NotNull Num2D goal, @NotNull Number leniency) {
        Timer timer = Timers.newStopwatch().start();
        this.start = start;
        this.goal = goal;
        this.openSet.add(startNode());
        
        while (!openSet.isEmpty()) {
            AStarNode<T> current = openSet.poll();
            closedSet.add(current);
            
            if (current.isGoal()) {
                List<AStarNode<T>> path = formPath();
                System.out.println();
                System.out.println("Leniency: " + leniency);
                System.out.println("Path Size: " + path.size());
                System.out.println("Path Cost: " + L.last(path).gCost());
                System.out.println("Time: " + timer.getElapsedTime());
                System.out.println();
                return path;
            }
            
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
                        if (neighbor.getCostG() >= gCostCalc + leniency.doubleValue()) {
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
    public @NotNull List<AStarNode<T>> aStar(@NotNull Num2D start, @NotNull Num2D goal) { return aStar(start, goal, 0); }
    
    private @NotNull List<AStarNode<T>> formPath() {
        final ArrayList<AStarNode<T>> path = new ArrayList<>();
        AStarNode<T> current = goalNode();
        while (current != null) {
            path.add(current);
            current = current.previousNode();
        }
        return L.reversed(path);
    }
    
    public final boolean isOnPath(@NotNull AStarNode<T> node, @NotNull List<AStarNode<T>> path) {
        return path.contains(node);
    }
    
    public final @NotNull Image generateImage(@NotNull List<AStarNode<T>> path, @NotNull Number tileSize,
                                              @NotNull Function<AStarNode<T>, Image> emptyTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> blockedTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> pathTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> startTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> goalTileGenerator) {
        return FX.generateTiledImage(tileSize.intValue(), matrix(), constructAggregateFunction(
                path,
                emptyTileGenerator,
                blockedTileGenerator,
                pathTileGenerator,
                startTileGenerator,
                goalTileGenerator), 3);
    }
    
    public final @NotNull Image generateDefaultImage(@NotNull List<AStarNode<T>> path, @NotNull Number tileSize) {
        return generateDefaultImage(path, tileSize, Color.WHITE, Color.BLACK, Color.BLUE, Color.GREEN, Color.RED);
    }
    
    public final @NotNull Image generateDefaultImage(@NotNull List<AStarNode<T>> path, @NotNull Number tileSize,
                                                     @NotNull Color emptyTileColor, @NotNull Color blockedTileColor, @NotNull Color pathTileColor, @NotNull Color startTileColor, @NotNull Color goalTileColor) {
        return generateImage(path, tileSize,
                             node -> FX.generateFilledImage(tileSize, emptyTileColor),
                             node -> FX.generateFilledImage(tileSize, blockedTileColor),
                             node -> FX.generateFilledImage(tileSize, pathTileColor),
                             node -> FX.generateFilledImage(tileSize, startTileColor),
                             node -> FX.generateFilledImage(tileSize, goalTileColor));
    }
    
    private @NotNull Function<AStarNode<T>, Image> constructAggregateFunction(@NotNull List<AStarNode<T>> path,
                                                                              @NotNull Function<AStarNode<T>, Image> emptyTileGenerator,
                                                                              @NotNull Function<AStarNode<T>, Image> blockedTileGenerator,
                                                                              @NotNull Function<AStarNode<T>, Image> pathTileGenerator,
                                                                              @NotNull Function<AStarNode<T>, Image> startTileGenerator,
                                                                              @NotNull Function<AStarNode<T>, Image> goalTileGenerator) {
        return node -> {
            if (node.isStart())
                return startTileGenerator.apply(node);
            else if (node.isGoal())
                return goalTileGenerator.apply(node);
            else if (isOnPath(node, path))
                return pathTileGenerator.apply(node);
            else if (!node.isPathableFrom(null))
                return blockedTileGenerator.apply(node);
            else
                return emptyTileGenerator.apply(node);
        };
    }
}
