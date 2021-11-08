package com.taco.suit_lady.util._new.functional.filter;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * An enum representing the type of comparison to be used for objects being filtered by a {@link CompoundFilter}.
 * <br><br>
 * Note that the default type should always be {@link FilterType#ANY}
 */
public enum FilterType
{
    ANY, ALL, ONE, ALL_BUT_ONE, HALF_OR_MORE, HALF_OR_LESS, HALF
}
