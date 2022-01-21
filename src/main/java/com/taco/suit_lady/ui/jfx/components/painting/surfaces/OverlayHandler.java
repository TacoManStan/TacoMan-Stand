package com.taco.suit_lady.ui.jfx.components.painting.surfaces;

import com.taco.suit_lady._to_sort._new.ReadOnlyObservableList;
import com.taco.suit_lady._to_sort._new.ReadOnlyObservableListWrapper;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

// TO-DOC
public class OverlayHandler
        implements Springable, Lockable {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyObservableListWrapper<OverlaySurface> overlays;
    
    private final StackPane root;
    
    /**
     * <p>Constructs a new {@link OverlayHandler} instance with optional initial {@link OverlaySurface} contents.</p>
     *
     * @param springable      The {@link Springable} instance used to configure this {@link OverlayHandler} as a {@link Springable}.
     * @param lock            The {@link ReentrantLock} used to {@code synchronize} applicable actions performed by this {@link OverlayHandler}.
     * @param initialOverlays The vararg {@code array} of {@link OverlaySurface Overlays} to be added to this {@link OverlayHandler} upon its construction.
     */
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable OverlaySurface... initialOverlays) {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input");
        this.lock = lock;
        
        this.overlays = new ReadOnlyObservableListWrapper<>(initInitialOverlayList(initialOverlays));
        
        this.root = FXTools.togglePickOnBounds(new StackPane(), false);
        
        
        //        ListTools.applyListener(lock, overlays, (op1, op2, opType, triggerType)
        //                -> debugger().print("Operation Event Triggered:  [" + op1 + "  |  " + op2 + "  |  " + opType + "  |  " + triggerType + "]"));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes the {@link ReadOnlyObservableListWrapper} containing the {@link OverlaySurface Overlays} managed by this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The specified {@code array} can be both {@code null} or {@code empty}, in which case this {@link OverlayHandler} will be {@code empty} upon construction.</li>
     *     <li>The {@code array} is defined as a {@code parameter} passed to the {@link OverlayHandler} {@link OverlayHandler#OverlayHandler(Springable, ReentrantLock, OverlaySurface...) constructor}.</li>
     * </ol>
     *
     * @param initialOverlays An {@code array} containing any {@link OverlaySurface Overlays} to be added to this {@link OverlayHandler} upon its construction.
     *
     * @return The newly-initialized {@link ObservableList} to be used as the {@code backing list} for the {@link ReadOnlyObservableListWrapper} containing the {@link OverlaySurface Overlays} managed by this {@link OverlayHandler}.
     */
    private @NotNull ObservableList<OverlaySurface> initInitialOverlayList(@Nullable OverlaySurface[] initialOverlays) {
        return initialOverlays != null ? FXCollections.observableArrayList(initialOverlays) : FXCollections.observableArrayList();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link StackPane} used as the {@code root} for displaying all {@link OverlaySurface Overlays} added to this {@link OverlayHandler}.</p>
     *
     * @return The {@link StackPane} used as the {@code root} for displaying all {@link OverlaySurface Overlays} added to this {@link OverlayHandler}.
     */
    public final StackPane root() {
        return root;
    }
    
    //<editor-fold desc="--- OVERLAYS ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObservableList} containing the {@link OverlaySurface Overlays} that have been {@link #addOverlay(OverlaySurface) added} to this {@link OverlayHandler}.</p>
     *
     * @return The {@link ReadOnlyObservableList} containing the {@link OverlaySurface Overlays} that have been {@link #addOverlay(OverlaySurface) added} to this {@link OverlayHandler}.
     */
    public final ReadOnlyObservableList<OverlaySurface> overlays() {
        return overlays.readOnlyList();
    }
    
    /**
     * <p>Adds the specified {@link OverlaySurface} to this {@link OverlayHandler} and then {@link #resort(Runnable) resorts} its contents.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param overlay The {@link OverlaySurface} being added.
     */
    public final void addOverlay(@NotNull OverlaySurface overlay) {
        resort(() -> overlays.add(overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#add(int, Comparable) Inserts} the specified {@link OverlaySurface} into this {@link OverlayHandler} at the specified {@code index}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param index   The {@code index} at which the specified {@link OverlaySurface} is to be {@link ReadOnlyObservableListWrapper#add(int, Comparable) inserted}.
     * @param overlay The {@link OverlaySurface} to be {@link ReadOnlyObservableListWrapper#add(int, Comparable) inserted} into this {@link OverlayHandler}.
     *
     * @throws IndexOutOfBoundsException If the specified {@code index} is invalid: <u>{@code index < 0}</u> or <u>{@code index > size()}</u>.
     */
    public final void addOverlayAt(int index, @NotNull OverlaySurface overlay) {
        resort(() -> overlays.add(index, overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#remove(Object) Removes} the specified {@link OverlaySurface} from this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param overlay The {@link OverlaySurface} to be {@link ReadOnlyObservableListWrapper#remove(Object) removed}.
     */
    public final void removeOverlay(@NotNull OverlaySurface overlay) {
        resort(() -> overlays.remove(overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#remove(int) Removes} the {@link OverlaySurface} located at the specified {@code index} from this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param index The {@code index} at which to remove the {@link OverlaySurface} from.
     */
    public final void removeOverlayAt(int index) {
        resort(() -> overlays.remove(index));
    }
    
    /**
     * <p>
     * Returns the {@link OverlaySurface} contained within this {@link OverlayHandler} matching the specified {@link OverlaySurface#nameProperty() name}
     * or {@code null} if no such {@link OverlaySurface} can be found.
     * </p>
     *
     * @param name The {@link OverlaySurface#nameProperty() name} of the {@link OverlaySurface} to be retrieved.
     *
     * @return The {@link OverlaySurface} contained within this {@link OverlayHandler} matching the specified {@link OverlaySurface#nameProperty() name}.
     */
    public final @Nullable OverlaySurface getOverlay(String name) {
        return sync(() -> {
            for (OverlaySurface overlay: overlays)
                if (name.equalsIgnoreCase(overlay.getName()))
                    return overlay;
            return null;
        });
    }
    
    /**
     * <p>Returns the default {@link OverlaySurface} contained within this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@code default} {@link OverlaySurface} will always have the {@link OverlaySurface#nameProperty() name} of {@code "default"}.</li>
     *     <li>In most circumstances — especially in applications that are minimally graphics-intensive — the {@code default} {@link OverlaySurface} is the only {@link OverlaySurface} that is required.</li>
     * </ol>
     * <p><b>Passthrough Definition:</b> <i>{@link #getOverlay(String) getOverlay}<b>(</b>"default"<b>)</b></i>.</p>
     *
     * @return The {@code default} {@link OverlaySurface} for this {@link OverlayHandler}.
     */
    public final @NotNull OverlaySurface defaultOverlay() {
        return getOverlay("default");
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    /**
     * <p>Executes the specified {@link Runnable}.</p>
     * <p>Upon {@link Runnable} completion, <i>{@link #refreshOverlays()}}</i> is called.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Both of the aforementioned actions are performed in the same {@code synchronization} block.</li>
     *     <li>If a return value is required, refer to <i>{@link #resort(Supplier)}</i>.</li>
     * </ol>
     *
     * @param runnable The {@link Runnable} to be executed.
     */
    private void resort(Runnable runnable) {
        sync(() -> {
            runnable.run();
            refreshOverlays();
        });
    }
    
    /**
     * <p>Executes the specified {@link Supplier}.</p>
     * <p>Upon completion, <i>{@link #refreshOverlays()}</i> is called.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Both of the aforementioned actions are performed in the same {@code synchronization} block.</li>
     *     <li>If no return value is required, refer to <i>{@link #resort(Runnable)}</i>.</li>
     * </ol>
     *
     * @param supplier The {@link Supplier} to be executed and returned.
     * @param <T>      The type of value returned by the specified {@link Supplier}.
     *
     * @return The value returned by the specified {@link Supplier}.
     */
    private <T> T resort(Supplier<T> supplier) {
        return sync(() -> {
            final T t = supplier.get();
            refreshOverlays();
            return t;
        });
    }
    
    /**
     * <p>Refreshes the contents of all {@link OverlaySurface Overlays} contained within this {@link OverlayHandler}.</p>
     * <p><b>Execution Steps</b></p>
     * <ol>
     *     <li>{@link FXCollections#sort(ObservableList) Sorts} the {@link OverlaySurface Overlays} contained within this {@link OverlayHandler}.</li>
     *     <li>
     *         <b>Executed via the {@link FXTools#runFX(Runnable, boolean) JavaFX Thread}</b>
     *         <ol>
     *             <li>Clears the {@link StackPane#getChildren() contents} of the {@link #root() Root Pane} for this {@link OverlayHandler}.</li>
     *             <li>
     *                 <b>For each {@link OverlaySurface} contained within this {@link OverlayHandler}</b>
     *                 <ol>
     *                     <li>{@link FXTools#bindToParent(Region, Region, boolean) Binds} the {@link OverlaySurface} to the {@link #root() Root Pane} for this {@link OverlayHandler}.</li>
     *                 </ol>
     *             </li>
     *             <li>{@link FXTools#togglePickOnBounds(Node, boolean) Toggles} {@link Pane#pickOnBoundsProperty() Pick on Bounds} for the {@link #root() Root Pane} to {@code false}.</li>
     *         </ol>
     *     </li>
     *     <li>Waits for completion.</li>
     * </ol>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within {@link #refreshOverlays() this method} are executed in the same {@code synchronization} block.</li>
     * </ol>
     */
    private void refreshOverlays() {
        sync(() -> {
            FXCollections.sort(overlays);
            FXTools.runFX(() -> {
                root().getChildren().retainAll();
                for (OverlaySurface overlay: overlays) {
                    FXTools.bindToParent(overlay.getRoot(), root(), true);
                    FXTools.togglePickOnBounds(overlay.getRoot(), false);
                }
            }, true);
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public @NotNull Lock getLock() {
        return lock != null ? lock : new ReentrantLock();
    }
    
    //</editor-fold>
}
