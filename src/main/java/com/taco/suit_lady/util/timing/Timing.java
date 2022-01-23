package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.SimplePredicate;
import com.taco.suit_lady.util.tools.TB;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public final class Timing
{
    
    private Timing() { } // No instance
    
    private static final long START_TIME = System.currentTimeMillis();
    private static final long NANO_TIME = System.nanoTime();
    
    /**
     * Returns the current time in milliseconds using {@link System#nanoTime()}
     * to avoid problems with changing clock times.
     *
     * @return The current time in milliseconds.
     * @see System#currentTimeMillis()
     * @see System#nanoTime()
     */
    public static long currentTimeMillis()
    {
        return Timing.START_TIME + ((System.nanoTime() - Timing.NANO_TIME) / 1000000);
    }
    
    /**
     * Returns the current time represented by the specified {@link TimeUnit}.
     *
     * @param timeUnit The {@code TimeUnit} of the returned value.
     * @return The current time represented by the specified {@link TimeUnit}.
     * @see #currentTimeMillis()
     * @see System#currentTimeMillis()
     */
    public static long currentTime(TimeUnit timeUnit)
    {
        return SLExceptions.nullCheck(timeUnit, "Time Unit").convert(currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    //
    
    /**
     * The default wait time. In milliseconds.
     * <p>
     * Used by {@link #wait(SimplePredicate)}.
     */
    public static long wait_time = 2000;
    
    /**
     * Waits for the specified {@link SimplePredicate} to be active. Uses the default
     * {@link #wait_time} as the timeout.
     *
     * @param condition The {@link SimplePredicate}.
     * @return True if the {@link SimplePredicate} was met before the timeout, false
     * otherwise.
     */
    public static boolean wait(SimplePredicate condition)
    {
        return wait(condition, wait_time);
    }
    
    /**
     * Waits for the specified {@link SimplePredicate} to be active.
     *
     * @param condition The {@link SimplePredicate}.
     * @param timeout   The timeout. In milliseconds. -1 for no timeout.
     * @return True if the {@link SimplePredicate} was met before the timeout, false
     * otherwise.
     */
    public static boolean wait(SimplePredicate condition, long timeout)
    {
        Timer timer = Timers.newCountdown(timeout).start();
        while (timeout == -1 || !timer.isTimedOut())
        {
            if (condition.test())
                return true;
            TB.general().sleepLoop();
        }
        return false;
    }
    
    //
    
    public static Duration createDurationMillis(Number duration)
    {
        SLExceptions.nullCheck(duration, "Duration");
        return new Duration();
    }
    
    public static String timestamp(LocalDateTime ldt)
    {
        if (ldt == null)
            return "[LDT is NULL]";
    
        return "[" + ldt.format(DateTimeFormatter.ISO_LOCAL_TIME) + "]";
    }
    
    public static String now()
    {
        return timestamp(LocalDateTime.now());
    }
}
