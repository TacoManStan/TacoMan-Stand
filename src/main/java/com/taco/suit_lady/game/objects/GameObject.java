package com.taco.suit_lady.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.attributes.AttributeManager;
import com.taco.suit_lady.game.objects.collision.Collidable;
import com.taco.suit_lady.game.objects.collision.CollisionArea;
import com.taco.suit_lady.game.objects.collision.CollisionMap;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.logic.triggers.implementations.UnitMovedEvent;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import com.taco.suit_lady.util.values.numbers.shapes.Box;
import com.taco.suit_lady.util.values.numbers.shapes.Circle;
import com.taco.suit_lady.util.values.numbers.shapes.Shape;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serial;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class GameObject
        implements Entity, MapObject, JObject, JLoadable, UIDProcessable, Tickable<GameObject>, Collidable<GameObject>, Movable {
    
    private final GameComponent gameComponent;
    private Lock lock;
    
    private String objID;
    
    private final TaskManager<GameObject> taskManager;
    private final Mover mover;
    
    private final GameObjectModel model;
    private final AttributeManager attributes;
    private final CollisionMap<GameObject> collisionMap;
    
    
    private final DoubleProperty xLocationProperty;
    private final DoubleProperty yLocationProperty;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    //
    
    private final DoubleBinding xLocationCenteredBinding;
    private final DoubleBinding yLocationCenteredBinding;
    
    private final ObjectBinding<Num2D> locationBinding;
    private final ObjectBinding<Num2D> locationCenteredBinding;
    
    private final ObjectBinding<Num2D> dimensionsBinding;
    
    private ObjectBinding<List<GameTile>> occupiedTilesBinding = null;
    private final ListProperty<GameTile> occupiedTilesList;
    
    //<editor-fold desc="--- CONSTRUCTORS ---">
    
    public GameObject(@Nullable Lock lock, @NotNull GameComponent gameComponent, @Nullable String objID, @Nullable String modelID) {
        this.gameComponent = gameComponent;
        this.taskManager = new TaskManager<>(this);
        
        this.objID = objID;
        
        this.model = new GameObjectModel(this, "units", modelID);
        this.attributes = new AttributeManager(this);
        this.collisionMap = new CollisionMap<>(this);
        
        
        this.xLocationProperty = new SimpleDoubleProperty();
        this.yLocationProperty = new SimpleDoubleProperty();
        
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        //
        
        this.xLocationCenteredBinding = Bind.doubleBinding(() -> getLocationX(false) + (getWidth() / 2D), xLocationProperty, widthProperty);
        this.yLocationCenteredBinding = Bind.doubleBinding(() -> getLocationY(false) + (getHeight() / 2D), yLocationProperty, heightProperty);
        
        this.locationBinding = Bind.objBinding(() -> new Num2D(getLocationX(false), getLocationY(false)), xLocationProperty, yLocationProperty);
        this.locationCenteredBinding = Bind.objBinding(() -> new Num2D(getLocationX(true), getLocationY(true)), xLocationProperty, yLocationProperty);
        
        this.dimensionsBinding = Bind.objBinding(() -> new Num2D(getWidth(), getHeight()), widthProperty, heightProperty);
        
        //
        
        initAttributes();
        
        taskManager().addTask(this.mover = new Mover(this));
        
        //
        
        this.occupiedTilesList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    public GameObject(@Nullable Lock lock, @NotNull GameComponent gameComponent, @Nullable String objID) { this(lock, gameComponent, objID, null); }
    public GameObject(@Nullable Lock lock, @NotNull GameComponent gameComponent) { this(lock, gameComponent, null, null); }
    
    public GameObject(@NotNull GameComponent gameComponent, @Nullable String objID, @Nullable String modelID) { this(null, gameComponent, objID, modelID); }
    public GameObject(@NotNull GameComponent gameComponent, @Nullable String objID) { this(null, gameComponent, objID, null); }
    public GameObject(@NotNull GameComponent gameComponent) { this(null, gameComponent, null, null); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameObject init() {
        return init(() -> { });
    }
    
    public final GameObject init(@NotNull Runnable postInitOperation) {
        setWidth(getGameMap().getTileSize());
        setHeight(getGameMap().getTileSize());
        
        taskManager().init();
        getModel().init();
        
        this.occupiedTilesBinding = Bind.objBinding(this::calculateOccupiedTiles, xLocationProperty, yLocationProperty, widthProperty, heightProperty, gameMapProperty());
        //TODO: This is more efficient than before but still comically inefficient - not a problem now but will quickly become one as more GameObjects are added to the map (and moving at the same time)
        this.occupiedTilesBinding.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                oldValue.forEach(tile -> Obj.doIf(() -> tile, t -> newValue == null || !newValue.contains(t), t -> t.getOccupyingObjects().remove(this)));
            if (newValue != null)
                newValue.forEach(tile -> Obj.doIf(() -> tile, t -> !t.getOccupyingObjects().contains(this), t -> t.getOccupyingObjects().add(this)));
        });
        
        initTriggerEvents();
        initTaskManager();
        initCollisionMap(postInitOperation);
        
        //        locationBinding.addListener((observable, oldValue, newValue) -> Print.print("GameObject Location Changed:  [ " + oldValue + "  -->  " + newValue + " ]", false));
        
        return this;
    }
    
    private void initAttributes() {
        attributes().addDoubleAttribute(Mover.SPEED_ID, 8); //Measured in tiles/second
        attributes().addDoubleAttribute(Mover.MAX_SPEED_ID, 100);
        attributes().addAttribute("health", 500);
    }
    
    private void initTriggerEvents() {
        locationCenteredBinding.addListener((observable, oldValue, newValue) -> triggers().submit(new UnitMovedEvent(this, oldValue, newValue)));
        triggers().register(Galaxy.newUnitMovedTrigger(this, event -> {
            //            System.out.println("Unit Moved  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
        }));
    }
    
    private void initTaskManager() {
        taskManager().addShutdownOperation(() -> getGameMap().removeGameObject(this));
        taskManager().addGfxShutdownOperation(() -> getModel().shutdown());
        taskManager().addGfxShutdownOperation(() -> getGameMap().getModel().refreshMapImage());
        taskManager().addShutdownOperation(() -> getOccupiedTiles().forEach(tile -> tile.getOccupyingObjects().remove(this)));
//        taskManager().addShutdownOperation(() -> A.iterateMatrix(tile -> tile.getOccupyingObjects().remove(this), getOccupiedTiles()));
    }
    
    private final boolean useCollisionBox = false;
    private CollisionArea<GameObject> collisionArea = null;
    
    private Shape collShape;
    
    private void initCollisionMap(@NotNull Runnable postInitOperation) {
        executeOnce(() -> sync(() -> {
            printer().get(getClass()).setEnabled(true);
            printer().get(getClass()).setPrintPrefix(false);
            printer().get(getClass()).print("Initializing Collision Map For: " + this);
            
            collisionArea = new CollisionArea<>(collisionMap());
            collisionArea.includedShapes().add((collShape = (useCollisionBox ? new Box(this) : new Circle(this))).init());
            collisionMap().addCollisionArea(collisionArea);
            
            return null;
        }), o -> {
            refreshCollisionData();
            postInitOperation.run();
            startCollisionRefreshTask();
        });
    }
    
    private void startCollisionRefreshTask() {
        execute(() -> sync(this::refreshCollisionData));
    }
    
    private void refreshCollisionData() {
        collShape.setDimensions(getDimensions());
        collShape.setLocation(getLocation(!useCollisionBox));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameObjectModel getModel() { return model; }
    public final @NotNull AttributeManager attributes() { return attributes; }
    @Override public final @NotNull CollisionMap<GameObject> collisionMap() { return collisionMap; }
    
    public final String getObjID() { return objID; }
    public final String setObjID(@Nullable String newValue) {
        return sync(() -> {
            String oldValue = getObjID();
            objID = newValue;
            return oldValue;
        });
    }
    
    //<editor-fold desc="> Map Properties">
    
    //<editor-fold desc=">> Location Properties">
    
    @Override public final @NotNull DoubleProperty xLocationProperty() { return (DoubleProperty) xLocationProperty(false); }
    public final @NotNull ObservableDoubleValue xLocationProperty(boolean center) { return center ? xLocationCenteredBinding : xLocationProperty; }
    
    public final double getLocationX(boolean center) { return center ? xLocationCenteredBinding.get() : xLocationProperty.get(); }
    public final double setLocationX(@NotNull Number newValue, boolean center) { return Props.setProperty(xLocationProperty, center ? newValue.doubleValue() - (getWidth() / 2D) : newValue.doubleValue()); }
    public final double translateX(@NotNull Number amount) { return setLocationX(getLocationX(false) + amount.doubleValue(), false); }
    
    public final double getTileLocationX(boolean center) { return pixelToTile(getLocationX(center)).doubleValue(); }
    public final double setTileLocationX(@NotNull Number newValue, boolean center) { return pixelToTile(setLocationX(tileToPixel(newValue), center)).doubleValue(); }
    public final double translateTileX(@NotNull Number amount) { return pixelToTile(translateX(tileToPixel(amount))).doubleValue(); }
    
    
    @Override public final @NotNull DoubleProperty yLocationProperty() { return (DoubleProperty) yLocationProperty(false); }
    public final @NotNull ObservableDoubleValue yLocationProperty(boolean center) { return center ? yLocationCenteredBinding : yLocationProperty; }
    
    public final double getLocationY(boolean center) { return center ? yLocationCenteredBinding.get() : yLocationProperty.get(); }
    public final double setLocationY(@NotNull Number newValue, boolean center) { return Props.setProperty(yLocationProperty, center ? newValue.doubleValue() - (getHeight() / 2D) : newValue.doubleValue()); }
    public final double translateY(@NotNull Number amount) { return setLocationY(getLocationY(false) + amount.doubleValue(), false); }
    
    public final double getTileLocationY(boolean center) { return pixelToTile(getLocationY(center)).doubleValue(); }
    public final double setTileLocationY(@NotNull Number newValue, boolean center) { return pixelToTile(setLocationY(tileToPixel(newValue), center)).doubleValue(); }
    public final double translateTileY(@NotNull Number amount) { return pixelToTile(translateY(tileToPixel(amount))).doubleValue(); }
    
    //
    
    //<editor-fold desc="> Location Bindings">
    
    public final ObjectBinding<Num2D> locationBinding(boolean center) { return center ? locationCenteredBinding : locationBinding; }
    
    //TODO: Change boolean param to instead accept a LocType enum
    public final Num2D getLocation(boolean center) { return locationBinding(center).get(); }
    public final Num2D setLocation(@NotNull NumExpr2D<?> newValue, boolean center) {
        return sync(() -> {
            return new Num2D(setLocationX(newValue.a(), center), setLocationY(newValue.b(), center));
        });
    }
    
    public final Num2D translateLocation(@NotNull Number x, @NotNull Number y) {
        return sync(() -> {
            return new Num2D(translateX(x), translateY(y));
        });
    }
    public final Num2D translateLocation(@NotNull Point2D amount) { return translateLocation(amount.getX(), amount.getY()); }
    public final Num2D translateLocation(@NotNull NumExpr2D<?> amount) { return translateLocation(amount.asPoint()); }
    
    // Tile Location
    
    public final Num2D getTileLocation(boolean center) { return new Num2D(getTileLocationX(center), getTileLocationY(center)); }
    public final Num2D setTileLocation(@NotNull Point2D newValue, boolean center) {
        return new Num2D(setTileLocationX(newValue.getX(), center), setTileLocationY(newValue.getY(), center));
    }
    
    
    public final Num2D translateTileLocation(@NotNull Number x, @NotNull Number y) { return new Num2D(translateTileX(x), translateTileY(y)); }
    public final Num2D translateTileLocation(@NotNull Point2D amount) { return translateTileLocation(amount.getX(), amount.getY()); }
    public final Num2D translateTileLocation(@NotNull NumExpr2D<?> amount) { return translateTileLocation(amount.asPoint()); }
    
    //</editor-fold>
    
    //
    
    public final ObjectBinding<List<GameTile>> occupiedTilesBinding() { return occupiedTilesBinding; }
    public final List<GameTile> getOccupiedTiles() { return occupiedTilesBinding.get(); }
    
    
    //</editor-fold>
    
    
    public final IntegerProperty widthProperty() { return widthProperty; }
    public final int getWidth() { return widthProperty.get(); }
    public final int setWidth(int newValue) { return Props.setProperty(widthProperty, newValue); }
    
    public final IntegerProperty heightProperty() { return heightProperty; }
    public final int getHeight() { return heightProperty.get(); }
    public final int setHeight(int newValue) { return Props.setProperty(heightProperty, newValue); }
    
    
    public final ObjectBinding<Num2D> dimensionsBinding() { return dimensionsBinding; }
    public final Num2D getDimensions() { return dimensionsBinding.get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //<editor-fold desc="> Game">
    
    @Override public @NotNull GameViewContent getGame() { return gameComponent.getGame(); }
    @Override public @NotNull Mover mover() { return mover; }
    
    //
    
    @Override public @NotNull Springable springable() { return getGame(); }
    
    @Override public final @Nullable Lock getLock() { return lock != null ? lock : getGame().getLock(); }
    public final void setLock(@Nullable Lock lock) { this.lock = lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="> Json">
    
    @Override public String getJID() { return "game-object"; }
    
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("width", getWidth()),
                JUtil.create("height", getHeight()),
                JUtil.create("x-location", getLocationX(false)),
                JUtil.create("y-location", getLocationY(false)),
                JUtil.createObject("model", getModel())
        };
    }
    
    @Override public void load(JsonObject parent) {
        setWidth(JUtil.loadInt(parent, "width"));
        setHeight(JUtil.loadInt(parent, "height"));
        setLocationX(JUtil.loadDouble(parent, "x-location"), false);
        setLocationY(JUtil.loadDouble(parent, "y-location"), false);
        JUtil.loadObject(parent, "model", getModel());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Generic">
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            return new UIDProcessor("game-objects");
        return uidProcessor;
    }
    
    
    @Override public String toString() {
        return "GameObject{" +
               "xLocation=" + xLocationProperty.get() +
               ", yLocation=" + yLocationProperty.get() +
               ", width=" + widthProperty.get() +
               ", height=" + heightProperty.get() +
               ", xLocationCentered=" + xLocationCenteredBinding.get() +
               ", yLocationCentered=" + yLocationCenteredBinding.get() +
               ", objID='" + objID + '\'' +
               '}';
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Serialization">
    
    @Serial private void writeObject(java.io.ObjectOutputStream out) throws IOException { }
    @Serial private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException { }
    @Serial private void readObjectNoData() throws ObjectStreamException { }
    
    //</editor-fold>
    
    //<editor-fold desc="> Tickable">
    
    @Override public final @NotNull TaskManager<GameObject> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean addToMap() {
        return sync(() -> {
            if (getGameMap().gameObjects().contains(this))
                return true;
            return getGameMap().addGameObject(this);
        });
    }
    
    //
    
    private @NotNull List<GameTile> calculateOccupiedTiles() {
        return sync(() -> {
            final Box boundsBox = boundsBox();
            return getGameMap().getTilesInBounds(boundsBox.getLocation(LocType.MIN), boundsBox.getLocation(LocType.MAX));
        });
    }
    
    //    private @NotNull GameTile[][] calculateOccupiedTiles() {
    //        final int adjustedMinX = (int) Math.floor(getLocationX(false) / getGameMap().getTileSize());
    //        final int adjustedMinY = (int) Math.floor(getLocationY(false) / getGameMap().getTileSize());
    //        final int adjustedMaxX = (int) Math.floor((getWidth() - 1 + getLocationX(false)) / getGameMap().getTileSize());
    //        final int adjustedMaxY = (int) Math.floor((getHeight() - 1 + getLocationY(false)) / getGameMap().getTileSize());
    //
    //
    //        final GameTile[][] occupyingGameTiles = new GameTile[(adjustedMaxX - adjustedMinX) + 1][(adjustedMaxY - adjustedMinY) + 1];
    //        for (int i = 0; i < occupyingGameTiles.length; i++)
    //            for (int j = 0; j < occupyingGameTiles[i].length; j++)
    //                occupyingGameTiles[i][j] = getGameMap().getTileMatrix()[i + adjustedMinX][j + adjustedMinY];
    //
    //        return occupyingGameTiles;
    //    }
    
    
    public final boolean isAtPoint(@NotNull NumExpr2D<?> point) { return isAtPoint(point, true); }
    public final boolean isAtPoint(@NotNull NumExpr2D<?> point, boolean center) {
        final double v1 = Math.round((getLocationX(center))) - Math.round(point.aD());
        final double v2 = Math.round(getLocationY(center)) - Math.round(point.bD());
        return Math.abs(v1) == 0 && Math.abs(v2) == 0;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TESTING ---">
    
    public final boolean isTestObjectAny() { return isTestObject1() || isTestObject2(); }
    public final boolean isTestObject1() { return this.equals(getGame().getTestObject1()); }
    public final boolean isTestObject2() { return this.equals(getGame().getTestObject2()); }
    
    //</editor-fold>
}
