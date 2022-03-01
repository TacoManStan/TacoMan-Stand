package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumberValueable<T extends NumberValueable<T>>
        extends Valueable<Number> {
    
    @NotNull T modify(Function<Number, Number> aFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean aBool() { return ValueUtil.asBool(a()); }
    
    default int aInt() { return ValueUtil.asInt(a()); }
    default long aLong() { return ValueUtil.asLong(a()); }
    
    default float aFloat() { return ValueUtil.asFloat(a()); }
    default double aDouble() { return ValueUtil.asDouble(a()); }
    
    //
    
    default NumberValue asNumberValue() { return new NumberValue(a()); }
    
    //</editor-fold>
}
