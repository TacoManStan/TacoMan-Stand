package com.taco.suit_lady.util.timing;

import com.taco.suit_lady.util.tools.Calc;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <p>An implementation of {@link Timeable} used specifically for {@code debugging}.</p>
 * <p><b>Printing</b></p>
 * The primary purpose of a {@link DebugTimer} is to provide {@code debug} {@link #print() Print Functions}:
 * <ul>
 *     <li><i>{@link #print(String, Supplier)}</i></li>
 *     <li><i>{@link #print(String, boolean)}</i></li>
 *     <li><i>{@link #print(Supplier)}</i></li>
 *     <li><i>{@link #print(String)}</i></li>
 *     <li><i>{@link #print(boolean)}</i></li>
 *     <li><i>{@link #print()}</i></li>
 * </ul>
 * <p><b>Functionality Properties</b></p>
 * {@link DebugTimer} also contains several {@link Property properties} that define specific functionalities of this {@link DebugTimer}:
 * <ul>
 *     <li><b>{@link #dispTimeUnitProperty()}:</b> The {@link TimeUnit} this {@link DebugTimer} {@link #print() prints} in.</li>
 *     <li><b>{@link #dispDecimalProperty()}:</b> {@code True} if this {@link DebugTimer} {@link #print() prints} values as {@code decimals}, {@code false} if as {@link Integer ints}.</li>
 *     <li><b>{@link #resetOnPrintProperty()}:</b> {@code True} if this {@link DebugTimer} should {@link #reset() reset} when any <i>{@link #print()}</i> operation is executed, {@code false} if it should not.</li>
 * </ul>
 *
 * @see ReadOnlyTimeable
 * @see ReadOnlyTimerable
 * @see ReadOnlyReactiveTimerable
 * @see Timeable
 * @see Timerable
 * @see ReactiveTimerable
 * @see Timer
 * @see Timers
 * @see Timing
 */
public class DebugTimer
        implements Timeable {
    
    private long startTime;
    private long limit;
    private final String name;
    
    private final ObjectProperty<TimeUnit> dispTimeUnitProperty;
    private final BooleanProperty dispDecimalProperty;
    private final BooleanProperty resetOnPrintProperty;
    
    protected DebugTimer(String name, long limit) {
        this.name = name;
        this.limit = limit;
        
        // TODO [S]: Add implementation.
        this.dispTimeUnitProperty = new SimpleObjectProperty<>(null);
        this.dispDecimalProperty = new SimpleBooleanProperty(true);
        this.resetOnPrintProperty = new SimpleBooleanProperty(false);
    }
    
    //<editor-fold desc="Properties">
    
    public final @NotNull ObjectProperty<TimeUnit> dispTimeUnitProperty() { return dispTimeUnitProperty; }
    public final @NotNull BooleanProperty dispDecimalProperty() { return dispDecimalProperty; }
    public final @NotNull BooleanProperty resetOnPrintProperty() { return resetOnPrintProperty; }
    
    @Override public long getStartTime() {
        return (long) getStartTimeDbl();
    }
    
    public double getStartTimeDbl() {
        return getStartTimeNanos() / 1000000D;
    }
    
    public long getStartTimeNanos() {
        return startTime;
    }
    
    //
    
    @Override public final long getElapsedTime() {
        return (long) getElapsedTimeDbl();
    }
    
    public final double getElapsedTimeDbl() {
        return getElapsedTimeNanos() / 1000000D;
    }
    
    public final long getElapsedTimeNanos() {
        return (System.nanoTime() - startTime);
    }
    
    //
    
    public final void setLimit(long limit) {
        this.limit = limit;
    }
    
    public final long getLimit() {
        return limit;
    }
    
    public final boolean meetsLimit() {
        return limit == -1 || getElapsedTime() >= limit;
    }
    
    //</editor-fold>
    
    /* *************************************************************************** *
     *                                                                             *
     * Start/Stop/Reset                                                            *
     *                                                                             *
     * *************************************************************************** */
    
    @Override public DebugTimer start() {
        startTime = System.nanoTime();
        return this;
    }
    
    @Override public DebugTimer reset() {
        return start();
    }
    
    @Override public DebugTimer stop() {
        startTime = 0L;
        return this;
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Printing                                                                    *
     *                                                                             *
     * *************************************************************************** */
    
    public void print() {
        print(null, true);
    }
    
    public void print(String message) {
        print(message, true);
    }
    
    public void print(boolean reset) {
        print(null, reset);
    }
    
    public void print(String message, boolean reset) {
        if (meetsLimit())
            if (reset)
                reset();
    }
    
    //
    
    public <T> T print(Supplier<T> supplier) {
        return print(null, supplier);
    }
    
    public <T> T print(String message, Supplier<T> supplier) {
        reset();
        T t = supplier.get();
        print(message);
        return t;
    }
    
    //
    
    private String hlpr_getText(String message) {
        return "[TIMER] " + (name == null ? "NONE" : name) + ":" + (message != null ? " " + message : "")
               + " (" + Calc.roundD(getElapsedTimeDbl(), 0, 2) + " ms)";
    }
}


/*
 * TODO LIST:
 * [S] Make DebugTimer stoppable.
 *     [S] When a DebugTimer is stopped, the stop time is saved, and any info retrieved uses that stop time.
 */