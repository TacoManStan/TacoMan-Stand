package com.taco.suit_lady.logic;

import com.taco.suit_lady._to_sort._new.initialization.Initializable;
import com.taco.suit_lady._to_sort._new.initialization.Initializer;
import com.taco.suit_lady._to_sort._new.initialization.LockMode;
import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.triggers.TriggerEventManager;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.legacy.TickableMk1;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

@Component
public class LogiCore
        implements Springable, Lockable, GameComponent, Initializable<LogiCore> {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<GameViewContent> gameProperty;
    
    //
    
    //    private final ThreadPoolExecutor sequentialExecutor; // TODO - Implement both asynchronous and synchronous executor options
    //    private final ScheduledThreadPoolExecutor scheduledExecutor;
    
    //
    
    private final ScheduledThreadPoolExecutor gameLoopExecutor;
    private final ListProperty<TickableMk2<?>> tickablesMk2; //Absolutely NO blocking calls to FX thread can be made here. None.
    private final List<TickableMk2<?>> emptyMk2;
    
    private final ListProperty<GFXObject> gfxActions2;
    
    private final int targetUPS = 144;
    private final ReadOnlyIntegerWrapper upsProperty;
    
    private int tickCount = 0;
    
    private final Timer timer;
    
    private final TriggerEventManager triggers;
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        
        this.gameProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        //        this.sequentialExecutor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        //        this.scheduledExecutor = new ScheduledThreadPoolExecutor(10);
        
        //
        
        this.gameLoopExecutor = new ScheduledThreadPoolExecutor(1);
        
        this.tickablesMk2 = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.emptyMk2 = new ArrayList<>();
        
        this.gfxActions2 = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.upsProperty = new ReadOnlyIntegerWrapper();
        
        this.timer = Timers.newStopwatch(true);
        
        this.triggers = new TriggerEventManager(this);
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
    
    public final @NotNull TriggerEventManager triggers() { return triggers; }
    
    
    //
    
    public final boolean submitMk2(@NotNull TickableMk2<?> tickable) { return tickablesMk2.add(tickable); }
    
    public final boolean submitGfxAction2(@NotNull GFXObject gfxObject) {
        if (getLock() != null)
            return sync(() -> gfxActions2.add(gfxObject));
        return gfxActions2.add(gfxObject);
    }
    
    //
    
    public final int getTargetUPS() { return targetUPS; }
    
    public final @NotNull ReadOnlyIntegerProperty readOnlyUpsProperty() { return upsProperty.getReadOnlyProperty(); }
    protected final @NotNull ReadOnlyIntegerWrapper upsProperty() { return upsProperty; }
    public final int getUps() { return upsProperty.get(); }
    protected final int setUps(int newValue) { return ToolsFX.runFX(() -> PropertiesSL.setProperty(upsProperty, newValue)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME LOOP ---">
    
    public long upsRefreshTime = 2000;
    
    private void tick() {
        if (getLock() != null) {
            sync(() -> {
//            TasksSL.printThread();
                tickCount++;
                if (checkSpringClosure()) return;
                if (timer.isTimedOut()) {
                    if (checkSpringClosure()) return;
                    timer.getOnTimeout().run();
                }
                tickablesMk2.forEach(tickable -> {
                    if (checkSpringClosure()) return;
                    tickable.taskManager().execute();
                });
//                Print.errCallingInfo();
//                System.out.println(gfxActions2);
                ToolsFX.runFX(() -> gfxActions2.forEach(action -> {
//                    Print.err();
                    if (checkSpringClosure()) return;
                    if (action.needsUpdate())
                        action.update();
                }), true);
                //                gfxActions.forEach(runnable -> {
                //                    runnable.run();
                //                });
                //                gfxActions.clear();
            });
        }
    }
    
    private void tick(@NotNull TickableMk1 tickable) {
        tickable.tick(this);
        if (tickable.hasSubActions())
            tickable.subActions().forEach(this::tick);
    }
    
    //</editor-fold>
    
    public final ThreadPoolExecutor executor() { return gameLoopExecutor; }
    
    private boolean checkSpringClosure() {
        if (!ctx().isRunning()) {
            shutdown();
            return true;
        }
        return false;
    }
    
    private void shutdown() {
        Print.err("Shutting Down LogiCore");
        gameLoopExecutor.shutdown();
        //        sequentialExecutor.shutdown();
        //        scheduledExecutor.shutdown();
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
    
    @Override public @Nullable Lock getLock() {
        return gameProperty.get() != null ? gameProperty.get().getLock() : null;
    }
    
    
    //    @Override public boolean isNullableLock() { return true; }
    
    //</editor-fold>
    
    public double secondsToTicks(@NotNull Number input) {
        return input.doubleValue() / getTargetUPS();
    }
}
