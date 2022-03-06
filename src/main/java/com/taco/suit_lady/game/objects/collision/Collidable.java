package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Obj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public interface Collidable<T extends Collidable<T>>
        extends SpringableWrapper, Lockable, GameComponent {
    
    @NotNull CollisionMap<T> collisionMap();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default boolean collidesWith(@NotNull Collidable<?> other, @NotNull Number xMod, @NotNull Number yMod) { return collisionMap().collidesWith(other.collisionMap(), xMod, yMod); }
    default boolean collidesWith(@NotNull Collidable<?> other) { return collidesWith(other, 0, 0); }
    
    //
    
    default boolean isSibling(@NotNull Collidable<?> other) { return Obj.equals(collisionMap(), other.collisionMap()); }
    
    //</editor-fold>
}
