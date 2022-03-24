package com.taco.tacository.util;

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
