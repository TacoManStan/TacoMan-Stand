package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.CroppedImagePaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final ReentrantLock lock;
    private final GameViewContent content;
    
    //
    
    private final Camera camera;
    
    private final ObjectProperty<Image> mapImageProperty;
    
    //
    
    private final ObjectProperty<StackPane> parentPaneProperty;
    private final CanvasPane canvasPane;
    
    private final CroppedImagePaintCommand mapImagePaintCommand;
    
    public GameMapModel(@NotNull GameViewContent content, @NotNull ReentrantLock lock) {
        this.lock = ExceptionsSL.nullCheck(lock, "Lock");
        this.content = content;
        
        //
        
        this.camera = new Camera(getGame());
        
        //
        
        this.parentPaneProperty = new SimpleObjectProperty<>();
        this.canvasPane = new CanvasPane(this);
        
        this.mapImageProperty = new SimpleObjectProperty<>(ResourcesSL.getGameImage("map"));
        this.mapImagePaintCommand = new CroppedImagePaintCommand(this, lock);
        this.mapImagePaintCommand.setPaintPriority(5);
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
        
        //        CroppedImagePaintCommand paintCommand = new CroppedImagePaintCommand(this, lock).init();
        //        //TODO: Configure the paint command initialization properly & bind w & h to correct values (in Camera)
        //        paintCommand.croppingBoundsBinding().setWidth(getOwner().get)
        //        paintCommand.croppingBoundsBinding().setHeight(getOwner().getFullHeight());
        //
        //        getCanvas().addPaintable(paintCommand);
        //        getCanvas().repaint();
        
        getCamera().init();
        
        mapImagePaintCommand.init();
        initPaintCommand();
        
        return this;
    }
    
    private void initPaintCommand() {
        camera.xOffsetProperty().bind(BindingsSL.intBinding(() -> -Math.round(camera.getViewportWidth() / 2d), camera.viewportWidthBinding()));
        camera.yOffsetProperty().bind(BindingsSL.intBinding(() -> -Math.round(camera.getViewportHeight() / 2d), camera.viewportHeightBinding()));
        
        mapImagePaintCommand.imageProperty().bind(getGameMap().getModel().mapImageProperty());
        
        
        mapImagePaintCommand.boundsBinding().widthProperty().bind(camera.viewportWidthBinding());
        mapImagePaintCommand.boundsBinding().heightProperty().bind(camera.viewportHeightBinding());
        
        mapImagePaintCommand.boundsBinding().xProperty().bind(camera.xAggregateBinding());
        mapImagePaintCommand.boundsBinding().yProperty().bind(camera.yAggregateBinding());
        
        
        mapImagePaintCommand.xScaleProperty().bind(camera.xMultiplierBinding());
        mapImagePaintCommand.yScaleProperty().bind(camera.yMultiplierBinding());
        
        
        getCanvas().addPaintable(mapImagePaintCommand);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final Camera getCamera() { return camera; }
    
    
    public final ObjectProperty<StackPane> parentPaneProperty() { return parentPaneProperty; }
    public final StackPane getParentPane() { return parentPaneProperty.get(); }
    public final StackPane setParentPane(StackPane newValue) { return PropertiesSL.setProperty(parentPaneProperty, newValue); }
    
    protected final CanvasPane getCanvasPane() { return canvasPane; }
    @Contract(pure = true) public final @Nullable CanvasSurface getCanvas() { return canvasPane != null ? canvasPane.canvas() : null; }
    
    public final ObjectProperty<Image> mapImageProperty() { return mapImageProperty; }
    public final Image getMapImage() { return mapImageProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
    
    /**
     * <p>A helper method that fully resets the JFX UI elements comprising the map image and parent pane.</p>
     */
    private void refreshCanvas() {
        ToolsFX.runFX(() -> {
            StackPane parent = getParentPane();
            if (parent != null) {
                parent.getChildren().retainAll();
                parent.getChildren().add(canvasPane);
            }
        }, true);
    }
}
