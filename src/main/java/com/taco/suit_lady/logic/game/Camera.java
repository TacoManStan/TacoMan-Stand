package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.*;
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
    
    private final ReadOnlyObjectWrapper<GameMap> gameMapProperty;
    
    private final IntegerProperty xLocationProperty;
    private final IntegerProperty yLocationProperty;
    private final IntegerProperty xOffsetProperty;
    private final IntegerProperty yOffsetProperty;
    
    public Camera(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
        
        this.gameMapProperty = new ReadOnlyObjectWrapper<>();
        
        this.xLocationProperty = new SimpleIntegerProperty(0);
        this.yLocationProperty = new SimpleIntegerProperty(0);
        this.xOffsetProperty = new SimpleIntegerProperty(0);
        this.yOffsetProperty = new SimpleIntegerProperty(0);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<GameMap> gameMapModifiableProperty() {
        return gameMapProperty;
    }
    
    public final ReadOnlyObjectProperty<GameMap> gameMapProperty() {
        return gameMapProperty.getReadOnlyProperty();
    }
    
    public final GameMap getGameMap() {
        return gameMapProperty.get();
    }
    
    protected final GameMap setGameMap(GameMap newValue) {
        GameMap oldValue = getGameMap();
        gameMapProperty.set(newValue);
        return oldValue;
    }
    
    //<editor-fold desc="--- COORDINATES ---">
    
    /**
     * <p>Defines the x coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty xLocationProperty() {
        return xLocationProperty;
    }
    
    public final int getXLocation() {
        return xLocationProperty.get();
    }
    
    public final int setXLocation(int newValue) {
        int oldValue = getXLocation();
        xLocationProperty.set(newValue);
        return oldValue;
    }
    
    
    public final IntegerProperty yLocationProperty() {
        return yLocationProperty;
    }
    
    public final int getYLocation() {
        return yLocationProperty.get();
    }
    
    public final int setYLocation(int newValue) {
        int oldValue = getYLocation();
        yLocationProperty.set(newValue);
        return oldValue;
    }
    
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the x offset value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty xOffsetProperty() {
        return xOffsetProperty;
    }
    
    public final int getXOffset() {
        return xOffsetProperty.get();
    }
    
    protected final int setXOffset(int newValue) {
        int oldValue = getXOffset();
        xOffsetProperty.set(newValue);
        return oldValue;
    }
    
    
    public final IntegerProperty yOffsetProperty() {
        return yOffsetProperty;
    }
    
    public final int getYOffset() {
        return yOffsetProperty.get();
    }
    
    public final int setYOffset(int newValue) {
        int oldValue = getYOffset();
        yOffsetProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return springable;
    }
    
    //</editor-fold>
}
