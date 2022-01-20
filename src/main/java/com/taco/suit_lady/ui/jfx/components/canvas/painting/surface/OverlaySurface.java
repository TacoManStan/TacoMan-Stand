package com.taco.suit_lady.ui.jfx.components.canvas.painting.surface;

import com.taco.suit_lady._to_sort._new.interfaces.ReadOnlyNameableProperty;
import com.taco.suit_lady.ui.jfx.components.canvas.painting.OverlayPainter;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ExceptionTools;
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
        implements SpringableWrapper, Lockable, ReadOnlyNameableProperty, Comparable<OverlaySurface>, Surface<OverlayPainter, OverlaySurface> {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyStringWrapper nameProperty;
    
    private final StackPane root;
    
    private final ReadOnlyIntegerWrapper paintPriorityProperty;
    
    private final SurfaceData<OverlayPainter, OverlaySurface> data;
    
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
     * <p>Constructs a new {@link OverlaySurface} instance used to display a set of {@link OverlayPainter Paint Commands}.</p>
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
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock; // Null-checking is done in get method via lazy instantiation
        
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.root = new StackPane();
        this.root.setAlignment(Pos.TOP_LEFT);
        
        this.paintPriorityProperty = new ReadOnlyIntegerWrapper(paintPriority);
        
        this.data = new SurfaceData<>(springable, lock, this, root.widthProperty(), root.heightProperty());
        
        //
        
        //TODO
        ListTools.applyListener(lock, paintablesV2(), (op1, op2, opType, triggerType) -> {
            root.getChildren().retainAll();
            paintablesV2().forEach(paintable -> root.getChildren().add(paintable.getNode()));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link StackPane} instance on which all {@link OverlayPainter Paint Commands} assigned to this {@link OverlaySurface} are displayed.</p>
     *
     * @return The {@link StackPane} instance on which all {@link OverlayPainter Paint Commands} assigned to this {@link OverlaySurface} are displayed.
     */
    protected final StackPane root() {
        return root;
    }
    
    //
    
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
    public final ReadOnlyIntegerProperty paintPriorityProperty() {
        return paintPriorityProperty.getReadOnlyProperty();
    }
    
    /**
     * <p><b>Passthrough Definition:</b></p>
     * <blockquote><i><code>{@link #paintPriorityProperty()}<b>.</b>{@link ReadOnlyIntegerProperty#get() get()}</code></i></blockquote>
     */
    public final int getPaintPriority() {
        return paintPriorityProperty.get();
    }
    
    /**
     * <p><b>Passthrough Definition:</b></p>
     * <blockquote><i><code>{@link #paintPriorityProperty}<b>.</b>{@link ReadOnlyIntegerWrapper#set(int) set}<b>(</b>paintPriority<b>)</b></code></i></blockquote>
     */
    public final void setPaintPriority(int paintPriority) {
        if (paintPriority <= 0)
            throw ExceptionTools.ex(new IndexOutOfBoundsException("Paint Priority Must Be Greater Than Zero! [" + paintPriority + "]"));
        paintPriorityProperty.set(paintPriority);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull SurfaceData<OverlayPainter, OverlaySurface> data() { return data; }
    
    @Override public @NotNull OverlaySurface repaint() {
        //TODO
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
}
