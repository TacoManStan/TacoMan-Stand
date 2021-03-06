package com.taco.tacository.ui.jfx.components.painting.paintables.canvas;

import com.taco.tacository.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.tacository.util.values.numbers.Bounds;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.tools.fx_tools.FX;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class CroppedImagePaintCommand extends ImagePaintCommandBase {
    
    private final DoubleProperty xScaleProperty;
    private final DoubleProperty yScaleProperty;
    
    public CroppedImagePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, 1d, 1d);
    }
    
    public CroppedImagePaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock, double xScale, double yScale) {
        super(springable, lock);
        
        this.xScaleProperty = new SimpleDoubleProperty(xScale);
        this.yScaleProperty = new SimpleDoubleProperty(yScale);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final DoubleProperty xScaleProperty() { return xScaleProperty; }
    public final double getScaleX() { return xScaleProperty.get(); }
    public final double setScaleX(double newValue) { return Props.setProperty(xScaleProperty, newValue); }
    
    public final DoubleProperty yScaleProperty() { return yScaleProperty; }
    public final double getScaleY() { return yScaleProperty.get(); }
    public final double setScaleY(double newValue) { return Props.setProperty(yScaleProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CroppedImagePaintCommand init() {
        return (CroppedImagePaintCommand) super.init();
    }
    
    @Override protected void drawImage(@NotNull Image image, @NotNull CanvasSurface surface, @NotNull Bounds bounds) {
        FX.drawImageScaled(surface, image, bounds, getScaleX(), getScaleY(), false);
    }
    
    //</editor-fold>
}
