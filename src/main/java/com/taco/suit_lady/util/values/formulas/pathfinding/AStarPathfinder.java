package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.enums.CardinalDirectionType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;
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
 *     <li>The {@code matrix} of {@link AStarNode nodes} can then be used to calculate the {@link AStarNode#fCost() best} path from {@link #getStartIndex() start} and {@link #getGoalIndex() goal} coordinates.</li>
 * </ol>
 * <hr>
 * <p><b>How to Use</b></p>
 * <ol>
 *     <li>Construct a new {@link AStarPathfinder} instance using any of the available {@code constructors}.</li>
 *     <li></li>
 * </ol>
 *
 * @param <T>
 */
public class AStarPathfinder<T> {
    
    private final ReadOnlyObjectWrapper<CardinalDirectionType> validDirectionsProperty;
    private final ReadOnlyObjectWrapper<T[][]> rawMatrixProperty;
    private final ReadOnlyObjectWrapper<BiFunction<Num2D, T, AStarNode<T>>> nodeFactoryProperty;
    
    private final ReadOnlyObjectWrapper<Num2D> startIndexProperty;
    private final ReadOnlyObjectWrapper<Num2D> goalIndexProperty;
    
    private ObjectBinding<AStarNode<T>> startNodeBinding;
    private ObjectBinding<AStarNode<T>> goalNodeBinding;
    
    //
    
    private final ReadOnlyObjectWrapper<AStarNode<T>[][]> mapMatrixProperty;
    
    private final PriorityQueue<AStarNode<T>> openSet;
    private final PriorityQueue<AStarNode<T>> closedSet;
    
    public AStarPathfinder() {
        this.validDirectionsProperty = new ReadOnlyObjectWrapper<>();
        this.rawMatrixProperty = new ReadOnlyObjectWrapper<>();
        this.nodeFactoryProperty = new ReadOnlyObjectWrapper<>();
        
        this.startIndexProperty = new ReadOnlyObjectWrapper<>();
        this.goalIndexProperty = new ReadOnlyObjectWrapper<>();
        
        this.mapMatrixProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.openSet = new PriorityQueue<>();
        this.closedSet = new PriorityQueue<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public @NotNull AStarPathfinder<T> init() {
        readyCheckPathPoints();
        
        this.startNodeBinding = Bind.objBinding(() -> getNodeAt(getStartIndex()), startIndexProperty);
        this.goalNodeBinding = Bind.objBinding(() -> getNodeAt(getGoalIndex()), goalIndexProperty);
        
        readyCheck(false);
        
        regenerateMapMatrix();
        
        return this;
    }
    
    public @NotNull AStarPathfinder<T> initDefaults() {
        setValidDirections(CardinalDirectionType.ALL_BUT_CENTER);
        return init();
    }
    
    public @NotNull AStarPathfinder<T> reset() {
        setValidDirections(null);
        setNodeFactory(null);
        
        setRawMatrix(null);
        setMapMatrix(null);
        
        setStartIndex(null);
        setGoalIndex(null);
        
        this.startNodeBinding = null;
        this.goalNodeBinding = null;
        
        this.openSet.clear();
        this.closedSet.clear();
        
        return this;
    }
    
    //</editor-fold>
    
    public final boolean isReady(boolean checkGeneratedMatrix) {
        return (!checkGeneratedMatrix || getMapMatrix() != null) &&
               getValidDirections() != null &&
               getRawMatrix() != null &&
               getStartIndex() != null &&
               getGoalIndex() != null &&
               getStartNode() != null &&
               getGoalNode() != null &&
               getNodeFactory() != null;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected @NotNull ReadOnlyObjectProperty<CardinalDirectionType> readOnlyValidDirectionsProperty() { return validDirectionsProperty.getReadOnlyProperty(); }
    protected @Nullable CardinalDirectionType getValidDirections() { return validDirectionsProperty.get(); }
    protected @Nullable CardinalDirectionType setValidDirections(@Nullable CardinalDirectionType newValue) { return Props.setProperty(validDirectionsProperty, newValue); }
    
    
    private @NotNull ReadOnlyObjectProperty<AStarNode<T>[][]> readOnlyMapMatrixProperty() { return mapMatrixProperty.getReadOnlyProperty(); }
    protected final @Nullable AStarNode<T>[][] getMapMatrix() { return mapMatrixProperty.get(); }
    private @Nullable AStarNode<T>[][] setMapMatrix(@Nullable AStarNode<T>[][] newValue) { return Props.setProperty(mapMatrixProperty, newValue); }
    
    protected final @NotNull ReadOnlyObjectProperty<T[][]> readOnlyRawMatrixProperty() { return rawMatrixProperty.getReadOnlyProperty(); }
    protected final @Nullable T[][] getRawMatrix() { return rawMatrixProperty.get(); }
    protected final @Nullable T[][] setRawMatrix(@Nullable T[][] newValue) { return Props.setProperty(rawMatrixProperty, newValue); }
    
    
    protected final @NotNull ReadOnlyObjectProperty<BiFunction<Num2D, T, AStarNode<T>>> readOnlyNodeFactoryProperty() { return nodeFactoryProperty.getReadOnlyProperty(); }
    protected final @Nullable BiFunction<Num2D, T, AStarNode<T>> getNodeFactory() { return nodeFactoryProperty.get(); }
    protected final @Nullable BiFunction<Num2D, T, AStarNode<T>> setNodeFactory(@Nullable BiFunction<Num2D, T, AStarNode<T>> newValue) { return Props.setProperty(nodeFactoryProperty, newValue); }
    
    //<editor-fold desc="> Start/Goal Properties">
    
    protected final @NotNull ReadOnlyObjectProperty<Num2D> readOnlyStartIndexProperty() { return startIndexProperty.getReadOnlyProperty(); }
    protected final @Nullable Num2D getStartIndex() { return startIndexProperty.get(); }
    protected final @Nullable Num2D setStartIndex(@Nullable Num2D newValue) { return Props.setProperty(startIndexProperty, newValue); }
    
    protected final @NotNull ReadOnlyObjectProperty<Num2D> readOnlyGoalIndexProperty() { return goalIndexProperty.getReadOnlyProperty(); }
    protected final @Nullable Num2D getGoalIndex() { return goalIndexProperty.get(); }
    protected final @Nullable Num2D setGoalIndex(@Nullable Num2D newValue) { return Props.setProperty(goalIndexProperty, newValue); }
    
    
    protected final @Nullable ObjectBinding<AStarNode<T>> startNodeBinding() { return startNodeBinding; }
    protected final @Nullable AStarNode<T> getStartNode() { return startNodeBinding != null ? startNodeBinding.get() : null; }
    
    protected final @Nullable ObjectBinding<AStarNode<T>> goalNodeBinding() { return goalNodeBinding; }
    protected final @Nullable AStarNode<T> getGoalNode() { return goalNodeBinding != null ? goalNodeBinding.get() : null; }
    
    //</editor-fold>
    
    //
    
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull NumExpr2D<?> matrixIndex) { return matrixIndex instanceof AStarNode indexNode ? indexNode : A.grab(matrixIndex, getMapMatrix()); }
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull Number indexX, @NotNull Number indexY) { return getNodeAt(new Num2D(indexX, indexY)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final @Nullable Num2D getDimensions() {
        final T[][] rawMatrix = getRawMatrix();
        if (rawMatrix != null)
            return A.matrixDimensions(rawMatrix);
        return null;
    }
    
    //<editor-fold desc="> Node Neighbor Methods">
    
    @SafeVarargs protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex, @Nullable FilterType filterType, @NotNull Predicate<AStarNode<T>>... filters) {
        return A.grabNeighbors(matrixIndex, getValidDirections(), getMapMatrix(), filterType,
                               checkPathing ? A.concat(filters, neighbor -> neighbor.isPathableFrom(getNodeAt(matrixIndex))) : filters);
    }
    
    protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex) { return getNeighbors(checkPathing, matrixIndex, null, new Predicate[0]); }
    @SafeVarargs protected final @NotNull List<AStarNode<T>> getNeighbors(boolean checkPathing, @NotNull NumExpr2D<?> matrixIndex, @NotNull Predicate<AStarNode<T>>... filters) { return getNeighbors(checkPathing, matrixIndex, null, filters); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Pathfinding Methods">
    
    public @NotNull List<AStarNode<T>> aStar() {
        readyCheck(true);
        
        Timer timer = Timers.newStopwatch().start();
        this.openSet.add(getStartNode());
        
        while (!openSet.isEmpty()) {
            AStarNode<T> current = openSet.poll();
            closedSet.add(current);
            
            if (current.isGoal()) {
                List<AStarNode<T>> path = formPath();
                System.out.println();
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
        AStarNode<T> current = getGoalNode();
        while (current != null) {
            path.add(current);
            current = current.previousNode();
        }
        return L.reversed(path);
    }
    
    public final boolean isOnPath(@NotNull AStarNode<T> node, @NotNull List<AStarNode<T>> path) {
        return path.contains(node);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Image Generation Methods">
    
    public final @NotNull Image generateImage(@NotNull List<AStarNode<T>> path, @NotNull Number tileSize,
                                              @NotNull Function<AStarNode<T>, Image> emptyTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> blockedTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> pathTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> startTileGenerator,
                                              @NotNull Function<AStarNode<T>, Image> goalTileGenerator) {
        return FX.generateTiledImage(tileSize.intValue(), getMapMatrix(), constructAggregateFunction(
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
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void regenerateMapMatrix() {
        readyCheck(false);
        
        final Num2D inputDimensions = A.matrixDimensions(getRawMatrix());
        setMapMatrix(A.fillMatrix(getNodeFactory(), getRawMatrix(), new AStarNode[inputDimensions.aI()][inputDimensions.bI()]));
        A.iterateMatrix((matrixIndex, node) -> {
            node.init(this);
            return null;
        }, getMapMatrix());
    }
    
    //
    
    private void readyCheck(boolean checkGeneratedMatrix) {
        readyCheckPathPoints();
        readyCheckNodeFactory();
        readyCheckValidDirections();
        readyCheckRawMatrix();
        if (checkGeneratedMatrix)
            readyCheckGenMatrix();
    }
    
    private void readyCheckPathPoints() {
        if (getStartIndex() == null && getGoalIndex() == null)
            throw Exc.ex("AStarPathfinder start & goal indexes have not been set.");
        else if (getStartIndex() == null)
            throw Exc.ex("AStarPathfinder start index has not been set.");
        else if (getGoalIndex() == null)
            throw Exc.ex("AStarPathfinder goal index has not been set.");
    }
    
    private void readyCheckNodeFactory() {
        if (getNodeFactory() == null)
            throw Exc.ex("AStarPathfinder Node Factory has not been set.");
    }
    
    private void readyCheckValidDirections() {
        if (getValidDirections() == null)
            throw Exc.ex("AStarPathfinder Valid Directions has not been set.");
    }
    
    private void readyCheckGenMatrix() {
        if (getMapMatrix() == null)
            throw Exc.ex("AStarPathfinder Generated Map Matrix has not been processed - call to AStarPathfinder.init() likely missing.");
    }
    
    private void readyCheckRawMatrix() {
        if (getRawMatrix() == null)
            throw Exc.ex("AStarPathfinder Raw Matrix has not been set.");
    }
    
    //</editor-fold>
}
