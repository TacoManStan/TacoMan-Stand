package com.taco.suit_lady.util.values.numbers.expressions;

import com.taco.suit_lady.util.values.ValueExpr3D;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num3D;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface NumExpr3D<T extends NumExpr3D<T>>
        extends ValueExpr3D<Number, Number, Number>, NumExpr2D<T> {
    
    @NotNull T modify(Function<Number, Number> aFunction, Function<Number, Number> bFunction, Function<Number, Number> cFunction);
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default int cI() { return N.i(c()); }
    default long cL() { return N.l(c()); }
    
    default float cF() { return N.f(c()); }
    default double cD() { return N.d(c()); }
    
    //
    
    default Num3D asNum3D() { return new Num3D(a(), b(), c()); }
    
    //</editor-fold>
}
