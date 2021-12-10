package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.tools.CalculationTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * User for debug timing.
 *
 * @author Spencer
 */
public class DebugTimer
        implements Timeable
{
    
    private long startTime;
    private long limit;
    private final String name;
    
    private final ObjectProperty<TimeUnit> dispTimeUnitProperty;
    private final BooleanProperty dispDecimalProperty;
    private final BooleanProperty resetOnPrintProperty;
    
    protected DebugTimer(String name, long limit)
    {
        this.name = name;
        this.limit = limit;
        
        // TODO [S]: Add implementation.
        this.dispTimeUnitProperty = new SimpleObjectProperty<>(null);
        this.dispDecimalProperty = new SimpleBooleanProperty(true);
        this.resetOnPrintProperty = new SimpleBooleanProperty(false);
    }
    
    //<editor-fold desc="Properties">
    
    @Override public long getStartTime()
    {
        return (long) getStartTimeDbl();
    }
    
    public double getStartTimeDbl()
    {
        return getStartTimeNanos() / 1000000D;
    }
    
    public long getStartTimeNanos()
    {
        return startTime;
    }
    
    //
    
    @Override public final long getElapsedTime()
    {
        return (long) getElapsedTimeDbl();
    }
    
    public final double getElapsedTimeDbl()
    {
        return getElapsedTimeNanos() / 1000000D;
    }
    
    public final long getElapsedTimeNanos()
    {
        return (System.nanoTime() - startTime);
    }
    
    //
    
    public final void setLimit(long limit)
    {
        this.limit = limit;
    }
    
    public final long getLimit()
    {
        return limit;
    }
    
    public final boolean meetsLimit()
    {
        return limit == -1 || getElapsedTime() >= limit;
    }
    
    //</editor-fold>
    
    /* *************************************************************************** *
     *                                                                             *
     * Start/Stop/Reset                                                            *
     *                                                                             *
     * *************************************************************************** */
    
    @Override public DebugTimer start()
    {
        startTime = System.nanoTime();
        return this;
    }
    
    @Override public DebugTimer reset()
    {
        return start();
    }
    
    @Override public DebugTimer stop()
    {
        startTime = 0L;
        return this;
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Printing                                                                    *
     *                                                                             *
     * *************************************************************************** */
    
    public void print()
    {
        print(null, true);
    }
    
    public void print(String message)
    {
        print(message, true);
    }
    
    public void print(boolean reset)
    {
        print(null, reset);
    }
    
    public void print(String message, boolean reset)
    {
        if (meetsLimit())
            if (reset)
                reset();
    }
    
    //
    
    public <T> T print(Supplier<T> supplier)
    {
        return print(null, supplier);
    }
    
    public <T> T print(String message, Supplier<T> supplier)
    {
        reset();
        T t = supplier.get();
        print(message);
        return t;
    }
    
    //
    
    private String hlpr_getText(String message)
    {
        return "[TIMER] " + (name == null ? "NONE" : name) + ":" + (message != null ? " " + message : "")
               + " (" + CalculationTools.roundD(getElapsedTimeDbl(), 0, 2) + " ms)";
    }
}


/*
 * TODO LIST:
 * [S] Make DebugTimer stoppable.
 *     [S] When a DebugTimer is stopped, the stop time is saved, and any info retrieved uses that stop time.
 */