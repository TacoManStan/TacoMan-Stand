package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.values.ValueExpr;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * <p>Defines a {@link ValueExpr} implementation containing only {@link Number} values.</p>
 *
 * @param <T> The {@link Class} type of this {@link NumExpr} implementation.
 */
//TO-EXPAND
public interface NumExpr<T extends NumExpr<T>>
        extends ValueExpr<Number> {
    
    default @NotNull NumExpr<?> modify(Function<Number, Number> aFunction) {
        return new Num(aFunction.apply(a()));
    }
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default int aI() { return N.i(a()); }
    default long aL() { return N.l(a()); }
    
    default float aF() { return N.f(a()); }
    default double aD() { return N.d(a()); }
    
    //
    
    default Num asNum() { return new Num(a()); }
    
    //</editor-fold>
}
