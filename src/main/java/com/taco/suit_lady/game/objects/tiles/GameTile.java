package com.taco.suit_lady.game.objects.tiles;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.MapObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.tacository.json.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameTile
        implements SpringableWrapper, Lockable, GameComponent, MapObject, JObject, JLoadable {
    
    private final ReadOnlyIntegerWrapper xTileLocationProperty;
    private final ReadOnlyIntegerWrapper yTileLocationProperty;
    
    private final IntegerBinding xPixelLocationBinding;
    private final IntegerBinding yPixelLocationBinding;
    
    private final GameMap owner;
    private final ListProperty<GameObject> occupyingObjects;
    
    
    private TileModel model;
    
    public GameTile(@NotNull GameMap owner) {
        this(owner, 0, 0);
    }
    
    public GameTile(@NotNull GameMap owner, @NotNull Dimensions dimensions) {
        this(owner, dimensions.width(), dimensions.height());
    }
    
    public GameTile(@NotNull GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xTileLocationProperty = new ReadOnlyIntegerWrapper(xLoc);
        this.yTileLocationProperty = new ReadOnlyIntegerWrapper(yLoc);
        
        this.xPixelLocationBinding = BindingsSL.intBinding(() -> getLocationX() * getGameMap().getTileSize());
        this.yPixelLocationBinding = BindingsSL.intBinding(() -> getLocationY() * getGameMap().getTileSize());
        
        this.occupyingObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        
        this.model = new TileModel(this);
    }
    
    public final GameTile getNeighbor(int x, int y) { return owner.getNeighbor(this, x, y); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    @Override public final @NotNull IntegerBinding xLocationProperty() { return xPixelLocationBinding; }
    @Override public @NotNull Integer getLocationY() { return MapObject.super.getLocationY().intValue(); }
    
    public final ReadOnlyIntegerProperty readOnlyTileLocationXBinding() { return xTileLocationProperty.getReadOnlyProperty(); }
    public final int setTileLocationX(int newValue) { return PropertiesSL.setProperty(xTileLocationProperty, newValue); }
    
    
    @Override public final @NotNull IntegerBinding yLocationProperty() { return yPixelLocationBinding; }
    @Override public @NotNull Integer getLocationX() { return MapObject.super.getLocationX().intValue(); }
    
    public final ReadOnlyIntegerProperty readOnlyTileLocationYProperty() { return yTileLocationProperty.getReadOnlyProperty(); }
    public final int setTileLocationY(int newValue) { return PropertiesSL.setProperty(yTileLocationProperty, newValue); }
    
    
    
    public final GameMap getOwner() { return owner; }
    public final ListProperty<GameObject> getOccupyingObjects() { return occupyingObjects; }
    
    public final TileModel getModel() { return model; }
    private void setModel(@NotNull TileModel model) { this.model = model; }
    
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
        setModel(JUtil.loadObject(parent, new TileModel(this)));
        setTileLocationX(JUtil.loadInt(parent, "x-loc"));
        setTileLocationY(JUtil.loadInt(parent, "y-loc"));
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
