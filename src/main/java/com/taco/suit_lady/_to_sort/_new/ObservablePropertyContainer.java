package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

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
