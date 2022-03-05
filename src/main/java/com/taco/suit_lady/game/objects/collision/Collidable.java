package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;

public interface Collidable<T extends Collidable<T>>
        extends Springable, Lockable {
    
    @NotNull CollisionMap<T> collisionMap();
    
    //
    
    default boolean collidesWith(CollisionMap<?> other, @NotNull Number xMod, @NotNull Number yMod) { return collisionMap().collidesWith(other, xMod, yMod); }
    default boolean collidesWith(CollisionArea<?> otherArea, @NotNull Number xMod, @NotNull Number yMod) { return collisionMap().collidesWith(otherArea, xMod, yMod); }
    default boolean collidesWith(Collidable<?> other, @NotNull Number xMod, @NotNull Number yMod) { return collisionMap().collidesWith(other.collisionMap(), xMod, yMod); }
    
    default boolean collidesWith(CollisionArea<?> otherArea) { return collidesWith(otherArea, 0, 0); }
    default boolean collidesWith(CollisionMap<?> other) { return collidesWith(other, 0, 0); }
    default boolean collidesWith(Collidable<?> other) { return collidesWith(other, 0, 0); }
}
