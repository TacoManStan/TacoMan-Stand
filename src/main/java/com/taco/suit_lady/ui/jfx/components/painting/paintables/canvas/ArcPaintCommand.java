package com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas;

import com.taco.suit_lady.util.values.bounds.Bounds;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.ArcType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class ArcPaintCommand extends ShapePaintCommand {
    
    private final DoubleProperty startAngleProperty;
    private final DoubleProperty arcExtentProperty;
    private final ObjectProperty<ArcType> closureProperty;
    
    public ArcPaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock,
                           double startAngle, double arcExtent, @Nullable ArcType closure) {
        super(springable, lock);
        
        this.startAngleProperty = new SimpleDoubleProperty(startAngle);
        this.arcExtentProperty = new SimpleDoubleProperty(arcExtent);
        this.closureProperty = new SimpleObjectProperty<>(closure);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final DoubleProperty startAngleProperty() { return startAngleProperty; }
    public final double getStartAngle() { return startAngleProperty.get(); }
    public final double setStartAngle(double newValue) { return Props.setProperty(startAngleProperty, newValue); }
    
    public final DoubleProperty arcExtentProperty() { return arcExtentProperty; }
    public final double getArcExtent() { return arcExtentProperty.get(); }
    public final double setArcExtent(double newValue) { return Props.setProperty(arcExtentProperty, newValue); }
    
    public final ObjectProperty<ArcType> closureProperty() { return closureProperty; }
    public final ArcType getClosure() { return closureProperty.get(); }
    public final ArcType setClosure(ArcType newValue) { return Props.setProperty(closureProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void onPaint() {
        Bounds bounds = getBounds();
        if (isValidDimensions())
            FX.drawArc(getSurface(), bounds, getStartAngle(), getArcExtent(), getClosure(), false, isFill());
    }
    
    //</editor-fold>
}
