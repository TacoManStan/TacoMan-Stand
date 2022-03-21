package com.taco.suit_lady.util.values.enums;

import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import org.jetbrains.annotations.NotNull;

/**
 * <p>An {@link Enum} defining the {@code operation} to perform.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Use the <i>{@link #apply(NumExpr2D, OpResultType)}</i> method to {@code execute} the {@link OpType Operation Logic} defined by this {@link OpType} constant on the specified {@code parameter values}.</li>
 * </ol>
 */
//TO-EXPAND
public enum OpType {
    
    //<editor-fold desc="--- ENUM VALUE DEFINITIONS ---">
    
    ADD() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(num1.doubleValue() + num2.doubleValue());
        }
    },
    SUB() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(num1.doubleValue() - num2.doubleValue());
        }
    },
    MULTI() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(num1.doubleValue() * num2.doubleValue());
        }
    },
    DIV() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(num1.doubleValue() / num2.doubleValue());
        }
    },
    MOD() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(num1.doubleValue() % num2.doubleValue());
        }
    },
    
    EXPO() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(Math.pow(num1.doubleValue(), num2.doubleValue()));
        }
    },
    HYPOT() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(Math.hypot(num1.doubleValue(), num2.doubleValue()));
        }
    },
    
    MIN() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(Math.min(num1.doubleValue(), num2.doubleValue()));
        }
    },
    MAX() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply(Math.max(num1.doubleValue(), num2.doubleValue()));
        }
    },
    AVG() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            return opResultType.apply((num1.doubleValue() + num2.doubleValue()) / 2D);
        }
    },
    
    RAND() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType) {
            final double f = Math.random() / Math.nextDown(1.0);
            return opResultType.apply((num1.doubleValue() * (1.0 - f)) + num2.doubleValue() * f);
        }
    };
    
    //</editor-fold>
    
    OpType() { }
    
    public abstract @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull OpResultType opResultType);
    
    public final @NotNull Number apply(@NotNull NumExpr2D<?> numPair, @NotNull OpResultType opResultType) {
        return apply(numPair.a(), numPair.b(), opResultType);
    }
}
