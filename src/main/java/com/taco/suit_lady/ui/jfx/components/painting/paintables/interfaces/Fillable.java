package com.taco.suit_lady.ui.jfx.components.painting.paintables.interfaces;

import com.taco.suit_lady.util.tools.SLProperties;
import javafx.beans.property.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public interface Fillable {
    
    @NotNull BooleanProperty isFillProperty();
    default boolean isFill() { return isFillProperty().get(); }
    default boolean setFill(boolean newValue) { return SLProperties.setProperty(isFillProperty(), newValue); }
}
