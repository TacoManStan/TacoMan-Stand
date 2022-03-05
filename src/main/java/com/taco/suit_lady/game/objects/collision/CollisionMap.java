package com.taco.suit_lady.game.objects.collision;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.util.values.NumberValuePairable;
import com.taco.suit_lady.util.values.ValuePairable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class CollisionMap<T extends Collidable<T>>
        implements SpringableWrapper, Lockable {
    
    private final T owner;
    
    //    private final ReadOnlyIntegerWrapper widthProperty;
    //    private final ReadOnlyIntegerWrapper heightProperty;
    
    private final ListProperty<CollisionArea<T>> collisionAreas;
    
    public CollisionMap(T owner) {
        this.owner = owner;
        
        //        this.widthProperty = new ReadOnlyIntegerWrapper();
        //        this.heightProperty = new ReadOnlyIntegerWrapper();
        
        this.collisionAreas = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected CollisionMap<T> init() {
        //        widthProperty.bind(getOwner().widthProperty());
        //        heightProperty.bind(getOwner().heightProperty());
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final T getOwner() { return owner; }
    
    public final @NotNull List<CollisionArea<T>> collisionAreasCopy() { return new ArrayList<>(collisionAreas); }
    
    public final boolean addCollisionArea(@NotNull CollisionArea<T> area) {
        return FX.forbidFX(getLock(), () -> {
            if (collisionAreas.contains(area))
                throw Exc.ex("Collision Area is already contained in this Collision Map:  [" + area + "]");
            return collisionAreas.add(area);
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    @Override public boolean isNullableLock() { return getOwner().isNullableLock(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Contains Point Methods">
    
    public final boolean containsPoint(@NotNull Number x, @NotNull Number y) {
        return sync(() -> collisionAreas.stream().anyMatch(area -> area.containsPoint(x, y)));
    }
    
    public final boolean containsPoint(@NotNull Point2D point) { return containsPoint(point.getX(), point.getY()); }
    public final boolean containsPoint(@NotNull ValuePairable<Number, Number> point) { return containsPoint(point.a(), point.b()); }
    
    //</editor-fold>
    
    
    //<editor-fold desc="> Collision Checks">
    
    public final boolean collidesWith(@NotNull CollisionMap<?> other, @NotNull Number xMod, @NotNull Number yMod) {
        return !this.equals(other) && FX.forbidFX(
                getLock(), () -> other.collisionAreasCopy().stream().anyMatch(
                        otherArea -> this.collidesWith(otherArea, xMod, yMod)));
    }
    
    public final boolean collidesWith(@NotNull CollisionMap<?> other, @NotNull Point2D mod) { return collidesWith(other, mod.getX(), mod.getY()); }
    public final boolean collidesWith(@NotNull CollisionMap<?> other, @NotNull NumberValuePairable<?> mod) { return collidesWith(other, mod.asPoint()); }
    
    public final boolean collidesWith(@NotNull CollisionMap<?> other, @NotNull Number mod) { return collidesWith(other, mod, mod); }
    public final boolean collidesWith(@NotNull CollisionMap<?> other) { return collidesWith(other, 0, 0); }
    
    //
    
    public final boolean collidesWith(@NotNull CollisionArea<?> other, @NotNull Number xMod, @NotNull Number yMod) {
        return !this.equals(other.getOwner()) && FX.forbidFX(
                getLock(), () -> collisionAreas.stream().anyMatch(
                        area -> area.collidesWith(other, xMod, yMod)));
    }
    
    public final boolean collidesWith(@NotNull CollisionArea<?> other, @NotNull Point2D mod) { return collidesWith(other, mod.getX(), mod.getY()); }
    public final boolean collidesWith(@NotNull CollisionArea<?> other, @NotNull NumberValuePairable<?> mod) { return collidesWith(other, mod.asPoint()); }
    
    public final boolean collidesWith(@NotNull CollisionArea<?> other, @NotNull Number mod) { return collidesWith(other, mod, mod); }
    public final boolean collidesWith(@NotNull CollisionArea<?> other) { return collidesWith(other, 0, 0); }
    
    //</editor-fold>
    
    //</editor-fold>
}
