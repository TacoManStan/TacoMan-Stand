package com.taco.suit_lady.util.timing;

public interface Timerable
    extends ReadOnlyTimerable
{
    void setTimeout(Number timeout);
    boolean isTimedOut();
    
    Timerable start();
    Timerable start(Number newTimeout);
    
    Timerable reset(Number newTimeout);
}
