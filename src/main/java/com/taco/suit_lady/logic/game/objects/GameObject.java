package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.AttributeContainer;
import com.taco.suit_lady.logic.game.Entity;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.interfaces.AttributeContainable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import net.rgielen.fxweaver.core.FxWeaver;
import org.docx4j.wml.R;
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
    
    private final ReadOnlyObjectWrapper<GameMap> gameMapProperty;
    private final AttributeContainer attributes;
    
    
    private final ReadOnlyIntegerWrapper xLocProperty;
    private final ReadOnlyIntegerWrapper yLocProperty;
    
    private final ReadOnlyIntegerWrapper widthProperty;
    private final ReadOnlyIntegerWrapper heightProperty;
    
    public GameObject(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull GameMap gameMap) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.gameMapProperty = new ReadOnlyObjectWrapper<>(gameMap);
        this.attributes = new AttributeContainer(this, lock, this);
        
        
        this.xLocProperty = new ReadOnlyIntegerWrapper();
        this.yLocProperty = new ReadOnlyIntegerWrapper();
        
        this.widthProperty = new ReadOnlyIntegerWrapper();
        this.heightProperty = new ReadOnlyIntegerWrapper();
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
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public final @NotNull AttributeContainer attributes() {
        return attributes;
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
    
    public final Tile[][] getOccupyingTiles() {
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
