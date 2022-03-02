package com.taco.suit_lady.util.values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("SpellCheckingInspection")
public interface Valueable<A> {
    
    @Nullable A a();
    
    //
    
    default @NotNull Valueable<A> asValueable() { return new Value<>(a()); }
    
    default @NotNull <B> ValuePairable<A, B> asValuePairable(B b) { return new ValuePair<>(a(), b); }
    default @NotNull <B, C> ValueTrioable<A, B, C> asValueTrioable(B b, C c) { return new ValueTrio<>(a(), b, c); }
}
