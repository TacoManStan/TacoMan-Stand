package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.Nullable;

public interface ValuePairable<A, B>
        extends Valueable<A> {
    @Nullable B b();
}
