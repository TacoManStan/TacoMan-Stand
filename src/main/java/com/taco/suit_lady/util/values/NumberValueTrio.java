package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record NumberValueTrio(Number a, Number b, Number c)
        implements NumberValueTrioable<NumberValueTrio> {
    
    @Contract("_ -> new")
    @Override public @NotNull NumberValueTrio modify(@NotNull Function<Number, Number> aFunction) {
        return new NumberValueTrio(aFunction.apply(a()), b(), c());
    }
    
    @Contract("_, _ -> new")
    @Override public @NotNull NumberValueTrio modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction) {
        return new NumberValueTrio(aFunction.apply(a()), bFunction.apply(b()), c());
    }
    
    @Contract("_, _, _ -> new")
    @Override public @NotNull NumberValueTrio modify(@NotNull Function<Number, Number> aFunction, @NotNull Function<Number, Number> bFunction, @NotNull Function<Number, Number> cFunction) {
        return new NumberValueTrio(aFunction.apply(a()), bFunction.apply(b()), cFunction.apply(c()));
    }
}
