package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.GameMap;

public class Tile {
    
    private final GameMap owner;
    
    private final int xLoc;
    private final int yLoc;
    
    public Tile(GameMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }
    
    public final Tile getNeighbor(int x, int y) {
        return owner.getNeighbor(this, x, y);
    }
    
    public final int getXLoc() {
        return xLoc;
    }
    
    public final int getYLoc() {
        return yLoc;
    }
}
