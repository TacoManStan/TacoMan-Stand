package com.taco.tacository.logic.triggers;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

/**
 * <p>The abstract parent of all {@link TriggerEvent Trigger Events}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Once implemented, a {@link TriggerEvent} must be {@link TriggerEventManager#submit(TriggerEvent) submitted} to the {@link TriggerEventManager}.</li>
 *     <li>Upon {@link TriggerEventManager#submit(TriggerEvent) submission}, all {@link Trigger Triggers} {@link TriggerEventManager#register(Trigger) registered} to the {@link TriggerEvent} are {@link Trigger#trigger(TriggerEvent) triggered}.</li>
 *     <p><i>For additional information, refer to {@link Trigger}.</i></p>
 * </ol>
 *
 * @param <T> The type of {@link TriggerEvent} implementation.
 *            Should be the same as this {@link TriggerEvent} implementation.
 */
public abstract class TriggerEvent<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameComponent source;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerEvent(@NotNull GameComponent source) {
        this.source = source;
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public GameComponent getSource() { return source; }
    public final ListProperty<Trigger<T>> triggerList() { return triggers; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final void submit() { triggers().submit((T) this); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getGame() { return source.getGame(); }
    
    @Override public @NotNull Springable springable() { return getGame(); }
    @Override public @Nullable Lock getLock() { return getGame().getLock(); }
    
    //</editor-fold>
}
