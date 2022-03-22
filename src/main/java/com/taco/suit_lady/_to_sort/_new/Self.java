package com.taco.suit_lady._to_sort._new;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines implementing classes as being of type {@link T}.</p>
 * <p>The purpose of {@link Self} is to provide the <i>{@link #self()}</i> method alternative to casting a call to {@code this}.</p>
 *
 * @param <T> The {@link Class} type of this {@link Self} implementation.
 */
public interface Self<T extends Self<T>> {
    
    /**
     * <p>Returns a self-reference to {@code this} {@link Self} instance, cast to type {@link T}.</p>
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>return ({@link T}) this;</i></blockquote>
     *
     * @return A self-reference to {@code this} {@link Self} instance, cast to type {@link T}.
     */
    default @NotNull T self() { return (T) this; }
}
