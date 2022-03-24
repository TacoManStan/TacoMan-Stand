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

import java.util.Collection;
import java.util.concurrent.locks.Lock;

/**
 * <p>Defines a {@link Collection} of {@link Trigger Triggers} assigned to a specific {@link TriggerEvent} of type <{@link T}>.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link TriggerGroup TriggerGroups} are {@code managed} and {@code constructed} automatically whenever a new type of {@link Trigger} is {@link TriggerEventManager#register(Trigger) registered} with the {@link TriggerEventManager}.</li>
 *     <li>
 *         To retrieve a {@link TriggerGroup} instance, use any of the following {@link TriggerGroup} accessor methods:
 *         <ul>
 *             <li><i>{@link TriggerEventManager#getTriggerGroup(TriggerEvent)}</i></li>
 *             <li><i>{@link TriggerEventManager#getTriggerGroup(Class)}</i></li>
 *             <li><i>{@link TriggerEventManager#getTriggerGroup(Trigger)}</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The type of {@link TriggerEvent} the {@link Trigger Triggers} contained within this {@link TriggerGroup} are assigned to.
 */
//TO-EXPAND - Examples
public final class TriggerGroup<T extends TriggerEvent<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final TriggerEventManager manager;
    private final ListProperty<Trigger<T>> triggers;
    
    public TriggerGroup(@NotNull TriggerEventManager manager) {
        this.manager = manager;
        this.triggers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- LOGIC ---">
    
    void trigger(@NotNull T event) { triggers.forEach(trigger -> process(event, trigger)); }
    
    boolean register(@NotNull Trigger<T> trigger) { return triggers.add(trigger); }
    boolean unregister(@NotNull Trigger<T> trigger) { return triggers.remove(trigger); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return manager.getGame(); }
    
    @Override public @NotNull Springable springable() { return manager; }
    @Override public @Nullable Lock getLock() { return manager.getLock(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void process(@NotNull T event, @NotNull Trigger<T> trigger) {
        if (trigger.test(event))
            trigger.trigger(event);
    }
    
    //</editor-fold>
}
