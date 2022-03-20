package com.taco.suit_lady.game.galaxy.effects;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.galaxy.abilities.Ability;
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

/**
 * <p>Defines a specific {@link Effect} with a specified {@link GameObject} source.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>In most contexts, an {@link Effect} is used to define the {@link Ability#execute(Map) Execution Logic} for an {@link Ability} instance.</li>
 *     <li>All {@code parameter input} required to {@link #trigger(Map) trigger} an {@link Effect} is defined by the {@link Map} parameter passed to the <i>{@link #trigger(Map)}</i> method.</li>
 *     <li>To define the specific {@code parameters} required to {@link #trigger(Map) trigger} this {@link Effect}, override the <i>{@link Paramable#requiredParams()}</i> method.</li>
 *     <li>Once defined in the {@link Effect} implementation, <i>{@link Paramable#requiredParams()}</i> can be again called to confirm all required {@code parameters} have been provided.</li>
 * </ol>
 */
//TO-EXPAND - Examples
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
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean trigger(@NotNull Map<String, Object> params) {
        if (Params.validateParams(this, params, true))
            return onTrigger(params);
        return false;
    }
    @SafeVarargs public final boolean trigger(@NotNull Value2D<String, Object>... params) { return trigger(L.map(params)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return getSource().getGame(); }
    
    @Override public final @NotNull Springable springable() { return getSource(); }
    @Override public @Nullable Lock getLock() { return getSource().getLock(); }
    
    //</editor-fold>
}
