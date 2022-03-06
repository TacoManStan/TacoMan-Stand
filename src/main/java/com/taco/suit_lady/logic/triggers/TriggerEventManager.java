package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class TriggerEventManager
        implements SpringableWrapper, Lockable, GameComponent {
    
    private Lock lock;
    private final GameComponent gameComponent;
    
    private final HashMap<Class<? extends TriggerEvent<?>>, TriggerGroup<?>> triggerMap;
    
    public TriggerEventManager(@NotNull GameComponent gameComponent) { this(null, gameComponent); }
    public TriggerEventManager(@Nullable Lock lock, @NotNull GameComponent gameComponent) {
        this.lock = lock;
        this.gameComponent = gameComponent;
        
        this.triggerMap = new HashMap<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    final @NotNull HashMap<Class<? extends TriggerEvent<?>>, TriggerGroup<?>> triggerMap() { return triggerMap; }
    
    //
    
    public final <T extends TriggerEvent<T>> void submit(@NotNull T event) { getTriggerGroup(event).trigger(event); }
    
    public final <T extends TriggerEvent<T>> boolean register(@NotNull Trigger<T> trigger) { return getTriggerGroup(trigger).register(trigger); }
    public final <T extends TriggerEvent<T>> boolean unregister(@NotNull Trigger<T> trigger) { return getTriggerGroup(trigger).unregister(trigger); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return gameComponent.getGame(); }
    
    @Override public final @NotNull Springable springable() { return getGame(); }
    @Override public final @Nullable Lock getLock() { return lock != null ? lock : getGame().getLock(); }
    public final void setLock(@Nullable Lock lock) { this.lock = lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull Class<T> key) {
        TriggerGroup<T> triggerGroup = (TriggerGroup<T>) triggerMap.get(key);
        if (triggerGroup == null)
            triggerMap.put(key, triggerGroup = new TriggerGroup<>(this));
        return triggerGroup;
    }
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull T keyObj) { return getTriggerGroup(keyObj.getClass()); }
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull Trigger<T> keyObj) { return getTriggerGroup(keyObj.type()); }
    
    //</editor-fold>
}
