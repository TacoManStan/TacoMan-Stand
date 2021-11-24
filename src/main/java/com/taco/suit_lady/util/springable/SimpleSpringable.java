package com.taco.suit_lady.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public record SimpleSpringable(FxWeaver weaver, ConfigurableApplicationContext ctx)
        implements Springable { }
