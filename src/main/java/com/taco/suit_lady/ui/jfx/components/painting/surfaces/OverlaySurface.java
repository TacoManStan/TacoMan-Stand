package com.taco.suit_lady.ui.jfx.components.painting.surfaces;

import com.taco.suit_lady._to_sort._new.interfaces.ReadOnlyNameableProperty;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.Paintable;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.PaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay.PaintNode;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.util.tools.list_tools.ListTools;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

// TO-DOC
public class OverlaySurface
        implements SpringableWrapper, Lockable, ReadOnlyNameableProperty, Comparable<OverlaySurface>, Surface<PaintNode, OverlaySurface> {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyStringWrapper nameProperty;
    
    private final StackPane root;
    private final StackPane overlayPane;
    private final CanvasPane foregroundCanvasPane;
    private final CanvasPane backgroundCanvasPane;
    
    private final ReadOnlyIntegerWrapper paintPriorityProperty;
    
    private final SurfaceData<PaintNode, OverlaySurface> data;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    // TO-DOC
    public OverlaySurface(@NotNull Springable springable) {
        this(springable, null, null, 1);
    }
    
    // TO-DOC
    public OverlaySurface(@NotNull Springable springable, @Nullable String name) {
        this(springable, null, name, 1);
    }
    
    // TO-DOC
    public OverlaySurface(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, null, 1);
    }
    
    // TO-DOC
    public OverlaySurface(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name) {
        this(springable, lock, name, 1);
    }
    
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p>Constructs a new {@link OverlaySurface} instance used to display a set of {@link PaintNode Paint Commands}.</p>
     * <p><hr>
     * <p><b>Parameter Details</b></p>
     * <ol>
     *     <li>
     *         <b>Name:</b> {@literal String} — Refer to <code><i>{@link #nameProperty()}</i></code> for additional information.
     *     </li>
     *     <li>
     *         <b>Paint Priority:</b> {@literal int} — Refer to <code><i>{@link #paintPriorityProperty()}</i></code> for additional information.
     *     </li>
     * </ol>
     *
     * @param name          The {@link String name} of this {@link OverlaySurface}.
     * @param paintPriority The priority of this {@link OverlaySurface}.
     */
    public OverlaySurface(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name, int paintPriority) {
        this.springable = SLExceptions.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock; // Null-checking is done in get method via lazy instantiation
        
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.root = new StackPane();
        this.root.setAlignment(Pos.TOP_LEFT);
        
        this.overlayPane = new StackPane();
        this.overlayPane.setAlignment(Pos.TOP_LEFT);
        //        this.overlayPane.setStyle("-fx-border-color: blue");
        
        this.foregroundCanvasPane = new CanvasPane(springable);
        this.backgroundCanvasPane = new CanvasPane(springable);
        
        this.data = new SurfaceData<>(springable, lock, this, root.widthProperty(), root.heightProperty());
        this.paintPriorityProperty = new ReadOnlyIntegerWrapper(paintPriority);
        
        initPainting();
        
        //
        
        //TODO
        ListTools.applyListener(lock, paintables(), (op1, op2, opType, triggerType) -> {
            overlayPane.getChildren().retainAll();
            paintables().forEach(paintable -> overlayPane.getChildren().add(paintable.getAndRefreshNode()));
        });
    }
    
    private void initPainting() {
        FX.bindToParent(backgroundCanvasPane, root, true);
        FX.bindToParent(overlayPane, root, true);
        FX.bindToParent(foregroundCanvasPane, root, true);
        
        FX.togglePickOnBounds(root, false);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final StackPane getRoot() { return root; }
    
    protected final StackPane getOverlayPane() { return overlayPane; }
    protected final CanvasPane getForegroundCanvasPane() { return foregroundCanvasPane; }
    protected final CanvasPane getBackgroundCanvasPane() { return backgroundCanvasPane; }
    
    public final CanvasSurface getForegroundCanvas() { return foregroundCanvasPane.canvas(); }
    public final CanvasSurface getBackgroundCanvas() { return backgroundCanvasPane.canvas(); }
    
    /**
     * <p>Returns the {@link ReadOnlyIntegerProperty} representing the priority at which this {@link OverlaySurface} is to be painted.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link #paintPriorityProperty() paint priority} is used by the {@link OverlayHandler} to overlap its {@link OverlayHandler#overlays() overlays} based on their {@link #getPaintPriority() priorities}..</li>
     *     <li>The {@link #getPaintPriority() paint priority} is an {@code int} with acceptable bounds of {@code 1} to {@link Integer#MAX_VALUE}.</li>
     *     <li>
     *         {@link OverlaySurface Overlays} with larger {@link #getPaintPriority() paint priority} values are painted {@code first}.
     *         Subsequent {@link OverlaySurface Overlays} are then painted on top, with {@link OverlaySurface Overlays} of a {@link #getPaintPriority() paint priority} of {@code 1} being painted last (on top).
     *     </li>
     * </ol>
     *
     * @return The {@link ReadOnlyIntegerProperty} representing the priority at which this {@link OverlaySurface} is to be painted.
     */
    public final ReadOnlyIntegerProperty paintPriorityProperty() { return paintPriorityProperty.getReadOnlyProperty(); }
    public final int getPaintPriority() { return paintPriorityProperty.get(); }
    public final void setPaintPriority(int paintPriority) {
        if (paintPriority <= 0)
            throw SLExceptions.ex(new IndexOutOfBoundsException("Paint Priority Must Be Greater Than Zero! [" + paintPriority + "]"));
        paintPriorityProperty.set(paintPriority);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">3.
    
    @Override public @NotNull SurfaceData<PaintNode, OverlaySurface> data() { return data; }
    @Override public @NotNull OverlaySurface repaint() {
        sync(() -> paintables().forEach(paintable -> paintable.paint()));
        return this;
    }
    
    //
    
    @Override public final ReadOnlyStringProperty nameProperty() {
        return nameProperty.getReadOnlyProperty();
    }
    
    //
    
    @Override public int compareTo(@NotNull OverlaySurface o) {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
    
    
    /**
     * <p>Adds the specified {@link Paintable} instance to this {@link OverlaySurface}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@link Paintable} is an instance of {@link PaintNode}, the {@link Paintable} is {@link #addPaintable(Paintable) added} directly to this {@link OverlaySurface}.</li>
     *     <li>If the specified {@link Paintable} is an instance of {@link PaintCommand}, the {@link Paintable} is {@link CanvasSurface#addPaintable(Paintable) added} to the {@link #getForegroundCanvas() foreground canvas} of this {@link OverlaySurface}.</li>
     *     <li>If the specified {@link Paintable} is of an unknown type, an {@link UnsupportedOperationException} is thrown.</li>
     *     <li>If the specified {@link Paintable} is {@code null}, a {@link NullPointerException} is thrown.</li>
     * </ol>
     *
     * @param paintable The {@link Paintable} object being added to this {@link OverlaySurface}.
     *
     * @return True if the specified {@link Paintable} was added successfully, false if it was not.
     */
    public boolean add(@NotNull Paintable<?, ?> paintable) {
        if (paintable instanceof PaintNode paintNode)
            return addPaintable(paintNode);
        else if (paintable instanceof PaintCommand paintCommand)
            return foregroundCanvasPane.canvas().addPaintable(paintCommand);
        throw SLExceptions.unsupported("Unknown Paintable Type: " + paintable.getClass().getSimpleName());
    }
    
    /**
     * <p>Removes the specified {@link Paintable} instance from this {@link OverlaySurface}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the specified {@link Paintable} is an instance of {@link PaintNode}, the {@link Paintable} is {@link #addPaintable(Paintable) removed} directly from this {@link OverlaySurface}.</li>
     *     <li>If the specified {@link Paintable} is an instance of {@link PaintCommand}, the {@link Paintable} is {@link CanvasSurface#addPaintable(Paintable) removed} from the {@link #getForegroundCanvas() foreground canvas} of this {@link OverlaySurface}.</li>
     *     <li>If the specified {@link Paintable} is of an unknown type, an {@link UnsupportedOperationException} is thrown.</li>
     *     <li>If the specified {@link Paintable} is {@code null}, a {@link NullPointerException} is thrown.</li>
     * </ol>
     *
     * @param paintable The {@link Paintable} object being removed from this {@link OverlaySurface}.
     *
     * @return True if the specified {@link Paintable} was removed successfully, false if it was not.
     */
    public boolean remove(@NotNull Paintable<?, ?> paintable) {
        if (paintable instanceof PaintNode paintNode)
            return removePaintableV2(paintNode);
        else if (paintable instanceof PaintCommand paintCommand)
            return foregroundCanvasPane.canvas().removePaintableV2(paintCommand);
        throw SLExceptions.unsupported("Unknown Paintable Type: " + paintable.getClass().getSimpleName());
    }
}
