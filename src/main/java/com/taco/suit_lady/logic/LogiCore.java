package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class LogiCore
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    //
    
    private final ThreadPoolExecutor sequentialExecutor; // TODO - Implement both asynchronous and synchronous executor options
    private final ScheduledThreadPoolExecutor scheduledExecutor;
    
    //
    
    private final ScheduledThreadPoolExecutor gameLoopExecutor;
    private final ListProperty<Tickable> tickables;
    
    private final IntegerProperty upsTargetProperty;
    private final ReadOnlyIntegerWrapper upsProperty;
    
    private int tickCount = 0;
    
    private final Timer timer;
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.sequentialExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(10);
        
        //
        
        this.gameLoopExecutor = new ScheduledThreadPoolExecutor(1);
        this.tickables = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.upsTargetProperty = new ReadOnlyIntegerWrapper(144);
        this.upsProperty = new ReadOnlyIntegerWrapper();
        
        this.timer = Timers.newStopwatch(true);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ListProperty<Tickable> getTickables() { return tickables; }
    
    public final boolean submit(@NotNull Tickable tickable) { return tickables.add(tickable); }
    public final boolean remove(@NotNull Tickable tickable) { return tickables.remove(tickable); }
    
    //
    
    public final IntegerProperty upsTargetProperty() { return upsTargetProperty; }
    public final int getTargetUPS() { return upsTargetProperty.get(); }
    public final int setTargetUPS(int newValue) { return PropertiesSL.setProperty(upsTargetProperty, newValue); }
    
    public final ReadOnlyIntegerProperty readOnlyUpsProperty() { return upsProperty.getReadOnlyProperty(); }
    protected final ReadOnlyIntegerWrapper upsProperty() { return upsProperty; }
    public final int getUps() { return upsProperty.get(); }
    protected final int setUps(int newValue) { return ToolsFX.runFX(() -> PropertiesSL.setProperty(upsProperty, newValue)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME LOOP ---">
    
    public final void init() {
        gameLoopExecutor.scheduleAtFixedRate(this::tick, 0, (long) 1000000 / getTargetUPS(), TimeUnit.MICROSECONDS);
        timer.setTimeout(3000);
        timer.setOnTimeout(() -> {
            System.out.println("Tick Rate: " + (tickCount / 3));
            setUps(tickCount / 3);
            tickCount = 0;
            timer.reset(3000);
        });
        timer.start();
    }
    
    private void tick() {
        tickCount++;
        if (timer.isTimedOut())
            timer.getOnTimeout().run();
        tickables.forEach(Tickable::tick);
    }
    
    //</editor-fold>
    
    public final ThreadPoolExecutor executor() {
        return sequentialExecutor;
    }
    
    public final ScheduledThreadPoolExecutor scheduledExecutor() {
        return scheduledExecutor;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    //</editor-fold>
}
