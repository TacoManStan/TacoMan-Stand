package com.taco.suit_lady.util.timing;

public interface Timerable
    extends ReadOnlyTimerable, Timeable
{
    void setTimeout(Number timeout);
    boolean isTimedOut();
    
    ReadOnlyTimerable start(Number newTimeout);
    ReadOnlyTimerable reset(Number newTimeout);
}
