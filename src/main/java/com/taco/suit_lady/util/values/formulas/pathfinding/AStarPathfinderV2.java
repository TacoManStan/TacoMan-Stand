package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.Enu;
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

public class AStarPathfinderV2<T> {
    
    private final CardinalDirectionType directionType;
    
    private Num2D start;
    private Num2D goal;
    
    private final PriorityQueue<AStarNodeV2<T>> openSet;
    private final PriorityQueue<AStarNodeV2<T>> closedSet;
    
    private final AStarNodeV2<T>[][] nodeMatrix;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public AStarPathfinderV2(@Nullable CardinalDirectionType directionType, @NotNull BiFunction<Num2D, T, AStarNodeV2<T>> nodeFactory, @NotNull T[][] rawMatrix) {
        this.directionType = directionType != null ? directionType : CardinalDirectionType.ALL_BUT_CENTER;
        
        this.openSet = new PriorityQueue<>();
        this.closedSet = new PriorityQueue<>();
        
        this.nodeMatrix = generateMatrix(nodeFactory, rawMatrix);
    }
    
    public AStarPathfinderV2(@NotNull BiFunction<Num2D, T, AStarNodeV2<T>> nodeFactory, @NotNull T[][] rawMatrix) {
        this(null, nodeFactory, rawMatrix);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private @NotNull AStarPathfinderV2<T> init() {
        A.iterateMatrix((matrixIndex, node) -> {
            node.init(this);
            return null;
        }, matrix());
        
        return this;
    }
    
    //</editor-fold>
    
    private @NotNull AStarNodeV2<T>[][] matrix() { return nodeMatrix; }
    
    public final @NotNull CardinalDirectionType getDirectionType() { return directionType; }
    
    public @NotNull AStarNodeV2<T>[][] generateMatrix(@NotNull BiFunction<Num2D, T, AStarNodeV2<T>> nodeFactory, @NotNull T[][] inputMap) {
        final Num2D inputDimensions = A.matrixDimensions(inputMap);
        final AStarNodeV2<T>[][] outputMap = new AStarNodeV2[inputDimensions.aI()][inputDimensions.bI()];
        return A.fillMatrix(nodeFactory, inputMap, outputMap);
    }
    
    protected final @NotNull AStarNodeV2<T> startNode() { return A.grab(start(), matrix()); }
    protected final @NotNull AStarNodeV2<T> goalNode() { return A.grab(goal(), matrix()); }
    
    protected final @NotNull Num2D start() { return start; }
    protected final @NotNull Num2D goal() { return goal; }
    
    public @NotNull List<AStarNodeV2<T>> aStar(@NotNull Num2D start, @NotNull Num2D goal) {
        this.start = start;
        this.goal = goal;
        this.openSet.add(startNode());
        
        while (!openSet.isEmpty()) {
            AStarNodeV2<T> current = openSet.poll();
            closedSet.add(current);
            
            if (current.isGoal())
                return formPath();
            
            for (AStarNodeV2<T> neighbor: current.pathableNeighbors()) {
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
    
    private @NotNull List<AStarNodeV2<T>> formPath() {
        final ArrayList<AStarNodeV2<T>> path = new ArrayList<>();
        AStarNodeV2<T> current = goalNode();
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
        final AStarPathfinderV2<DummyElement> pathfinder = new AStarPathfinderV2<>((matrixIndex, rawElement) -> {
            return new AStarNodeV2<>(rawElement) {
                
                private ArrayList<AStarNodeV2<DummyElement>> pathableNeighbors;
                
                @Override protected @NotNull List<AStarNodeV2<@NotNull DummyElement>> pathableNeighbors() { return pathableNeighbors; }
                
                @Override protected void onInit(@NotNull AStarPathfinderV2<DummyElement> pathfinder) {
                    this.pathableNeighbors = new ArrayList<>(A.grabNeighbors(
                            this,
                            pathfinder.getDirectionType(),
                            AStarNodeV2::isPathable,
                            pathfinder.matrix()));
                }
                
                @Override protected @NotNull Num2D matrixIndex() { return data().getMatrixIndex(); }
                @Override protected boolean isPathable() {
                    return data().isPathable();
                }
                @Override protected double edgeCost(@NotNull AStarNodeV2<@NotNull DummyElement> other) { return super.edgeCost(other); }
            };
        }, generateTestMatrix()).init();
        
        
        final List<AStarNodeV2<DummyElement>> path = pathfinder.aStar(new Num2D(30, 5), new Num2D(30, 98));
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
                AStarNodeV2<DummyElement> current = pathfinder.nodeMatrix[i][j];
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
