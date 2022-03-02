package com.taco.suit_lady.util.tools.util.values;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumberValueTrioable<T extends NumberValueTrioable<T>>
        extends ValueTrioable<Number, Number, Number>, NumberValuePairable<T> {
    
    @NotNull T modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction, Function<Number, Number> cFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean cBool() { return ValueUtil.asBool(a()); }
    
    default int cInt() { return ValueUtil.asInt(a()); }
    default long cLong() { return ValueUtil.asLong(a()); }
    
    default float cFloat() { return ValueUtil.asFloat(a()); }
    default double cDouble() { return ValueUtil.asDouble(a()); }
    
    //
    
    default NumberValueTrio asNumberValueTrio() { return new NumberValueTrio(a(), b(), c()); }
    
    //</editor-fold>
}
