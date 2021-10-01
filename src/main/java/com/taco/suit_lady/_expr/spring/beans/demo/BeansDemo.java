package com.taco.suit_lady._expr.spring.beans.demo;

import com.taco.util.obj_traits.common.Testable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class BeansDemo
        implements ApplicationContextAware
{
    private ApplicationContext ctx;
    
    public BeansDemo() { }
    
    //
    
    public void demo()
    {
        prototypeComponentBeanDemo();
        xmlDemo();
        autowiredComponentDemo();
        configurationDemo();
        configurationSingletonDemo();
    }
    
    public void prototypeComponentBeanDemo()
    {
        System.out.println("Initializing: Prototype Component Bean Demo...");
        
        ArrayList<DEMO_PrototypeComponentBean> beans = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            DEMO_PrototypeComponentBean bean = ctx.getBean(DEMO_PrototypeComponentBean.class);
            bean.setName("Bean " + i);
    
            beans.add(bean);
            
            beans.add(ctx.getBean(DEMO_PrototypeComponentBean.class, "Injected Args Bean " + i));
        });
    
        beans.forEach(Testable::test);
    }
    
    public void xmlDemo()
    {
        System.out.println("Initializing: XML Bean Demo...");
        
        DEMO_XmlBean bean = ctx.getBean(DEMO_XmlBean.class);
        bean.test();
    }
    
    public void autowiredComponentDemo()
    {
        System.out.println("Initializing: Autowired Component Bean Demo...");
        
        DEMO_AutowiredComponentBean bean = ctx.getBean(DEMO_AutowiredComponentBean.class);
        bean.test();
    }
    
    public void configurationDemo()
    {
        System.out.println("Initializing: External Configuration Bean Demo");
        
        DEMO_ExternalConfigurationBean bean = ctx.getBean(DEMO_ExternalConfigurationBean.class);
        bean.test();
    }
    
    public void configurationSingletonDemo()
    {
        System.out.println("Initializing: External Configuration Singleton Bean Demo");
        
        final ArrayList<DEMO_SingletonBean> beanList = new ArrayList<>();
        
        beanList.add(ctx.getBean("bean1", DEMO_SingletonBean.class));
        beanList.add(ctx.getBean("bean2", DEMO_SingletonBean.class));
        beanList.add(ctx.getBean("bean3", DEMO_SingletonBean.class));
        beanList.add(ctx.getBean("bean4", DEMO_SingletonBean.class));
        beanList.add(ctx.getBean(DEMO_SingletonBean.class));
        
        System.out.println("---------------------------------------------");
        System.out.println("--------------- TESTING BEANS ---------------");
        System.out.println("---------------------------------------------");
        
        final AtomicInteger i = new AtomicInteger();
        beanList.forEach(bean -> System.out.println(
                "[Iteration #" + i.incrementAndGet() + "]: " +
                bean.getName() + beanList.indexOf(bean) + bean.call()
        ));
        
        System.out.println("--------------------");
    }
    
    //
    
    //<editor-fold desc="Application Context">
    
    public ApplicationContext ctx()
    {
        return this.ctx;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
    
    //</editor-fold>
}
