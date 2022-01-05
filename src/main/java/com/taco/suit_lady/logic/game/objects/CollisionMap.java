package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.Entity;

public class CollisionMap {
    
    private final Entity owner;
    
    public CollisionMap(Entity owner) {
        this.owner = owner;
    }
    
    public final Entity getOwner() {
        return owner;
    }
}
