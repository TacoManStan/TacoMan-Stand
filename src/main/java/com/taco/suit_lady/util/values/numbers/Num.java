package com.taco.suit_lady.util.values.numbers;


/**
 * <p>Defines an {@code immutable}, {@code default} implementation of {@link NumExpr}.</p>
 *
 * @param a The {@link Number} {@link #a() Value} contained within this {@link NumExpr} instance.
 */
//TO-EXPAND
public record Num(Number a)
        implements NumExpr<Num> {
}
