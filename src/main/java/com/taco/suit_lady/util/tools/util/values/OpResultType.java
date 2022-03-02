package com.taco.suit_lady.util.tools.util.values;

import org.jetbrains.annotations.NotNull;

public enum OpResultType {
    
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
    
    OpResultType() { }
    
    public abstract @NotNull Number apply(@NotNull Number num);
}