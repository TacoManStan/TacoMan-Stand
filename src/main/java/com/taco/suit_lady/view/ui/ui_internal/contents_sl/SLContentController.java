package com.taco.suit_lady.view.ui.ui_internal.contents_sl;

import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class SLContentController<T extends SLContent<?>> extends Controller
{
    public SLContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
    }
}
