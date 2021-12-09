package com.taco.suit_lady.util.exceptions;

public class NYIException extends RuntimeException
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
