package com.taco.suit_lady.game.galaxy.abilities;

import com.taco.suit_lady.game.galaxy.validators.Validatable;
import com.taco.suit_lady.game.galaxy.validators.Validator;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.params.Paramable;
import com.taco.suit_lady.util.values.params.Params;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * <p>Defines an {@link Ability} for a specific {@link GameObject} source.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Override <i>{@link #execute(Map)}</i> to define the {@code execution logic} for this {@link Ability}.</li>
 *     <li>Use <i>{@link #validator()}</i> to access the {@link Validator} for this {@link Ability}.</li>
 *     <li>Once defined, an {@link Ability} implementation is {@link #execute(Map) executed} by calling the <i>{@link #use(Map)}</i> method.</li>
 *     <li><i>See {@link Ability_InstantEffect} and {@link Ability_TargetEffect} for commonly-used {@link Ability} implementation examples.</i></li>
 * </ol>
 */
//TO-EXPAND
public abstract class Ability
        implements Validatable<Ability>, Paramable<String> {
    
    private Lock lock;
    private final GameObject source;
    
    private final Validator<Ability> validator;
    
    public Ability(@NotNull GameObject source) { this(source, null); }
    public Ability(@NotNull GameObject source, @Nullable Lock lock) {
        this.lock = lock;
        this.source = source;
        
        this.validator = new Validator<>(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameObject getSource() { return source; }
    public final void setLock(@Nullable Lock lock) { this.lock = lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    @SafeVarargs protected final boolean execute(@NotNull Value2D<String, Object>... params) { return execute(L.map(params)); }
    protected abstract boolean execute(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    @SafeVarargs public final boolean use(@NotNull Value2D<String, Object>... params) { return use(L.map(params)); }
    public final boolean use(@NotNull Map<String, Object> params) {
        return sync(() -> {
            return Params.validateParams(this, params, true) && revalidate(params) && execute(params);
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull Validator<Ability> validator() { return validator; }
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return getSource().getGame(); }
    
    @Override public final @NotNull Springable springable() { return getSource(); }
    @Override public final @NotNull Lock getLock() { return lock != null ? lock : getSource().getLock(); }
    
    //</editor-fold>
}
