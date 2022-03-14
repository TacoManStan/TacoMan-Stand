package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PathfindingTest {
    private PathfindingTest() { } //No Instance
    
    public static void main(String[] args) {
        final AStarPathfinder<DummyElement> pathfinder = new AStarPathfinder<>((matrixIndex, rawElement) -> {
            return new AStarNode<>(rawElement) {
                
                private ArrayList<AStarNode<DummyElement>> pathableNeighbors;
                
                @Override protected @NotNull List<AStarNode<@NotNull DummyElement>> pathableNeighbors() { return pathableNeighbors; }
                
                @Override protected void onInit(@NotNull AStarPathfinder<DummyElement> pathfinder) {
                    this.pathableNeighbors = new ArrayList<>(pathfinder.neighbors(this, FilterType.ALL, node -> node.isPathable()));
//                    this.pathableNeighbors = new ArrayList<>(A.grabNeighbors(
//                            this,
//                            pathfinder.getDirectionType(),
//                            dummyElementAStarNode -> dummyElementAStarNode.isPathable(),
//                            pathfinder.matrix()));
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
        
        final Num2D mapSize = pathfinder.mapSize();
        final int mapWidth = mapSize.aI();
        final int mapHeight = mapSize.bI();
        
        for (int j = mapHeight - 1; j >= 0; j--) {
            vGapPrinter.run();
            for (int i = 0; i < mapWidth; i++) {
//                AStarNode<DummyElement> current = pathfinder.nodeMatrix[i][j];
                AStarNode<DummyElement> current = pathfinder.getNodeAt(i, j);
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
}
