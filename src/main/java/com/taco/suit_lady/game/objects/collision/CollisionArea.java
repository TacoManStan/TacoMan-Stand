package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.values.shapes.Box;
import com.taco.suit_lady.util.values.shapes.Shape;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

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
    
    public final boolean containsPoint(@NotNull Num2D point) {
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
    
    public final boolean containsPoint(@NotNull Number x, @NotNull Number y) { return containsPoint(new Num2D(x, y)); }
    public final boolean containsPoint(@NotNull Point2D point) { return containsPoint(new Num2D(point.getX(), point.getY())); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Collision Check Methods">
    
    public final boolean collidesWithArea(@NotNull CollisionArea<?> other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> collidesWith(translate, xMod, yMod, other.shapes()));
    }
    public final boolean collidesWithArea(@NotNull CollisionArea<?> other, boolean translate) { return collidesWithArea(other, translate, 0, 0); }
    
    public final boolean collidesWithMap(@NotNull CollisionMap<?> collisionMap, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> collisionMap.collisionAreasCopy().stream().anyMatch(other -> collidesWithArea(other, translate, xMod, yMod)));
    }
    public final boolean collidesWithMap(@NotNull CollisionMap<?> collisionMap, boolean translate) { return collidesWithMap(collisionMap, translate, 0, 0); }
    
    @Override public final boolean collidesWith(@NotNull Collidable<?> collidable, boolean translate, @NotNull Number xMod, @NotNull Number yMod) { return collidesWithMap(collidable.collisionMap(), translate, xMod, yMod); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Bounds Methods">
    
    @Override public final @NotNull Box boundsBox(boolean synchronize, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        final Lock lock = synchronize ? getLock() : null;
        return Calc.boundsBox(this, lock, pixelGenerator, includedShapes());
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull CollisionMap<T> collisionMap() { return collisionMap; }
    @Override public @NotNull List<Shape> shapeList() { return sync(() -> Collections.unmodifiableList(includedShapes())); }
    
    @Override public boolean collidesWith(boolean translate, @NotNull Number xMod, @NotNull Number yMod, @NotNull Shape... shapes) {
        return syncForbidFX(() -> {
            for (Shape included: shapeList())
                for (Shape otherIncluded: shapes)
                    if (included.intersects(otherIncluded, translate, xMod, yMod))
                        return true;
            return false;
        });
    }
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return collisionMap().getGame(); }
    
    @Override public @NotNull Springable springable() { return collisionMap(); }
    @Override public @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
}
