package com.taco.suit_lady._to_sort._expr.spring.beans.demo;

import com.taco.tacository.obj_traits.common.Nameable;

import java.util.concurrent.Callable;

public class DEMO_SingletonBean
        implements Nameable, Callable<String>
{
    private final String name;
    private final Callable<String> contentsCallback;
    
    public DEMO_SingletonBean(String name, Callable<String> contentsCallback)
    {
        this.name = name;
        this.contentsCallback = contentsCallback;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public String call()
    {
        try
        {
            return contentsCallback.call();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
