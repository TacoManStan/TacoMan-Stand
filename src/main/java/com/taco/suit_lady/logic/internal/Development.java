package com.taco.suit_lady.logic.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Singleton {@code Spring} {@link Bean} housing development-related variables, functionality, and utility.
 */
@Component
public class Development
{
    private final boolean dev_mode;
    
    Development()
    {
        this.dev_mode = true;
    }
    
    //
    
    public boolean isDevMode()
    {
        return dev_mode;
    }
}
