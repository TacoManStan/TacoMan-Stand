package com.taco.tacository.game.objects.tiles;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.tacository.game.Entity;
import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.GameMap;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.MapObject;
import com.taco.tacository.game.objects.Mover;
import com.taco.tacository.game.objects.collision.Collidable;
import com.taco.tacository.game.objects.collision.CollisionArea;
import com.taco.tacository.game.objects.collision.CollisionMap;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Bind;
import com.taco.tacository.util.tools.Props;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.shapes.Box;
import com.taco.tacository._to_sort.json.JElement;
import com.taco.tacository._to_sort.json.JLoadable;
import com.taco.tacository._to_sort.json.JObject;
import com.taco.tacository._to_sort.json.JUtil;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

/**
 * <p>A {@link GameComponent} {@link Entity} defining a particular {@link GameTile} defined by a {@link #getLocation() Matrix Index} value contained within and managed by a {@link GameMap} instance.</p>
 * <br>
 * <p><b>{@link #getOccupyingObjects() Occupying Game Objects}</b></p>
 * <ol>
 *     <li>The {@link GameObject} instances currently occupying this {@link GameTile} are stored within the {@link #getOccupyingObjects() Occupying Objects} {@link ListProperty}.</li>
 *     <li>
 *         The values contained within the {@link #getOccupyingObjects() Occupying Objects} {@link ListProperty List} are automatically-updated when a {@link GameObject} instance is...
 *         <ul>
 *             <li><i>{@link GameMap#addGameObjects(GameObject...) added to} or {@link GameMap#removeGameObjects(GameObject...) removed from} a {@link GameMap},</i></li>
 *             <li><i>{@link GameObject#mover() moved} from one {@link GameObject#locationBinding(boolean) location} to another,</i></li>
 *             <li><i>or if any other relevant {@link GameObject}, {@link GameTile}, or {@link GameMap} property is changed.</i></li>
 *         </ul>
 *     </li>
 *     <li>{@link GameObject Game Objects} are not added directly to {@link GameTile Game Tiles}, but rather to a {@link GameMap} which then manages the {@link #getOccupyingObjects() contents} of each {@link GameTile} accordingly.</li>
 * </ol>
 * <br>
 * <p><b>{@link GameTileModel Graphics}</b></p>
 * <ol>
 *     <li>{@link GameTile} {@code graphics data} is, <u>loaded</u>, <u>processed</u>, <u>stored</u>, and <u>displayed</u> by the {@link GameTileModel} instance assigned to this {@link GameTile}.</li>
 * </ol>
 * <p><i>See {@link GameTileModel} for additional information.</i></p>
 * <br>
 * <p><b>{@link #collisionMap() Collision Handling}</b></p>
 * <ol>
 *     <li>Unlike with cases such as seen with {@link GameObject}, the {@link GameTile} {@link #collisionMap() Collision Map} is used primarily to detect the {@link #getOccupyingObjects() Occupying Objects} for each {@link GameTile}.</li>
 *     <li>i.e., {@code Collision Data} for a {@link GameTile} is <i>not</i> used in {@link Mover movement} {@link Mover#collisionMap() collision detection}.</li>
 * </ol>
 */
public class GameTile
        implements SpringableWrapper, Entity, MapObject, Collidable<GameTile>, JObject, JLoadable {
    
    private final GameMap owner;
    private GameTileModel model;
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
        
        
        this.model = new GameTileModel(this);
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
    
    public final GameTileModel getModel() { return model; }
    private void setModel(@NotNull GameTileModel model) { this.model = model; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @NotNull Lock getLock() { return getOwner().getLock(); }
    
    //
    
    @Override public @NotNull GameViewContent getContent() { return getOwner().getGame(); }
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
        setModel(JUtil.loadObject(parent, new GameTileModel(this)));
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
