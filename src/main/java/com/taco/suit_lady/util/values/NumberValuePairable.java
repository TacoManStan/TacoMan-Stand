package com.taco.suit_lady.util.values;

import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumberValuePairable<T extends NumberValuePairable<T>>
        extends ValuePairable<Number, Number>, NumberValueable<T> {
    
    @NotNull T modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean bBool() { return ValueUtil.asBool(b()); }
    
    default int bInt() { return ValueUtil.asInt(b()); }
    default long bLong() { return ValueUtil.asLong(a()); }
    
    default float bFloat() { return ValueUtil.asFloat(b()); }
    default double bDouble() { return ValueUtil.asDouble(b()); }
    
    
    default NumberValuePair asNumberValuePair() { return new NumberValuePair(a(), b()); }
    
    //
    
    default T modify(@NotNull CardinalDirection direction) { return modify(direction, 1, 1); }
    default T modify(@NotNull CardinalDirection direction, @NotNull Number magnitude) { return modify(direction, magnitude, magnitude); }
    default T modify(@NotNull CardinalDirection direction, @NotNull NumberValuePairable<?> magnitude) { return modify(direction, magnitude.a(), magnitude.b()); }
    default T modify(@NotNull CardinalDirection direction, @NotNull Number xMagnitude, @NotNull Number yMagnitude) {
        return modify(
                numA -> numA.doubleValue() + (direction.xMod() * xMagnitude.doubleValue()),
                numB -> numB.doubleValue() + (direction.yMod() * yMagnitude.doubleValue()));
    }
    
    //
    
    default Point2D asPoint() { return new Point2D(aDouble(), bDouble()); }
    
    //<editor-fold desc="> Apply">
    
    default @NotNull Number apply(@NotNull ValueOpType opType, @NotNull OpResultType resultType) { return opType.apply(this, resultType); }
    default @NotNull Number apply(@NotNull ValueOpType opType) { return apply(opType, OpResultType.EXACT); }
    
    //
    
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier,
                                 @NotNull ValueOpType aOpType, @NotNull ValueOpType bOpType,
                                 @NotNull OpResultType resultType) {
        return modify(numA -> aOpType.apply(a(), aModifier, resultType),
                      numB -> bOpType.apply(b(), bModifier, resultType));
    }
    
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull ValueOpType aOpType, @NotNull ValueOpType bOpType) {
        return applyEach(aModifier, bModifier, aOpType, bOpType, OpResultType.EXACT);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull ValueOpType opType, @NotNull OpResultType resultType) {
        return applyEach(aModifier, bModifier, opType, opType, resultType);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier, @NotNull ValueOpType opType) {
        return applyEach(aModifier, bModifier, opType, opType);
    }
    default @NotNull T applyEach(@NotNull Number aModifier, @NotNull Number bModifier) {
        return applyEach(aModifier, bModifier, ValueOpType.ADD);
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
