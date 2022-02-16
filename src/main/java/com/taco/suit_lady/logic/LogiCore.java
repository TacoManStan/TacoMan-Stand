package com.taco.suit_lady.logic;

import com.taco.suit_lady._to_sort._new.initialization.Initializable;
import com.taco.suit_lady._to_sort._new.initialization.Initializer;
import com.taco.suit_lady._to_sort._new.initialization.LockMode;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class LogiCore
        implements Springable, GameComponent, Initializable<LogiCore> {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<GameViewContent> gameProperty;
    
    //
    
    private final ThreadPoolExecutor sequentialExecutor; // TODO - Implement both asynchronous and synchronous executor options
    private final ScheduledThreadPoolExecutor scheduledExecutor;
    
    //
    
    private final ScheduledThreadPoolExecutor gameLoopExecutor;
    private final ListProperty<TickableMk1> tickables;
    private final List<TickableMk1> empty;
    
    private final int targetUPS = 144;
    private final int baselineUPS = 60;
    private final ReadOnlyIntegerWrapper upsProperty;
    
    private int tickCount = 0;
    
    private final Timer timer;
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.gameProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.sequentialExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(10);
        
        //
        
        this.gameLoopExecutor = new ScheduledThreadPoolExecutor(1);
        this.tickables = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.empty = new ArrayList<>();
        
        this.upsProperty = new ReadOnlyIntegerWrapper();
        
        this.timer = Timers.newStopwatch(true);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void startup(@NotNull Object[] params) {
        gameProperty.set((GameViewContent) params[0]);
        
        initExecutors();
        initTimer();
    }
    
    private void initExecutors() {
        gameLoopExecutor.scheduleAtFixedRate(this::tick, 0, (long) 1000000 / getTargetUPS(), TimeUnit.MICROSECONDS);
    }
    
    private void initTimer() {
        timer.setTimeout(upsRefreshTime);
        timer.setOnTimeout(() -> {
            setUps((int) (tickCount / (upsRefreshTime / 1000)));
            tickCount = 0;
            timer.reset(upsRefreshTime);
        });
        timer.start();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyObjectProperty<GameViewContent> readOnlyGameProperty() {
        return gameProperty.getReadOnlyProperty();
    }
    @Override public final @NotNull GameViewContent getGame() { return gameProperty.get(); }
    
    //
    
    public final @NotNull ListProperty<TickableMk1> getTickables() { return tickables; }
    public final @NotNull List<TickableMk1> getEmpty() { return empty; }
    
    public final boolean submit(@NotNull TickableMk1 tickable) { return tickables.add(tickable); }
    public final boolean remove(@NotNull TickableMk1 tickable) { return tickables.remove(tickable); }
    
    //
    
    public final int getTargetUPS() { return targetUPS; }
    public final int getBaselineUPS() { return baselineUPS; }
    public final double getUPSMultiplier() { return (double) getBaselineUPS() / (double) getTargetUPS(); }
    
    public final @NotNull ReadOnlyIntegerProperty readOnlyUpsProperty() { return upsProperty.getReadOnlyProperty(); }
    protected final @NotNull ReadOnlyIntegerWrapper upsProperty() { return upsProperty; }
    public final int getUps() { return upsProperty.get(); }
    protected final int setUps(int newValue) { return ToolsFX.runFX(() -> PropertiesSL.setProperty(upsProperty, newValue)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME LOOP ---">
    
    public long upsRefreshTime = 2000;
    
    private void tick() {
        tickCount++;
        if (timer.isTimedOut())
            timer.getOnTimeout().run();
        tickables.forEach(this::tick);
    }
    
    private void tick(@NotNull TickableMk1 tickable) {
        tickable.tick(this);
        if (tickable.hasSubActions())
            tickable.subActions().forEach(this::tick);
    }
    
    //</editor-fold>
    
    public final ThreadPoolExecutor executor() {
        return sequentialExecutor;
    }
    
    public final ScheduledThreadPoolExecutor scheduledExecutor() {
        return scheduledExecutor;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    private Initializer<LogiCore> initializer;
    @Override public final @NotNull Initializer<LogiCore> initializer() {
        if (initializer == null)
            initializer = new Initializer<>(
                    this,
                    this::startup,
                    null,
                    LockMode.OWNER_OR_NEW_LOCK);
        return initializer;
    }
    
    //
    
    @Override public @NotNull FxWeaver weaver() { return weaver; }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return ctx; }
    
    //</editor-fold>
}
