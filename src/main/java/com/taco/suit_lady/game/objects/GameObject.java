package com.taco.suit_lady.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.attributes.AttributeManager;
import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.commands.MoveCommand;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.logic.triggers.implementations.UnitMovedEvent;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.shapes.Circle;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.values.NumberValuePairable;
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
import java.util.ArrayList;

public class GameObject
        implements WrappedGameComponent, Entity, MapObject, JObject, JLoadable, UIDProcessable, Tickable<GameObject>, Collidable {
    
    private final GameViewContent content;
    private final TaskManager<GameObject> taskManager;
    
    private final GameObjectModel model;
    private final AttributeManager attributes;
    private final CollisionMap collisionMap;
    
    
    private final DoubleProperty xLocationProperty;
    private final DoubleProperty yLocationProperty;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    //
    
    private final DoubleBinding xLocationCenteredBinding;
    private final DoubleBinding yLocationCenteredBinding;
    
    private final ObjectBinding<Point2D> locationBinding;
    private final ObjectBinding<Point2D> locationCenteredBinding;
    
    private final ObjectBinding<Dimensions> dimensionsBinding;
    
    private ObjectBinding<GameTile[][]> occupiedTilesBinding = null;
    private final ListProperty<GameTile> occupiedTilesList;
    
    
    private String objID;
    
    //
    
    private final MoveCommand command;
    
    public GameObject(@NotNull GameComponent gameComponent, @Nullable String objID, @Nullable String modelId) {
        this.content = gameComponent.getGame();
        this.taskManager = new TaskManager<>(this);
        
        this.objID = objID;
        
        this.model = new GameObjectModel(this, "units", modelId);
        this.attributes = new AttributeManager(this);
        this.collisionMap = new CollisionMap(this);
        
        
        this.xLocationProperty = new SimpleDoubleProperty();
        this.yLocationProperty = new SimpleDoubleProperty();
        
        this.widthProperty = new SimpleIntegerProperty();
        this.heightProperty = new SimpleIntegerProperty();
        
        //
        
        this.xLocationCenteredBinding = Bind.doubleBinding(() -> getLocationX(false) + (getWidth() / 2D), xLocationProperty, widthProperty);
        this.yLocationCenteredBinding = Bind.doubleBinding(() -> getLocationY(false) + (getHeight() / 2D), yLocationProperty, heightProperty);
        
        this.locationBinding = Bind.objBinding(() -> new Point2D(getLocationX(false), getLocationY(false)), xLocationProperty, yLocationProperty);
        this.locationCenteredBinding = Bind.objBinding(() -> new Point2D(getLocationX(true), getLocationY(true)), xLocationProperty, yLocationProperty);
        
        this.dimensionsBinding = Bind.objBinding(() -> new Dimensions(getWidth(), getHeight()), widthProperty, heightProperty);
        
        //
        
        initAttributes();
        
        taskManager().addTask(this.command = new MoveCommand(this));
        
        //
        
        this.occupiedTilesList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    public GameObject(@NotNull GameComponent gameComponent, @Nullable String objID) { this(gameComponent, objID, null); }
    public GameObject(@NotNull GameComponent gameComponent) { this(gameComponent, null, null); }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameObject init() {
        setWidth(getGameMap().getTileSize());
        setHeight(getGameMap().getTileSize());
        
        taskManager().init();
        getModel().init();
        
        this.occupiedTilesBinding = Bind.objBinding(this::calculateOccupiedTiles, xLocationProperty, yLocationProperty, widthProperty, heightProperty, gameMapProperty());
        //TODO: This is more efficient than before but still comically inefficient - not a problem now but will quickly become one as more GameObjects are added to the map (and moving at the same time)
        this.occupiedTilesBinding.addListener((observable, oldValue, newValue) -> {
            final ArrayList<GameTile> oldTiles = new ArrayList<>();
            final ArrayList<GameTile> newTiles = new ArrayList<>();
            
            A.iterateMatrix(tile -> oldTiles.add(tile), oldValue);
            A.iterateMatrix(tile -> newTiles.add(tile), newValue);
            
            oldTiles.forEach(tile -> Obj.doIf(() -> tile, t -> !newTiles.contains(t), t -> t.getOccupyingObjects().remove(this)));
            newTiles.forEach(tile -> Obj.doIf(() -> tile, t -> !t.getOccupyingObjects().contains(this), t -> t.getOccupyingObjects().add(this)));
        });
        
        initTriggerEvents();
        initTaskManager();
        initCollisionMap();
        
        //        locationBinding.addListener((observable, oldValue, newValue) -> Print.print("GameObject Location Changed:  [ " + oldValue + "  -->  " + newValue + " ]", false));
        
        return this;
    }
    
    private void initAttributes() {
        attributes().addDoubleAttribute(MoveCommand.SPEED_ID, 8); //Measured in tiles/second
        attributes().addDoubleAttribute(MoveCommand.MAX_SPEED_ID, 100);
        attributes().addAttribute("health", 500);
    }
    
    private void initTriggerEvents() {
        locationCenteredBinding.addListener((observable, oldValue, newValue) -> triggers().submit(new UnitMovedEvent(this, oldValue, newValue)));
        triggers().register(Galaxy.newUnitMovedTrigger(this, event -> {
            //            System.out.println("Unit Moved  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
        }));
    }
    
    private void initTaskManager() {
        taskManager().addShutdownOperation(() -> getGameMap().gameObjects().remove(this));
        taskManager().addGfxShutdownOperation(() -> getModel().shutdown());
        taskManager().addGfxShutdownOperation(() -> getGameMap().getModel().refreshMapImage());
        taskManager().addShutdownOperation(() -> A.iterateMatrix(tile -> tile.getOccupyingObjects().remove(this), getOccupiedTiles()));
    }
    
    private CollisionArea collisionArea = null;
    private Circle circle;
    
    private void initCollisionMap() {
        executeOnce(() -> {
            printer().get(getClass()).setEnabled(true);
            printer().get(getClass()).setPrintPrefix(false);
            printer().get(getClass()).print("Initializing Collision Map For: " + this);
            
            collisionArea = new CollisionArea(collisionMap());
            collisionArea.includedShapes().add(circle = new Circle(this).init());
            
            collisionMap().addCollisionArea(collisionArea);
        });
        refreshCollisionData();
    }
    
    private void refreshCollisionData() {
        execute(() -> {
            double modifier = 1.0; //Only to test different sizes of collision ranges
            //        circle.setRadius(Math.max((int) ((getWidth() / 2) * modifier), (int) ((getHeight() / 2) * modifier)));
            circle.setDiameter(Math.max(getWidth(), getHeight()));
            circle.setLocation(getLocation(true));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameObjectModel getModel() { return model; }
    public final @NotNull AttributeManager attributes() { return attributes; }
    @Override public final @NotNull CollisionMap collisionMap() { return collisionMap; }
    
    public final MoveCommand getCommand() { return command; }
    public final String getObjID() { return objID; }
    public final String setObjID(@Nullable String newValue) {
        String oldValue = getObjID();
        objID = newValue;
        return oldValue;
    }
    
    //<editor-fold desc="> Map Properties">
    
    //<editor-fold desc=">> Location Properties">
    
    @Override public final @NotNull DoubleProperty xLocationProperty() { return (DoubleProperty) xLocationProperty(false); }
    public final @NotNull ObservableDoubleValue xLocationProperty(boolean center) { return center ? xLocationCenteredBinding : xLocationProperty; }
    
    public final double getLocationX(boolean center) { return center ? xLocationCenteredBinding.get() : xLocationProperty.get(); }
    public final double setLocationX(@NotNull Number newValue, boolean center) { return Props.setProperty(xLocationProperty, center ? newValue.doubleValue() - (getWidth() / 2D) : newValue.doubleValue()); }
    public final double moveX(@NotNull Number amount) { return setLocationX(getLocationX(false) + amount.doubleValue(), false); }
    
    public final double getTileLocationX(boolean center) { return pixelToTile(getLocationX(center)).doubleValue(); }
    public final double setTileLocationX(@NotNull Number newValue, boolean center) { return pixelToTile(setLocationX(tileToPixel(newValue), center)).doubleValue(); }
    public final double moveTileX(@NotNull Number amount) { return pixelToTile(moveX(tileToPixel(amount))).doubleValue(); }
    
    
    @Override public final @NotNull DoubleProperty yLocationProperty() { return (DoubleProperty) yLocationProperty(false); }
    public final @NotNull ObservableDoubleValue yLocationProperty(boolean center) { return center ? yLocationCenteredBinding : yLocationProperty; }
    
    public final double getLocationY(boolean center) { return center ? yLocationCenteredBinding.get() : yLocationProperty.get(); }
    public final double setLocationY(@NotNull Number newValue, boolean center) { return Props.setProperty(yLocationProperty, center ? newValue.doubleValue() - (getHeight() / 2D) : newValue.doubleValue()); }
    public final double moveY(@NotNull Number amount) { return setLocationY(getLocationY(false) + amount.doubleValue(), false); }
    
    public final double getTileLocationY(boolean center) { return pixelToTile(getLocationY(center)).doubleValue(); }
    public final double setTileLocationY(@NotNull Number newValue, boolean center) { return pixelToTile(setLocationY(tileToPixel(newValue), center)).doubleValue(); }
    public final double moveTileY(@NotNull Number amount) { return pixelToTile(moveY(tileToPixel(amount))).doubleValue(); }
    
    //
    
    //<editor-fold desc="> Location Bindings">
    
    public final ObjectBinding<Point2D> locationBinding(boolean center) { return center ? locationCenteredBinding : locationBinding; }
    
    public final Point2D getLocation(boolean center) { return locationBinding(center).get(); }
    public final Point2D setLocation(@NotNull Point2D newValue, boolean center) {
        return new Point2D(setLocationX(newValue.getX(), center), setLocationY(newValue.getY(), center));
    }
    
    public final Point2D move(@NotNull Number x, @NotNull Number y) { return new Point2D(moveX(x), moveY(y)); }
    public final Point2D move(@NotNull Point2D amount) { return move(amount.getX(), amount.getY()); }
    public final Point2D move(@NotNull NumberValuePairable<?> amount) { return move(amount.asPoint()); }
    
    // Tile Location
    
    public final Point2D getTileLocation(boolean center) { return new Point2D(getTileLocationX(center), getTileLocationY(center)); }
    public final Point2D setTileLocation(@NotNull Point2D newValue, boolean center) {
        return new Point2D(setTileLocationX(newValue.getX(), center), setTileLocationY(newValue.getY(), center));
    }
    
    
    public final Point2D moveTile(@NotNull Number x, @NotNull Number y) { return new Point2D(moveTileX(x), moveTileY(y)); }
    public final Point2D moveTile(@NotNull Point2D amount) { return moveTile(amount.getX(), amount.getY()); }
    public final Point2D moveTile(@NotNull NumberValuePairable<?> amount) { return moveTile(amount.asPoint()); }
    
    //</editor-fold>
    
    //
    
    public final ObjectBinding<GameTile[][]> occupiedTilesBinding() { return occupiedTilesBinding; }
    public final GameTile[][] getOccupiedTiles() { return occupiedTilesBinding.get(); }
    
    
    //</editor-fold>
    
    
    public final IntegerProperty widthProperty() { return widthProperty; }
    public final int getWidth() { return widthProperty.get(); }
    public final int setWidth(int newValue) { return Props.setProperty(widthProperty, newValue); }
    
    public final IntegerProperty heightProperty() { return heightProperty; }
    public final int getHeight() { return heightProperty.get(); }
    public final int setHeight(int newValue) { return Props.setProperty(heightProperty, newValue); }
    
    
    public final ObjectBinding<Dimensions> dimensionsBinding() { return dimensionsBinding; }
    public final Dimensions getDimensions() { return dimensionsBinding.get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
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
    
    private @NotNull GameTile[][] calculateOccupiedTiles() {
        final int adjustedMinX = (int) Math.floor(getLocationX(false) / getGameMap().getTileSize());
        final int adjustedMinY = (int) Math.floor(getLocationY(false) / getGameMap().getTileSize());
        final int adjustedMaxX = (int) Math.floor((getWidth() - 1 + getLocationX(false)) / getGameMap().getTileSize());
        final int adjustedMaxY = (int) Math.floor((getHeight() - 1 + getLocationY(false)) / getGameMap().getTileSize());
        
        
        final GameTile[][] occupyingGameTiles = new GameTile[(adjustedMaxX - adjustedMinX) + 1][(adjustedMaxY - adjustedMinY) + 1];
        for (int i = 0; i < occupyingGameTiles.length; i++)
            for (int j = 0; j < occupyingGameTiles[i].length; j++)
                occupyingGameTiles[i][j] = getGameMap().getTileMatrix()[i + adjustedMinX][j + adjustedMinY];
        
        return occupyingGameTiles;
    }
    
    
    public final boolean isAtPoint(@NotNull Point2D point) { return isAtPoint(point, true); }
    public final boolean isAtPoint(@NotNull Point2D point, boolean center) {
        final double v1 = Math.round((getLocationX(center))) - Math.round(point.getX());
        final double v2 = Math.round(getLocationY(center)) - Math.round(point.getY());
        return Math.abs(v1) == 0 && Math.abs(v2) == 0;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TESTING ---">
    
    public final boolean isTestObjectAny() { return isTestObject1() || isTestObject2(); }
    public final boolean isTestObject1() { return this.equals(getGame().getTestObject()); }
    public final boolean isTestObject2() { return this.equals(getGame().getTestObject2()); }
    
    //</editor-fold>
}
