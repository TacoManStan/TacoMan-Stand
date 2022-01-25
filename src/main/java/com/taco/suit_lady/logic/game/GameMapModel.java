package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Provides a 2D, JavaFX-based GameMap visual definition.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>In the future, all models should be abstracted such that an entirely different display technology can be used to visualize a GameMap (or other modelable objects), such as a 3D engine or web page.</li>
 * </ol>
 */
//TO-UPDATE
public class GameMapModel
        implements SpringableWrapper, Lockable {
    
    private final ReentrantLock lock;
    private final GameMap owner;
    
    //
    
    private final ObjectProperty<StackPane> parentPaneProperty;
    private final CanvasPane canvasPane;
    
    private final ObjectBinding<Image> mapImageBinding;
    
    
    //    private final ImageOverlayCommand paintCommand;
    
    public GameMapModel(@NotNull GameMap owner, @NotNull ReentrantLock lock) {
        this.lock = ExceptionsSL.nullCheck(lock, "Lock");
        this.owner = ExceptionsSL.nullCheck(owner, "GameMap Owner");
        
        this.parentPaneProperty = new SimpleObjectProperty<>();
        this.canvasPane = new CanvasPane(this);
        
        this.mapImageBinding = BindingsSL.constObjBinding(ResourcesSL.getDummyImage(ResourcesSL.MAP));
        
        //        this.paintCommand = new ImageOverlayCommand(lock, this, "map", null, 1);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMapModel init() {
        parentPaneProperty.addListener((observable, oldValue, newValue) -> ObjectsSL.doIfNonNull(
                () -> newValue, value -> {
                    ToolsFX.setAnchors(value);
                    refreshCanvas();
                }));
        
        setParentPane(new StackPane());
        
        //        getParentPane().setStyle("-fx-border-color: red");
        //        getCanvasPane().setStyle("-fx-border-color: blue");
        
        ImagePaintCommand paintCommand = new ImagePaintCommand(this, lock).init();
        //TODO: Configure the paint command initialization properly & bind w & h to correct values (in Camera)
        paintCommand.boundsBinding().setWidth(getOwner().getFullWidth());
        paintCommand.boundsBinding().setHeight(getOwner().getFullHeight());
        
        getCanvas().addPaintable(paintCommand);
        getCanvas().repaint();
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameMap getOwner() {
        return owner;
    }
    
    public final ObjectProperty<StackPane> parentPaneProperty() { return parentPaneProperty; }
    public final StackPane getParentPane() { return parentPaneProperty.get(); }
    public final StackPane setParentPane(StackPane newValue) { return PropertiesSL.setProperty(parentPaneProperty, newValue); }
    
    protected final CanvasPane getCanvasPane() { return canvasPane; }
    public final CanvasSurface getCanvas() { return canvasPane != null ? canvasPane.canvas() : null; }
    
    
    public final ObjectBinding<Image> mapImageBinding() { return mapImageBinding; }
    public final Image getMapImage() { return mapImageBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
    
    /**
     * <p>A helper method that fully resets the JFX UI elements comprising the map image and parent pane.</p>
     */
    private void refreshCanvas() {
        syncFX(() -> {
            StackPane parent = getParentPane();
            if (parent != null) {
                parent.getChildren().retainAll();
                parent.getChildren().add(canvasPane);
            }
        });
    }
}
