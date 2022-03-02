package com.taco.suit_lady.util.tools.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValuePairable<A, B>
        extends Valueable<A> {
    
    @Nullable B b();
    
    //
    
    default @NotNull ValuePairable<A, B> asValuePairable() { return new ValuePair<>(a(), b()); }
    
    default @NotNull <C> ValueTrioable<A, B, C> asValueTrioable(C c) { return new ValueTrio<>(a(), b(), c); }
}
