package com.taco.suit_lady.logic.triggers;

import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.LogiCore;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedTrigger;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>Used to define the response to a {@link T specific} {@link TriggerEvent}.</p>
 * <br><hr><br>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>A {@link Trigger} functions as a {@link #trigger(TriggerEvent) response} to a {@link T particular} {@link TriggerEvent}.</li>
 *     <li>To define the specific {@link TriggerEvent} response for a {@link Trigger} implementation, override the {@code abstract} {@link #trigger(TriggerEvent)} method.</li>
 *     <li>
 *         The {@link TriggerCondition} class can be used to refine the {@link #test(TriggerEvent) conditions} that must be met for this {@link Trigger} to be {@link #trigger(TriggerEvent) triggered}.
 *         <ul>
 *             <li><i>Use the {@link #setCondition(TriggerCondition)} method to set the {@link TriggerCondition} for this {@link Trigger} instance.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 * <br>
 * <p><b>How to Use</b></p>
 * <ol>
 *     <li>Define a new {@link Trigger} instance either by {@link #Trigger(Entity, TriggerCondition) constructor} or by using a {@link Galaxy} {@link Galaxy#newUnitMovedTrigger(GameObject, Predicate, Consumer) Factory Method}.</li>
 *     <li>Before a {@link Trigger} will {@link #trigger(TriggerEvent) respond} to {@link TriggerEvent events}, it must be {@link TriggerEventManager#register(Trigger) registered} to the {@link TriggerEventManager}.</li>
 *     <li>
 *         The {@link TriggerEventManager} is a singleton {@link SpringApplication Spring} object that handles all {@link Trigger}-related functions.
 *         <ul>
 *             <li>The {@link TriggerEventManager} can be conveniently accessed using the <i>{@link Springable#triggers()}</i> accessor method.</li>
 *             <li>Before a {@link Trigger} will {@link #trigger(TriggerEvent) respond} to {@link TriggerEvent TriggerEvents}, it must first be <i>{@link TriggerEventManager#register(Trigger) registered}</i> with the {@link TriggerEventManager}.</li>
 *             <li>To stop a {@link Trigger} from {@link #trigger(TriggerEvent) responding} to {@link TriggerEvent TriggerEvents}, it must be {@link TriggerEventManager#unregister(Trigger) unregistered}.</li>
 *         </ul>
 *     </li>
 * </ol>
 * <br><hr><br>
 * <p><b>Example Implementation 1</b></p>
 * <br>
 * <pre>{@code
 * public static class TriggerEventImpl extends TriggerEvent<TriggerEventImpl> {
 *     public TriggerEventImpl(@NotNull GameComponent source) {
 *         super(source);
 *     }
 * }
 *
 *
 * public static class TriggerImpl extends Trigger<TriggerEventImpl> {
 *
 *     public TriggerImpl(@NotNull Entity owner, @Nullable TriggerCondition<TriggerEventImpl> condition) {
 *         super(owner, condition);
 *     }
 *
 *
 *     @Override
 *     protected void trigger(@NotNull TriggerEventImpl event) {
 *         //event response
 *     }
 *
 *     @Override
 *     protected Class<TriggerEventImpl> type() {
 *         //the TriggerEvent class object assigned to this Trigger implementation
 *         return TriggerEventImpl.class;
 *     }
 * }
 *
 *
 * public static class TriggerConditionImpl extends TriggerCondition<TriggerEventImpl> {
 *
 *     public TriggerConditionImpl(@NotNull GameComponent gameComponent) {
 *         super(gameComponent);
 *     }
 *
 *     @Override
 *     public boolean test(TriggerEventImpl triggerEvent) {
 *         //condition logic, if applicable
 *     }
 * }
 * }</pre>
 * <br>
 * <pre>{@code
 * //construct a new TriggerCondition
 * //use any GameComponent as constructor parameter
 * TriggerConditionImpl condition = new TriggerConditionImpl(gameComponent);
 *
 * //construct a new Trigger
 * //specify the Entity and TriggerCondition for the Trigger
 * TriggerImpl trigger = new TriggerImpl(entity, condition);
 *
 * //register the Trigger with the TriggerEventManager
 * triggers().register(trigger);
 * }</pre>
 * <br><br>
 * <p><b>Example Implementation 2A</b></p>
 * <br>
 * <pre>{@code
 * //the predicate function to be automatically converted to a TriggerCondition
 * Predicate<UnitArrivedEvent> condition = event -> {
 *     //condition logic
 * };
 *
 * //the consumer function serving as the trigger response action
 * Consumer<UnitArrivedEvent> action = event -> {
 *     //event response
 * };
 *
 *
 * //factory Galaxy methods can be used to quickly construct new specific Trigger instances
 * UnitArrivedTrigger trigger = Galaxy.newUnitArrivedTrigger(gameObject, condition, action);
 *
 * //register the Trigger
 * triggers().register(trigger);
 * }</pre>
 * <br>
 * <p><b>Example Implementation 2B</b></p>
 * <br>
 * <pre>{@code
 * UnitArrivedTrigger trigger = Galaxy.newUnitArrivedTrigger(
 *     gameObject, event -> {
 *         //condition logic
 *     }, event -> {
 *         //event response
 *     });
 *
 * //once again register the Trigger
 * triggers().register(trigger);
 * }</pre>
 * <br>
 * <br><hr><br>
 * <p><b>TriggerEvent Submission</b></p>
 * <ol>
 *     <li>First, {@link TriggerEventManager#submit(TriggerEvent) submit} a new {@link TriggerEvent} implementation to the {@link LogiCore#triggers() TriggerEventManager}.</li>
 *     <li>Upon {@link TriggerEventManager#submit(TriggerEvent) submitting} a {@link TriggerEvent}, all {@link Trigger Triggers} {@link TriggerEventManager#register(Trigger) registered} to that particular {@link TriggerEvent} will be {@link Trigger#trigger(TriggerEvent) triggered}.</li>
 *     <li>Note that the {@link Trigger#readOnlyConditionProperty() Trigger Condition} must also be {@link TriggerCondition#test(Object) passed} for the {@link Trigger} to be {@link Trigger#trigger(TriggerEvent) triggered}.</li>
 * </ol>
 * <p><b>Example Implementations</b></p>
 * <br>
 * <pre>{@code
 * //submit a custom TriggerEvent implementation
 * triggers().submit(new TriggerEventImpl(sourceGameObject));
 *
 * //submit a pre-defined TriggerEvent implementation
 * triggers().submit(new UnitArrivedEvent(sourceGameObject, movedFrom, movedTo, eventTypeId));
 *
 * //submit a TriggerEvent from the event directly
 * new UnitArrivedEvent(sourceGameObject, movedFrom, movedTo, eventTypeId).submit();
 * }</pre>
 *
 * @param <T> The type of {@link TriggerEvent} that this {@link Trigger} responds to.
 */
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
