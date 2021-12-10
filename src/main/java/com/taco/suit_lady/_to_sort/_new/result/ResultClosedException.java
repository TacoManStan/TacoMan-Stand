package com.taco.suit_lady._to_sort._new.result;

public class ResultClosedException extends RuntimeException
{
    private static final String msg = "Result has been closed and can no longer be modified";
    
    public ResultClosedException()
    {
        super(msg + ".");
    }
    
    public ResultClosedException(String msg)
    {
        super(msgBlock(msg));
    }
    
    public ResultClosedException(String msg, Throwable cause)
    {
        super(msgBlock(msg), cause);
    }
    
    public ResultClosedException(Throwable cause)
    {
        super(cause);
    }
    
    private static String msgBlock(String msg)
    {
        return msg + ":  [" + msg + "]";
    }
}
