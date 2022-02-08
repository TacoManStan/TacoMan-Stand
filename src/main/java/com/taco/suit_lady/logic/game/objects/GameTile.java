package com.taco.suit_lady.logic.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.tacository.json.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameTile
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private final ReadOnlyIntegerWrapper xLocationProperty;
    private final ReadOnlyIntegerWrapper yLocationProperty;
    
    private final GameMap owner;
    private final ListProperty<GameObject> occupyingObjects;
    
    
    private GameTileModel model;
    
    public GameTile(@NotNull GameMap owner) {
        this(owner, 0, 0);
    }
    
    public GameTile(@NotNull GameMap owner, @NotNull Dimensions dimensions) {
        this(owner, dimensions.width(), dimensions.height());
    }
    
    public GameTile(@NotNull GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xLocationProperty = new ReadOnlyIntegerWrapper(xLoc);
        this.yLocationProperty = new ReadOnlyIntegerWrapper(yLoc);
        
        this.occupyingObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        
        this.model = new GameTileModel(this);
    }
    
    public final GameTile getNeighbor(int x, int y) { return owner.getNeighbor(this, x, y); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyIntegerProperty readOnlyXLocationProperty() { return xLocationProperty.getReadOnlyProperty(); }
    public final int getPixelLocationX() { return getLocationX() * getOwner().getTileSize(); }
    public final int getLocationX() { return xLocationProperty.get(); }
    public final int setLocationX(int newValue) { return PropertiesSL.setProperty(xLocationProperty, newValue); }
    
    public final ReadOnlyIntegerProperty readOnlyYLocationProperty() { return yLocationProperty.getReadOnlyProperty(); }
    public final int getPixelLocationY() { return getLocationY() * getOwner().getTileSize(); }
    public final int getLocationY() { return yLocationProperty.get(); }
    public final int setLocationY(int newValue) { return PropertiesSL.setProperty(yLocationProperty, newValue); }
    
    
    
    public final GameMap getOwner() { return owner; }
    public final ListProperty<GameObject> getOccupyingObjects() { return occupyingObjects; }
    
    public final GameTileModel getModel() { return model; }
    private void setModel(@NotNull GameTileModel model) { this.model = model; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @NotNull Lock getLock() { return getOwner().getLock(); }
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //
    
    @Override public String toString() {
        return "GameTile{" +
               "owner=" + owner +
               ", xLoc=" + getLocationX() +
               ", yLoc=" + getLocationY() +
               '}';
    }
    
    //
    
    @Override public String getJID() { return "map-tile"; }
    @Override public void load(JsonObject parent) {
        setModel(JUtil.loadObject(parent, new GameTileModel(this)));
        setLocationX(JUtil.loadInt(parent, "x-loc"));
        setLocationY(JUtil.loadInt(parent, "y-loc"));
    }
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.createObject("game-tile-model", getModel()),
                JUtil.create("x-loc", getLocationX()),
                JUtil.create("y-loc", getLocationY())
        };
    }
    
    //</editor-fold>
}
