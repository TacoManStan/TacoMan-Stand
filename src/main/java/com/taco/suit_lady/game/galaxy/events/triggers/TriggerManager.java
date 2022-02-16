package com.taco.suit_lady.game.galaxy.events.triggers;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TriggerManager
        implements WrappedGameComponent {
    
    private final GameViewContent game;
    private final HashMap<Class<? extends TriggerEvent<?>>, TriggerGroup<?>> triggerMap;
    
    public TriggerManager(@NotNull GameComponent gameComponent) {
        this.game = gameComponent.getGame();
        this.triggerMap = new HashMap<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    final @NotNull HashMap<Class<? extends TriggerEvent<?>>, TriggerGroup<?>> triggers() { return triggerMap; }
    
    
    public final <T extends TriggerEvent<T>> boolean register(@NotNull Trigger<T> trigger) { return getTriggerGroup(trigger).register(trigger); }
    public final <T extends TriggerEvent<T>> boolean unregister(@NotNull Trigger<T> trigger) { return getTriggerGroup(trigger).unregister(trigger); }
    
    public final <T extends TriggerEvent<T>> void submitEvent(@NotNull T event) { getTriggerGroup(event).trigger(event); }
    
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull Class<T> key) {
        TriggerGroup<T> triggerGroup = (TriggerGroup<T>) triggerMap.get(key);
        if (triggerGroup == null)
            triggerMap.put(key, triggerGroup = new TriggerGroup<>(this));
        return triggerGroup;
    }
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull T keyObj) { return getTriggerGroup(keyObj.getClass()); }
    protected final @NotNull <T extends TriggerEvent<T>> TriggerGroup<T> getTriggerGroup(@NotNull Trigger<T> keyObj) { return getTriggerGroup(keyObj.type()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return game; }
    
    //</editor-fold>
}
