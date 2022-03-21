package com.taco.suit_lady.util.values.enums;

import com.taco.suit_lady.util.enums.Enumable;
import com.taco.suit_lady.util.tools.Exc;
import org.jetbrains.annotations.NotNull;

/**
 * <p>An {@link Enum} defining an {@link Axis} for an {@code operation}.</p>
 */
//TO-EXPAND
public enum Axis
        implements Enumable<Axis> {
    
    //<editor-fold desc="--- VALUE DEFINITIONS ---">
    
    X_AXIS() {
        @Override public <T> T getFor(@NotNull T v1, @NotNull T v2) {
            return v1;
        }
    },
    Y_AXIS() {
        @Override public <T> T getFor(@NotNull T v1, @NotNull T v2) {
            return v2;
        }
    };
    
    //</editor-fold>
    
    Axis() { }
    
    public <T> T getFor(@NotNull T v1, @NotNull T v2) { throw Exc.unsupported("\"getFor(...)\" operations are unsupported for Axis [" + name() + "]"); }
}
