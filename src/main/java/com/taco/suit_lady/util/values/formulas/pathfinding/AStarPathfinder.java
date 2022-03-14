package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.enums.CardinalDirectionType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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
    
    private @NotNull AStarPathfinder<T> init() {
        A.iterateMatrix((matrixIndex, node) -> {
            node.init(this);
            return null;
        }, matrix());
        
        return this;
    }
    
    //</editor-fold>
    
    private @NotNull AStarNode<T>[][] matrix() { return nodeMatrix; }
    
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
    
    private static final boolean PRINT_INDEX = false;
    
    private static @NotNull DummyElement[][] generateTestMatrix() {
        final DummyElement[][] matrix = A.fillMatrix(matrixIndex -> {
            return new DummyElement(matrixIndex, true);
        }, new DummyElement[100][100]);
        
        for (int i = 10; i < 90; i++)
            for (int j = 40; j < 43; j++)
                matrix[i][j].setPathable(false);
        for (int j = 2; j < 40; j++)
            matrix[20][j].setPathable(false);
        
        return matrix;
    }
    
    public static void main(String[] args) {
        final AStarPathfinder<DummyElement> pathfinder = new AStarPathfinder<>((matrixIndex, rawElement) -> {
            return new AStarNode<>(rawElement) {
                
                private ArrayList<AStarNode<DummyElement>> pathableNeighbors;
                
                @Override protected @NotNull List<AStarNode<@NotNull DummyElement>> pathableNeighbors() { return pathableNeighbors; }
                
                @Override protected void onInit(@NotNull AStarPathfinder<DummyElement> pathfinder) {
                    this.pathableNeighbors = new ArrayList<>(A.grabNeighbors(
                            this,
                            pathfinder.getDirectionType(),
                            AStarNode::isPathable,
                            pathfinder.matrix()));
                }
                
                @Override protected @NotNull Num2D matrixIndex() { return data().getMatrixIndex(); }
                @Override protected boolean isPathable() {
                    return data().isPathable();
                }
                @Override protected double edgeCost(@NotNull AStarNode<@NotNull DummyElement> other) { return super.edgeCost(other); }
            };
        }, generateTestMatrix()).init();
        
        
        final List<AStarNode<DummyElement>> path = pathfinder.aStar(new Num2D(30, 5), new Num2D(30, 98));
        //        System.out.println(path);
        final Runnable vGapPrinter = () -> {
            System.out.println();
            if (PRINT_INDEX)
                System.out.println();
        };
        final Runnable hGapPrinter = () -> System.out.print(PRINT_INDEX ? "     " : "   ");
        final Runnable startPrinter = () -> System.out.print(PRINT_INDEX ? "[ S ]" : "S");
        final Runnable goalPrinter = () -> System.out.print(PRINT_INDEX ? "[ G ]" : "G");
        final Runnable pathablePrinter = () -> System.out.print(PRINT_INDEX ? "[   ]" : "-");
        final Runnable unpathablePrinter = () -> System.out.print(PRINT_INDEX ? "[XXX]" : "X");
        final BiConsumer<Num2D, Integer> occupiedPrinter = (matrixIndex, pathIndex) -> {
            if (PRINT_INDEX) {
                if (pathIndex < 10)
                    System.out.print("[00" + pathIndex + "]");
                else if (pathIndex < 100)
                    System.out.print("[0" + pathIndex + "]");
                else
                    System.out.print("[" + pathIndex + "]");
            } else {
                System.out.print("O");
            }
        };
        
        
        for (int j = pathfinder.nodeMatrix[0].length - 1; j >= 0; j--) {
            vGapPrinter.run();
            for (int i = 0; i < pathfinder.nodeMatrix.length; i++) {
                AStarNode<DummyElement> current = pathfinder.nodeMatrix[i][j];
                Num2D matrixIndex = new Num2D(i, j);
                hGapPrinter.run();
                if (current.equals(pathfinder.startNode())) {
                    startPrinter.run();
                } else if (current.equals(pathfinder.goalNode())) {
                    goalPrinter.run();
                } else {
                    if (path.contains(current)) {
                        occupiedPrinter.accept(matrixIndex, path.indexOf(current));
                    } else {
                        if (current.isPathable())
                            pathablePrinter.run();
                        else
                            unpathablePrinter.run();
                    }
                }
            }
        }
    }
}
