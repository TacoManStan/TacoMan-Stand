package com.taco.suit_lady._to_sort._new.interfaces.functional;

@FunctionalInterface
public interface TriFunction<A, B, C, R>
{
    R apply(A a, B b, C c);
}
