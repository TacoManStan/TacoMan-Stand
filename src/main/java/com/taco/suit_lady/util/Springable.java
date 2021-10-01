package com.taco.suit_lady.util;

import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <b>--- To Format ---</b>
 * <br><br>
 * An interface that guarantees all implementations will have direct and public access to...
 * 1. Application Context
 * 2. FxWeaver Singleton Instance
 */
public interface Springable
{
    FxWeaver weaver();
    
    ConfigurableApplicationContext ctx();
}
