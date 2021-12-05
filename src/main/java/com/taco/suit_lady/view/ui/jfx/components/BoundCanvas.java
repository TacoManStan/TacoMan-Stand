package com.taco.suit_lady.view.ui.jfx.components;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.canvas.Canvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoundCanvas extends Canvas
{
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    
    public BoundCanvas()
    {
        super();
    }
    
    public BoundCanvas(double width, double height)
    {
        super(width, height);
    }
    
    {
        canvasListenerProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyObjectProperty<CanvasListener> canvasListenerProperty()
    {
        return canvasListenerProperty.getReadOnlyProperty();
    }
    
    public final @Nullable CanvasListener getCanvasListener()
    {
        return canvasListenerProperty.get();
    }
    
    public final void setCanvasListener(@Nullable CanvasListener canvasListener)
    {
        canvasListenerProperty.set(canvasListener);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public double minHeight(double width)
    {
        return 64;
    }
    
    @Override
    public double maxHeight(double width)
    {
        return 1000;
    }
    
    @Override
    public double prefHeight(double width)
    {
        return minHeight(width);
    }
    
    @Override
    public double minWidth(double height)
    {
        return 0;
    }
    
    @Override
    public double maxWidth(double height)
    {
        return 10000;
    }
    
    @Override
    public boolean isResizable()
    {
        return true;
    }
    
    private double width;
    private double height;
    
    @Override
    public void resize(double width, double height)
    {
        if (this.width == width && this.height == height)
            return;
    
        this.width = width;
        this.height = height;
        
        super.setWidth(width);
        super.setHeight(height);
        
        final CanvasListener listener = getCanvasListener();
        if (listener != null)
            listener.redraw(this, width, height);
    }
    
    //</editor-fold>
    
    public interface CanvasListener
    {
        void redraw(BoundCanvas source, double newWidth, double newHeight);
    }
}
