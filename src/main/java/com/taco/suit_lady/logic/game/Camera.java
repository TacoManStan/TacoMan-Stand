package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.SLBindings;
import com.taco.suit_lady.util.tools.SLProperties;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 * <p>An object that does not implement/extend any abstract game concept, but is rather a unique object that is assigned to a game window to define how the game is viewed.</p>
 * <p>All {@link Entity} objects should be able to generate a new {@link Camera} object that is bound to their location.</p>
 * <p>
 * {@link Camera} objects should also permit an unbounded view, in which case the {@link Camera} is not bound to the location of a {@link Entity}, but rather follows a pre-defined set of rules.
 *     <ul>
 *         <li>For example, an unbounded {@link Camera} could be used via a CameraTransition (not yet available) to pan/move/zoom/etc. to any location on the map given a set of movement instructions.</li>
 *     </ul>
 * </p>
 * <p>{@link Camera} objects should be able to have tints, zoom values, vision radius, etc.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The location of the {@link Camera} represents the <i>center</i> of the {@link Camera camera's} view region, <i>not</i> the top-left corner as is the case with most {@code rectangle-based} implementations.</li>
 * </ol>
 */
public class Camera
        implements SpringableWrapper {
    
    private final StrictSpringable springable;
    
    //
    
    private final IntegerBinding viewportWidthBinding; //Bound to the specified observable value representing the width in pixels of the UI viewport object (e.g., CanvasSurface).
    private final IntegerBinding viewportHeightBinding; //Bound to the specified observable value representing the height in pixels of the UI viewport object (e.g., CanvasSurface).
    
    private final IntegerProperty xLocationProperty;
    private final IntegerProperty yLocationProperty;
    private final IntegerProperty xOffsetProperty;
    private final IntegerProperty yOffsetProperty;
    
    private final GameMap gameMap;
    
    //
    
    private final ObjectBinding<Image> mapImageBinding; //Bound to the actual raw game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    
    private final IntegerBinding mapImageWidthBinding; //Bound via recursive binding to the width of the actual game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    private final IntegerBinding mapImageHeightBinding; //Ditto
    
    
    private final DoubleBinding xMultiplierBinding; //Multiplier representing the scaling ratio between the map image width and the map data object width
    private final DoubleBinding yMultiplierBinding; //Multiplier representing the scaling ratio between the map image height and the map data object height
    
    
    private final IntegerBinding scaledViewportWidthBinding; //The viewport width scaled to match the dimensions of the map image object
    private final IntegerBinding scaledViewportHeightBinding; //The viewport height scaled to match the dimensions of the map image object
    
    private final IntegerBinding scaledViewportXLocationBinding; //The viewport x location scaled to match the dimensions of the map image object
    private final IntegerBinding scaledViewportYLocationBinding; //The viewport y location scaled to match the dimensions of the map image object
    
    
    public Camera(@NotNull GameMap gameMap, @NotNull ObservableNumberValue observableViewportWidth, @NotNull ObservableNumberValue observableViewportHeight) {
        this.springable = gameMap.asStrict();
        
        this.viewportWidthBinding = Bindings.createIntegerBinding(() -> observableViewportWidth.intValue(), observableViewportWidth);
        this.viewportHeightBinding = Bindings.createIntegerBinding(() -> observableViewportHeight.intValue(), observableViewportHeight);
        
        this.xLocationProperty = new SimpleIntegerProperty(0);
        this.yLocationProperty = new SimpleIntegerProperty(0);
        this.xOffsetProperty = new SimpleIntegerProperty(0);
        this.yOffsetProperty = new SimpleIntegerProperty(0);
        
        this.gameMap = gameMap;
        
        //
        
        this.mapImageBinding = Bindings.createObjectBinding(() -> gameMap.getModel().getMapImage(), gameMap.getModel().mapImageBinding());
        
        this.mapImageWidthBinding = SLBindings.bindIntegerRecursive(image -> image.widthProperty(), mapImageBinding);
        this.mapImageHeightBinding = SLBindings.bindIntegerRecursive(image -> image.heightProperty(), mapImageBinding);
        
        
//        this.xMultiplierBinding = SLBindings.bindInteger(() -> );
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameMap getGameMap() { return gameMap; }
    
    //<editor-fold desc="> Bindings">
    
    public final ObjectBinding<Image> mapImageBinding() { return mapImageBinding; }
    public final Image getMapImage() { return mapImageBinding.get(); }
    
    
    public final IntegerBinding mapImageWidthBinding() { return mapImageWidthBinding; }
    public final int getMapImageWidth() { return mapImageWidthBinding.get(); }
    
    public final IntegerBinding mapImageHeightBinding() { return mapImageHeightBinding; }
    public final int getMapImageHeight() { return mapImageHeightBinding.get(); }
    
    //
    
    public final DoubleBinding xMultiplierBinding() { return xMultiplierBinding; }
    public final double getXMultiplier() { return xMultiplierBinding.get(); }
    
    public final DoubleBinding yMultiplierBinding() { return yMultiplierBinding; }
    public final double getYMultiplier() { return yMultiplierBinding.get(); }
    
    //
    
    public final IntegerBinding scaledViewportWidthBinding() { return scaledViewportWidthBinding; }
    public final int getScaledViewportWidth() { return scaledViewportWidthBinding.get(); }
    
    public final IntegerBinding getScaledViewportHeightBinding() { return scaledViewportHeightBinding; }
    public final int getScaledViewportHeight() { return scaledViewportHeightBinding.get(); }
    
    
    public final IntegerBinding scaledViewportXLocationBinding() { return scaledViewportXLocationBinding; }
    public final int getScaledViewportXLocation() { return scaledViewportXLocationBinding.get(); }
    
    public final IntegerBinding scaledViewportYLocationBinding() { return scaledViewportYLocationBinding; }
    public final int getScaledViewportYLocation() { return scaledViewportYLocationBinding.get(); }
    
    //</editor-fold>
    
    
    //<editor-fold desc="--- COORDINATES ---">
    
    /**
     * <p>Defines the {@code x} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty xLocationProperty() { return xLocationProperty; }
    public final int getXLocation() { return xLocationProperty.get(); }
    public final int setXLocation(int newValue) { return SLProperties.setProperty(xLocationProperty, newValue); }
    
    /**
     * <p>Defines the {@code y} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty yLocationProperty() { return yLocationProperty; }
    public final int getYLocation() { return yLocationProperty.get(); }
    public final int setYLocation(int newValue) { return SLProperties.setProperty(yLocationProperty, newValue); }
    
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getXOffset() X Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty xOffsetProperty() { return xOffsetProperty; }
    public final int getXOffset() { return xOffsetProperty.get(); }
    protected final int setXOffset(int newValue) { return SLProperties.setProperty(xOffsetProperty, newValue); }
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getYOffset() Y Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty yOffsetProperty() { return yOffsetProperty; }
    public final int getYOffset() { return yOffsetProperty.get(); }
    public final int setYOffset(int newValue) { return SLProperties.setProperty(yOffsetProperty, newValue); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
}
