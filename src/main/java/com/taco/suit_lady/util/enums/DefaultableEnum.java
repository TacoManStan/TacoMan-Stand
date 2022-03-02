package com.taco.suit_lady.util.enums;

import org.jetbrains.annotations.NotNull;

public interface DefaultableEnum<E extends Enum<E>>
        extends Enumable<E> {
    @NotNull E defaultValue();
}
