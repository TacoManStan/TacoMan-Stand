package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class RectanglePaintCommand extends SimplePaintCommand
{
    private final ObjectProperty<Rectangle> rectangleProperty;
    private final BooleanProperty fillProperty;
    
    public RectanglePaintCommand(boolean fill, ReentrantLock lock)
    {
        this(null, fill, c -> false, lock);
        
        rectangleProperty.addListener((observable, oldValue, newValue) -> repaintOwners());
        fillProperty.addListener((observable, oldValue, newValue) -> repaintOwners());
    }
    
    public RectanglePaintCommand(
            @Nullable Rectangle rectangle,
            boolean fill,
            @NotNull Predicate<BoundCanvas> autoRemoveCondition,
            @Nullable ReentrantLock lock)
    {
        super(autoRemoveCondition, lock);
        
        this.rectangleProperty = new SimpleObjectProperty<>(rectangle);
        this.fillProperty = new SimpleBooleanProperty(fill);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ObjectProperty<Rectangle> rectangleProperty()
    {
        return rectangleProperty;
    }
    
    public final @Nullable Rectangle getRectangle()
    {
        return rectangleProperty.get();
    }
    
    public final void setRectangle(@Nullable Rectangle rectangle)
    {
        rectangleProperty.set(rectangle);
    }
    
    public final @NotNull BooleanProperty fillProperty()
    {
        return fillProperty;
    }
    
    public final boolean isFill()
    {
        return fillProperty.get();
    }
    
    public final void setFill(boolean fill)
    {
        fillProperty.set(fill);
    }
    
    //</editor-fold>
    
    @Override
    protected void onPaint(BoundCanvas canvas)
    {
        final Rectangle rect = getRectangle();
        if (rect != null)
            FXTools.drawRectangle(canvas, rect, false, isFill());
    }
}
