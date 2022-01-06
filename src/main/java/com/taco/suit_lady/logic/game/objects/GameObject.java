package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.AttributeContainer;
import com.taco.suit_lady.logic.game.Entity;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.execution.AutoManagedTickable;
import com.taco.suit_lady.logic.game.execution.WrappedTickable;
import com.taco.suit_lady.logic.game.interfaces.AttributeContainable;
import com.taco.suit_lady.logic.game.objects.commands.Commander;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.*;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameObject
        implements Lockable, AttributeContainable, Entity, WrappedTickable<GameObject> {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final AutoManagedTickable<GameObject> tickable;
    
    private final ReadOnlyObjectWrapper<GameMap> gameMapProperty;
    private final AttributeContainer attributes;
    
    
    private final ReadOnlyIntegerWrapper xLocProperty;
    private final ReadOnlyIntegerWrapper yLocProperty;
    
    private final ReadOnlyIntegerWrapper widthProperty;
    private final ReadOnlyIntegerWrapper heightProperty;
    
    //
    
    /**
     * <p>Represents the number of units (pixels, for now, at least) this GameObject can move per tick.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         Interpolation can be used to handle decimal values.
     *         <ul>
     *             <li>For example, if the unit can move .25 units per tick, the object's location is expected to have moved 1 unit every 4 ticks.</li>
     *         </ul>
     *     </li>
     *     <li>Keep in mind that you're going to need properties for both max move speed & current move speed (i.e., velocity).</li>
     *     <li>
     *         A collection of "forces" dictates how an object moves.
     *         <ul>
     *             <li>For example, if a unit is knocked-back, it has the force of the knock-back applied to it.</li>
     *             <li>Eventually, forces will be defined by velocity, impact location, impact angle, impacting object weight, impacting object speed, etc.</li>
     *             <li>For now, however, forces should be defined by simple equations that dictate how the speed of an object is influenced each tick.</li>
     *         </ul>
     *     </li>
     * </ol>
     */
    // TO-EXPAND
    private final ReadOnlyDoubleWrapper moveSpeedProperty;
    
    private final Commander commander;
    
    public GameObject(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull GameMap gameMap) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.tickable = new AutoManagedTickable<>(this) {
            @Override
            protected void step() {
                commander.tick();
            }
        };
        
        this.gameMapProperty = new ReadOnlyObjectWrapper<>(gameMap);
        this.attributes = new AttributeContainer(this, lock, this);
        
        
        this.xLocProperty = new ReadOnlyIntegerWrapper();
        this.yLocProperty = new ReadOnlyIntegerWrapper();
        
        this.widthProperty = new ReadOnlyIntegerWrapper();
        this.heightProperty = new ReadOnlyIntegerWrapper();
        
        //
        
        this.moveSpeedProperty = new ReadOnlyDoubleWrapper();
    
        this.commander = new Commander(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="--- MAP PROPERTIES ---">
    
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
    
    protected final ReadOnlyIntegerWrapper xLocModifiableProperty() {
        return xLocProperty;
    }
    
    public final ReadOnlyIntegerProperty xLocProperty() {
        return xLocProperty.getReadOnlyProperty();
    }
    
    public final int getXLocation() {
        return xLocProperty.get();
    }
    
    protected final int setXLocation(int newValue) {
        int oldValue = getXLocation();
        xLocProperty.set(newValue);
        return oldValue;
    }
    
    
    protected final ReadOnlyIntegerWrapper yLocModifiableProperty() {
        return yLocProperty;
    }
    
    public final ReadOnlyIntegerProperty yLocProperty() {
        return yLocProperty.getReadOnlyProperty();
    }
    
    public final int getYLocation() {
        return yLocProperty.get();
    }
    
    protected final int setYLocation(int newValue) {
        int oldValue = getYLocation();
        yLocProperty.set(newValue);
        return oldValue;
    }
    
    
    protected final ReadOnlyIntegerWrapper widthModifiableProperty() {
        return widthProperty;
    }
    
    public final ReadOnlyIntegerProperty widthProperty() {
        return widthProperty.getReadOnlyProperty();
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    protected final int setWidth(int newValue) {
        int oldValue = getWidth();
        widthProperty.set(newValue);
        return oldValue;
    }
    
    
    protected final ReadOnlyIntegerWrapper heightModifiableProperty() {
        return heightProperty;
    }
    
    public final ReadOnlyIntegerProperty heightProperty() {
        return heightProperty.getReadOnlyProperty();
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    protected final int setHeight(int newValue) {
        int oldValue = getHeight();
        heightProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME STAT PROPERTIES ---">
    
    public final ReadOnlyDoubleProperty readOnlyMoveSpeedProperty() {
        return moveSpeedProperty.getReadOnlyProperty();
    }
    
    public final double getMoveSpeed() {
        return moveSpeedProperty.get();
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull AttributeContainer attributes() {
        return attributes;
    }
    
    @Override
    public @NotNull AutoManagedTickable<GameObject> tickable() {
        return tickable;
    }
    
    //<editor-fold desc="--- GENERIC ---">
    
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
        return null;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull Tile[][] getOccupyingTiles() {
        final int adjustedMinX = getXLocation() / getGameMap().getTileSize();
        final int adjustedMinY = getYLocation() / getGameMap().getTileSize();
        final int adjustedMaxX = (getWidth() + getXLocation()) / getGameMap().getTileSize();
        final int adjustedMaxY = (getHeight() + getYLocation()) / getGameMap().getTileSize();
        
        final Tile[][] occupyingTiles = new Tile[(adjustedMaxX - adjustedMinX) + 1][(adjustedMaxY - adjustedMinY) + 1];
        for (int i = 0; i < occupyingTiles.length; i++)
            for (int j = 0; j < occupyingTiles[i].length; j++)
                occupyingTiles[i][j] = getGameMap().getTileMap()[i + adjustedMinX][j + adjustedMinY];
        
        return occupyingTiles;
    }
}
