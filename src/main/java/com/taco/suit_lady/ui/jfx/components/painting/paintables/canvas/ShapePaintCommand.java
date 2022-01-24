package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ShapePaintCommand extends PaintCommand {
    
    private final BooleanProperty isFillProperty;
    
    public ShapePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.isFillProperty = new SimpleBooleanProperty(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final BooleanProperty isFillProperty() { return isFillProperty; }
    public final boolean isFill() { return isFillProperty.get(); }
    public final boolean setIsFill(boolean newValue) { return PropertiesSL.setProperty(isFillProperty, newValue); }
    
    //</editor-fold>
}
