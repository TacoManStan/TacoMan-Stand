package com.taco.suit_lady.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.Entity;
import com.taco.suit_lady.game.commands.MoveCommand;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ObjectsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameObject
        implements Lockable, Entity, JObject, JLoadable, UIDProcessable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final GameViewContent content;
    
    private final GameObjectModel model;
    private final AttributeManager attributes;
    
    
    private final DoubleProperty xLocationProperty;
    private final DoubleProperty yLocationProperty;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    //
    
    private final DoubleBinding xLocationCenteredBinding;
    private final DoubleBinding yLocationCenteredBinding;
    
    private ObjectBinding<GameTile[][]> occupiedTilesBinding = null;
    private final ListProperty<GameTile> occupiedTilesList;
    
    
    private String objID;
    
    //
    
    private final MoveCommand command;
    
    private final ObservableList<Tickable> tickables;
    
    public GameObject(@NotNull GameViewContent content, @Nullable ReentrantLock lock) {
        this.springable = content.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.content = content;
        
        this.model = new GameObjectModel(this);
        this.attributes = new AttributeManager(this);
        
        
        this.xLocationProperty = new SimpleDoubleProperty();
        this.yLocationProperty = new SimpleDoubleProperty();
        
        this.widthProperty = new SimpleIntegerProperty(32);
        this.heightProperty = new SimpleIntegerProperty(32);
        
        //
        
        this.xLocationCenteredBinding = BindingsSL.doubleBinding(() -> getLocationX(false) + (getWidth() / 2D), xLocationProperty, widthProperty);
        this.yLocationCenteredBinding = BindingsSL.doubleBinding(() -> getLocationY(false) + (getHeight() / 2D), yLocationProperty, heightProperty);
        
        //
        
        initAttributes();
        
        
        this.command = new MoveCommand(this);
        
        this.tickables = new SimpleListProperty<>(FXCollections.observableArrayList());
        tickables.add(command);
        
        //
        
        this.occupiedTilesList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameObject init() {
        getModel().init();
        
        this.occupiedTilesBinding = BindingsSL.objBinding(this::calculateOccupiedTiles, xLocationProperty, yLocationProperty, widthProperty, heightProperty, gameMapProperty());
        //TODO: This is more efficient than before but still comically inefficient - not a problem now but will quickly become one as more GameObjects are added to the map (and moving at the same time)
        this.occupiedTilesBinding.addListener((observable, oldValue, newValue) -> {
            final ArrayList<GameTile> oldTiles = new ArrayList<>();
            final ArrayList<GameTile> newTiles = new ArrayList<>();
            
            ArraysSL.iterateMatrix(tile -> oldTiles.add(tile), oldValue);
            ArraysSL.iterateMatrix(tile -> newTiles.add(tile), newValue);
            
            oldTiles.forEach(tile -> ObjectsSL.doIf(() -> tile, t -> !newTiles.contains(t), t -> t.getOccupyingObjects().remove(this)));
            newTiles.forEach(tile -> ObjectsSL.doIf(() -> tile, t -> !t.getOccupyingObjects().contains(this), t -> t.getOccupyingObjects().add(this)));
        });
        
        return this;
    }
    
    private void initAttributes() {
        attributes().addDoubleAttribute(MoveCommand.ATTRIBUTE_ID, 100);
        attributes().addAttribute("health", 500);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull AttributeManager attributes() { return attributes; }
    
    public final GameObjectModel getModel() { return model; }
    public final MoveCommand getCommand() { return command; }
    public final String getObjID() { return objID; }
    
    //<editor-fold desc="> Map Properties">
    
    //<editor-fold desc=">> Location Properties">
    
    public final DoubleProperty xLocationProperty() { return xLocationProperty; }
    public final DoubleBinding xLocationCenteredBinding() { return xLocationCenteredBinding; }
    
    public final double getLocationX(boolean center) { return center ? xLocationCenteredBinding.get() : xLocationProperty.get(); }
    public final double setLocationX(@NotNull Number newValue) { return PropertiesSL.setProperty(xLocationProperty, newValue.doubleValue()); }
    
    public final double setTileLocationX(@NotNull Number newValue) { return PropertiesSL.setProperty(xLocationProperty, newValue.doubleValue() * getGameMap().getTileSize()); }
    public final double moveX(@NotNull Number amount) { return setLocationX(getLocationX(false) + amount.doubleValue()); }
    public final double moveTileX(@NotNull Number amount) { return setLocationX(getLocationX(false) + (amount.doubleValue() * getGameMap().getTileSize())); }
    
    
    public final DoubleProperty yLocationProperty() { return yLocationProperty; }
    public final DoubleBinding yLocationCenteredBinding() { return yLocationCenteredBinding; }
    
    public final double getLocationY(boolean center) { return center ? yLocationCenteredBinding.get() : yLocationProperty.get(); }
    public final double setLocationY(@NotNull Number newValue) { return PropertiesSL.setProperty(yLocationProperty, newValue.doubleValue()); }
    
    public final double setTileLocationY(@NotNull Number newValue) { return PropertiesSL.setProperty(yLocationProperty, newValue.doubleValue() * getGameMap().getTileSize()); }
    public final double moveY(@NotNull Number amount) { return setLocationY(getLocationY(false) + amount.doubleValue()); }
    public final double moveTileY(@NotNull Number amount) { return setLocationY(getLocationY(false) + (amount.doubleValue() * getGameMap().getTileSize())); }
    
    
    public final ObjectBinding<GameTile[][]> occupiedTilesBinding() { return occupiedTilesBinding; }
    public final GameTile[][] getOccupiedTiles() { return occupiedTilesBinding.get(); }
    
    
    //</editor-fold>
    
    public final boolean isAtPoint(@NotNull Point2D point, boolean center) {
        return Math.abs(Math.round((getLocationX(center))) - Math.round(point.getX())) <= 1 && Math.abs(Math.round(getLocationY(center)) - Math.round(point.getY())) <= 1;
    }
    
    public final IntegerProperty widthProperty() { return widthProperty; }
    public final int getWidth() { return widthProperty.get(); }
    public final int setWidth(int newValue) { return PropertiesSL.setProperty(widthProperty, newValue); }
    
    public final IntegerProperty heightProperty() { return heightProperty; }
    public final int getHeight() { return heightProperty.get(); }
    public final int setHeight(int newValue) { return PropertiesSL.setProperty(heightProperty, newValue); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    //<editor-fold desc="> Logic">
    
    @Override public void tick(double ups) { }
    
    @Override public boolean hasSubActions() { return true; }
    @Override public final @NotNull List<Tickable> subActions() { return tickables; }
    
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
        setLocationX(JUtil.loadDouble(parent, "x-location"));
        setLocationY(JUtil.loadDouble(parent, "y-location"));
        JUtil.loadObject(parent, "model", getModel());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Generic">
    
    @Override public @NotNull FxWeaver weaver() { return springable.weaver(); }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return springable.ctx(); }
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    
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
    
    //</editor-fold>
    
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
}
