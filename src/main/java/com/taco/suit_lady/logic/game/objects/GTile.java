package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.GMap;

public class GTile {
    
    private final GMap owner;
    
    private final int xLoc;
    private final int yLoc;
    
    public GTile(GMap owner, int xLoc, int yLoc) {
        this.owner = owner;
        
        this.xLoc = xLoc;
        this.yLoc = yLoc;
    }
    
    public final GTile getNeighbor(int x, int y) {
        return owner.getNeighbor(this, x, y);
    }
    
    public final int getXLoc() {
        return xLoc;
    }
    
    public final int getYLoc() {
        return yLoc;
    }
}
