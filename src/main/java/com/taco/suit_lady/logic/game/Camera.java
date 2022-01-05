package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
    
    private final ReadOnlyIntegerWrapper xOffsetProperty;
    private final ReadOnlyIntegerWrapper yOffsetProperty;
    
    public Camera(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
        
        this.gameMapProperty = new ReadOnlyObjectWrapper<>();
        
        this.xOffsetProperty = new ReadOnlyIntegerWrapper(0);
        this.yOffsetProperty = new ReadOnlyIntegerWrapper(0);
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
    
    //
    
    protected final ReadOnlyIntegerWrapper xOffsetModifiableProperty() {
        return xOffsetProperty;
    }
    
    public final ReadOnlyIntegerProperty xOffsetProperty() {
        return xOffsetProperty.getReadOnlyProperty();
    }
    
    public final int getXOffset() {
        return xOffsetProperty.get();
    }
    
    protected final int setXOffset(int newValue) {
        int oldValue = getXOffset();
        xOffsetProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ReadOnlyIntegerWrapper yOffsetModifiableProperty() {
        return yOffsetProperty;
    }
    
    public final ReadOnlyIntegerProperty yOffsetProperty() {
        return yOffsetProperty.getReadOnlyProperty();
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
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return springable;
    }
    
    //</editor-fold>
}
