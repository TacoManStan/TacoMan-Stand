package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueTrioable<A, B, C>
        extends ValuePairable<A, B> {
    
    @Nullable C c();
    
    //
    
    default @NotNull ValueTrioable<A, B, C> asValueTrioable() { return new ValueTrio<>(a(), b(), c()); }
}