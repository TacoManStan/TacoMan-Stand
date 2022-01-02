package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady._to_sort._new.ReadOnlyObservableList;
import com.taco.suit_lady._to_sort._new.ReadOnlyObservableListWrapper;
import com.taco.suit_lady._to_sort._new.interfaces.ReadOnlyNameableProperty;
import com.taco.suit_lady.ui.*;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.list_tools.ListTools;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TO-DOC
public class Overlay
        implements Springable, Lockable, ReadOnlyNameableProperty, Comparable<Overlay> {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyStringWrapper nameProperty;
    
    private final StackPane root;
    
    private final ReadOnlyIntegerWrapper paintPriorityProperty;
    private final ReadOnlyObservableListWrapper<SLPaintCommand<?>> paintCommands;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    // TO-DOC
    public Overlay(@NotNull Springable springable) {
        this(springable, null, null, 1);
    }
    
    // TO-DOC
    public Overlay(@NotNull Springable springable, @Nullable String name) {
        this(springable, null, name, 1);
    }
    
    // TO-DOC
    public Overlay(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this(springable, lock, null, 1);
    }
    
    // TO-DOC
    public Overlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name) {
        this(springable, lock, name, 1);
    }
    
    
    /**
     * <p><b>Fully-Parameterized Constructor</b></p>
     * <p>Constructs a new {@link Overlay} instance used to display a set of {@link SLPaintCommand Paint Commands}.</p>
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
     * @param name          The {@link String name} of this {@link Overlay}.
     * @param paintPriority The priority of this {@link Overlay}.
     */
    public Overlay(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable String name, int paintPriority) {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input").asStrict();
        this.lock = lock; // Null-checking is done in get method via lazy instantiation
        this.nameProperty = new ReadOnlyStringWrapper(name);
        
        this.root = new StackPane();
        this.root.setAlignment(Pos.TOP_LEFT);
        
        this.paintPriorityProperty = new ReadOnlyIntegerWrapper(paintPriority);
        
        this.paintCommands = new ReadOnlyObservableListWrapper<>();
        
        ListTools.applyListener(lock, paintCommands, (op1, op2, opType, triggerType) -> {
            root.getChildren().retainAll();
            paintCommands.forEach(paintCommand -> root.getChildren().add(paintCommand.getNode()));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link StackPane} instance on which all {@link SLPaintCommand Paint Commands} assigned to this {@link Overlay} are displayed.</p>
     *
     * @return The {@link StackPane} instance on which all {@link SLPaintCommand Paint Commands} assigned to this {@link Overlay} are displayed.
     */
    protected final StackPane root() {
        return root;
    }
    
    //
    
    /**
     * <p>Returns the {@link ReadOnlyIntegerProperty} representing the priority at which this {@link Overlay} is to be painted.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link #paintPriorityProperty() paint priority} is used by the {@link OverlayHandler} to overlap its {@link OverlayHandler#overlays() overlays} based on their {@link #getPaintPriority() priorities}..</li>
     *     <li>The {@link #getPaintPriority() paint priority} is an {@code int} with acceptable bounds of {@code 1} to {@link Integer#MAX_VALUE}.</li>
     *     <li>
     *         {@link Overlay Overlays} with larger {@link #getPaintPriority() paint priority} values are painted {@code first}.
     *         Subsequent {@link Overlay Overlays} are then painted on top, with {@link Overlay Overlays} of a {@link #getPaintPriority() paint priority} of {@code 1} being painted last (on top).
     *     </li>
     * </ol>
     *
     * @return The {@link ReadOnlyIntegerProperty} representing the priority at which this {@link Overlay} is to be painted.
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
    
    
    /**
     * <p>Returns the {@link ReadOnlyObservableList} containing the {@link SLPaintCommand Paint Commands} assigned to this {@link Overlay}.</p>
     *
     * @return The {@link ReadOnlyObservableList} containing the {@link SLPaintCommand Paint Commands} assigned to this {@link Overlay}.
     */
    public final ReadOnlyObservableList<SLPaintCommand<?>> paintCommands() {
        return paintCommands.readOnlyList();
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#add(Object) Adds} the specified {@link SLPaintCommand} to this {@link Overlay}.</p>
     *
     * @param paintCommand The {@link SLPaintCommand} to be {@link ReadOnlyObservableListWrapper#add(Object) added}.
     */
    public final void addPaintCommand(@NotNull SLPaintCommand<?> paintCommand) {
        sync(() -> {
            ExceptionTools.nullCheck(paintCommand, "Paint Command Input").setOwner(this); // TODO: Move to listener & also track remove events
            paintCommands.add(paintCommand);
            FXCollections.sort(paintCommands);
            debugger().printList(paintCommands, "Paint Commands (Added)");
        });
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#remove(Object) Removes} the specified {@link SLPaintCommand} to this {@link Overlay}.</p>
     *
     * @param paintCommand The {@link SLPaintCommand} to be {@link ReadOnlyObservableListWrapper#remove(Object) removed}.
     */
    public final boolean removePaintCommand(@NotNull SLPaintCommand<?> paintCommand) {
        ExceptionTools.nullCheck(paintCommand, "Paint Command Input");
        return sync(() -> {
            paintCommands.remove(paintCommand);
            debugger().printList(paintCommands, "Paint Commands (Removed)");
            return !paintCommands.contains(paintCommand);
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public final @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public final @NotNull Lock getLock() {
        return lock != null ? lock : new ReentrantLock();
    }
    
    
    @Override
    public final ReadOnlyStringProperty nameProperty() {
        return nameProperty.getReadOnlyProperty();
    }
    
    //
    
    @Override
    public int compareTo(@NotNull Overlay o) {
        return Integer.compare((Math.abs(getPaintPriority())), Math.abs(o.getPaintPriority()));
    }
    
    //</editor-fold>
}
