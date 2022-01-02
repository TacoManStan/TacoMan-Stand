package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady._to_sort._new.ReadOnlyObservableList;
import com.taco.suit_lady._to_sort._new.ReadOnlyObservableListWrapper;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.util.tools.list_tools.ListTools;
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

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class OverlayHandler
        implements Springable, Lockable {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyObservableListWrapper<Overlay> overlays;
    
    private final StackPane root;
    
    /**
     * <p>Constructs a new {@link OverlayHandler} instance with optional initial {@link Overlay} contents.</p>
     *
     * @param springable      The {@link Springable} instance used to configure this {@link OverlayHandler} as a {@link Springable}.
     * @param lock            The {@link ReentrantLock} used to {@code synchronize} applicable actions performed by this {@link OverlayHandler}.
     * @param initialOverlays The vararg {@code array} of {@link Overlay Overlays} to be added to this {@link OverlayHandler} upon its construction.
     */
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable Overlay... initialOverlays) {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input");
        this.lock = lock;
        
        this.overlays = new ReadOnlyObservableListWrapper<>(initInitialOverlayList(initialOverlays));
        
        this.root = FXTools.togglePickOnBounds(new StackPane(), false);
        
        
        //        ListTools.applyListener(lock, overlays, (op1, op2, opType, triggerType)
        //                -> debugger().print("Operation Event Triggered:  [" + op1 + "  |  " + op2 + "  |  " + opType + "  |  " + triggerType + "]"));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private @NotNull ObservableList<Overlay> initInitialOverlayList(@Nullable Overlay[] initialOverlays) {
        return initialOverlays != null ? FXCollections.observableArrayList(initialOverlays) : FXCollections.observableArrayList();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link StackPane} used as the {@code root} for displaying all {@link Overlay Overlays} added to this {@link OverlayHandler}.</p>
     *
     * @return The {@link StackPane} used as the {@code root} for displaying all {@link Overlay Overlays} added to this {@link OverlayHandler}.
     */
    public final StackPane root() {
        return root;
    }
    
    //<editor-fold desc="--- OVERLAYS ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObservableList} containing the {@link Overlay Overlays} that have been {@link #addOverlay(Overlay) added} to this {@link OverlayHandler}.</p>
     *
     * @return The {@link ReadOnlyObservableList} containing the {@link Overlay Overlays} that have been {@link #addOverlay(Overlay) added} to this {@link OverlayHandler}.
     */
    public final ReadOnlyObservableList<Overlay> overlays() {
        return overlays.readOnlyList();
    }
    
    /**
     * <p>Adds the specified {@link Overlay} to this {@link OverlayHandler} and then {@link #resort(Runnable) resorts} its contents.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param overlay The {@link Overlay} being added.
     */
    public final void addOverlay(@NotNull Overlay overlay) {
        resort(() -> overlays.add(overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#add(int, Comparable) Inserts} the specified {@link Overlay} into this {@link OverlayHandler} at the specified {@code index}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param index   The {@code index} at which the specified {@link Overlay} is to be {@link ReadOnlyObservableListWrapper#add(int, Comparable) inserted}.
     * @param overlay The {@link Overlay} to be {@link ReadOnlyObservableListWrapper#add(int, Comparable) inserted} into this {@link OverlayHandler}.
     *
     * @throws IndexOutOfBoundsException If the specified {@code index} is invalid: <u>{@code index < 0}</u> or <u>{@code index > size()}</u>.
     */
    public final void addOverlayAt(int index, @NotNull Overlay overlay) {
        resort(() -> overlays.add(index, overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#remove(Object) Removes} the specified {@link Overlay} from this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param overlay The {@link Overlay} to be {@link ReadOnlyObservableListWrapper#remove(Object) removed}.
     */
    public final void removeOverlay(@NotNull Overlay overlay) {
        resort(() -> overlays.remove(overlay));
    }
    
    /**
     * <p>{@link ReadOnlyObservableListWrapper#remove(int) Removes} the {@link Overlay} located at the specified {@code index} from this {@link OverlayHandler}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>All operations contained within this method are executed via the <i>{@link #resort(Runnable)}</i> utility method.</li>
     * </ol>
     *
     * @param index The {@code index} at which to remove the {@link Overlay} from.
     */
    public final void removeOverlayAt(int index) {
        resort(() -> overlays.remove(index));
    }
    
    /**
     * <p>
     * Returns the {@link Overlay} contained within this {@link OverlayHandler} matching the specified {@link Overlay#nameProperty() name}
     * or {@code null} if no such {@link Overlay} can be found.
     * </p>
     *
     * @param name The {@link Overlay#nameProperty() name} of the {@link Overlay} to be retrieved.
     *
     * @return The {@link Overlay} contained within this {@link OverlayHandler} matching the specified {@link Overlay#nameProperty() name}.
     */
    public final @Nullable Overlay getOverlay(String name) {
        return sync(() -> {
            for (Overlay overlay: overlays)
                if (name.equalsIgnoreCase(overlay.getName()))
                    return overlay;
            return null;
        });
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
     * <p>Refreshes the contents of all {@link Overlay Overlays} contained within this {@link OverlayHandler}.</p>
     * <p><b>Execution Steps</b></p>
     * <ol>
     *     <li>{@link FXCollections#sort(ObservableList) Sorts} the {@link Overlay Overlays} contained within this {@link OverlayHandler}.</li>
     *     <li>
     *         <b>Executed via the {@link FXTools#runFX(Runnable, boolean) JavaFX Thread}</b>
     *         <ol>
     *             <li>Clears the {@link StackPane#getChildren() contents} of the {@link #root() Root Pane} for this {@link OverlayHandler}.</li>
     *             <li>
     *                 <b>For each {@link Overlay} contained within this {@link OverlayHandler}</b>
     *                 <ol>
     *                     <li>{@link FXTools#bindToParent(Region, Region, boolean) Binds} the {@link Overlay} to the {@link #root() Root Pane} for this {@link OverlayHandler}.</li>
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
                for (Overlay overlay: overlays) {
                    FXTools.bindToParent(overlay.root(), root(), true);
                    FXTools.togglePickOnBounds(overlay.root(), false);
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
