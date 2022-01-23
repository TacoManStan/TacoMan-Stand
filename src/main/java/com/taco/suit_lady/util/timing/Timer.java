package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.tools.SLCalculations;
import com.taco.suit_lady.util.tools.SLExceptions;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO - Rewrite/update ALL JavaDocs

/**
 * Allows for the creation of various countdown timers.
 *
 * To create a new Timer object, refer to the {@link Timers} class.
 *
 * @see Timers
 */
public class Timer
        implements Timerable, ReactiveTimerable
{
    
    private final boolean restartable;
    private final ReadOnlyIntegerWrapper startCountProperty;
    
    private final ReadOnlyLongWrapper timeoutProperty;
    private long startTime;
    private boolean stopped;
    
    private final ReadOnlyObjectWrapper<Runnable> onTimeoutProperty;
    
    /**
     * Constructs a new timer with the specified timeout (ms) and timeout action.
     *
     * @param timeout   The timeout. In milliseconds.
     * @param onTimeout A Runnable that is executed when the timer times out. Null for no action.
     *
     * @see Timers
     */
    protected Timer(@NotNull Number timeout, @Nullable Runnable onTimeout, boolean restartable)
    {
        this.restartable = restartable;
        this.startCountProperty = new ReadOnlyIntegerWrapper(0);
        
        this.timeoutProperty = new ReadOnlyLongWrapper(timeout.longValue());
        this.startTime = 0L;
        this.stopped = false;
        
        this.onTimeoutProperty = new ReadOnlyObjectWrapper<>(onTimeout);
    }
    
    //<editor-fold desc="Properties">
    
    public final boolean isRestartable()
    {
        return restartable;
    }
    
    public final @NotNull ReadOnlyIntegerProperty startCountProperty()
    {
        return startCountProperty.getReadOnlyProperty();
    }
    
    public final int getStartCount()
    {
        return startCountProperty.get();
    }
    
    /**
     * Returns the timeout property for this timer.
     *
     * @return The timeout property for this timer.
     */
    @Override
    public final @NotNull ReadOnlyLongProperty timeoutProperty()
    {
        return timeoutProperty.getReadOnlyProperty();
    }
    
    /**
     * Returns the timeout of the timer.
     *
     * @return The timeout.
     */
    @Override
    public final long getTimeout()
    {
        return timeoutProperty.get();
    }
    
    /**
     * Sets the timeout of the timer to be equal to the specified timeout.
     *
     * @param timeout The timeout. Any number less than 0 will result in no timeout being used.
     */
    @Override
    public final void setTimeout(@NotNull Number timeout)
    {
        timeoutProperty.set(SLCalculations.clampMin(timeout.longValue(), -1L));
    }
    
    //
    
    /**
     * Checks to see if the timer has been started or not.
     * <p>
     * This method differs from {@link Timer#isStopped()} in that this method will return true if this method has ever been started, regardless if the timer is currently stopped.
     *
     * @return True if the timer has been started, false otherwise.
     */
    @Override
    public final boolean isStarted()
    {
        return startTime != 0;
    }
    
    /**
     * Checks to see if the timer is stopped or not.
     * <p>
     * This method differs from {@link Timer#isStarted()} in that this method will return true even if the timer has been previously started.
     *
     * @return True if the timer is stopped, false otherwise.
     */
    @Override
    public final boolean isStopped()
    {
        return stopped;
    }
    
    //
    
    @Override
    public final @NotNull ReadOnlyObjectProperty<Runnable> onTimeoutProperty()
    {
        return onTimeoutProperty.getReadOnlyProperty();
    }
    
    @Override
    public final @Nullable Runnable getOnTimeout()
    {
        return onTimeoutProperty.get();
    }
    
    @Override
    public final void setOnTimeout(@Nullable Runnable onTimeout)
    {
        onTimeoutProperty.set(onTimeout);
    }
    
    //</editor-fold>
    
    //
    
    /**
     * Gets the start time of the timer.
     *
     * @return The start time. 0 if the timer has not yet been started.
     */
    @Override
    public final long getStartTime()
    {
        return startTime;
    }
    
    /**
     * Gets the time that has elapsed since the start of the timer.
     *
     * @return The time that has elapsed since the start of the timer.
     */
    @Override
    public final long getElapsedTime()
    {
        return Timing.currentTimeMillis() - getStartTime();
    }
    
    /**
     * Gets the time left before the timer times out.
     * <p>
     * If this timer is not using a timeout, {@link Double#POSITIVE_INFINITY} is returned.
     *
     * @return The amount of milliseconds before the timer times out.
     */
    @Override
    public final long getRemainingTime()
    {
        return (getStartTime() + getTimeout()) - Timing.currentTimeMillis();
    }
    
    /**
     * Checks to see whether or not the timer has timed out.
     *
     * @return True if the timer has timed out, false otherwise.
     */
    @Override
    public final boolean isTimedOut()
    {
        final long timeout = getTimeout();
        return timeout >= 0 && (stopped || Timing.currentTimeMillis() > startTime + timeout);
    }
    
    //
    
    /**
     * Starts this timer.
     */
    @Override
    public final @NotNull Timer start()
    {
        if (getStartCount() > 0 && !isRestartable())
            throw new TimerException("Attempted to restart a Timer that is not restartable.");
        startCountProperty.set(getStartCount() + 1);
        
        this.stopped = false;
        this.startTime = Timing.currentTimeMillis();
        
        return this;
    }
    
    /**
     * Starts this timer, setting the timer's timeout to the given value.
     *
     * @param newTimeout The new timeout for this timer. -1 to start the timer without changing the timeout.
     */
    @Override
    public final @NotNull Timer start(@NotNull Number newTimeout)
    {
        long long_new_timeout = SLExceptions.nullCheck(newTimeout, "New Timeout").longValue();
        setTimeout(long_new_timeout);
        return start();
    }
    
    /**
     * Resets the timer. Calling this method has no effect if the timer has not yet been started.
     */
    @Override
    public final @NotNull Timer reset()
    {
        return reset(-1);
    }
    
    /**
     * Resets this timer, setting the timer's timeout to the given value.
     *
     * @param newTimeout The new timeout for this timer. -1 to not change the timeout. Cannot be less than -1.
     */
    @Override
    public final @NotNull Timer reset(@NotNull Number newTimeout)
    {
        long long_new_timeout = SLExceptions.nullCheck(newTimeout, "New Timeout").longValue();
        if (getStartTime() == 0)
            return this;
        return start(long_new_timeout);
    }
    
    /**
     * Stops this timer.
     */
    @Override
    public final @NotNull Timer stop()
    {
        stopped = true;
        return this;
    }
    
    // TODO
    // Add support for start and stop event responses w/ detailed Timer information passed via constructor (time in ms the event took place, etc.)
}
