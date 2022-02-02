package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
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
        
        this.timer = Timers.newStopwatch(true);
    }
    
    public final ListProperty<Tickable> getTickables() { return tickables; }
    
    public final boolean submit(@NotNull Tickable tickable) { return tickables.add(tickable); }
    public final boolean remove(@NotNull Tickable tickable) { return tickables.remove(tickable); }
    
    //<editor-fold desc="--- GAME LOOP ---">
    
    public final void init() {
        gameLoopExecutor.scheduleAtFixedRate(this::tick, 0, (long) 1000 / 60, TimeUnit.MILLISECONDS);
        timer.setTimeout(3000);
        timer.setOnTimeout(() -> {
            System.out.println("Ticks in 3s: " + tickCount);
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
