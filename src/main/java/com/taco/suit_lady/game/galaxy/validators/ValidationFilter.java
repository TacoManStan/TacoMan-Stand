package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.ValuePair;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public abstract class ValidationFilter<T extends Validatable<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final T owner;
    private final ReadOnlyBooleanWrapper validProperty;
    
    public ValidationFilter(@NotNull T owner) {
        this.owner = owner;
        this.validProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull T getOwner() { return owner; }
    
    protected final @NotNull ReadOnlyBooleanProperty readOnlyValidProperty() { return validProperty.getReadOnlyProperty(); }
    public final boolean isValid() { return validProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean revalidate(@NotNull Map<String, Object> params) {
        return sync(() -> {
            final boolean valid = validate(params);
            validProperty.set(valid);
            return valid;
        });
    }
    @SafeVarargs public final boolean revalidate(@NotNull ValuePair<String, Object>... params) { return revalidate(L.map(params)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean validate(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    
    //</editor-fold>
}
