package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.values.ValueExpr3D;
import com.taco.suit_lady.util.values.ValueUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumExpr3D<T extends NumExpr3D<T>>
        extends ValueExpr3D<Number, Number, Number>, NumExpr2D<T> {
    
    @NotNull T modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction, Function<Number, Number> cFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean cBool() { return ValueUtil.asBool(a()); }
    
    default int cInt() { return ValueUtil.asInt(a()); }
    default long cLong() { return ValueUtil.asLong(a()); }
    
    default float cFloat() { return ValueUtil.asFloat(a()); }
    default double cDouble() { return ValueUtil.asDouble(a()); }
    
    //
    
    default Num3D asNumberValueTrio() { return new Num3D(a(), b(), c()); }
    
    //</editor-fold>
}
