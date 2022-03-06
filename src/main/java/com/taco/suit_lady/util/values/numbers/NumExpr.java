package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.values.ValueExpr;
import com.taco.suit_lady.util.values.ValueUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumExpr<T extends NumExpr<T>>
        extends ValueExpr<Number> {
    
    @NotNull T modify(Function<Number, Number> aFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean aBool() { return ValueUtil.asBool(a()); }
    
    default int aInt() { return ValueUtil.asInt(a()); }
    default long aLong() { return ValueUtil.asLong(a()); }
    
    default float aFloat() { return ValueUtil.asFloat(a()); }
    default double aDouble() { return ValueUtil.asDouble(a()); }
    
    //
    
    default Num asNumberValue() { return new Num(a()); }
    
    //</editor-fold>
}
