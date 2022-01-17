package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.ui.jfx.components.painting.Paintable;
import com.taco.suit_lady.ui.jfx.components.painting.PaintableCanvas;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>A {@link #isResizable() resizable} implementation of {@link Canvas}.</p>
 */
public class BoundCanvas extends Canvas
        implements PaintableCanvas<PaintCommand, BoundCanvas> {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyObjectWrapper<CanvasListener> canvasListenerProperty;
    private final ListProperty<PaintCommand> paintCommands;
    
    private final ReadOnlyObjectWrapper<Image> imageProperty;
    
    private final IntegerBinding widthBinding;
    private final IntegerBinding heightBinding;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public BoundCanvas(@NotNull Springable springable) {
        this(springable, null);
    }
    
    public BoundCanvas(@NotNull Springable springable, double width, double height) {
        this(springable, null, width, height);
    }
    
    /**
     * <p>Constructs a new {@link BoundCanvas} instance with default {@link #widthProperty() width} and {@link #heightProperty() height} values.</p>
     */
    public BoundCanvas(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, 0, 0);
    }
    
    /**
     * <p>Constructs a new {@link BoundCanvas} instance with the specified initial {@link #heightProperty() height} and {@code width}.</p>
     *
     * @param width  The initial {@link #widthProperty() width} of this {@link BoundCanvas}.
     * @param height The initial {@link #heightProperty() height} of this {@link BoundCanvas}.
     */
    public BoundCanvas(@NotNull Springable springable, @Nullable ReentrantLock lock, double width, double height) {
        super(width, height);
        
        this.springable = springable;
        this.lock = lock != null ? lock : new ReentrantLock();
    
        //
    
        this.canvasListenerProperty = new ReadOnlyObjectWrapper<>();
        this.paintCommands = new SimpleListProperty<>(FXCollections.observableArrayList());
    
        this.imageProperty = new ReadOnlyObjectWrapper<>();
    
        this.widthBinding = Bindings.createIntegerBinding(() -> (int) getWidth(), widthProperty());
        this.heightBinding = Bindings.createIntegerBinding(() -> (int) getHeight(), heightProperty());
    }
    
    //</editor-fold>
    
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
    public final @NotNull ReadOnlyObjectProperty<CanvasListener> canvasListenerProperty() { return canvasListenerProperty.getReadOnlyProperty(); }
    public final @Nullable CanvasListener getCanvasListener() { return canvasListenerProperty.get(); }
    
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
    public final void setCanvasListener(@Nullable CanvasListener canvasListener) { canvasListenerProperty.set(canvasListener); }
    
    
//    public final boolean containsPaintCommand(@Nullable PaintCommand paintCommand) {
//        return paintCommand != null && sync(() -> paintCommands.contains(paintCommand));
//    }
//
//    public final boolean removePaintCommand(@Nullable PaintCommand paintCommand) {
//        return paintCommand != null && sync(() -> {
//            if (containsPaintCommand(paintCommand)) {
//                final boolean removed = paintCommands.remove(paintCommand);
//
//                paintCommand.setOwner(null);
//                paintCommand.onRemove(this);
//
//                return removed;
//            } else
//                return false;
//        });
//    }
//
//    public final boolean addPaintCommand(@Nullable PaintCommand paintCommand) {
//        return paintCommand != null && sync(() -> {
//            if (containsPaintCommand(paintCommand))
//                return true;
//            else {
//                final boolean added = paintCommands.add(paintCommand);
//
//                paintCommand.setOwner(this);
//                paintCommand.onAdd(this);
//
//                return added;
//            }
//        });
//    }
    
    
    public final ReadOnlyObjectProperty<Image> imageProperty() { return imageProperty.getReadOnlyProperty(); }
    public final Image getImage() { return imageProperty.get(); }
    
    public void refreshImage() {
        sync(() -> imageProperty.set(snapshot(new SnapshotParameters(), null)));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull ReentrantLock getLock() { return lock; }
    
    @Override public @NotNull FxWeaver weaver() { return springable.weaver(); }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return springable.ctx(); }
    
    //
    
    @Override public ListProperty<PaintCommand> paintables() { return paintCommands; }
    
    @Override public IntegerBinding widthBinding() { return widthBinding; }
    @Override public IntegerBinding heightBinding() { return heightBinding; }
    
    
    @Override public String getName() { throw ExceptionTools.nyi(); }
    
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
    
    @Override
    public void repaint() {
        sync(() -> FXTools.runFX(() -> {
            FXTools.clearCanvasUnsafe(this);
            
            paintables().forEach(uiCommand -> uiCommand.paint());
            
            final CanvasListener listener = getCanvasListener();
            if (listener != null)
                listener.redraw(this, width, height);
            
            refreshImage();
        }, true));
    }
    
    public interface CanvasListener {
        void redraw(BoundCanvas source, double newWidth, double newHeight);
    }
}
