package com.taco.suit_lady.util.values.numbers.expressions;

import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.values.ValueExpr2D;
import com.taco.suit_lady.util.values.enums.CardinalDirection;
import com.taco.suit_lady.util.values.enums.OpResultType;
import com.taco.suit_lady.util.values.enums.OpType;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface NumExpr2D<T extends NumExpr2D<T>>
        extends ValueExpr2D<Number, Number>, NumExpr<T> {
    
    default @NotNull NumExpr2D<?> modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction) {
            return new Num2D(aFunction.apply(a()), bFunction.apply(b()));
    }
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    //<editor-fold desc="--- EQUALITY METHODS ---">
    
    default boolean equalTo(@Nullable NumExpr2D<?> other) { return other != null && aI() == other.aI() && bI() == other.bI(); }
    default boolean equalToExact(@Nullable NumExpr2D<?> other) { return other != null && aD() == other.aD() && bD() == other.bD(); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Converter Methods">
    
    default int bI() { return N.i(b()); }
    default long bL() { return N.l(b()); }
    
    default float bF() { return N.f(b()); }
    default double bD() { return N.d(b()); }
    
    default Num2D asNum2D() { return this instanceof Num2D this2D ? this2D : new Num2D(a(), b()); }
    default Point2D asPoint() { return new Point2D(aD(), bD()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Modify Methods">
    
    default NumExpr2D<?> modify(@NotNull CardinalDirection direction) { return modify(direction, 1, 1); }
    default NumExpr2D<?> modify(@NotNull CardinalDirection direction, @NotNull Number magnitude) { return modify(direction, magnitude, magnitude); }
    default NumExpr2D<?> modify(@NotNull CardinalDirection direction, @NotNull NumExpr2D<?> magnitude) { return modify(direction, magnitude.a(), magnitude.b()); }
    default NumExpr2D<?> modify(@NotNull CardinalDirection direction, @NotNull Number xMagnitude, @NotNull Number yMagnitude) {
        return modify(numA -> numA.doubleValue() + (direction.xMod() * xMagnitude.doubleValue()),
                      numB -> numB.doubleValue() + (direction.yMod() * yMagnitude.doubleValue()));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Apply Methods">
    
    default @NotNull Number apply(@NotNull OpType opType, @NotNull OpResultType resultType) { return opType.apply(this, resultType); }
    default @NotNull Number apply(@NotNull OpType opType) { return apply(opType, OpResultType.EXACT); }
    
    //
    
    default @NotNull NumExpr2D<?> applyEach(@NotNull Number aModifier, @NotNull Number bModifier,
                                 @NotNull OpType aOpType, @NotNull OpType bOpType,
                                 @NotNull OpResultType resultType) {
        return modify(numA -> aOpType.apply(a(), aModifier, resultType),
                      numB -> bOpType.apply(b(), bModifier, resultType));
    }
    
    default @NotNull NumExpr2D<?> applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType aOpType, @NotNull OpType bOpType) {
        return applyEach(aModifier, bModifier, aOpType, bOpType, OpResultType.EXACT);
    }
    default @NotNull NumExpr2D<?> applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType opType, @NotNull OpResultType resultType) {
        return applyEach(aModifier, bModifier, opType, opType, resultType);
    }
    default @NotNull NumExpr2D<?> applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType opType) {
        return applyEach(aModifier, bModifier, opType, opType);
    }
    default @NotNull NumExpr2D<?> applyEach(@NotNull Number aModifier, @NotNull Number bModifier) {
        return applyEach(aModifier, bModifier, OpType.ADD);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Copy Methods">
    
    default @NotNull NumExpr2D<?> copyOf(@NotNull Number x, @NotNull Number y) { return modify(numX -> x, numY -> y); }
    
    default @NotNull NumExpr2D<?> copyOf(@NotNull NumExpr2D<?> point) { return copyOf(point.a(), point.b()); }
    default @NotNull NumExpr2D<?> copyOf(@NotNull Point2D point) { return copyOf(point.getX(), point.getY()); }
    default @NotNull NumExpr2D<?> copyOf() { return copyOf(a(), b()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Calculation Methods">
    
    default double distance(@NotNull Point2D other) { return Calc.distance(asPoint(), other); }
    default double distance(@NotNull Number x, @NotNull Number y) { return Calc.distance(a(), b(), x, y); }
    default double distance(@NotNull NumExpr2D<?> other) { return Calc.distance(this, other); }
    
    default double angle(@NotNull Point2D other) { return Calc.angle(this, other); }
    default double angle(@NotNull Number x, @NotNull Number y) { return Calc.angle(this, x, y); }
    default double angle(@NotNull NumExpr2D<?> other) { return Calc.angle(this, other); }
    
    //<editor-fold desc=">> Interpolation Methods">
    
    default @NotNull NumExpr2D<?> interpolate(@NotNull Point2D other, @NotNull Number percentage) { return copyOf(Calc.interpolate(asPoint(), other, percentage)); }
    default @NotNull NumExpr2D<?> interpolate(@NotNull Number oX, @NotNull Number oY, @NotNull Number percentage) { return copyOf(Calc.interpolate(a(), b(), oX, oY, percentage)); }
    default @NotNull NumExpr2D<?> interpolate(@NotNull NumExpr2D<?> other, @NotNull Number percentage) { return copyOf(Calc.interpolate(this, other, percentage)); }
    
    //
    
    default @NotNull NumExpr2D<?> interpolateTowards(@NotNull Point2D other, @NotNull Number distance) { return copyOf(Calc.interpolateTowards(asPoint(), other, distance)); }
    default @NotNull NumExpr2D<?> interpolateTowards(@NotNull Number oX, @NotNull Number oY, @NotNull Number distance) { return copyOf(Calc.interpolateTowards(a(), b(), oX, oY, distance)); }
    default @NotNull NumExpr2D<?> interpolateTowards(@NotNull NumExpr2D<?> other, @NotNull Number distance) { return copyOf(Calc.interpolateTowards(this, other, distance)); }
    
    default @NotNull NumExpr2D<?> interpolateTowards(@NotNull Number angle, @NotNull Number distance) { return copyOf(Calc.interpolateTowards(this, angle, distance)); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //
    
    default String getString(boolean asInt) {
        if (asInt)
            return "[" + aI() + ", " + bI() + "]";
        else
            return "[" + a() + ", " + b() + "]";
    }
    default String getString() { return getString(false); }
    
    //</editor-fold>
}
