package com.taco.suit_lady.util.springable;

import com.taco.suit_lady.util.tools.ExceptionTools;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public record StrictSpringable(FxWeaver weaver, ConfigurableApplicationContext ctx)
        implements Springable
{
    public StrictSpringable(@NotNull FxWeaver weaver, @NotNull ConfigurableApplicationContext ctx)
    {
        this.weaver = ExceptionTools.nullCheck(weaver, "FxWeaver");
        this.ctx = ExceptionTools.nullCheck(ctx, "Application Context");
    }
}
