package com.taco.tacository.game.objects.collision;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.util.values.numbers.shapes.Box;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Obj;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import com.taco.tacository.util.values.numbers.shapes.Shape;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>Defines implementing classes as {@link Collidable}, providing a {@link CollisionMap} for each implementation.</p>
 * @param <T>
 */
//TO-EXPAND
public interface Collidable<T extends Collidable<T>>
        extends SpringableWrapper, Lockable, GameComponent {
    
    @NotNull CollisionMap<T> collisionMap();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default @NotNull List<Shape> shapeList() { return collisionMap().shapeList(); }
    default @NotNull Shape[] shapes() { return shapeList().toArray(new Shape[0]); }
    
    //
    
    default boolean collidesWith(@NotNull Collidable<?> other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) { return collisionMap().collidesWith(other.collisionMap(), translate, xMod, yMod); }
    default boolean collidesWith(@NotNull Collidable<?> other, boolean translate) { return collidesWith(other, translate, 0, 0); }
    
    //
    
    default boolean collidesWith(boolean translate, @NotNull Number xMod, @NotNull Number yMod, @NotNull Shape... shapes) { return collisionMap().collidesWith(translate, xMod, yMod, shapes); }
    default boolean collidesWith(@NotNull Shape... shapes) { return collidesWith(true, 0, 0, shapes); }
    default boolean collidesWith(@NotNull List<Shape> shapes) { return collidesWith(shapes.toArray(new Shape[0])); }
    
    //
    
    default @NotNull Box boundsBox(boolean synchronize, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        return collisionMap().boundsBox(synchronize, pixelGenerator);
    }
    
    default @NotNull Box boundsBox(boolean synchronize) { return boundsBox(synchronize, null); }
    default @NotNull Box boundsBox(@Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) { return boundsBox(false, pixelGenerator); }
    default @NotNull Box boundsBox() { return boundsBox(false, null); }
    
    //
    
    default boolean isSibling(@NotNull Collidable<?> other) { return Obj.equals(collisionMap(), other.collisionMap()); }
    
    //</editor-fold>
}
