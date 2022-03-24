package com.taco.tacository._to_sort._expr.spring.beans.demo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("bean4")
@Primary
public class DEMO_SingletonBeanImpl extends DEMO_SingletonBean
{
    public DEMO_SingletonBeanImpl()
    {
        super(
                "singleton bean child",
                () -> "contents of component-constructed singleton bean implementation. cool."
        );
    }
}
