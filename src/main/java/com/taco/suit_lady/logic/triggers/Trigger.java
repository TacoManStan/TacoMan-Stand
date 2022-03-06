package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public abstract class Trigger<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final Entity owner;
    private final ReadOnlyObjectWrapper<TriggerCondition<T>> conditionProperty;
    
    public Trigger(@NotNull Entity owner, @Nullable TriggerCondition<T> condition) {
        this.owner = owner;
        this.conditionProperty = new ReadOnlyObjectWrapper<>(condition);
    }
    
    //<editor-fold desc="--- LOGIC ---">
    
    final boolean test(@NotNull T event) { return Exc.nullCheck(getCondition(), "Trigger Condition").test(event); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public Entity getOwner() { return owner; }
    
    public ReadOnlyObjectProperty<TriggerCondition<T>> readOnlyConditionProperty() { return conditionProperty.getReadOnlyProperty(); }
    public TriggerCondition<T> getCondition() { return conditionProperty.get(); }
    public TriggerCondition<T> setCondition(TriggerCondition<T> newValue) { return Props.setProperty(conditionProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void trigger(@NotNull T event);
    protected abstract Class<T> type();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    @Override public final @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    
    //</editor-fold>
}
