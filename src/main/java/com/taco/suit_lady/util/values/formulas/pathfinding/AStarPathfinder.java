package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.enums.CardinalDirection;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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
        for (int i = 20; i < 22; i++)
            for (int j = 20; j < 40; j++)
                matrix[i][j].setPathable(false);
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
            
            for (CardinalDirection direction: CardinalDirection.valuesUnidirectional()) {
                AStarNode neighbor = current.getNeighbor(direction);
                if (neighbor != null && !closedSet.contains(neighbor))
                    if (!openSet.contains(neighbor)) {
                        if (neighbor.isPathable()) {
                            neighbor.setPrevious(current);
                            neighbor.hCost = neighbor.hCost();
                            neighbor.gCost = neighbor.gCost(current);
                            openSet.add(neighbor);
                        }
                    } else {
                        double gCostCalc = neighbor.gCost(current);
                        if (neighbor.gCost > gCostCalc) {
                            neighbor.setPrevious(current);
                            neighbor.gCost = gCostCalc;
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
    
    public static void main(String[] args) {
        final AStarPathfinder pathfinder = new AStarPathfinder();
        final List<AStarNode> path = pathfinder.aStar(new Num2D(30, 1), new Num2D(30, 98));
        //        System.out.println(path);
        for (int j = pathfinder.nodeMatrix[0].length - 1; j >= 0; j--) {
            System.out.println();
            for (int i = 0; i < pathfinder.nodeMatrix.length; i++) {
                AStarNode current = pathfinder.nodeMatrix[i][j];
                    System.out.print(" ");
                if (current.equals(pathfinder.getStart())) {
                    System.out.print("S");
                } else if (current.equals(pathfinder.getGoal())) {
                    System.out.print("G");
                }
                else {
                    if (path.contains(current))
                        System.out.print("*");
                    else {
                        if (current.isPathable())
                            System.out.print(" ");
                        else
                            System.out.print("X");
                    }
                }
            }
        }
    }
}
