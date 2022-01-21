package com.taco.suit_lady.ui.jfx.components.painting.paintables.overlay;

import com.taco.suit_lady.ui.jfx.components.painting.paintables.Paintable;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.PaintableData;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.OverlaySurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class OverlayPaintNode
        implements SpringableWrapper, Paintable<OverlayPaintNode, OverlaySurface> {
    
    private final PaintableData<OverlayPaintNode, OverlaySurface> data;
    
    private final ReadOnlyObjectWrapper<Node> nodeProperty;
    
    public OverlayPaintNode(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableData<>(springable, lock, this);
        
        this.nodeProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<Node> nodeProperty() { return nodeProperty.getReadOnlyProperty(); }
    public Node getNode() { return nodeProperty.get(); }
    public Node getAndRefreshNode() {
        return sync(() -> {
            Node n = getNode();
            if (n != null)
                return n;
            nodeProperty.set(refreshNodeImpl());
            return getNode();
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract Node refreshNode();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableData<OverlayPaintNode, OverlaySurface> data() { return data; }
    
    @Override public void onAdd(OverlaySurface surface) { }
    @Override public void onRemove(OverlaySurface surface) { }
    
    
    @Override public @NotNull OverlayPaintNode paint() {
        refreshNodeImpl();
        return this;
    }
    
    //</editor-fold>
    
    protected Node applyRefresh(@NotNull Node n) {
        n.setManaged(false);
        n.visibleProperty().bind(Bindings.not(disabledProperty()));
        
        return sync(() -> syncBounds(n));
    }
    
    protected Node syncBounds(@NotNull Node n) {
        n.resizeRelocate(x(), y(), width(), height());
        return n;
    }
    
    private Node refreshNodeImpl() {
        final Node n = refreshNode();
        return applyRefresh(n);
    }
    
    //Currently Unused
    private boolean autoRemove() {
        return sync(() -> {
            OverlaySurface surface = getSurface();
            if (surface != null && getAutoRemoveCondition().test(surface))
                return surface.removePaintableV2(this);
            return false;
        });
    }
}