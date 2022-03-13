package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Num2D(Number a, Number b)
        implements NumExpr2D<Num2D> {
    
    @Contract("_ -> new")
    @Override public @NotNull Num2D modify(@NotNull Function<Number, Number> aFunction) {
        return new Num2D(aFunction.apply(a()), b());
    }
    
    @Contract("_, _ -> new")
    @Override public @NotNull Num2D modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction) {
        return new Num2D(aFunction.apply(a()), bFunction.apply(b()));
    }
    
    @Override public String toString() {
        return getString(false);
    }
    
    //
    
    public static <T extends NumExpr2D<T>> @NotNull Function<Number, Number> emptyModifier() { return number -> number; }
    
}
