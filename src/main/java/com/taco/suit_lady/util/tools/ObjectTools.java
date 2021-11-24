package com.taco.suit_lady.util.tools;

import java.util.Objects;

public class ObjectTools
{
    public static boolean equals(Object obj1, Object obj2)
    {
        return Objects.equals(obj1, obj2);
    }
    
    public static boolean equalsExcludeNull(Object obj1, Object obj2)
    {
        return obj1 != null && obj2 != null && Objects.equals(obj1, obj2);
    }
    
    //
    
    public static boolean equalsAny(Object obj, Object... objs)
    {
        return equals(obj, false, true, false, objs);
    }
    
    public static boolean equalsAll(Object obj, Object... objs)
    {
        return equals(obj, true, true, false, objs);
    }
    
    public static boolean equals(Object obj, boolean requireAll, boolean allowNull, boolean deep, Object... objs)
    {
        if (objs == null || (!allowNull && obj == null))
            return false;
        else if (objs.length == 1)
            return equalsImpl(obj, objs[0], allowNull, deep);
        else
        {
            for (Object arrObj: objs)
                if (equalsImpl(obj, arrObj, allowNull, deep))
                {
                    if (requireAll)
                        return false;
                }
                else
                {
                    if (!requireAll)
                        return true;
                }
            return requireAll;
        }
    }
    
    private static boolean equalsImpl(Object obj1, Object obj2, boolean allowNull, boolean deep)
    {
        if (allowNull && obj1 == null && obj2 == null)
            return true;
        else
            return deep ? Objects.deepEquals(obj1, obj2) : Objects.equals(obj1, obj2);
    }
}