package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record NumberValuePair(Number a, Number b)
        implements NumberValuePairable {
    
    @Contract("_ -> new") @Override public @NotNull NumberValuePair modify(@NotNull Function<Number, Number> aFunction) {
        return new NumberValuePair(aFunction.apply(a()), b());
    }
    
    @Contract("_, _ -> new") @Override public @NotNull NumberValuePair modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction) {
        return new NumberValuePair(aFunction.apply(a()), bFunction.apply(b()));
    }
}
