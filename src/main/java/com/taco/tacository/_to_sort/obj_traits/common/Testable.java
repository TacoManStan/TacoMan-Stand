package com.taco.tacository._to_sort.obj_traits.common;

public interface Testable
{
    default void test(String... args)
    {
        if (this instanceof com.taco.tacository._to_sort.obj_traits.common.Nameable)
        {
            System.out.println("Testing object of type [" + getClass().getSimpleName() + "]: " + "\"" + ((Nameable) this).getName() + "\"");
        }
        else
        {
            System.out.println("Testing Object: " + getClass().getSimpleName());
        }
    }
}
