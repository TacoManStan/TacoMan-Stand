package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.ui.UIPageController;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class SidebarNodeGroupController<T extends UIPage<?>> extends UIPageController<T>
{
    protected SidebarNodeGroupController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
}
