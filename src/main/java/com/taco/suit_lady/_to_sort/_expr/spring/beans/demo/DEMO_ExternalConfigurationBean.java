package com.taco.suit_lady._to_sort._expr.spring.beans.demo;

import com.taco.util.obj_traits.common.ModifiableNameable;
import com.taco.util.obj_traits.common.Testable;

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
