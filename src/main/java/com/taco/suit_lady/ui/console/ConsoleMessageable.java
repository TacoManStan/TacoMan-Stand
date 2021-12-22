package com.taco.suit_lady.ui.console;

import com.taco.suit_lady.util.UIDProcessable;

import java.time.LocalDateTime;

public interface ConsoleMessageable<T>
        extends UIDProcessable
{
    T getText();
    
    LocalDateTime getTimestamp();
}
