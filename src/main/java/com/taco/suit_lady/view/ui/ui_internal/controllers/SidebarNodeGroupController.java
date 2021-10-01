package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.view.ui.UIPage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class SidebarNodeGroupController<T extends UIPage<?>> extends UIPageController<T>
{
    protected SidebarNodeGroupController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
}
