package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.canvas.Canvas;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>A {@link #isResizable() resizable} implementation of {@link Canvas}.</p>
 */
public class BoundCanvas extends Canvas
        implements Springable, Lockable
{
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final ReadOnlyListWrapper<PaintCommandable> paintCommands;
    
    {
        this.lock = new ReentrantLock();
        
        this.canvasListenerProperty = new ReadOnlyObjectWrapper<>();
        this.paintCommands = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    }
    
    /**
     * <p>Constructs a new {@link BoundCanvas} instance with default {@link #widthProperty() width} and {@link #heightProperty() height} values.</p>
     */
    public BoundCanvas(@NotNull Springable springable)
    {
        super();
        
        this.springable = springable;
    }
    
    /**
     * <p>Constructs a new {@link BoundCanvas} instance with the specified initial {@link #heightProperty() height} and {@code width}.</p>
     *
     * @param width  The initial {@link #widthProperty() width} of this {@link BoundCanvas}.
     * @param height The initial {@link #heightProperty() height} of this {@link BoundCanvas}.
     */
    public BoundCanvas(@NotNull Springable springable, double width, double height)
    {
        super(width, height);
        
        this.springable = springable;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link CanvasListener} instance that is called to {@link CanvasListener#redraw(BoundCanvas, double, double) redraw} whenever this {@link BoundCanvas} is {@link #repaint() repainted}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the {@link CanvasListener} is set to {@code null}, no additional {@link CanvasListener#redraw(BoundCanvas, double, double) redrawing} operations will be executed.</li>
     *     <li>Default {@link #repaint() repaint} operations are still performed.</li>
     * </ol>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link CanvasListener} instance that is called whenever the {@link BoundCanvas canvas} needs to be {@link CanvasListener#redraw(BoundCanvas, double, double) redrawn}.
     */
    public final @NotNull ReadOnlyObjectProperty<CanvasListener> canvasListenerProperty()
    {
        return canvasListenerProperty.getReadOnlyProperty();
    }
    
    /**
     * <b>Passthrough Definition:</b> <i><code>{@link #canvasListenerProperty()}<b>.</b>{@link ReadOnlyObjectProperty#get() get()}</code></i>
     *
     * @return See {@link #canvasListenerProperty()}.
     */
    public final @Nullable CanvasListener getCanvasListener()
    {
        return canvasListenerProperty.get();
    }
    
    /**
     * <b>Passthrough Definition:</b> <i><code>{@code canvasListenerProperty}<b>.</b>{@link ReadOnlyObjectWrapper#set(Object) set}<b>(</b>{@link CanvasListener}</code><b>)</b></i>
     * <p><b>Note That...</b></p>
     * <ol>
     *     <li>
     *         <i>{@link #canvasListenerProperty()}</i> returns a {@link ReadOnlyObjectProperty}, not a {@link ReadOnlyObjectWrapper}.
     *         <ul>
     *             <li>As such, {@link #setCanvasListener(CanvasListener) this method} is the only way to {@link ReadOnlyObjectWrapper#set(Object) change} the {@link CanvasListener} for this {@link BoundCanvas}.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param canvasListener See {@link #canvasListenerProperty()}}.
     */
    public final void setCanvasListener(@Nullable CanvasListener canvasListener)
    {
        canvasListenerProperty.set(canvasListener);
    }
    
    protected final @NotNull ReadOnlyListProperty<PaintCommandable> getPaintCommands()
    {
        return paintCommands.getReadOnlyProperty();
    }
    
    public final boolean containsPaintCommand(@Nullable PaintCommandable command)
    {
        return command != null && sync(() -> getPaintCommands().contains(command));
    }
    
    public final boolean removePaintCommand(@Nullable PaintCommandable command)
    {
        return command != null && sync(() -> {
            if (containsPaintCommand(command)) {
                final boolean removed = getPaintCommands().remove(command);
                command.onRemoved(this);
                return removed;
            } else
                return false;
        });
    }
    
    public final boolean addPaintCommand(@Nullable PaintCommandable command)
    {
        return command != null && sync(() -> {
            if (containsPaintCommand(command))
                return true;
            else {
                final boolean added = getPaintCommands().add(command);
                command.onAdded(this);
                return added;
            }
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull ReentrantLock getLock()
    {
        return lock;
    }
    
    //
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return springable.ctx();
    }
    
    //<editor-fold desc="--- CANVAS ---">
    
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
    
    //</editor-fold>
    
    //</editor-fold>
    
    protected void repaint()
    {
        sync(() -> {
            FXTools.clearCanvasUnsafe(this);
            for (PaintCommandable paintCommand: getPaintCommands())
                paintCommand.paint(this);
            
            final CanvasListener listener = getCanvasListener();
            if (listener != null)
                listener.redraw(this, width, height);
        });
    }
    
    public interface CanvasListener
    {
        void redraw(BoundCanvas source, double newWidth, double newHeight);
    }
}
