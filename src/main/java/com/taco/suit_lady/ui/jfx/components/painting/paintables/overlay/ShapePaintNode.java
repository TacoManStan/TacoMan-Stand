package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class ShapePaintNode extends PaintNode {
    
    private final ObjectProperty<Paint> fillProperty;
    private final ObjectProperty<Paint> strokeProperty;
    
    public ShapePaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        super(springable, lock);
        
        this.fillProperty = new SimpleObjectProperty<>(Color.TRANSPARENT);
        this.strokeProperty = new SimpleObjectProperty<>(Color.BLACK);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<Paint> fillProperty() { return fillProperty; }
    public final Paint getFill() { return fillProperty.get(); }
    public final Paint setFill(Paint newValue) { return PropertiesSL.setProperty(fillProperty, newValue); }
    
    public final ObjectProperty<Paint> strokeProperty() { return strokeProperty; }
    public final Paint getStroke() { return strokeProperty.get(); }
    public final Paint setStroke(Paint newValue) { return PropertiesSL.setProperty(strokeProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //NOTE: Methods are overwritten to provide more specific return value type (Shape vs. Node).
    //The same should be done for child classes and other similar cases.
    
    @Override protected abstract Shape refreshNode();
    
    @Override public Shape getNode() { return (Shape) super.getNode(); }
    @Override public Shape getAndRefreshNode() { return (Shape) super.getAndRefreshNode(); }
    
    @Override protected Shape applyRefresh(@NotNull Node n) { return (Shape) super.applyRefresh(n); }
    @Override protected Shape syncBounds(@NotNull Node n) { return (Shape) super.syncBounds(n); }
    
    //</editor-fold>
}
