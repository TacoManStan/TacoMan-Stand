package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.AttributeContainer;
import com.taco.suit_lady.logic.game.Entity;
import com.taco.suit_lady.logic.game.interfaces.AttributeContainable;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.*;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameObject
        implements Lockable, AttributeContainable, Entity {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final GameViewContent content;
    
    private final GameObjectModel model;
    private final AttributeContainer attributes;
    
    
    private final IntegerProperty xLocProperty;
    private final IntegerProperty yLocProperty;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
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
    
    public GameObject(@NotNull GameViewContent content, @Nullable ReentrantLock lock) {
        this.springable = content.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.content = content;
        
        this.model = new GameObjectModel(this);
        this.attributes = new AttributeContainer(this, lock, this);
        
        
        this.xLocProperty = new SimpleIntegerProperty();
        this.yLocProperty = new SimpleIntegerProperty();
        
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        //
        
        this.moveSpeedProperty = new ReadOnlyDoubleWrapper();
    }
    
    public final GameObject init() {
        getModel().init();
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObjectModel getModel() { return model; }
    
    //<editor-fold desc="--- MAP PROPERTIES ---">
    
    public final IntegerProperty xLocProperty() {
        return xLocProperty;
    }
    
    public final int getXLocation() {
        return xLocProperty.get();
    }
    
    public final int setXLocation(int newValue) {
        int oldValue = getXLocation();
        xLocProperty.set(newValue);
        return oldValue;
    }
    
    public final int moveX(int amount) { return setXLocation(getXLocation() + amount); }
    
    
    public final IntegerProperty yLocProperty() {
        return yLocProperty;
    }
    
    public final int getYLocation() {
        return yLocProperty.get();
    }
    
    public final int setYLocation(int newValue) {
        int oldValue = getYLocation();
        yLocProperty.set(newValue);
        return oldValue;
    }
    
    public final int moveY(int amount) { return setYLocation(getYLocation() + amount); }
    
    
    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int setWidth(int newValue) {
        int oldValue = getWidth();
        widthProperty.set(newValue);
        return oldValue;
    }
    
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int setHeight(int newValue) {
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
    
    @Override public final @NotNull AttributeContainer attributes() { return attributes; }
    
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
    
    public final @NotNull GameTile[][] getOccupyingTiles() {
        final int adjustedMinX = getXLocation() / getGameMap().getTileSize();
        final int adjustedMinY = getYLocation() / getGameMap().getTileSize();
        final int adjustedMaxX = (getWidth() + getXLocation()) / getGameMap().getTileSize();
        final int adjustedMaxY = (getHeight() + getYLocation()) / getGameMap().getTileSize();
        
        final GameTile[][] occupyingGameTiles = new GameTile[(adjustedMaxX - adjustedMinX) + 1][(adjustedMaxY - adjustedMinY) + 1];
        for (int i = 0; i < occupyingGameTiles.length; i++)
            for (int j = 0; j < occupyingGameTiles[i].length; j++)
                occupyingGameTiles[i][j] = getGameMap().getTileMap()[i + adjustedMinX][j + adjustedMinY];
        
        return occupyingGameTiles;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    @Override public void tick() {
        //TODO
    }
    
    //</editor-fold>
}
