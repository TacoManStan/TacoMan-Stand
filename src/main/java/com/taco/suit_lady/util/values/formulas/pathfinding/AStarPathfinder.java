package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.enums.CardinalDirection;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AStarPathfinder {
    
    private AStarNode start;
    private AStarNode goal;
    private final PriorityQueue<AStarNode> openSet;
    private final PriorityQueue<AStarNode> closedSet;
    
    private final AStarNode[][] nodeMatrix;
    
    public AStarPathfinder() {
        this.openSet = new PriorityQueue<>();
        this.closedSet = new PriorityQueue<>();
        
        this.nodeMatrix = generateTestMatrix();
    }
    
    private AStarNode[][] generateTestMatrix() {
        final AStarNode[][] matrix = A.fillMatrix(num2D -> new AStarNode(this, num2D, true), new AStarNode[100][100]);
        for (int i = 10; i < 90; i++)
            for (int j = 40; j < 43; j++)
                matrix[i][j].setPathable(false);
        for (int j = 2; j < 40; j++)
            matrix[20][j].setPathable(false);
        return matrix;
    }
    
    public @NotNull AStarNode getNodeAt(@NotNull Num2D location) {
        return nodeMatrix[location.aI()][location.bI()];
    }
    
    protected final @NotNull AStarNode getStart() { return start; }
    protected final @NotNull AStarNode getGoal() { return goal; }
    
    public @NotNull List<AStarNode> aStar(@NotNull Num2D start, @NotNull Num2D goal) {
        this.start = getNodeAt(start);
        this.goal = getNodeAt(goal);
        this.openSet.add(this.start);
        
        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();
            closedSet.add(current);
            
            if (current.isGoal())
                return formPath();
            
            for (CardinalDirection direction: CardinalDirection.valuesNoC()) {
                AStarNode neighbor = current.getNeighbor(direction);
                if (neighbor != null && !closedSet.contains(neighbor))
                    if (!openSet.contains(neighbor)) {
                        if (neighbor.isPathable()) {
                            neighbor.setPrevious(current);
                            neighbor.hCost = neighbor.hCost();
                            neighbor.gCost = current.gCost(neighbor);
                            openSet.add(neighbor);
                        }
                    } else {
                        double gCostCalc = current.gCost(neighbor);
                        if (neighbor.gCost >= gCostCalc) {
                            neighbor.setPrevious(current);
                            neighbor.gCost = gCostCalc;
                            openSet.remove(neighbor);
                        }
                    }
            }
            
            if (openSet.isEmpty())
                return new ArrayList<>();
        }
        
        return null;
    }
    
    private @NotNull List<AStarNode> formPath() {
        final ArrayList<AStarNode> path = new ArrayList<>();
        AStarNode current = getGoal();
        while (current != null) {
            path.add(current);
            current = current.getPrevious();
        }
        return path;
    }
    
    protected @Nullable AStarNode getNeighbor(@NotNull Num2D origin, @NotNull CardinalDirection direction) {
        return direction.getNeighbor(origin, nodeMatrix);
    }
    
    private static final boolean PRINT_INDEX = false;
    
    public static void main(String[] args) {
        final AStarPathfinder pathfinder = new AStarPathfinder();
        final List<AStarNode> path = pathfinder.aStar(new Num2D(30, 5), new Num2D(30, 98));
        //        System.out.println(path);
        final Runnable vGapPrinter = () -> {
            System.out.println();
            if (PRINT_INDEX)
                System.out.println();
        };
        final Runnable hGapPrinter = () -> System.out.print(PRINT_INDEX ? "     " : "  ");
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
                AStarNode current = pathfinder.nodeMatrix[i][j];
                Num2D matrixIndex = new Num2D(i, j);
                hGapPrinter.run();
                if (current.equals(pathfinder.getStart())) {
                    startPrinter.run();
                } else if (current.equals(pathfinder.getGoal())) {
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
