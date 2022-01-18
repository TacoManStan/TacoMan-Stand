package com.taco.suit_lady._to_sort._new;

import org.jetbrains.annotations.NotNull;

public interface Self<T extends Self<T>> {
    default @NotNull T self() { return (T) this; }
}
