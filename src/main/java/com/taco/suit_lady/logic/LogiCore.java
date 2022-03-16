package com.taco.suit_lady.logic;

import com.taco.suit_lady._to_sort._new.initialization.Initializable;
import com.taco.suit_lady._to_sort._new.initialization.Initializer;
import com.taco.suit_lady._to_sort._new.initialization.LockMode;
import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.triggers.TriggerEventManager;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.Exe;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Component
public class LogiCore
        implements Springable, Lockable, GameComponent, Initializable<LogiCore> {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReentrantLock tickableLock;
    private final ReentrantLock gfxLock;
    
    private final ReadOnlyObjectWrapper<GameViewContent> gameProperty;
    
    private final ScheduledThreadPoolExecutor gameLoopExecutor;
    private final ListProperty<Tickable<?>> tickables; //Absolutely NO blocking calls to FX thread can be made here. None.
    private ArrayList<Tickable<?>> tickablesCopy;
    private final List<Tickable<?>> empty;
    private boolean needsCopyRefresh;
    
    private final ListProperty<GFXObject<?>> gfxObjects;
    
    private final int targetUPS = 144;
    private final ReadOnlyIntegerWrapper upsProperty;
    
    private int tickCount = 0;
    
    private final Timer timer;
    
    private final TriggerEventManager triggers;
    
    public LogiCore(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.tickableLock = new ReentrantLock();
        this.gfxLock = new ReentrantLock();
        
        
        this.gameProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.gameLoopExecutor = new ScheduledThreadPoolExecutor(1);
        
        this.tickables = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.tickablesCopy = new ArrayList<>();
        this.empty = new ArrayList<>();
        this.needsCopyRefresh = false;
        
        this.gfxObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.upsProperty = new ReadOnlyIntegerWrapper();
        
        this.timer = Timers.newStopwatch(true);
        
        this.triggers = new TriggerEventManager(this);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void startup(@NotNull Object @NotNull [] params) {
        gameProperty.set((GameViewContent) params[0]);
        
        Printer.print("Starting Up");
        tickables.addListener((ListChangeListener<? super Tickable<?>>) c -> {
            needsCopyRefresh = true;
            Printer.err("Value Changed");
        });
        needsCopyRefresh = true;
        //        ListsSL.applyListener(null, tickables, op -> {
        //            needsCopyRefresh = true;
        //            Print.err("Value Changes " + needsCopyRefresh);
        //        });
        
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
    
    public final @NotNull ReadOnlyObjectProperty<GameViewContent> readOnlyGameProperty() { return gameProperty.getReadOnlyProperty(); }
    @Override public final @NotNull GameViewContent getGame() { return gameProperty.get(); }
    
    public final @NotNull TriggerEventManager triggers() { return triggers; }
    
    //
    
    public final int getTargetUPS() { return targetUPS; }
    
    public final @NotNull ReadOnlyIntegerProperty readOnlyUpsProperty() { return upsProperty.getReadOnlyProperty(); }
    protected final @NotNull ReadOnlyIntegerWrapper upsProperty() { return upsProperty; }
    public final int getUps() { return upsProperty.get(); }
    protected final int setUps(int newValue) { return FX.callFX(() -> Props.setProperty(upsProperty, newValue)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Execution">
    
    public final <V> @Nullable ScheduledFuture<V> execute(@NotNull Callable<V> action) { return gameLoopExecutor.schedule(action, 0L, TimeUnit.MILLISECONDS); }
    public final void execute(@NotNull Runnable action) { execute(Obj.asCallable(action)); }
    
    public final <V> V executeAndGet(@NotNull Callable<V> action, @Nullable Consumer<Throwable> exceptionHandler) {
        exceptionHandler = exceptionHandler != null ? exceptionHandler : Throwable::printStackTrace;
        try {
            return execute(action).get();
        } catch (Exception e) {
            exceptionHandler.accept(e);
            throw Exc.ex(e);
        }
    }
    public final <V> V executeAndGet(@NotNull Callable<V> action) { return executeAndGet(action, null); }
    
    //
    
    public final boolean submit(@NotNull Tickable<?> tickable) { return Exe.sync(tickableLock, () -> tickables.add(tickable)); }
    
    
    private List<Tickable<?>> tickablesCopy() {
        if (needsCopyRefresh)
            tickablesCopy = Exe.sync(tickableLock, () -> new ArrayList<>(Collections.unmodifiableList(tickables)));
        needsCopyRefresh = false;
        return tickablesCopy;
    }
    
    public final boolean addGfxObject(@Nullable GFXObject<?> gfxObject) { return gfxObject != null & Exe.sync(gfxLock, () -> gfxObjects.add(gfxObject)); }
    public final boolean removeGfxObject(@Nullable GFXObject<?> gfxObject) { return gfxObject != null & Exe.sync(gfxLock, () -> gfxObjects.remove(gfxObject)); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Game Loop">
    
    public long upsRefreshTime = 2000;
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    private void tick() {
        if (checkSpringClosure(null)) return;
        if (checkSpringClosure(() -> {
            tickCount++;
            if (timer.isTimedOut())
                timer.getOnTimeout().run();
        })) return;
        
        final ArrayList<Tickable<?>> toRemove = new ArrayList<>();
        final ArrayList<GFXObject<?>> gfxObjects = new ArrayList<>();
        final List<Tickable<?>> tickableCopy = tickablesCopy();
        
        FX.runFX(() -> ui().refreshMouseTracking());
        Exe.sync(tickableLock, () -> {
            tickablesCopy().forEach(tickable -> {
                if (checkSpringClosure(() -> {
                    if (tickable.taskManager().isShutdown())
                        toRemove.add(tickable);
                    else {
                        tickable.taskManager().execute();
                        FX.runFX(() -> tickable.taskManager().executeGfx());
                    }
                }))
                    return;
            });
            toRemove.forEach(tickables::remove);
        });
        toRemove.forEach(this::shutdown);
        
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
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
        Printer.err("Shutting Down LogiCore");
        gameLoopExecutor.shutdown();
    }
    
    @Contract("_ -> param1") private @NotNull Tickable<?> shutdown(@NotNull Tickable<?> tickable) {
        tickable.taskManager().shutdownOperations();
        tickable.taskManager().gfxShutdownOperations();
        return tickable;
    }
    
    //</editor-fold>
    
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
    
    //</editor-fold>
    
    public double secondsToTicks(@NotNull Number input) {
        return input.doubleValue() / getTargetUPS();
    }
}
