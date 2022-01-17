package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.util.function.Predicate;

public interface Paintable<O extends PaintableCanvas> {
    
    //For paint commands that you want to be painted on multiple surfaces, create a "PaintableGroup" implementation of PaintableCanvas that passes
    // all commands sent to it to its list of wrapped PaintableCanvas objects (e.g., Overlay or BoundCanvas).
    ObjectProperty<O> ownerProperty();
    
    ObjectProperty<Predicate<O>> autoRemoveConditionProperty();
    BoundsBinding boundsBinding();
    BooleanProperty disabledProperty();
    IntegerProperty paintPriorityProperty();
    
    //
    
    void onAdd(O owner);
    void onRemove(O owner);
    
    void paint();
    
    //<editor-fold desc="--- DEFAULT IMPLEMENTATIONS ---">
    
    default O getOwner() {
        return ownerProperty().get();
    }
    default O setOwner(O newValue) {
        O oldValue = getOwner();
        ownerProperty().set(newValue);
        return oldValue;
    }
    
    default Predicate<O> getAutoRemoveCondition() {
        return autoRemoveConditionProperty().get();
    }
    default Predicate<O> setAutoRemoveCondition(Predicate<O> newValue) {
        Predicate<O> oldValue = getAutoRemoveCondition();
        autoRemoveConditionProperty().set(newValue);
        return oldValue;
    }
    
    default boolean isDisabled() {
        return disabledProperty().get();
    }
    default boolean setDisabled(boolean newValue) {
        boolean oldValue = isDisabled();
        disabledProperty().set(newValue);
        return oldValue;
    }
    
    default int getPaintPriority() {
        return paintPriorityProperty().get();
    }
    default int setPaintPriority(int newValue) {
        int oldValue = getPaintPriority();
        paintPriorityProperty().set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
}
