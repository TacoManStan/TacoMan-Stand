package com.taco.tacository.logic.triggers;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.galaxy.abilities.Ability;
import com.taco.tacository.game.galaxy.abilities.Ability_InstantEffect;
import com.taco.tacository.game.galaxy.abilities.Ability_TargetEffect;
import com.taco.tacository.game.galaxy.effects.Effect;
import com.taco.tacository.game.galaxy.effects.EffectGroup;
import com.taco.tacository.game.galaxy.effects.Effect_Scan;
import com.taco.tacository.game.galaxy.effects.Effect_Targeted;
import com.taco.tacository.game.galaxy.validators.Validatable;
import com.taco.tacository.game.galaxy.validators.ValidationFilter;
import com.taco.tacository.game.galaxy.validators.Validator;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.logic.*;
import com.taco.tacository.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.tacository.logic.triggers.implementations.UnitArrivedTrigger;
import com.taco.tacository.logic.triggers.implementations.UnitMovedEvent;
import com.taco.tacository.logic.triggers.implementations.UnitMovedTrigger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>Contains static utility methods for {@link Galaxy} Objects.</p>
 * <p><b>Example {@link Galaxy} Objects</b></p>
 * <ul>
 *     <li>
 *         <b>Triggers</b>
 *         <ol>
 *             <li>{@link Trigger}</li>
 *             <li>{@link TriggerEvent}</li>
 *             <li>{@link TriggerEventManager}</li>
 *         </ol>
 *     </li>
 *     <li>
 *         <b>Validators</b>
 *         <ol>
 *             <li>{@link Validator}</li>
 *             <li>{@link Validatable}</li>
 *             <li>{@link ValidationFilter}</li>
 *         </ol>
 *     </li>
 *     <li>
 *         <b>Abilities</b>
 *         <ol>
 *             <li>{@link Ability}</li>
 *             <li>{@link Ability_TargetEffect}</li>
 *             <li>{@link Ability_InstantEffect}</li>
 *         </ol>
 *     </li>
 *     <li>
 *         <b>Effects</b>
 *         <ol>
 *             <li>{@link Effect}</li>
 *             <li>{@link Effect_Targeted}</li>
 *             <li>{@link Effect_Scan}</li>
 *             <li><i>{@link EffectGroup} - nyi</i></li>
 *         </ol>
 *     </li>
 *     <li>
 *         <b>Tickables and GameTasks</b>
 *         <ol>
 *             <li>{@link Tickable}</li>
 *             <li>{@link TaskManager}</li>
 *             <li>{@link GameTask}</li>
 *             <li>{@link LogiCore}</li>
 *         </ol>
 *     </li>
 * </ul>
 * <br><hr><br>
 * <p><b>See the Following for Additional Information</b></p>
 * <ul>
 *     <li>{@link Trigger}</li>
 *     <li>{@link TaskManager}</li>
 *     <li>{@link Ability}</li>
 *     <li>{@link Effect}</li>
 *     <li>{@link Validator}</li>
 * </ul>
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
    
    //<editor-fold desc="> Validator Factory Methods">
    
    public static <T extends Validatable<T>> @NotNull ValidationFilter<T> newValidator(@NotNull T owner, @NotNull Function<Map<String, Object>, Boolean> paramHandler) {
        return new ValidationFilter<>(owner) {
            @Override protected boolean validate(@NotNull Map<String, Object> params) {
                return paramHandler.apply(params);
            }
        };
    }
    
    public static <T extends Validatable<T>> @NotNull ValidationFilter<T> newValidator(@NotNull T owner, @NotNull Supplier<Boolean> paramHandler) {
        return newValidator(owner, map -> paramHandler.get());
    }
    
    //</editor-fold>
    
    //
    
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
