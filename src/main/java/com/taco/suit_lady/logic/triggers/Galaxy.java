package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>Contains static utility methods for streamlined {@link Trigger}-related operations (e.g., {@link Trigger} factory methods).</p>
 */
public final class Galaxy {
    Galaxy() { } //No Instance
    
    //<editor-fold desc="--- FACTORY METHODS ---">
    
    //<editor-fold desc="> Trigger Factory Methods">
    
    @Contract("_, _, _ -> new")
    public static @NotNull UnitMovedTrigger newUnitMovedTrigger(@NotNull GameObject source, @NotNull Predicate<UnitMovedEvent> condition, @NotNull Consumer<UnitMovedEvent> action) {
        return new UnitMovedTrigger(source, newCondition(source, condition)) {
            @Override protected void trigger(@NotNull UnitMovedEvent event) { action.accept(event); }
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
    
    //</editor-fold>
}
