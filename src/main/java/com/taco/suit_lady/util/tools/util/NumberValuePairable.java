package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumberValuePairable
        extends ValuePairable<Number, Number>, NumberValueable {
    
    @NotNull NumberValuePairable modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean bBool() { return ValueUtil.asBool(b()); }
    
    default int bInt() { return ValueUtil.asInt(b()); }
    default long bLong() { return ValueUtil.asLong(a()); }
    
    default float bFloat() { return ValueUtil.asFloat(b()); }
    default double bDouble() { return ValueUtil.asDouble(b()); }
    
    //
    
    default NumberValuePair asNumberValuePair() { return new NumberValuePair(a(), b()); }
    
    //</editor-fold>
}
