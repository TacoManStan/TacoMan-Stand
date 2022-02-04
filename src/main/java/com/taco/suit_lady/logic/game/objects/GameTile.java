package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

public class GameTile {
    
    private final GameMap owner;
    
    private final int xLoc;
    private final int yLoc;
    
    private final ListProperty<GameObject> occupyingObjects;
    
    public GameTile(@NotNull GameMap owner, @NotNull Dimensions dimensions) {
        this(owner, dimensions.width(), dimensions.height());
    }
    
    public GameTile(@NotNull GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        
        this.occupyingObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    public final GameTile getNeighbor(int x, int y) {
        return owner.getNeighbor(this, x, y);
    }
    
    public final int getXLoc() {
        return xLoc;
    }
    
    public final int getYLoc() {
        return yLoc;
    }
    
    public final ListProperty<GameObject> getOccupyingObjects() { return occupyingObjects; }
    
    @Override public String toString() {
        return "GameTile{" +
               "owner=" + owner +
               ", xLoc=" + xLoc +
               ", yLoc=" + yLoc +
               '}';
    }
}
