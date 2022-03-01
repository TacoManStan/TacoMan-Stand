package com.taco.suit_lady.game.objects;

import org.jetbrains.annotations.NotNull;

public interface Collidable {
    
    @NotNull CollisionMap collisionMap();
    
    //
    
    default boolean collidesWith(CollisionArea other) { return collisionMap().collidesWith(other); }
    default boolean collidesWith(CollisionMap other) { return collisionMap().collidesWith(other); }
    default boolean collidesWith(Collidable other) { return collisionMap().collidesWith(other.collisionMap()); }
}
