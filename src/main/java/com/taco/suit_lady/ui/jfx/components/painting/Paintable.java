package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.util.function.Predicate;

public interface Paintable {
    
    //For paint commands that you want to be painted on multiple surfaces, create a "PaintableGroup" implementation of PaintableCanvas that passes
    // all commands sent to it to its list of wrapped PaintableCanvas objects (e.g., Overlay or BoundCanvas).
    ObjectProperty<PaintableCanvas> ownerProperty();
    
    ObjectProperty<Predicate<PaintableCanvas>> autoRemoveConditionProperty();
    BoundsBinding boundsBinding();
    BooleanProperty disabledProperty();
    IntegerProperty paintPriorityProperty();
    
    //
    
    void onAdd(PaintableCanvas owner);
    void onRemove(PaintableCanvas owner);
    
    void paint();
    
    //<editor-fold desc="--- DEFAULT IMPLEMENTATIONS ---">
    
    default PaintableCanvas getOwner() {
        return ownerProperty().get();
    }
    default PaintableCanvas setOwner(PaintableCanvas newValue) {
        PaintableCanvas oldValue = getOwner();
        ownerProperty().set(newValue);
        return oldValue;
    }
    
    default Predicate<PaintableCanvas> getAutoRemoveCondition() {
        return autoRemoveConditionProperty().get();
    }
    default Predicate<PaintableCanvas> setAutoRemoveCondition(Predicate<PaintableCanvas> newValue) {
        Predicate<PaintableCanvas> oldValue = getAutoRemoveCondition();
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
