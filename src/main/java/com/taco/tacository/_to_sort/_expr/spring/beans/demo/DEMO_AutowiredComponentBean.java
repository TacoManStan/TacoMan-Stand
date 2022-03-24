package com.taco.tacository._to_sort._expr.spring.beans.demo;

import com.taco.tacository._to_sort.obj_traits.common.ModifiableNameable;
import com.taco.tacository._to_sort.obj_traits.common.Testable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DEMO_AutowiredComponentBean
        implements Testable, ModifiableNameable
{
    private String name;
    
    public DEMO_AutowiredComponentBean(@Value("autowired_bean") String name)
    {
        this.name = name;
    }
    
    //
    
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
}
