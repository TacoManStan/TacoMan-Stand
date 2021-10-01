package com.taco.suit_lady._expr.spring.beans.demo;

import com.taco.util.obj_traits.common.ModifiableNameable;
import com.taco.util.obj_traits.common.Testable;

public class DEMO_XmlBean
        implements Testable, ModifiableNameable
{
    private String name;
    
    public DEMO_XmlBean(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    //
    
    public static DEMO_XmlBean createInstance()
    {
        return new DEMO_XmlBean("example bean");
    }
}
