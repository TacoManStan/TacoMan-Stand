package com.taco.suit_lady.util.tools.util.values;

import org.jetbrains.annotations.NotNull;

public enum ValueOpType {
    
    ADD() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(num1.doubleValue() + num2.doubleValue());
        }
    },
    SUB() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(num1.doubleValue() - num2.doubleValue());
        }
    },
    MULTI() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(num1.doubleValue() * num2.doubleValue());
        }
    },
    DIV() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(num1.doubleValue() / num2.doubleValue());
        }
    },
    
    EXPO() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(Math.pow(num1.doubleValue(), num2.doubleValue()));
        }
    },
    HYPOT() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(Math.hypot(num1.doubleValue(), num2.doubleValue()));
        }
    },
    
    MIN() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(Math.min(num1.doubleValue(), num2.doubleValue()));
        }
    },
    MAX() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply(Math.max(num1.doubleValue(), num2.doubleValue()));
        }
    },
    AVG() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            return roundType.apply((num1.doubleValue() + num2.doubleValue()) / 2D);
        }
    },
    
    RAND() {
        @Override public @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType) {
            final double f = Math.random() / Math.nextDown(1.0);
            return roundType.apply((num1.doubleValue() * (1.0 - f)) + num2.doubleValue() * f);
        }
    };
    
    ValueOpType() { }
    
    public abstract @NotNull Number apply(@NotNull Number num1, @NotNull Number num2, @NotNull RoundType roundType);
    
    //
    
    public enum RoundType {
        
        DO_NOT_ROUND() {
            @Override protected @NotNull Number apply(@NotNull Number num) {
                return num;
            }
        },
        FLOOR() {
            @Override protected @NotNull Number apply(@NotNull Number num) {
                return Math.floor(num.doubleValue());
            }
        },
        CEIL() {
            @Override protected @NotNull Number apply(@NotNull Number num) {
                return Math.ceil(num.doubleValue());
            }
        },
        ROUND() {
            @Override protected @NotNull Number apply(@NotNull Number num) {
                return Math.floor(num.doubleValue());
            }
        };
        
        RoundType() { }
        
        protected abstract @NotNull Number apply(@NotNull Number num);
    }
}
