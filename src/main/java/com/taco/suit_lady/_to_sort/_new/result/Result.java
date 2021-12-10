package com.taco.suit_lady._to_sort._new.result;

import com.taco.suit_lady.util.timing.*;
import javafx.beans.property.ReadOnlyLongProperty;

import java.util.Optional;

public abstract class Result<T>
        implements Timerable
{
    private T value;
    private RuntimeException exception;
    private String message;
    
    private final Timer timer;
    private boolean closed;
    
    {
        this.timer = Timers.newStopwatch(false);
        this.closed = false;
    }
    
    public Result(T defaultValue)
    {
        setValue(defaultValue);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Optional<T> value()
    {
        return Optional.ofNullable(value);
    }
    
    public final void setValue(T value)
    {
        check().value = value;
    }
    
    public final RuntimeException exception()
    {
        return exception;
    }
    
    public final void setException(RuntimeException exception)
    {
        check().exception = exception;
    }
    
    public final String message()
    {
        return message;
    }
    
    public final void setMessage(String message)
    {
        check().message = message;
    }
    
    //
    
    public final boolean isClosed()
    {
        return closed;
    }
    
    public final void close()
    {
        closed = true;
        timer.stop();
    }
    
    //</editor-fold>
    
    protected Result<T> check()
    {
        if (isClosed())
            throw new ResultClosedException();
        return this;
    }
    
    //
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public ReadOnlyLongProperty timeoutProperty()
    {
        return timer.timeoutProperty();
    }
    
    @Override
    public long getTimeout()
    {
        return timer.getTimeout();
    }
    
    @Override
    public boolean isStarted()
    {
        return timer.isStarted();
    }
    
    @Override
    public boolean isStopped()
    {
        return timer.isStopped();
    }
    
    @Override
    public long getRemainingTime()
    {
        return timer.getRemainingTime();
    }
    
    @Override
    public long getElapsedTime()
    {
        return timer.getElapsedTime();
    }
    
    @Override
    public long getStartTime()
    {
        return timer.getStartTime();
    }
    
    //
    
    @Override
    public ReadOnlyTimerable start()
    {
        return check().timer.start();
    }
    
    @Override
    public ReadOnlyTimerable reset()
    {
        return check().timer.reset();
    }
    
    @Override
    public ReadOnlyTimerable stop()
    {
        return check().timer.stop();
    }
    
    //</editor-fold>
}
