package com.taco.suit_lady._to_sort._new.interfaces.functional;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D>
{
    void accept(A a, B b, C c, D d);
}