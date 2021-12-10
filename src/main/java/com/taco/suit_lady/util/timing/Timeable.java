package com.taco.suit_lady.util.timing;

public interface Timeable
        extends ReadOnlyTimeable
{
    ReadOnlyTimeable start();
    ReadOnlyTimeable reset();
    ReadOnlyTimeable stop();
}


/*
 * TODO LIST:
 * [S] Finish implementation.
 * [S] Make sure functionality of all action methods (start, stop, reset, etc) are consistent across all implementations of Timeable.
 */