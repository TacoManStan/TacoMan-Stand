package com.taco.tacository.ui.ui_internal.controllers;

import com.taco.tacository._to_sort.obj_traits.common.Nameable;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class SettingGroupController extends Controller
        implements Nameable
{
    public SettingGroupController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
}

/*
 * TODO List:
 * [S] Add support for sub-directories and different types of FXCreators.
 */