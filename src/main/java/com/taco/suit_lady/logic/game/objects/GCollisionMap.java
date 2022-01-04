package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.GEntity;

public class GCollisionMap {
    
    private final GEntity owner;
    
    public GCollisionMap(GEntity owner) {
        this.owner = owner;
    }
    
    public final GEntity getOwner() {
        return owner;
    }
}
