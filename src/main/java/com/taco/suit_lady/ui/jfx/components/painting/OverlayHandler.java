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
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class OverlayHandler
        implements Springable, Lockable {
    
    private final Springable springable;
    private final ReentrantLock lock;
    
    private final ReadOnlyObservableListWrapper<Overlay> overlays;
    
    private final StackPane root;
    
    public OverlayHandler(@NotNull Springable springable, @Nullable ReentrantLock lock, @Nullable Overlay... initialOverlays) {
        this.springable = ExceptionTools.nullCheck(springable, "Springable Input");
        this.lock = lock;
        
        this.overlays = new ReadOnlyObservableListWrapper<>(initInitialOverlayList(initialOverlays));
        
        this.root = new StackPane();
        FXTools.togglePickOnBounds(root, false);
        
        ListTools.applyListener(lock, overlays, (op1, op2, opType, triggerType)
                -> debugger().print("Operation Event Triggered:  [" + op1 + "  |  " + op2 + "  |  " + opType + "  |  " + triggerType + "]"));
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private @NotNull ObservableList<Overlay> initInitialOverlayList(@Nullable Overlay[] initialOverlays) {
        return initialOverlays != null ? FXCollections.observableArrayList(initialOverlays) : FXCollections.observableArrayList();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final StackPane root() {
        return root;
    }
    
    //<editor-fold desc="--- OVERLAYS ---">
    
    public final ReadOnlyObservableList<Overlay> overlays() {
        return overlays.readOnlyList();
    }
    
    
    public final void addOverlay(@NotNull Overlay overlay) {
        resortSync(() -> overlays.add(overlay));
    }
    
    public final void addOverlayAt(int index, @NotNull Overlay overlay) {
        resortSync(() -> overlays.add(index, overlay));
    }
    
    public final void removeOverlay(@NotNull Overlay overlay) {
        resortSync(() -> overlays.remove(overlay));
    }
    
    public final void removeOverlayAt(int index) {
        resortSync(() -> overlays.remove(index));
    }
    
    public final Overlay getOverlay(String name) {
        return sync(() -> {
            for (Overlay overlay: overlays)
                if (name.equalsIgnoreCase(overlay.getName()))
                    return overlay;
            return null;
        });
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    private void resortSync(Runnable runnable) {
        sync(() -> {
            runnable.run();
            refreshOverlays();
        });
    }
    
    private <T> T resortSync(Supplier<T> supplier) {
        return sync(() -> {
            final T t = supplier.get();
            refreshOverlays();
            return t;
        });
    }
    
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
