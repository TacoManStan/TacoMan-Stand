package com.taco.suit_lady.util.tools.util;

import org.jetbrains.annotations.Nullable;

public interface ValueTrioable<A, B, C>
        extends Valueable<A>, ValuePairable<A, B> {
    @Nullable C c();
}
