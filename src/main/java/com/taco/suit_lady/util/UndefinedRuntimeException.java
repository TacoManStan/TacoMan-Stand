package com.taco.suit_lady.util;

public class UndefinedRuntimeException extends RuntimeException
{
    public UndefinedRuntimeException()
    {
        super();
    }
    
    public UndefinedRuntimeException(Exception e)
    {
        super(e);
    }
    
    public UndefinedRuntimeException(String msg)
    {
        super(msg);
    }
}
