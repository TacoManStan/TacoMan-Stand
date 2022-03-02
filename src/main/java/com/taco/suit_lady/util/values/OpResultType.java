package com.taco.suit_lady.util.values;

import com.taco.suit_lady.util.enums.DefaultableEnum;
import org.jetbrains.annotations.NotNull;

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