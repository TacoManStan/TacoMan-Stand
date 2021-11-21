package com.taco.suit_lady._to_sort._expr.spring.beans.demo;

import com.taco.util.obj_traits.common.ModifiableNameable;
import com.taco.util.obj_traits.common.Testable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DEMO_PrototypeComponentBean
        implements Testable, ModifiableNameable
{
    private String name;
    
    public DEMO_PrototypeComponentBean(@Value("autowired_bean") String name)
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
