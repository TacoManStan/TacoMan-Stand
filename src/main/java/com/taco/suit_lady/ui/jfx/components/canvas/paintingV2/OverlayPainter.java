package com.taco.suit_lady.ui.jfx.components.canvas.paintingV2;

import com.taco.suit_lady.ui.jfx.components.painting.OverlaySurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertyTools;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public abstract class OverlayPainter
        implements SpringableWrapper, PaintableV2<OverlayPainter, OverlaySurface> {
    
    private final PaintableData<OverlayPainter, OverlaySurface> data;
    
    private final ObjectProperty<Node> nodeProperty;
    
    public OverlayPainter(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.data = new PaintableData<>(springable, lock, this);
        
        this.nodeProperty = new SimpleObjectProperty<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ObjectProperty<Node> nodeProperty() { return nodeProperty; }
    public final Node getNode() { return nodeProperty.get(); }
    public final Node setNode(Node n) { return PropertyTools.setProperty(nodeProperty, n); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract Node refreshNode();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull PaintableData<OverlayPainter, OverlaySurface> data() { return data; }
    
    @Override public void onAdd(OverlaySurface surface) { }
    @Override public void onRemove(OverlaySurface surface) { }
    
    
    @Override public @NotNull OverlayPainter paint() {
        //TODO
        return this;
    }
    @Override public boolean isValidDimensions() {
        return false; //TODO
    }
    
    //</editor-fold>
    
    protected Node applyRefresh(@NotNull Node n) {
        n.setManaged(false);
        n.visibleProperty().bind(Bindings.not(disabledProperty()));
        
        return sync(() -> syncBounds(n));
    }
    
    protected Node syncBounds(@NotNull Node n) {
        getNode().resizeRelocate(getX(), getY(), getWidth(), getHeight());
        return n;
    }
    
    private Node refreshNodeImpl() {
        final Node n = refreshNode();
        return applyRefresh(n);
    }
}