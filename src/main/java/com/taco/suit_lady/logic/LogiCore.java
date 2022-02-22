package com.taco.suit_lady.logic;

import com.taco.suit_lady._to_sort._new.initialization.Initializable;
import com.taco.suit_lady._to_sort._new.initialization.Initializer;
import com.taco.suit_lady._to_sort._new.initialization.LockMode;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.triggers.TriggerEventManager;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.TasksSL;
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
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LogiCore
        implements Springable, Lockable, GameComponent, Initializable<LogiCore> {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReentrantLock tickableLock;
    
    private final ReadOnlyObjectWrapper<GameViewContent> gameProperty;
    
    //
    
    //    private final ThreadPoolExecutor sequentialExecutor; // TODO - Implement both asynchronous and synchronous executor options
    //    private final ScheduledThreadPoolExecutor scheduledExecutor;
    
    //
    
    private final ScheduledThreadPoolExecutor gameLoopExecutor;
    private final ListProperty<Tickable<?>> tickables; //Absolutely NO blocking calls to FX thread can be made here. None.
    private final List<Tickable<?>> empty;
    
    private final ListProperty<GFXObject> gfxObjects;
    
    private final int targetUPS = 144;
    private final ReadOnlyIntegerWrapper upsProperty;
    
    private int tickCount = 0;
    
    private final Timer timer;
    
    private final TriggerEventManager triggers;
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.tickableLock = new ReentrantLock();
        
        
        this.gameProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        //        this.sequentialExecutor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        //        this.scheduledExecutor = new ScheduledThreadPoolExecutor(10);
        
        //
        
        this.gameLoopExecutor = new ScheduledThreadPoolExecutor(1);
        
        this.tickables = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.empty = new ArrayList<>();
        
        this.gfxObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.upsProperty = new ReadOnlyIntegerWrapper();
        
        this.timer = Timers.newStopwatch(true);
        
        this.triggers = new TriggerEventManager(this);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void startup(@NotNull Object @NotNull [] params) {
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
    
    public final boolean submit(@NotNull Tickable<?> tickable) {
        //        if (getLock() != null)
        //            return sync(() -> tickables.add(tickable));
        return TasksSL.sync(tickableLock, () -> tickables.add(tickable));
    }
    
    //    /**
    //     * <p>Performs all {@link LogiCore} shutdown operations for the specified {@link Tickable} instance.</p>
    //     * <p><b>Details</b></p>
    //     * <ol>
    //     *     <li>This method is called automatically by <i>{@link GameObject#shutdown()}</i>.</li>
    //     *     <li>Calling this method directly on a {@link GameObject} instance will result in an incomplete shutdown of the specified {@link GameObject}.</li>
    //     *     <li>Other implementations of {@link Tickable} may be shutdown correctly, but this is not guaranteed.</li>
    //     *     <li>TODO: Add {@code shutdown} method to {@link Tickable} to ensure all {@link Tickable} implementations know they are responsible for their own shutdown operations.</li>
    //     * </ol>
    //     *
    //     * @param tickable The {@link Tickable} being shut down.
    //     *
    //     * @return True if the shutdown was successful, false if it was not.
    //     */
    //    public final boolean shutdown(@NotNull Tickable<?> tickable) {
    //        return TasksSL.sync(tickableLock, () -> tickables.remove(tickable));
    //    }
    public final List<Tickable<?>> tickablesCopy() {
        return sync(() -> new ArrayList<>(tickables));
    }
    
    public final boolean addGfxObject(@NotNull GFXObject gfxObject) {
        //        if (getLock() != null)
        //            return sync(() -> gfxActions2.add(gfxObject));
        return gfxObjects.add(gfxObject);
    }
    
    //
    
    public final int getTargetUPS() { return targetUPS; }
    
    public final @NotNull ReadOnlyIntegerProperty readOnlyUpsProperty() { return upsProperty.getReadOnlyProperty(); }
    protected final @NotNull ReadOnlyIntegerWrapper upsProperty() { return upsProperty; }
    public final int getUps() { return upsProperty.get(); }
    protected final int setUps(int newValue) { return ToolsFX.callFX(() -> PropertiesSL.setProperty(upsProperty, newValue)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME LOOP ---">
    
    public long upsRefreshTime = 2000;
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    private void tick() {
        if (checkSpringClosure(null)) return;
        if (getLock() != null) sync(() -> {
            if (checkSpringClosure(() -> {
                tickCount++;
                if (timer.isTimedOut())
                    timer.getOnTimeout().run();
            })) return;
            
            tickablesCopy().forEach(tickable -> {
                if (checkSpringClosure(() -> {
                    if (tickable.taskManager().isShutdown()) {
                        tickables.remove(tickable);
                        tickable.taskManager().shutdownOperations();
                    } else
                        tickable.taskManager().execute();
                }))
                    return;
            });
            
            checkSpringClosure(() -> ToolsFX.runFX(() -> gfxObjects.forEach(GFXObject::execute), true));
        });
    }
    
    //</editor-fold>
    
    public final ThreadPoolExecutor executor() { return gameLoopExecutor; }
    
    private boolean checkSpringClosure(@Nullable Runnable action) {
        if (!ctx().isRunning()) {
            shutdown();
            return true;
        } else {
            if (action != null) action.run();
            return false;
        }
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
