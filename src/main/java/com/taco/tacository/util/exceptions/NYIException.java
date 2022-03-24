package com.taco.tacository.util.exceptions;

public class NYIException extends UnsupportedOperationException
{
    public NYIException()
    {
        super();
    }
    
    public NYIException(String message)
    {
        super(message);
    }
    
    public NYIException(Throwable cause)
    {
        super(cause);
    }
    
    public NYIException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
