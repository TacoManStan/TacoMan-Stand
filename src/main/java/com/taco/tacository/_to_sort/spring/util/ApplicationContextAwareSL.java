package com.taco.suit_lady.uncategorized.spring.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public interface ApplicationContextAwareSL extends ApplicationContextAware
{
    ApplicationContext ctx();
    
    default boolean isContextValid()
    {
        return ctx() != null;
    }
}
