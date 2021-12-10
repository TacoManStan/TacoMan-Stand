package com.taco.suit_lady.util.timing;

import javafx.beans.property.ReadOnlyLongProperty;

public interface ReadOnlyTimerable
    extends Timeable
{
    ReadOnlyLongProperty timeoutProperty();
    long getTimeout();
    
    boolean isStarted();
    boolean isStopped();
    
    long getRemainingTime();
}
