package com.taco.suit_lady.util.values.numbers;


import com.taco.suit_lady.util.values.numbers.expressions.NumExpr;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Num(Number a)
        implements NumExpr<Num> {
}
