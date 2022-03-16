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
 * <p>Used to construct a {@code matrix} of {@link AStarNode Nodes}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>See the {@link Pathfinding} static utility class for a variety of {@code <i>Utility</i>, <i>Helper</i>, <i>Convenience</i>, and <i>Factory</i> Pathfinding Operations}.</li>
 *     <li>Each {@link AStarNode} contained within the {@link #readOnlyMapMatrixProperty() Map Matrix} is constructed using the {@link #readOnlyNodeFactoryProperty() Node Factory} assigned to this {@link AStarPathfinder}.</li>
 *     <li>The {@link #readOnlyNodeFactoryProperty() Node Factory} tells the {@link #aStar() Pathfinding Algorithm} how to retrieve {@link AStarNode#pathableFrom(AStarNode) Pathability Data} from a {@link #readOnlyRawMatrixProperty() Raw Tile Element}.</li>
 * </ol>
 * <br><hr><br>
 * <p><b>How to Use</b></p>
 * <ol>
 *     <li>Construct an empty {@link AStarPathfinder} instance using the {@link AStarPathfinder#AStarPathfinder() Default Constructor}.</li>
 *     <li>
 *         Assign values to the following properties:
 *         <ol>
 *             <li>
 *                 {@link #readOnlyValidDirectionsProperty() Valid Directions}
 *                 <ul>
 *                     <li><i>If not defined, the {@link #init()} will set the {@link #readOnlyValidDirectionsProperty() Valid Directions} to {@link CardinalDirectionType#ALL_BUT_CENTER ALL_BUT_CENTER}.</i></li>
 *                     <li><i>If the {@link #readOnlyValidDirectionsProperty() Valid Directions} <u>have</u> been defined, the {@link #init()} method uses the defined {@link CardinalDirectionType value}.</i></li>
 *                 </ul>
 *             </li>
 *             <li>{@link #readOnlyRawMatrixProperty() Raw Map Matrix}</li>
 *             <li>{@link #readOnlyStartIndexProperty() Start Map Index}</li>
 *             <li>{@link #readOnlyGoalIndexProperty() Goal Map Index}</li>
 *         </ol>
 *     </li>
 *     <li>
 *         Configure the {@link #readOnlyNodeFactoryProperty() Node Factory} for this {@link AStarPathfinder} using any of the following:
 *         <ol>
 *             <li>Use any of the simplified {@link Pathfinding#factory(Function) Factory Methods} located in the static {@link Pathfinding} utility class.</li>
 *             <li>
 *                 Define a fully-custom {@link #readOnlyNodeFactoryProperty() Node Factory Function} to construct a new {@link AStarNode} instance for each {@link T Raw Tile}.<br>
 *                 The {@link AStarNode} object constructed by the {@link #readOnlyNodeFactoryProperty() Node Factory} must provide {@code definitions} for the following {@code methods}:
 *                 <ol>
 *                     <li>
 *                         {@link AStarNode#edgeCost(AStarNode)}
 *                         <ul>
 *                             <li><i>Default implementation defines the {@link AStarNode#edgeCost(AStarNode) Edge Cost} as the {@link AStarNode#distance(NumExpr2D) Distance} between the calling {@link AStarNode} and specified {@link NumExpr2D} parameter.</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>{@link AStarNode#pathableNeighbors()}</li>
 *                     <li>
 *                         {@link AStarNode#pathableFrom(AStarNode)}
 *                         <ul>
 *                             <li><i>By default, {@link AStarNode#pathableFrom(AStarNode)} checks the {@link AStarNode#pathable() Generic Pathing} for both the calling {@link AStarNode} and the specified {@link AStarNode} parameter.</i></li>
 *                             <li><i>If the {@link #readOnlyRawMatrixProperty() Raw Matrix} assigned to this {@link AStarPathfinder} uses {@code Orientation-Dependent} pathing — i.e., pathing is clear from Tile A to Tile B but blocked from Tile B to Tile A — then a more specific implementation is required.</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>{@link AStarNode#pathable()}</li>
 *                     <li>
 *                         {@link AStarNode#onInit(AStarPathfinder)}
 *                         <ul>
 *                             <li><i>In most cases, the default implementation of {@link AStarNode#onInit(AStarPathfinder)} is sufficient.</i></li>
 *                             <li><i>If the {@link AStarNode#onInit(AStarPathfinder)} is defined, a call to {@code super.onInit()} is necessary for this {@link AStarPathfinder} to function properly.</i></li>
 *                         </ul>
 *                     </li>
 *                     <li>
 *                         {@link AStarNode#matrixIndex()}
 *                         <ul>
 *                             <li><i>In all but every case, the default implementation of {@link AStarNode#matrixIndex()} is sufficient.</i></li>
 *                             <li><i>If {@link AStarNode#matrixIndex()} is overwritten, the {@link Num2D value} it returns must match the actual {@code matrix index} of the {@link AStarNode} for this {@link AStarPathfinder} to function properly.</i></li>
 *                             <li><i>If the {@link Pathfinding#factory(Function) Factory Method} or {@link AStarNode} implementation does not define a default implementation for {@link AStarNode#matrixIndex()}, the {@link Num2D Matrix Index} parameter of the {@link #readOnlyNodeFactoryProperty() Node Factory} should be used to ensure index synchronization, consistency, and ease of use.</i></li>
 *                         </ul>
 *                     </li>
 *                 </ol>
 *                 <i>{@link CachedAStarNode} handles several of the above methods internally.</i>
 *             </li>
 *         </ol>
 *     </li>
 *     <li>Use the {@link #isReady()} method to confirm that all of the required properties for this {@link AStarPathfinder} have been set.</li>
 *     <li>
 *         Once all required properties have been defined, call the {@link #init()} method on this {@link AStarPathfinder} to generate and cache a new {@link #readOnlyMapMatrixProperty() AStarNode Matrix Map}.
 *         <ul>
 *             <li><i>Note that {@link #init()} prepares this {@link AStarPathfinder} for {@link #aStar() Path Generation}, but does not execute any {@code pathfinding logic} itself.</i></li>
 *         </ul>
 *     </li>
 *     <li>Execute the {@link #aStar()} method to use the {@code A* Pathfinding Algorithm} to generate the best {@code path} from the {@link #readOnlyStartIndexProperty() Start Index} to the {@link #readOnlyGoalIndexProperty() Goal Index}.</li>
 *     <li>
 *         Upon completion, the {@link #aStar()} method returns an ordered {@link List} containing the {@link AStarNode Nodes} defining the {@link #aStar() Generated} {@link #readOnlyPathProperty() Path} from the {@link #readOnlyStartIndexProperty() Start Index} to the {@link #readOnlyGoalIndexProperty() Goal Index}.
 *         <ul>
 *             <li><i>The {@link #aStar() Generated} {@link #readOnlyPathProperty() Path} {@link List} can be accessed again at any time by calling the {@link #getPath()} method.</i></li>
 *             <li><i>The {@link #readOnlyPathProperty() Path Property} is reset automatically upon calling the {@link #reset()}, {@link #resetAll()}, or {@link #aStar()} method.</i></li>
 *             <li><i>Calls to {@link #readOnlyPathProperty()} or {@link #getPath()} return a {@code cached} {@link List} instance.</i></li>
 *             <li><i>If the {@link #aStar()} method has not yet been called or this {@link AStarPathfinder} has been {@link #resetAll() reset}, {@link #getPath()} will return {@code null}.</i></li>
 *             <li><i>Alternatively, the {@link #getOrRegenPath()} method can be used to conveniently retrieve a {@code non-null} {@link List Path List} - If the {@link #getPath() Cached Path} is {@code null}, {@link #aStar()} is automatically called to refresh the cache.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * <p><b>Other Instructions</b></p>
 * <ol>
 *     <li>
 *         To reset this {@link AStarPathfinder} so it can be again used to generate a path, call either the {@link #reset()} or {@link #resetAll()} method.
 *         <ul>
 *             <li>
 *                 <i>{@link #reset()} wipes all but the following data from this {@link AStarPathfinder} instance:</i>
 *                 <ul>
 *                     <li><i>{@link #readOnlyValidDirectionsProperty() Valid Directions}</i></li>
 *                     <li><i>{@link #readOnlyNodeFactoryProperty() Node Factory}</i></li>
 *                 </ul>
 *             </li>
 *             <li><i>{@link #resetAll()} sets all values for this {@link AStarPathfinder} instance back to the values defined by the {@link AStarPathfinder#AStarPathfinder() Default Constructor} (typically {@code null}).</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The {@code type} of {@link #readOnlyRawMatrixProperty() Raw Map Data} contained within this {@link AStarPathfinder} instance.
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
    
    
    private final ReadOnlyObjectWrapper<List<AStarNode<T>>> aStarPathProperty;
    
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
        
        this.aStarPathProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public @NotNull AStarPathfinder<T> init() {
        if (getValidDirections() == null)
            setValidDirections(CardinalDirectionType.ALL_BUT_CENTER);
        
        readyCheckPathPoints();
        
        this.startNodeBinding = Bind.objBinding(() -> getNodeAt(getStartIndex()), startIndexProperty);
        this.goalNodeBinding = Bind.objBinding(() -> getNodeAt(getGoalIndex()), goalIndexProperty);
        
        readyCheck(false);
        
        regenerateMapMatrix();
        
        return this;
    }
    
    //
    
    //<editor-fold desc="> Reset Methods">
    
    public @NotNull AStarPathfinder<T> reset() {
        setRawMatrix(null);
        setMapMatrix(null);
        
        resetEndPoints();
        resetSetLists();
        
        setPath(null);
        
        return this;
    }
    
    public @NotNull AStarPathfinder<T> resetAll() {
        setValidDirections(null);
        setNodeFactory(null);
        return reset();
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final boolean isReady() {
        return getValidDirections() != null &&
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
    
    //
    
    protected final @NotNull ReadOnlyObjectProperty<List<AStarNode<T>>> readOnlyPathProperty() { return aStarPathProperty.getReadOnlyProperty(); }
    protected final @Nullable List<AStarNode<T>> getPath() { return aStarPathProperty.get(); }
    protected final @NotNull List<AStarNode<T>> getOrRegenPath() {
        final List<AStarNode<T>> cachedPath = getPath();
        if (cachedPath != null)
            return cachedPath;
        return aStar();
    }
    private @Nullable List<AStarNode<T>> setPath(@Nullable List<AStarNode<T>> newValue) { return Props.setProperty(aStarPathProperty, newValue); }
    
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
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final @Nullable Num2D getDimensions() {
        final T[][] rawMatrix = getRawMatrix();
        if (rawMatrix != null)
            return A.matrixDimensions(rawMatrix);
        return null;
    }
    
    //
    
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull NumExpr2D<?> matrixIndex) { return matrixIndex instanceof AStarNode indexNode ? indexNode : A.grab(matrixIndex, getMapMatrix()); }
    protected final @Nullable AStarNode<T> getNodeAt(@NotNull Number indexX, @NotNull Number indexY) { return getNodeAt(new Num2D(indexX, indexY)); }
    
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
                setPath(path);
                return getPath();
            }
            
            for (AStarNode<T> neighbor: current.pathableNeighbors()) {
                if (neighbor != null && !closedSet.contains(neighbor))
                    if (!openSet.contains(neighbor)) {
                        neighbor.setPreviousNode(current);
                        neighbor.setCostH(neighbor.hCost());
                        neighbor.setCostG(current.gCost(neighbor));
                        openSet.add(neighbor);
                    } else {
                        double gCostCalc = current.gCost(neighbor);
                        if (neighbor.getCostG() >= gCostCalc) {
                            neighbor.setPreviousNode(current);
                            neighbor.setCostG(gCostCalc);
                            openSet.remove(neighbor);
                        }
                    }
            }
            
            if (openSet.isEmpty()) {
                setPath(new ArrayList<>());
                return getPath();
            }
        }
        
        setPath(null);
        return getPath();
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
    
    //
    
    private void resetSetLists() {
        openSet.clear();
        closedSet.clear();
    }
    
    private void resetEndPoints() {
        startNodeBinding = null;
        goalNodeBinding = null;
        
        setStartIndex(null);
        setGoalIndex(null);
    }
    
    //</editor-fold>
}
