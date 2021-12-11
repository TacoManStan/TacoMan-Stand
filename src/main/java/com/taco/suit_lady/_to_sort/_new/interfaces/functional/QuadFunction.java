package com.taco.suit_lady._to_sort._new.interfaces.functional;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, R>
{
    R apply(A a, B b, C c, D d);
}
