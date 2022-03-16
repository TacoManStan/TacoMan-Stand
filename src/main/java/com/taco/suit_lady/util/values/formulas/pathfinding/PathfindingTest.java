package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PathfindingTest {
    private PathfindingTest() { } //No Instance
    
    private static final Num2D START = new Num2D(30, 5);
    private static final Num2D GOAL = new Num2D(30, 98);
    
    public static void main(String[] args) {
        test2();
    }
    
//    private static void test1() {
//        Arrays.stream(new double[]{0, 1, 2, 100, 1000})
//              .forEach(PathfindingTest::printPath);
//
//        IntStream.rangeClosed(1, 20)
//                 .mapToDouble(i -> i * 5)
//                 .forEach(PathfindingTest::printPath);
//    }
    
    private static void test2() {
        printPath(newPathfinder());
    }
    
    public static @NotNull List<AStarNode<DummyElement>> aStar() { return aStar(newPathfinder()); }
    public static @NotNull List<AStarNode<DummyElement>> aStar(@NotNull AStarPathfinder<DummyElement> pathfinder) { return pathfinder.aStar(); }
    
    //
    
    @Contract(" -> new")
    public static @NotNull AStarPathfinder<DummyElement> newPathfinder() {
        final AStarPathfinder<DummyElement> pathfinder = new AStarPathfinder<>();
        pathfinder.setRawMatrix(generateTestMatrix());
        pathfinder.setStartIndex(START);
        pathfinder.setGoalIndex(GOAL);
        pathfinder.setNodeFactory((matrixIndex, rawElement) -> new CachedAStarNode<>(rawElement) {
            @Override protected @NotNull Num2D matrixIndex() { return matrixIndex; }
            @Override protected boolean pathableFrom(@NotNull AStarNode<DummyElement> other) { return data().isPathable(); }
            @Override protected boolean pathable() {
                return data().isPathable();
            }
            @Override protected double edgeCost(@NotNull AStarNode<@NotNull DummyElement> other) { return super.edgeCost(other); }
        });
        return pathfinder.init();
    }
    
    private static final boolean PRINT_INDEX = false;
    private static final boolean PRINT_PATH = true;
    
    public static @NotNull DummyElement[][] generateTestMatrix() {
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
    
    public static void printPath(@NotNull AStarPathfinder<DummyElement> pathfinder) {
        final List<AStarNode<DummyElement>> path = pathfinder.aStar();
        
        if (PRINT_PATH) {
            final Runnable vGapPrinter = () -> {
                System.out.println();
                if (PRINT_INDEX)
                    System.out.println();
            };
            final Runnable hGapPrinter = () -> System.out.print(PRINT_INDEX ? "   " : "  ");
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
            
            final Num2D mapSize = pathfinder.getDimensions();
            final int mapWidth = mapSize.aI();
            final int mapHeight = mapSize.bI();
            
            for (int j = mapHeight - 1; j >= 0; j--) {
                vGapPrinter.run();
                for (int i = 0; i < mapWidth; i++) {
                    AStarNode<DummyElement> current = pathfinder.getNodeAt(i, j);
                    Num2D matrixIndex = new Num2D(i, j);
                    hGapPrinter.run();
                    if (current.equals(pathfinder.getStartNode())) {
                        startPrinter.run();
                    } else if (current.equals(pathfinder.getGoalNode())) {
                        goalPrinter.run();
                    } else {
                        if (path.contains(current)) {
                            occupiedPrinter.accept(matrixIndex, path.indexOf(current));
                        } else {
                            if (current.pathable())
                                pathablePrinter.run();
                            else
                                unpathablePrinter.run();
                        }
                    }
                }
            }
        }
    }
}
