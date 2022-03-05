package com.taco.suit_lady.game.galaxy.abilities;

import com.taco.suit_lady.game.galaxy.validators.Validatable;
import com.taco.suit_lady.game.galaxy.validators.Validator;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.ValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public abstract class Ability
        implements Validatable<Ability> {
    
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
    
    @SafeVarargs protected final boolean execute(@NotNull ValuePair<String, Object>... params) { return execute(L.map(params)); }
    protected abstract boolean execute(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    @SafeVarargs public final boolean use(@NotNull ValuePair<String, Object>... params) { return use(L.map(params)); }
    public final boolean use(@NotNull Map<String, Object> params) { return sync(() -> revalidate(params) && execute(params)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull Lock getLock() { return lock != null ? lock : Validatable.super.getLock(); }
    
    @Override public @NotNull GameViewContent getGame() { return getSource().getGame(); }
    @Override public final @NotNull Validator<Ability> validator() { return validator; }
    
    //</editor-fold>
}
