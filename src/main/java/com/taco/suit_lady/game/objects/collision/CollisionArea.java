package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.shapes.Shape;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.values.NumberValuePair;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CollisionArea<T extends Collidable<T>>
        implements Collidable<T> {
    
    private final ReentrantLock lock;
    
    private final CollisionMap<T> collisionMap;
    
    private final ListProperty<Shape> includedShapes;
    private final ListProperty<Shape> excludedShapes;
    
    public CollisionArea(@NotNull CollisionMap<T> collisionMap) {
        this.lock = new ReentrantLock();
        
        this.collisionMap = collisionMap;
        
        this.includedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.excludedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ListProperty<Shape> includedShapes() { return includedShapes; }
    public final ListProperty<Shape> excludedShapes() { return excludedShapes; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Contains Point Methods">
    
    public final boolean containsPoint(@NotNull NumberValuePair point) {
        return sync(() -> {
            for (Shape excluded: excludedShapes())
                if (excluded.containsPoint(point))
                    return false;
            for (Shape included: includedShapes())
                if (included.containsPoint(point))
                    return true;
            return false;
        });
    }
    
    public final boolean containsPoint(@NotNull Number x, @NotNull Number y) { return containsPoint(new NumberValuePair(x, y)); }
    public final boolean containsPoint(@NotNull Point2D point) { return containsPoint(new NumberValuePair(point.getX(), point.getY())); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Collision Check Methods">
    
    public final boolean collidesWithArea(@NotNull CollisionArea<?> other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            for (Shape included: includedShapes())
                for (Shape otherIncluded: other.includedShapes())
                    if (included.intersects(otherIncluded, translate, xMod, yMod))
                        return true;
//                for (NumberValuePair borderPoint: included.getBorderPoints(translate, xMod, yMod))
//                    if (other.containsPoint(borderPoint))
//                        return true;
            //            for (Shape otherIncluded: other.includedShapes())
            //                for (NumberValuePair borderPoint: otherIncluded.getBorderPoints(xMod, yMod))
            //                    if (contains(borderPoint))
            //                        return true;
            return false;
        });
    }
    public final boolean collidesWithArea(@NotNull CollisionArea<?> other, boolean translate) { return collidesWithArea(other, translate, 0, 0); }
    
    public final boolean collidesWithMap(@NotNull CollisionMap<?> collisionMap, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> collisionMap.collisionAreasCopy().stream().anyMatch(other -> collidesWithArea(other, translate, xMod, yMod)));
    }
    public final boolean collidesWithMap(@NotNull CollisionMap<?> collisionMap, boolean translate) { return collidesWithMap(collisionMap, translate, 0, 0); }
    
    public final boolean collidesWith(@NotNull Collidable<?> collidable, boolean translate, @NotNull Number xMod, @NotNull Number yMod) { return collidesWithMap(collidable.collisionMap(), translate, xMod, yMod); }
    public final boolean collidesWith(@NotNull Collidable<?> collidable, boolean translate) { return collidesWith(collidable, translate, 0, 0); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CollisionMap<T> collisionMap() { return collisionMap; }
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return collisionMap().getGame(); }
    
    @Override public @NotNull Springable springable() { return collisionMap(); }
    @Override public @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
}
