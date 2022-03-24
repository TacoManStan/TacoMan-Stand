package com.taco.tacository._to_sort._expr.spring.beans.demo;

import com.taco.tacository._to_sort.obj_traits.common.ModifiableNameable;
import com.taco.tacository._to_sort.obj_traits.common.Testable;

public class DEMO_ExternalConfigurationBean
        implements Testable, ModifiableNameable
{
    private String name;
    
    public DEMO_ExternalConfigurationBean(String name)
    {
        this.name = name;
    }
    
    //
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
}
