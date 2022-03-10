package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.params.Paramable;
import com.taco.suit_lady.util.values.params.Params;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public abstract class Effect
        implements SpringableWrapper, Lockable, GameComponent, Paramable<String> {
    
    private final GameObject source;
    
    public Effect(@NotNull GameObject source) {
        this.source = source;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getSource() { return source; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean onTrigger(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    public final boolean trigger(@NotNull Map<String, Object> params) {
        if (Params.validateParams(this, params, true))
            return onTrigger(params);
        return false;
    }
    @SafeVarargs public final boolean trigger(@NotNull Value2D<String, Object>... params) { return trigger(L.map(params)); }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return getSource().getGame(); }
    @Override public final @NotNull Springable springable() { return getSource(); }
    @Override public @Nullable Lock getLock() { return getSource().getLock(); }
    
    //</editor-fold>
}
