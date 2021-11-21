package com.taco.suit_lady._to_sort._expr.spring.beans.demo;

import org.springframework.context.annotation.*;

@Configuration
public class DEMO_BeanConfigurations
{
    @Bean
    public DEMO_ExternalConfigurationBean configurationBean_Demo()
    {
        return new DEMO_ExternalConfigurationBean("test name");
    }
    
    @Bean("bean1")
    @Scope("singleton")
    public DEMO_SingletonBean singletonBean1_Demo()
    {
        return new DEMO_SingletonBean(
                "singleton bean 1",
                () -> "Contents of bean 1 right here m8"
        );
    }
    
    @Bean("bean2")
    @Scope("singleton")
    public DEMO_SingletonBean singletonBean2_Demo()
    {
        return new DEMO_SingletonBean(
                "singleton bean 2",
                () -> "ooof m8, this be the contents of bean numba 2, m8"
        );
    }
    
    @Bean("bean3")
    @Scope("singleton")
    public DEMO_SingletonBean singletonBean3_Demo()
    {
        return new DEMO_SingletonBean(
                "singleton bean 3",
                () -> "AHHHHHHHHHHHHHHHHHHHH ok."
        );
    }
}
