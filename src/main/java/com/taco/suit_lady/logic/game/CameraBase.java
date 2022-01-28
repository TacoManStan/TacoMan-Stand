package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.interfaces.Camera;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.CroppedImagePaintCommand;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An object that does not implement/extend any abstract game concept, but is rather a unique object that is assigned to a game window to define how the game is viewed.</p>
 * <p>All {@link Entity} objects should be able to generate a new {@link CameraBase} object that is bound to their location.</p>
 * <p>
 * {@link CameraBase} objects should also permit an unbounded view, in which case the {@link CameraBase} is not bound to the location of a {@link Entity}, but rather follows a pre-defined set of rules.
 *     <ul>
 *         <li>For example, an unbounded {@link CameraBase} could be used via a CameraTransition (not yet available) to pan/move/zoom/etc. to any location on the map given a set of movement instructions.</li>
 *     </ul>
 * </p>
 * <p>{@link CameraBase} objects should be able to have tints, zoom values, vision radius, etc.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The location of the {@link CameraBase} represents the <i>top left</i> of the {@link CameraBase camera's} view region, <i>not</i> the center.</li>
 * </ol>
 */
public class CameraBase
        implements SpringableWrapper, Lockable, Camera {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final IntegerProperty xLocationProperty;
    private final IntegerProperty yLocationProperty;
    private final IntegerProperty xOffsetProperty;
    private final IntegerProperty yOffsetProperty;
    
    
    private final GameViewContent content;
    private final ObjectBinding<GameMap> mapBinding;
    
    private final ImagePaintCommand mapImagePaintCommandTest;
    private final CroppedImagePaintCommand mapImagePaintCommand;
    
    //<editor-fold desc="--- BINDING FIELDS ---">
    
    private final IntegerBinding viewportWidthBinding; //Bound to the specified observable value representing the width in pixels of the UI viewport object (e.g., CanvasSurface).
    private final IntegerBinding viewportHeightBinding; //Bound to the specified observable value representing the height in pixels of the UI viewport object (e.g., CanvasSurface).
    
    
    private final ObjectBinding<Image> mapImageBinding; //Bound to the actual raw game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    
    private final IntegerBinding mapImageWidthBinding; //Bound via recursive binding to the width of the actual game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    private final IntegerBinding mapImageHeightBinding; //Ditto
    
    
    private final DoubleBinding xMultiplierBinding; //Multiplier representing the scaling ratio between the map image width and the map data object width
    private final DoubleBinding yMultiplierBinding; //Multiplier representing the scaling ratio between the map image height and the map data object height
    
    //</editor-fold>
    
    public CameraBase(@NotNull GameViewContent content) {
        this.springable = content.asStrict();
        this.lock = content.getLock();
        
        //
        
        this.content = content;
        this.mapBinding = BindingsSL.directObjBinding(content.mapProperty());
        
        this.mapImagePaintCommandTest = new ImagePaintCommand(this, lock).init();
        this.mapImagePaintCommand = new CroppedImagePaintCommand(this, lock).init();
        
        //
    
        this.viewportWidthBinding = BindingsSL.directIntBinding(getContent().getController().getMapPane().widthProperty());
        this.viewportHeightBinding = BindingsSL.directIntBinding(getContent().getController().getMapPane().heightProperty());
    
    
        this.xLocationProperty = new SimpleIntegerProperty(0);
        this.yLocationProperty = new SimpleIntegerProperty(0);
        
        this.xOffsetProperty = new SimpleIntegerProperty(0);
        this.yOffsetProperty = new SimpleIntegerProperty(0);
        
        //
        
        this.mapImageBinding = BindingsSL.directObjBinding(getMap().getModel().mapImageBinding());
        
        this.mapImageWidthBinding = BindingsSL.recursiveIntBinding(lock, image -> image.widthProperty(), mapImageBinding);
        this.mapImageHeightBinding = BindingsSL.recursiveIntBinding(lock, image -> image.heightProperty(), mapImageBinding);
        
        
        this.xMultiplierBinding = BindingsSL.doubleBinding(() -> ((double) getMapImageWidth() / (double) getMapWidth()), mapImageWidthBinding);
        this.yMultiplierBinding = BindingsSL.doubleBinding(() -> ((double) getMapImageHeight() / (double) getMapHeight()), mapImageHeightBinding);
        
        //
        
        initPainting();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void initPainting() {
        mapImagePaintCommand.imageProperty().bind(getMap().getModel().mapImageBinding());
        
        
        mapImagePaintCommand.boundsBinding().widthProperty().bind(viewportWidthBinding);
        mapImagePaintCommand.boundsBinding().heightProperty().bind(viewportHeightBinding);
        
        mapImagePaintCommand.boundsBinding().xProperty().bind(xLocationProperty);
        mapImagePaintCommand.boundsBinding().yProperty().bind(yLocationProperty);
        
        
        mapImagePaintCommand.xScaleProperty().bind(xMultiplierBinding);
        mapImagePaintCommand.yScaleProperty().bind(yMultiplierBinding);
        
        
        getMap().getModel().getCanvas().addPaintable(mapImagePaintCommand);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameViewContent getContent() { return content; }
    
    public final ObjectBinding<GameMap> mapBinding() { return mapBinding; }
    public final GameMap getMap() { return mapBinding.get(); }
    
    public final CroppedImagePaintCommand getPaintCommand() { return mapImagePaintCommand; }
    
    //<editor-fold desc="> Coordinates">
    
    /**
     * <p>Defines the {@code x} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty xLocationProperty() { return xLocationProperty; }
    
    /**
     * <p>Defines the {@code y} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty yLocationProperty() { return yLocationProperty; }
    
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getOffsetX() X Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty xOffsetProperty() { return xOffsetProperty; }
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getOffsetY() Y Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty yOffsetProperty() { return yOffsetProperty; }
    
    //</editor-fold>
    
    //<editor-fold desc="> Bindings">
    
    public final IntegerBinding viewportWidthBinding() { return viewportWidthBinding; }
    
    public final IntegerBinding viewportHeightBinding() { return viewportHeightBinding; }
    
    //
    
    public final ObjectBinding<Image> mapImageBinding() { return mapImageBinding; }
    
    
    public final IntegerBinding mapImageWidthBinding() { return mapImageWidthBinding; }
    
    public final IntegerBinding mapImageHeightBinding() { return mapImageHeightBinding; }
    
    //
    
    public final DoubleBinding xMultiplierBinding() { return xMultiplierBinding; }
    public final double getXMultiplier() { return xMultiplierBinding.get(); }
    
    public final DoubleBinding yMultiplierBinding() { return yMultiplierBinding; }
    public final double getYMultiplier() { return yMultiplierBinding.get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
}
