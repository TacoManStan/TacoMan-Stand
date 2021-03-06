package com.taco.tacository.ui.ui_internal.controllers;

import com.taco.tacository.ui.UIPage;
import com.taco.tacository.ui.UIPageController;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Defines the base {@link UIPageController} implementation for a {@link UIPage} implementation of type {@link T}</p>
 *
 * @param <T> The {@link UIPage} implementation this {@link SidebarNodeGroupController} {@code controls}.
 */
public abstract class SidebarNodeGroupController<T extends UIPage<?>> extends UIPageController<T> {
    
    /**
     * {@inheritDoc}
     */
    protected SidebarNodeGroupController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
}
