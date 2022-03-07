package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.values.ValueExpr;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumExpr<T extends NumExpr<T>>
        extends ValueExpr<Number> {
    
    @NotNull T modify(Function<Number, Number> aFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default int aI() { return N.i(a()); }
    default long aL() { return N.l(a()); }
    
    default float aF() { return N.f(a()); }
    default double aD() { return N.d(a()); }
    
    //
    
    default Num asNum() { return new Num(a()); }
    
    //</editor-fold>
}
