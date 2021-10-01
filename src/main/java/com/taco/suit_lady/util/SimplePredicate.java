package com.taco.suit_lady.util;

import java.util.function.Predicate;

@FunctionalInterface
public interface SimplePredicate extends Predicate<Object>
{
    boolean test();
    
    @Override
    default boolean test(Object obj)
    {
        return test();
    }
}
