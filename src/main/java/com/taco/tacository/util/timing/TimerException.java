package com.taco.tacository.util.timing;

public class TimerException
    extends RuntimeException
{
    public TimerException()
    {
        super();
    }
    
    public TimerException(String message)
    {
        super(message);
    }
    
    public TimerException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public TimerException(Throwable cause)
    {
        super(cause);
    }
}
