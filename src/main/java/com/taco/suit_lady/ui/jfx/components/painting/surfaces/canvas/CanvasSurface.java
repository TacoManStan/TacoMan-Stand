package com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.Surface;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.SurfaceData;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;
import com.taco.suit_lady.util.tools.Exceptions;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>A {@link #isResizable() resizable} implementation of {@link Canvas}.</p>
 */
public class CanvasSurface extends Canvas
        implements Surface<PaintCommand, CanvasSurface>, GFXObject<CanvasSurface> {
    
    private final TaskManager<CanvasSurface> taskManager;
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final SurfaceData<PaintCommand, CanvasSurface> data;
    
    private final ReadOnlyObjectWrapper<Image> snapshotProperty;
    private final LongProperty snapshotFrequencyProperty;
    private final Timer snapshotTimer;
    
    private boolean needsUpdate;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public CanvasSurface(@NotNull Springable springable) {
        this(springable, null);
    }
    
    public CanvasSurface(@NotNull Springable springable, double width, double height) {
        this(springable, null, width, height);
    }
    
    /**
     * <p>Constructs a new {@link CanvasSurface} instance with default {@link #widthProperty() width} and {@link #heightProperty() height} values.</p>
     */
    public CanvasSurface(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, 0, 0);
    }
    
    /**
     * <p>Constructs a new {@link CanvasSurface} instance with the specified initial {@link #heightProperty() height} and {@code width}.</p>
     *
     * @param width  The initial {@link #widthProperty() width} of this {@link CanvasSurface}.
     * @param height The initial {@link #heightProperty() height} of this {@link CanvasSurface}.
     */
    public CanvasSurface(@NotNull Springable springable, @Nullable ReentrantLock lock, double width, double height) {
        super(width, height);
        
        this.canvasListenerProperty = new ReadOnlyObjectWrapper<>();
        this.data = new SurfaceData<>(
                springable, lock, this,
                widthProperty(), heightProperty());
        
        
        this.snapshotProperty = new ReadOnlyObjectWrapper<>();
        this.snapshotFrequencyProperty = new SimpleLongProperty();
        this.snapshotTimer = Timers.newStopwatch();
        
        this.snapshotFrequencyProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue.longValue() == -1)
                snapshotTimer.stop();
            else if (newValue.longValue() <= 0)
                throw Exceptions.unsupported("Snapshot Frequency must be either -1 (disabled) or greater than 0 [" + getSnapshotFrequency() + "]");
            else
                snapshotTimer.reset(newValue);
        });
        setSnapshotFrequency(-1L);
        
        this.snapshotTimer.setOnTimeout(() -> sync(() -> {
            updateSnapshot();
            snapshotTimer.reset(getSnapshotFrequency());
        }));
        
        this.needsUpdate = false;
        this.taskManager = new TaskManager<>(this).init();
//        logiCore().addGfxObject(this);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link CanvasListener} instance that is called to {@link CanvasListener#redraw(CanvasSurface, double, double) redraw} whenever this {@link CanvasSurface} is {@link #repaint() repainted}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the {@link CanvasListener} is set to {@code null}, no additional {@link CanvasListener#redraw(CanvasSurface, double, double) redrawing} operations will be executed.</li>
     *     <li>Default {@link #repaint() repaint} operations are still performed.</li>
     * </ol>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link CanvasListener} instance that is called whenever the {@link CanvasSurface canvas} needs to be {@link CanvasListener#redraw(CanvasSurface, double, double) redrawn}.
     */
    public final @NotNull ReadOnlyObjectProperty<CanvasListener> canvasListenerProperty() { return canvasListenerProperty.getReadOnlyProperty(); }
    public final @Nullable CanvasListener getCanvasListener() { return canvasListenerProperty.get(); }
    public final void setCanvasListener(@Nullable CanvasListener canvasListener) { canvasListenerProperty.set(canvasListener); }
    
    //
    
    public final ReadOnlyObjectProperty<Image> snapshotProperty() { return snapshotProperty.getReadOnlyProperty(); }
    public final Image getSnapshot() { return snapshotProperty.get(); }
    
    public final LongProperty snapshotFrequencyProperty() { return snapshotFrequencyProperty; }
    public final long getSnapshotFrequency() { return snapshotFrequencyProperty.get(); }
    public final long setSnapshotFrequency(@NotNull Number newValue) { return PropertiesSL.setProperty(snapshotFrequencyProperty, newValue.longValue()); }
    
    public void updateSnapshot() {
        ToolsFX.runFX(() -> snapshotProperty.set(snapshot(new SnapshotParameters(), null)), true);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull SurfaceData<PaintCommand, CanvasSurface> data() { return data; }
    
    @Override public @NotNull CanvasSurface repaint() {
        //        TasksSL.printThread();
        //        if (!ToolsFX.isFXThread()) {
        //            final Thread current = Thread.currentThread();
        //            final StackTraceElement[] es = current.getStackTrace();
        //            System.out.println("STACK TRACE ELEMENTS  [" + current.getName() + "]  (CanvasSurface.java | Line ~127)");
        //            for (int i = 0; i < es.length; i++)
        //                System.out.println(i + ": " + es[i]);
        //        }
        
        needsUpdate = true;
        
        return this;
        
        //        return ToolsFX.runFX(() -> {
        //            ToolsFX.clearCanvasUnsafe(this);
        //
        //            paintables().forEach(PaintCommand::paint);
        //
        //            final CanvasListener listener = getCanvasListener();
        //            if (listener != null)
        //                listener.redraw(this, width, height);
        //
        //            if (snapshotTimer.isTimedOut())
        //                snapshotTimer.getOnTimeout().run();
        //
        //            return this;
        //        });
    }
    
    //<editor-fold desc="--- CANVAS ---">
    
    @Override public double minHeight(double width) { return 64; }
    @Override public double maxHeight(double width) { return 1000; }
    @Override public double prefHeight(double width) { return minHeight(width); }
    
    @Override public double minWidth(double height) { return 0; }
    @Override public double maxWidth(double height) { return 10000; }
    
    @Override public boolean isResizable() { return true; }
    
    private double width;
    private double height;
    
    @Override
    public void resize(double width, double height) {
        if (this.width == width && this.height == height)
            return;
        
        this.width = width;
        this.height = height;
        
        super.setWidth(width);
        super.setHeight(height);
        
        repaint();
    }
    
    @Override public void onGfxUpdate() {
        ToolsFX.clearCanvasUnsafe(this);
        
        paintables().forEach(PaintCommand::paint);
        
        final CanvasListener listener = getCanvasListener();
        if (listener != null)
            listener.redraw(this, width, height);
        
        if (snapshotTimer.isTimedOut())
            snapshotTimer.getOnTimeout().run();
        
        needsUpdate = false;
    }
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    
    @Override public @NotNull TaskManager<CanvasSurface> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public interface CanvasListener {
        void redraw(CanvasSurface source, double newWidth, double newHeight);
    }
}
