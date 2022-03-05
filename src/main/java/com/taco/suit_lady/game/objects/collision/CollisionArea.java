package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.shapes.Shape;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.NumberValuePair;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CollisionArea<T extends Collidable<T>>
        implements SpringableWrapper, Lockable {
    
    private final ReentrantLock lock;
    private final ReadOnlyObjectWrapper<CollisionMap<T>> ownerProperty;
    
    private final ListProperty<Shape> includedShapes;
    private final ListProperty<Shape> excludedShapes;
    
    public CollisionArea(CollisionMap<T> owner) {
        this.lock = new ReentrantLock();
        this.ownerProperty = new ReadOnlyObjectWrapper<>(owner);
        
        this.includedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.excludedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<CollisionMap<T>> ownerProperty() { return ownerProperty; }
    public final ReadOnlyObjectProperty<CollisionMap<T>> readOnlyOwnerProperty() { return ownerProperty.getReadOnlyProperty(); }
    public final CollisionMap<T> getOwner() { return ownerProperty.get(); }
    protected final CollisionMap<T> setOwner(CollisionMap<T> newValue) { return Props.setProperty(ownerProperty, newValue); }
    
    public final ListProperty<Shape> includedShapes() { return includedShapes; }
    public final ListProperty<Shape> excludedShapes() { return excludedShapes; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Contains Point Methods">
    
    public final boolean containsPoint(@NotNull NumberValuePair point) {
        return sync(() -> {
            for (Shape excluded: excludedShapes())
                if (excluded.contains(point))
                    return false;
            for (Shape included: includedShapes())
                if (included.contains(point))
                    return true;
            return false;
        });
    }
    
    public final boolean containsPoint(@NotNull Number x, @NotNull Number y) { return containsPoint(new NumberValuePair(x, y)); }
    public final boolean containsPoint(@NotNull Point2D point) { return containsPoint(new NumberValuePair(point.getX(), point.getY())); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Collision Check Methods">
    
    public final boolean collidesWith(@NotNull CollisionArea<?> other, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            for (Shape included: includedShapes())
                for (NumberValuePair borderPoint: included.getBorderPoints(xMod, yMod))
                    if (other.containsPoint(borderPoint))
                        return true;
            //            for (Shape otherIncluded: other.includedShapes())
            //                for (NumberValuePair borderPoint: otherIncluded.getBorderPoints(xMod, yMod))
            //                    if (contains(borderPoint))
            //                        return true;
            return false;
        });
    }
    public final boolean collidesWith(@NotNull CollisionArea<?> other) { return collidesWith(other, 0, 0); }
    
    public final boolean collidesWith(@NotNull CollisionMap<?> collisionMap, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> collisionMap.collisionAreasCopy().stream().anyMatch(other -> collidesWith(other, xMod, yMod)));
    }
    public final boolean collidesWith(@NotNull CollisionMap<?> collisionMap) { return collidesWith(collisionMap, 0, 0); }
    
    public final boolean collidesWith(@NotNull Collidable<?> collidable, @NotNull Number xMod, @NotNull Number yMod) { return collidesWith(collidable.collisionMap(), xMod, yMod); }
    public final boolean collidesWith(@NotNull Collidable<?> collidable) { return collidesWith(collidable, 0, 0); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
}
