package com.taco.suit_lady.logic.internal;

import org.springframework.context.ApplicationContext;

public class CTXManager
{
    private ApplicationContext ctxRoot;
    
    public CTXManager() { }
    
    public final ApplicationContext ctx()
    {
        if (ctxRoot == null)
            throw new NullPointerException("Application Context has not been set — The CTX Manager must only be accessed after all application modules and contexts have finished initializing.");
        return ctxRoot;
    }
    
    void setCTXRoot(ApplicationContext ctx)
    {
        this.ctxRoot = ctx;
    }
    
    public static CTXManager createInstance()
    {
        return new CTXManager();
    }
}
