package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.GameMap;

public class GameTile {
    
    private final GameMap owner;
    
    private final int xLoc;
    private final int yLoc;
    
    public GameTile(GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xLoc = xLoc;
        this.yLoc = yLoc;
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
    
    @Override public String toString() {
        return "GameTile{" +
               "owner=" + owner +
               ", xLoc=" + xLoc +
               ", yLoc=" + yLoc +
               '}';
    }
}
