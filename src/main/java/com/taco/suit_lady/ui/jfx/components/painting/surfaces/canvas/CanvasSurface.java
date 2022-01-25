package com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.Surface;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.SurfaceData;
import com.taco.suit_lady.util.springable.Springable;
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
        implements Surface<PaintCommand, CanvasSurface> {
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    
    private final SurfaceData<PaintCommand, CanvasSurface> data;
    
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
        this.imageProperty = new ReadOnlyObjectWrapper<>();
        
        this.data = new SurfaceData<>(
                springable, lock, this,
                widthProperty(), heightProperty());
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
    
    public final ReadOnlyObjectProperty<Image> imageProperty() { return imageProperty.getReadOnlyProperty(); }
    public final Image getImage() { return imageProperty.get(); }
    
    public void refreshImage() {
        sync(() -> imageProperty.set(snapshot(new SnapshotParameters(), null)));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull SurfaceData<PaintCommand, CanvasSurface> data() { return data; }
    
    @Override public @NotNull CanvasSurface repaint() {
        return syncFX(() -> {
            ToolsFX.clearCanvasUnsafe(this);
            
            paintables().forEach(uiCommand -> uiCommand.paint());
            
            final CanvasListener listener = getCanvasListener();
            if (listener != null)
                listener.redraw(this, width, height);
            
            refreshImage();
            
            return this;
        });
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
    
    //</editor-fold>
    
    //</editor-fold>
    
    public interface CanvasListener {
        void redraw(CanvasSurface source, double newWidth, double newHeight);
    }
}
