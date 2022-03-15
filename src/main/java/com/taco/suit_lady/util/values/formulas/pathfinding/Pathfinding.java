package com.taco.suit_lady.util.values.formulas.pathfinding;

import com.taco.suit_lady.util.values.numbers.Num2D;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Pathfinding {
    private Pathfinding() { } //No Instance
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    public static <T> @NotNull BiFunction<Num2D, T, AStarNode<T>> newNodeFactory(@NotNull Function<T, Boolean> pathabilityFunction) {
        return (matrixIndex, rawElement) -> new CachedAStarNode<T>(rawElement) {
            @Override protected @NotNull Num2D matrixIndex() { return matrixIndex; }
            @Override protected boolean pathableFrom(@NotNull AStarNode<T> other) { return pathable() && other.pathable(); }
            @Override protected boolean pathable() { return pathabilityFunction.apply(rawElement); }
        };
    }
    
    //</editor-fold>
}
