package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class CanvasShapePaintableV2 extends CanvasPaintableV2 {
    
    private final BooleanProperty isFillProperty;
    
    public CanvasShapePaintableV2(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        this.isFillProperty = new SimpleBooleanProperty(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final BooleanProperty isFillProperty() { return isFillProperty; }
    public final boolean isFill() { return isFillProperty.get(); }
    public final boolean setIsFill(boolean newValue) { return PropertyTools.setProperty(isFillProperty, newValue); }
    
    //</editor-fold>
}
