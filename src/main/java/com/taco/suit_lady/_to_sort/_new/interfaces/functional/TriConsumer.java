package com.taco.suit_lady._to_sort._new.interfaces.functional;

import java.util.function.Consumer;

@FunctionalInterface
public interface TriConsumer<A, B, C>
{
    void accept(A a, B b, C c);
}
