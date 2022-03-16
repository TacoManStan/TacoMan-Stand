package com.taco.suit_lady.game.objects.tiles;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.MapObject;
import com.taco.suit_lady.game.objects.collision.Collidable;
import com.taco.suit_lady.game.objects.collision.CollisionArea;
import com.taco.suit_lady.game.objects.collision.CollisionMap;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.shapes.Box;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameTile
        implements SpringableWrapper, Entity, MapObject, Collidable<GameTile>, JObject, JLoadable {
    
    private final GameMap owner;
    private TileModel model;
    private final CollisionMap<GameTile> collisionMap;
    
    private final ReadOnlyIntegerWrapper xTileLocationProperty;
    private final ReadOnlyIntegerWrapper yTileLocationProperty;
    
    private final IntegerBinding xPixelLocationBinding;
    private final IntegerBinding yPixelLocationBinding;
    
    private final ListProperty<GameObject> occupyingObjects;
    
    public GameTile(@NotNull GameMap owner) {
        this(owner, 0, 0);
    }
    
    public GameTile(@NotNull GameMap owner, @NotNull Num2D dimensions) {
        this(owner, dimensions.aI(), dimensions.bI());
    }
    
    public GameTile(@NotNull GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        this.collisionMap = new CollisionMap<>(this);
        
        this.xTileLocationProperty = new ReadOnlyIntegerWrapper(xLoc);
        this.yTileLocationProperty = new ReadOnlyIntegerWrapper(yLoc);
        
        this.xPixelLocationBinding = Bind.intBinding(() -> getTileLocationX() * getGameMap().getTileSize(), readOnlyTileLocationXProperty());
        this.yPixelLocationBinding = Bind.intBinding(() -> getTileLocationY() * getGameMap().getTileSize(), readOnlyTileLocationYProperty());
        
        this.occupyingObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        
        this.model = new TileModel(this);
    }
    
    private Box collisionBox;
    private CollisionArea<GameTile> collisionArea;
    
    private void initCollisionMap() {
        this.collisionArea = new CollisionArea<>(collisionMap());
        this.collisionBox = new Box(this);
    }
    
    public final GameTile getNeighbor(int x, int y) { return owner.getNeighbor(this, x, y); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    @Override public final @NotNull IntegerBinding xLocationProperty() { return xPixelLocationBinding; }
    @Override public @NotNull Integer getLocationY() { return MapObject.super.getLocationY().intValue(); }
    
    public final ReadOnlyIntegerProperty readOnlyTileLocationXProperty() { return xTileLocationProperty.getReadOnlyProperty(); }
    public final int setTileLocationX(int newValue) { return Props.setProperty(xTileLocationProperty, newValue); }
    public final int getTileLocationX() { return xTileLocationProperty.get(); }
    
    
    @Override public final @NotNull IntegerBinding yLocationProperty() { return yPixelLocationBinding; }
    @Override public @NotNull Integer getLocationX() { return MapObject.super.getLocationX().intValue(); }
    
    public final ReadOnlyIntegerProperty readOnlyTileLocationYProperty() { return yTileLocationProperty.getReadOnlyProperty(); }
    public final int setTileLocationY(int newValue) { return Props.setProperty(yTileLocationProperty, newValue); }
    public final int getTileLocationY() { return yTileLocationProperty.get(); }
    
    
    
    public final @NotNull GameMap getOwner() { return owner; }
    public final ListProperty<GameObject> getOccupyingObjects() { return occupyingObjects; }
    
    public final TileModel getModel() { return model; }
    private void setModel(@NotNull TileModel model) { this.model = model; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @NotNull Lock getLock() { return getOwner().getLock(); }
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    @Override public @NotNull CollisionMap<GameTile> collisionMap() { return collisionMap; }
    
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
