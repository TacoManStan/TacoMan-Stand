package com.taco.tacository.numbers;

import java.util.Arrays;
import java.util.List;

public enum Filter
{
    THROW_NPE("Throw NullPointerException"),
    THROW_NFE("Throw NumberFormatException"),
    ALLOW_INFINITY("Allow Infinity"),
    ALLOW_NaN("Allow NaN"),
    INT_ONLY("Allow Decimal");
    
    //
    
    private final String key;
    
    Filter(String key)
    {
        this.key = key;
    }
    
    //
    
    public final String key()
    {
        return this.key.toLowerCase();
    }
    
    public boolean matches(String[] args)
    {
        final List<String> argsList = Arrays.asList(args);
        return matches(argsList);
    }
    
    public boolean matches(List<String> argsList)
    {
        for (int i = 0; i < argsList.size(); i++)
            argsList.set(i, argsList.get(i).toLowerCase());
        
        return argsList.contains(key());
    }
}
