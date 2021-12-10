package com.taco.suit_lady.util.timing;

public interface ReadOnlyTimeable
{
    long getElapsedTime();
    //	double getElapsedTime(TimeUnit timeUnit);
    
    long getStartTime();
    //	double getStartTime(TimeUnit timeUnit);
}
