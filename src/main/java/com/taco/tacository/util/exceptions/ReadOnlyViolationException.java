package com.taco.tacository.util.exceptions;

public class ReadOnlyViolationException extends UnsupportedOperationException
{
    public ReadOnlyViolationException()
    {
        super();
    }
    
    public ReadOnlyViolationException(String message)
    {
        super(message);
    }
    
    public ReadOnlyViolationException(Throwable cause)
    {
        super(cause);
    }
    
    public ReadOnlyViolationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
