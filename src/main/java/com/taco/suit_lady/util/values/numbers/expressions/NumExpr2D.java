package com.taco.suit_lady.util.values.numbers.expressions;

import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.tools.Calc.AngleType;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.values.ValueExpr2D;
import com.taco.suit_lady.util.values.enums.CardinalDirection;
import com.taco.suit_lady.util.values.enums.OpResultType;
import com.taco.suit_lady.util.values.enums.OpType;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumExpr2D<T extends NumExpr2D<T>>
        extends ValueExpr2D<Number, Number>, NumExpr<T> {
    
    @NotNull T modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    //<editor-fold desc="> Converter Methods">
    
    default int bI() { return N.i(b()); }
    default long bL() { return N.l(b()); }
    
    default float bF() { return N.f(b()); }
    default double bD() { return N.d(b()); }
    
    default Num2D asNum2D() { return this instanceof Num2D this2D ? this2D : new Num2D(a(), b()); }
    default Point2D asPoint() { return new Point2D(aD(), bD()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Modify Methods">
    
    default T modify(@NotNull CardinalDirection direction) { return modify(direction, 1, 1); }
    default T modify(@NotNull CardinalDirection direction, @NotNull Number magnitude) { return modify(direction, magnitude, magnitude); }
    default T modify(@NotNull CardinalDirection direction, @NotNull NumExpr2D<?> magnitude) { return modify(direction, magnitude.a(), magnitude.b()); }
    default T modify(@NotNull CardinalDirection direction, @NotNull Number xMagnitude, @NotNull Number yMagnitude) {
        return modify(numA -> numA.doubleValue() + (direction.xMod() * xMagnitude.doubleValue()),
                      numB -> numB.doubleValue() + (direction.yMod() * yMagnitude.doubleValue()));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Apply Methods">
    
    default @NotNull Number apply(@NotNull OpType opType, @NotNull OpResultType resultType) { return opType.apply(this, resultType); }
    default @NotNull Number apply(@NotNull OpType opType) { return apply(opType, OpResultType.EXACT); }
    
    //
    
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier,
                                 @NotNull OpType aOpType, @NotNull OpType bOpType,
                                 @NotNull OpResultType resultType) {
        return modify(numA -> aOpType.apply(a(), aModifier, resultType),
                      numB -> bOpType.apply(b(), bModifier, resultType));
    }
    
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType aOpType, @NotNull OpType bOpType) {
        return applyEach(aModifier, bModifier, aOpType, bOpType, OpResultType.EXACT);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType opType, @NotNull OpResultType resultType) {
        return applyEach(aModifier, bModifier, opType, opType, resultType);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull OpType opType) {
        return applyEach(aModifier, bModifier, opType, opType);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier) {
        return applyEach(aModifier, bModifier, OpType.ADD);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Copy Methods">
    
    default @NotNull T copyOf(@NotNull Number x, @NotNull Number y) { return modify(numX -> x, numY -> y); }
    
    default @NotNull T copyOf(@NotNull NumExpr2D<?> point) { return copyOf(point.a(), point.b()); }
    default @NotNull T copyOf(@NotNull Point2D point) { return copyOf(point.getX(), point.getY()); }
    default @NotNull T copyOf() { return copyOf(a(), b()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Calculation Methods">
    
    default double distance(@NotNull Point2D other) { return asPoint().distance(other); }
    default double distance(@NotNull Number x, @NotNull Number y) { return asPoint().distance(Calc.point2D(x, y)); }
    default double distance(@NotNull NumExpr2D<?> other) { return asPoint().distance(other.asPoint()); }
    
    default double angle(@NotNull Point2D other, @NotNull AngleType angleType) { return Calc.angle(this, other, angleType); }
    default double angle(@NotNull Number x, @NotNull Number y, @NotNull AngleType angleType) { return Calc.angle(this, x, y, angleType); }
    default double angle(@NotNull NumExpr2D<?> other, @NotNull AngleType angleType) { return Calc.angle(this, other, angleType); }
    
    //<editor-fold desc=">> Interpolation Methods">
    
    default @NotNull T interpolate(@NotNull Point2D other, @NotNull Number percentage) { return copyOf(asPoint().interpolate(other, percentage.doubleValue())); }
    default @NotNull T interpolate(@NotNull Number oX, @NotNull Number oY, @NotNull Number percentage) { return interpolate(Calc.point2D(oX, oY), percentage); }
    default @NotNull T interpolate(@NotNull NumExpr2D<?> other, @NotNull Number percentage) { return interpolate(other.asPoint(), percentage); }
    
    //
    
    default @NotNull T interpolateTowards(@NotNull Point2D other, @NotNull Number distance) { return interpolateTowards(Calc.degreesToRads(angle(other, AngleType.ACTUAL)), distance); }
    default @NotNull T interpolateTowards(@NotNull Number oX, @NotNull Number oY, @NotNull Number distance) { return interpolateTowards(Calc.point2D(oX, oY), distance); }
    default @NotNull T interpolateTowards(@NotNull NumExpr2D<?> other, @NotNull Number distance) { return interpolateTowards(other.asPoint(), distance); }
    
    default @NotNull T interpolateTowards(@NotNull Number angle, @NotNull Number distance) {
        final double retX = (distance.doubleValue() * Math.cos(angle.doubleValue())) + aD();
        final double retY = (distance.doubleValue() * Math.sin(angle.doubleValue())) + bD();
        return copyOf(retX, retY);
    }
    
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
