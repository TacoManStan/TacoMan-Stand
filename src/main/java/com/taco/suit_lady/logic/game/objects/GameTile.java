package com.taco.suit_lady.logic.game.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.tacository.json.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class GameTile
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadable {
    
    private int xLoc;
    private int yLoc;
    
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
        
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        
        this.occupyingObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        
        this.model = new GameTileModel(this);
    }
    
    public final GameTile getNeighbor(int x, int y) { return owner.getNeighbor(this, x, y); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final int getXLoc() { return xLoc; }
    private int setXLoc(@NotNull Number newValue) {
        int oldValue = getXLoc();
        xLoc = newValue.intValue();
        return oldValue;
    }
    
    public final int getYLoc() { return yLoc; }
    private int setYLoc(@NotNull Number newValue) {
        int oldValue = getYLoc();
        yLoc = newValue.intValue();
        return oldValue;
    }
    
    
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
               ", xLoc=" + xLoc +
               ", yLoc=" + yLoc +
               '}';
    }
    
    //
    
    @Override public String getJID() { return "map-tile"; }
    @Override public void load(JsonObject parent) {
        setModel(JUtil.loadObject(parent, new GameTileModel(this)));
        setXLoc(JUtil.loadInt(parent, "x-loc"));
        setYLoc(JUtil.loadInt(parent, "y-loc"));
    }
    @Override public JElement[] jFields() {
        return new JElement[]{
                JUtil.createObject("game-tile-model", getModel()),
                JUtil.create("x-loc", getXLoc()),
                JUtil.create("y-loc", getYLoc())
        };
    }
    
    //</editor-fold>
}
