package com.taco.suit_lady.util.enums;

import java.io.Serializable;

/**
 * <p>Indicates that a particular {@link Enum} implementation is {@link Enumable}.</p>
 *
 * @param <E> The {@link Enum} {@link Class Type} of this {@link Enumable} implementation.
 */
//TO-EXPAND: Purpose
public interface Enumable<E extends Enum<E>>
        extends Serializable {
}
