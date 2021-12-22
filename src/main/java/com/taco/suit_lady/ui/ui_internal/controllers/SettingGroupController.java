package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.tacository.obj_traits.common.Nameable;
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