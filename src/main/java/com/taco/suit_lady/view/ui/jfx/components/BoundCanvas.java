package com.taco.suit_lady.view.ui.jfx.components;

import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.util.tools.fxtools.FXTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.canvas.Canvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class BoundCanvas extends Canvas
{
    private final ReentrantLock lock;
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final ReadOnlyListWrapper<PaintCommandable> paintCommands;
    
    public BoundCanvas()
    {
        super();
    }
    
    public BoundCanvas(double width, double height)
    {
        super(width, height);
    }
    
    {
        this.lock = new ReentrantLock();
        
        this.canvasListenerProperty = new ReadOnlyObjectWrapper<>();
        this.paintCommands = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReentrantLock getLock()
    {
        return lock;
    }
    
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
    
    protected final @NotNull ReadOnlyListProperty<PaintCommandable> getPaintCommands()
    {
        return paintCommands.getReadOnlyProperty();
    }
    
    public final boolean containsPaintCommand(PaintCommandable command)
    {
        return TaskTools.sync(lock, () -> command != null && getPaintCommands().contains(command));
    }
    
    public final boolean removePaintCommand(PaintCommandable command)
    {
        return TaskTools.sync(lock, () -> {
            if (containsPaintCommand(command)) {
                final boolean removed = getPaintCommands().remove(command);
                command.onRemoved(this);
                return removed;
            }
            return false;
        });
    }
    
    public final boolean addPaintCommand(PaintCommandable command)
    {
        return TaskTools.sync(lock, () -> {
            if (command != null)
                if (containsPaintCommand(command))
                    return true;
                else {
                    final boolean added = getPaintCommands().add(command);
                    command.onAdded(this);
                    return added;
                }
            return false;
        });
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
        
        repaint();
    }
    
    protected void repaint()
    {
        TaskTools.sync(lock, () -> {
            FXTools.get().clearCanvasUnsafe(this);
            for (PaintCommandable paintCommand: getPaintCommands())
                paintCommand.paint(this);
            
            final CanvasListener listener = getCanvasListener();
            if (listener != null)
                listener.redraw(this, width, height);
        });
    }
    
    //</editor-fold>
    
    public interface CanvasListener
    {
        void redraw(BoundCanvas source, double newWidth, double newHeight);
    }
}
