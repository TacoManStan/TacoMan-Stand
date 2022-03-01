package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.OneTimeTask;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedTrigger;
import com.taco.suit_lady.logic.triggers.implementations.UnitMovedEvent;
import com.taco.suit_lady.logic.triggers.implementations.UnitMovedTrigger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>Contains static utility methods for streamlined {@link Trigger}-related operations (e.g., {@link Trigger} factory methods).</p>
 */
public final class Galaxy {
    Galaxy() { } //No Instance
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    //<editor-fold desc="> Trigger Factory Methods">
    
    public static @NotNull UnitMovedTrigger newUnitMovedTrigger(@NotNull GameObject source, @NotNull Consumer<UnitMovedEvent> action) {
        return newUnitMovedTrigger(source, event -> source.equals(event.getSource()), action);
    }
    
    @Contract("_, _, _ -> new")
    public static @NotNull UnitMovedTrigger newUnitMovedTrigger(@NotNull GameObject source, @NotNull Predicate<UnitMovedEvent> condition, @NotNull Consumer<UnitMovedEvent> action) {
        return new UnitMovedTrigger(source, newCondition(source, condition)) {
            @Override protected void trigger(@NotNull UnitMovedEvent event) { action.accept(event); }
        };
    }
    
    //
    
    public static @NotNull UnitArrivedTrigger newUnitArrivedTrigger(@NotNull GameObject source, @NotNull Consumer<UnitArrivedEvent> action) {
        return newUnitArrivedTrigger(source, event -> source.equals(event.getSource()), action);
    }
    
    @Contract("_, _, _ -> new")
    public static @NotNull UnitArrivedTrigger newUnitArrivedTrigger(@NotNull GameObject source, @NotNull Predicate<UnitArrivedEvent> condition, @NotNull Consumer<UnitArrivedEvent> action) {
        return new UnitArrivedTrigger(source, newCondition(source, condition)) {
            @Override protected void trigger(@NotNull UnitArrivedEvent event) { action.accept(event); }
        };
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Condition Factory Methods">
    
    public static @NotNull <T extends TriggerEvent<T>> TriggerCondition<T> newCondition(@NotNull GameComponent gameComponent, @NotNull Predicate<T> condition) {
        return new TriggerCondition<>(gameComponent) {
            @Override public boolean test(T event) { return condition.test(event); }
        };
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> GameTask Factory Methods">
    
    public static <E extends Tickable<E>> @NotNull OneTimeTask<E> newOneTimeTask(@NotNull E owner, @NotNull Runnable action) { return newOneTimeTask(null, owner, action); }
    public static <E extends Tickable<E>> @NotNull OneTimeTask<E> newOneTimeTask(@Nullable GameComponent gameComponent, @NotNull E owner, @NotNull Runnable action) {
        return new OneTimeTask<>(gameComponent, owner) {
            @Override protected void tick() { action.run(); }
        };
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
