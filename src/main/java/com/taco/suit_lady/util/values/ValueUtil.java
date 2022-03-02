package com.taco.suit_lady.util.values;

import com.taco.suit_lady.util.tools.Exceptions;
import org.jetbrains.annotations.NotNull;

class ValueUtil {
    private ValueUtil() { } //No Instance
    
    public static boolean asBool(@NotNull Number num) {
        if (asInt(num) == 0)
            return false;
        else if (asInt(num) == 1)
            return true;
        else
            throw Exceptions.unsupported("Input for boolean num conversion must be valid binary (0 or 1): " + num);
    }
    
    public static int asInt(@NotNull Number num) { return num.intValue(); }
    public static long asLong(@NotNull Number num) { return num.longValue(); }
    
    public static float asFloat(@NotNull Number num) { return num.floatValue(); }
    public static double asDouble(@NotNull Number num) { return num.doubleValue(); }
}
