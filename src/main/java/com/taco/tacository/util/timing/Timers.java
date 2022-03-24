package com.taco.tacository.util.timing;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Created by spenc on 5/17/2016.
 */
public class Timers
{
    private Timers() { } //No instance
    
    /**
     * <p>Default factory method for creating a new {@link Timer} object.</p>
     *
     * @param timeout     The {@code timeout} for the {@link Timer}, in milliseconds.
     * @param onTimeout   The {@link Runnable} that is executed whenever the {@link Timer} times out.
     * @param restartable True if the {@link Timer} is to be {@link Timer#isRestartable() restartable}, false it is not.
     *
     * @return The newly constructed {@link Timer} instance.
     */
    public static Timer newInstance(long timeout, Runnable onTimeout, boolean restartable)
    {
        return new Timer(timeout, onTimeout, restartable);
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Stopwatch Timers                                                              *
     *                                                                             *
     * *************************************************************************** */
    
    public static Timer newStopwatch()
    {
        return newStopwatch(null, true);
    }
    
    public static Timer newStopwatch(boolean restartable)
    {
        return newStopwatch(null, restartable);
    }
    
    public static Timer newStopwatch(@Nullable Runnable onTimeout, boolean restartable)
    {
        return new Timer(-1L, onTimeout, restartable);
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Countdown Timers                                                            *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Creates and then returns a new {@link Timer} that counts down from the specified {@code timeout} (milliseconds).
     * <ul>
     * <li>{@link Number#longValue()} is always used when determining the value used for the {@code timeout}.</li>
     * <li>A {@link Duration} can be used as the {@code timeout} to easily provide additional {@link TimeUnit} options.</li>
     * </ul>
     *
     * @param timeout The time in which the new {@code Timer} will count down from. Specified in milliseconds.
     *
     * @return The newly created {@code Countdown Timer}.
     */
    public static Timer newCountdown(Number timeout)
    {
        return new Timer(timeout, null, true);
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Debug Timers                                                                *
     *                                                                             *
     * *************************************************************************** */
    
    public static DebugTimer newDebug()
    {
        return newDebug(null, -1L);
    }
    
    public static DebugTimer newDebug(long limit)
    {
        return newDebug(null, limit);
    }
    
    public static DebugTimer newDebug(String name)
    {
        return newDebug(name, -1L);
    }
    
    public static DebugTimer newDebug(String name, long limit)
    {
        return new DebugTimer(name, limit);
    }
}
