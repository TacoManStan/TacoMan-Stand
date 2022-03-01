package com.taco.suit_lady.util.tools.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record NumberValue(Number a)
        implements NumberValueable<NumberValue> {
    
    @Contract("_ -> new")
    @Override public @NotNull NumberValue modify(@NotNull Function<Number, Number> aFunction) {
        return new NumberValue(aFunction.apply(a()));
    }
}
