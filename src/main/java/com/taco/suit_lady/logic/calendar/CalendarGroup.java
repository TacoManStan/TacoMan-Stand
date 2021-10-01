package com.taco.suit_lady.logic.calendar;

import com.taco.util.obj_traits.common.Nameable;

public class CalendarGroup
        implements Nameable
{
    private final String name;
    
    public CalendarGroup(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
}
