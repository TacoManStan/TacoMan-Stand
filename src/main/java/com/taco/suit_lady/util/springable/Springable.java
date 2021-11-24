package com.taco.suit_lady.util.springable;

import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <b>--- To Format ---</b>
 * <br>
 * <p>An interface that guarantees all implementations will have direct and public access to...</p>
 * <ol>
 *     <li>Application Context Instance</li>
 *     <li>FxWeaver Instance
 * </ol>
 */
public interface Springable
{
    FxWeaver weaver();
    
    ConfigurableApplicationContext ctx();
}
