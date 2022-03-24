package com.taco.tacository.ui.jfx.components.painting.paintables.canvas;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Props;
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
    public final boolean setIsFill(boolean newValue) { return Props.setProperty(isFillProperty, newValue); }
    
    //</editor-fold>
}
