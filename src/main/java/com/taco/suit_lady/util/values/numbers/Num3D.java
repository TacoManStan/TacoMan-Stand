package com.taco.suit_lady.util.values.numbers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Num3D(Number a, Number b, Number c)
        implements NumExpr3D<Num3D> {
    
    @Contract("_ -> new")
    @Override public @NotNull Num3D modify(@NotNull Function<Number, Number> aFunction) {
        return new Num3D(aFunction.apply(a()), b(), c());
    }
    
    @Contract("_, _ -> new")
    @Override public @NotNull Num3D modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction) {
        return new Num3D(aFunction.apply(a()), bFunction.apply(b()), c());
    }
    
    @Contract("_, _, _ -> new")
    @Override public @NotNull Num3D modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction, @NotNull Function<Number, Number> cFunction) {
        return new Num3D(aFunction.apply(a()), bFunction.apply(b()), cFunction.apply(c()));
    }
}
