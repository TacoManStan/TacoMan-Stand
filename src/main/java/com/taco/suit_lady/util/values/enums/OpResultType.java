package com.taco.suit_lady.util.values.enums;

import com.taco.suit_lady.util.enums.DefaultableEnum;
import com.taco.suit_lady.util.values.numbers.NumExpr2D;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines the {@code post-processing} to {@link #apply(Number) apply} to the {@code result} of a {@link OpType} {@link OpType#apply(NumExpr2D, OpResultType) Calculation}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link OpResultType} is most commonly used when an {@link OpType} {@link OpType#apply(NumExpr2D, OpResultType) Execution} must be returned as an {@link Integer} value.</li>
 * </ol>
 */
//TO-EXPAND
public enum OpResultType
        implements DefaultableEnum<OpResultType> {
    
    //<editor-fold desc="--- ENUM VALUE DEFINITIONS ---">
    
    EXACT() {
        @Override public @NotNull Number apply(@NotNull Number num) {
            return num;
        }
    },
    FLOOR() {
        @Override public @NotNull Number apply(@NotNull Number num) {
            return Math.floor(num.doubleValue());
        }
    },
    CEIL() {
        @Override public @NotNull Number apply(@NotNull Number num) {
            return Math.ceil(num.doubleValue());
        }
    },
    ROUND() {
        @Override public @NotNull Number apply(@NotNull Number num) {
            return Math.floor(num.doubleValue());
        }
    };
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract @NotNull Number apply(@NotNull Number num);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull OpResultType defaultValue() { return OpResultType.EXACT; }
    
    //</editor-fold>
}