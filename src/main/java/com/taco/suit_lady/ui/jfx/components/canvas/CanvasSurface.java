package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.ui.jfx.components.canvas.paintingV2.CanvasPainter;
import com.taco.suit_lady.ui.jfx.components.canvas.paintingV2.PaintableSurfaceDataContainerV2;
import com.taco.suit_lady.ui.jfx.components.canvas.paintingV2.PaintableSurfaceV2;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
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
        implements PaintableSurfaceV2<CanvasPainter, CanvasSurface> {
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    
    private final PaintableSurfaceDataContainerV2<CanvasPainter, CanvasSurface> data;
    
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
        
        this.data = new PaintableSurfaceDataContainerV2<>(
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
    /**
     * <b>Passthrough Definition:</b> <i><code>{@code canvasListenerProperty}<b>.</b>{@link ReadOnlyObjectWrapper#set(Object) set}<b>(</b>{@link CanvasListener}</code><b>)</b></i>
     * <p><b>Note That...</b></p>
     * <ol>
     *     <li>
     *         <i>{@link #canvasListenerProperty()}</i> returns a {@link ReadOnlyObjectProperty}, not a {@link ReadOnlyObjectWrapper}.
     *         <ul>
     *             <li>As such, {@link #setCanvasListener(CanvasListener) this method} is the only way to {@link ReadOnlyObjectWrapper#set(Object) change} the {@link CanvasListener} for this {@link CanvasSurface}.</li>
     *         </ul>
     *     </li>
     * </ol>
     *
     * @param canvasListener See {@link #canvasListenerProperty()}}.
     */
    public final void setCanvasListener(@Nullable CanvasListener canvasListener) { canvasListenerProperty.set(canvasListener); }
    
    
    public final ReadOnlyObjectProperty<Image> imageProperty() { return imageProperty.getReadOnlyProperty(); }
    public final Image getImage() { return imageProperty.get(); }
    
    public void refreshImage() {
        sync(() -> imageProperty.set(snapshot(new SnapshotParameters(), null)));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableSurfaceDataContainerV2<CanvasPainter, CanvasSurface> data() { return data; }
    
    @Override public @NotNull CanvasSurface repaint() {
        return sync(() -> FXTools.runFX(() -> {
            FXTools.clearCanvasUnsafe(this);
            
            paintablesV2().forEach(uiCommand -> uiCommand.paint());
            
            final CanvasListener listener = getCanvasListener();
            if (listener != null)
                listener.redraw(this, width, height);
            
            refreshImage();
            
            return this;
        }));
    }
    
    //<editor-fold desc="--- CANVAS ---">
    
    @Override
    public double minHeight(double width) {
        return 64;
    }
    
    @Override
    public double maxHeight(double width) {
        return 1000;
    }
    
    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }
    
    @Override
    public double minWidth(double height) {
        return 0;
    }
    
    @Override
    public double maxWidth(double height) {
        return 10000;
    }
    
    @Override
    public boolean isResizable() {
        return true;
    }
    
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
