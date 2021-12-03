package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady._to_sort._new.interfaces.functional.filter.CompoundFilter;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * An enum representing the type of comparison to be used for objects being compared, such as by a {@link CompoundFilter}.
 * <br><br>
 * Note that the default type should always be {@link CompareType#ANY}
 */
public enum CompareType
{
    ANY, ALL, ONE, ALL_BUT_ONE, HALF_OR_MORE, HALF_OR_LESS, HALF
}
