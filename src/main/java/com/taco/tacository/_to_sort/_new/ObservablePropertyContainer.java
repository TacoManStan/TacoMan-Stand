package com.taco.tacository._to_sort._new;

import com.taco.tacository._to_sort._new.interfaces.ObservablePropertyContainable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * <p>Wraps and contains additional functionality for an {@link ObservablePropertyContainable} instance.</p>
 * <p><b>{@link #valueProperties() Observable Value Array}</b></p>
 * <p><i>{@link #valueProperties()}</i> contains an {@code array} of {@link ObservableValue ObservableValues}.</p>
 * <ol>
 *     <li>The {@link ObservableValue ObservableValues} contain all {@link ObservableValue} instances contained within the {@link #valueProperties() Value Properries} of the {@link ObservablePropertyContainable} assigned to this {@link ObservablePropertyContainer}.</li>
 *     <li>The values contained within <i>{@link #valueProperties()}</i> are calculated from within and set by the {@link ObservablePropertyContainer} {@link ObservablePropertyContainer#ObservablePropertyContainer(ObservablePropertyContainable) Constructor}.</li>
 *     <li>Therefore, the contents of <i>{@link #valueProperties()}</i> are <u>NOT</u> updated if the {@link ObservablePropertyContainable#properties() Containable Properties} of the {@link ObservablePropertyContainable} assigned to this {@link ObservablePropertyContainer} are changed.</li>
 * </ol>
 * <p><b>{@link ChangeListener Change Listening}</b></p>
 * <ol>
 *     <li>Use <i>{@link #addListener(ChangeListener)}</i> to {@code add} any number of {@link ChangeListener ChangeListeners} to this {@link ObservablePropertyContainer} instance.</li>
 *     <li>Use <i>{@link #removeListener(ChangeListener)}</i> to {@code remove} an existing {@link ChangeListener} from this {@link ObservablePropertyContainer} instance.</li>
 *     <li>Use <i>{@link #clearListeners()}</i> to {@code remove} all existing {@link ChangeListener ChangeListeners} from this {@link ObservablePropertyContainer} instance.</li>
 *     Each {@link ChangeListener} {@link #addListener(ChangeListener) added} to this {@link ObservablePropertyContainer} is applied to every {@link ObservableValue} contained within the <i>{@link #valueProperties()}</i> {@code Array}.
 *     <ul>
 *         <li><i>Note that {@link ChangeListener ChangeListeners} added to a {@link ObservablePropertyContainer} are applied to the contents of {@link #valueProperties()}, NOT the contents of {@link ObservablePropertyContainable#properties()}.</i></li>
 *     </ul>
 * </ol>
 * <p><b>Other Details</b></p>
 * <ol>
 *     <li>Use <i>{@link #forEachObservableValue(Consumer)}</i> to {@link Consumer#accept(Object) Execute} an {@link Consumer Operation} on each {@link ObservableValue} contained in the {@link #valueProperties() Value Properties Array}.</li>
 * </ol>
 */
@SuppressWarnings("rawtypes")
public class ObservablePropertyContainer {
    
    private final ObservablePropertyContainable oPC;
    private final ObservableValue<?>[] valueProperties;
    
    private final ObservableList<ChangeListener> activeListeners;
    
    public ObservablePropertyContainer(ObservablePropertyContainable oPC) {
        this.oPC = oPC;
        this.valueProperties = Arrays.stream(oPC.properties()).filter(
                observable -> observable instanceof ObservableValue<?>).map(
                observable -> (ObservableValue<?>) observable).toArray(ObservableValue[]::new);
        this.activeListeners = FXCollections.observableArrayList();
    }
    
    public ObservablePropertyContainable oPC() {
        return oPC;
    }
    
    public ObservableValue<?>[] valueProperties() {
        return valueProperties;
    }
    
    public final void addListener(@NotNull ChangeListener listener) {
        forEachObservableValue(observable -> observable.addListener(listener));
        activeListeners.add(listener);
    }
    
    public final void removeListener(@NotNull ChangeListener listener) {
        forEachObservableValue(observable -> observable.removeListener(listener));
        activeListeners.remove(listener);
    }
    
    public final void clearListeners() {
        activeListeners.stream().<Consumer<ObservableValue<?>>>map(
                listener -> observable ->
                        observable.removeListener(listener)).forEach(this::forEachObservableValue);
    }
    
    public final void forEachObservableValue(Consumer<ObservableValue<?>> action) {
        Arrays.stream(valueProperties).forEach(action);
    }
}
