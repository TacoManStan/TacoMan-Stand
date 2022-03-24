package com.taco.tacository.util.values.formulas.pathfinding;

import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.values.numbers.Num2D;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;

public class DummyElement {
    
    private final Num2D matrixIndex;
    private final ReadOnlyBooleanWrapper pathableProperty;
    
    public DummyElement(@NotNull Num2D matrixIndex, boolean pathable) {
        this.matrixIndex = matrixIndex;
        this.pathableProperty = new ReadOnlyBooleanWrapper(pathable);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull Num2D getMatrixIndex() { return matrixIndex; }
    
    public final @NotNull ReadOnlyBooleanProperty readOnlyPathableProperty() { return pathableProperty.getReadOnlyProperty(); }
    public final boolean isPathable() { return pathableProperty.get(); }
    protected final boolean setPathable(boolean newValue) { return Props.setProperty(pathableProperty, newValue); }
    
    //</editor-fold>
}
