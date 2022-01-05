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
 * <p>All {@link GEntity} objects should be able to generate a new {@link GCamera} object that is bound to their location.</p>
 * <p>
 * {@link GCamera} objects should also permit an unbounded view, in which case the {@link GCamera} is not bound to the location of a {@link GEntity}, but rather follows a pre-defined set of rules.
 *     <ul>
 *         <li>For example, an unbounded {@link GCamera} could be used via a CameraTransition (not yet available) to pan/move/zoom/etc. to any location on the map given a set of movement instructions.</li>
 *     </ul>
 * </p>
 * <p>{@link GCamera} objects should be able to have tints, zoom values, vision radius, etc.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The location of the {@link GCamera} represents the <i>center</i> of the {@link GCamera camera's} view region, <i>not</i> the top-left corner as is the case with most {@code rectangle-based} implementations.</li>
 * </ol>
 */
public class GCamera
        implements SpringableWrapper {
    
    private final StrictSpringable springable;
    
    
    private final ReadOnlyObjectWrapper<GMap> gameMapProperty;
    
    private final ReadOnlyIntegerWrapper xOffsetProperty;
    private final ReadOnlyIntegerWrapper yOffsetProperty;
    
    public GCamera(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
        
        this.gameMapProperty = new ReadOnlyObjectWrapper<>();
        
        this.xOffsetProperty = new ReadOnlyIntegerWrapper(0);
        this.yOffsetProperty = new ReadOnlyIntegerWrapper(0);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<GMap> gameMapModifiableProperty() {
        return gameMapProperty;
    }
    
    public final ReadOnlyObjectProperty<GMap> gameMapProperty() {
        return gameMapProperty.getReadOnlyProperty();
    }
    
    public final GMap getGameMap() {
        return gameMapProperty.get();
    }
    
    protected final GMap setGameMap(GMap newValue) {
        GMap oldValue = getGameMap();
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
