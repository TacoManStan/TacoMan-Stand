package com.taco.tacository;

import com.taco.tacository.obj_traits.common.Textable;

public class Util
{
    public Util() { }
    
    // -- METHODS -- //
    
    public static String toText(Object obj)
    {
        if (obj == null)
            return "null";
        else if (obj instanceof String)
            return (String) obj;
        else if (obj instanceof Textable)
            return ((Textable) obj).toText();
        return obj.toString();
    }
    
    // -- INNER CLASSES -- //
}
