package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.UIPage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPageController<T extends UIPage<?>> extends Controller
{
    private T page;
    
    protected UIPageController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
    
    public T getPage()
    {
        return page;
    }
    
    public void setPage(T page)
    {
        this.page = page;
        this.onPageBindingComplete(); // TODO - This is smelly af, move it somewhere else and properly abstract it.
    }
    
    /**
     * <b>--- To Format ---</b>
     * <br><br>
     * Runs UIPageController have been {@link #initialize() auto-initialized} by the JFX internals and immediately after both the UIPage and UIPageController have non-null references to each other.
     */
    protected void onPageBindingComplete() { }
}