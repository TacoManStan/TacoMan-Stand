package com.taco.tacository.ui.console;

import com.taco.tacository.util.UIDProcessable;

import java.time.LocalDateTime;

public interface ConsoleMessageable<T>
        extends UIDProcessable
{
    T getText();
    
    LocalDateTime getTimestamp();
}
