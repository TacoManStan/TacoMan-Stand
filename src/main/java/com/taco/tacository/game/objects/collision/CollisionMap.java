package com.taco.tacository.game.objects.collision;

import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.values.numbers.shapes.Box;
import com.taco.tacository.util.values.numbers.shapes.Shape;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Calc;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.fx_tools.FX;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import com.taco.tacository.util.values.ValueExpr2D;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <p>Defines a {@link ListProperty List} of {@link CollisionArea Collision Areas} that together comprise the {@code collision data} for this {@link CollisionMap}.</p>
 * @param <T>
 */
//TO-EXPAND
public class CollisionMap<T extends Collidable<T>>
        implements SpringableWrapper, Lockable, Collidable<T> {
    
    private final T owner;
    
    private final ListProperty<CollisionArea<T>> collisionAreas;
    
    public CollisionMap(T owner) {
        if (owner instanceof CollisionMap<?>)
            throw Exc.typeMismatch("CollisionMaps cannot be the owner of another CollisionMap.");
        this.owner = owner;
        
        this.collisionAreas = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected CollisionMap<T> init() {
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull T getOwner() { return owner; }
    
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
    
    @Override public final @NotNull CollisionMap<T> collisionMap() { return this; }
    
    //
    
    @Override public @NotNull GameViewContent getContent() { return getOwner().getGame(); }
    
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
    public final boolean containsPoint(@NotNull ValueExpr2D<Number, Number> point) { return containsPoint(point.a(), point.b()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Collision Checks">
    
    @Override public final boolean collidesWith(@NotNull Collidable<?> other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        return !isSibling(other) && FX.forbidFX(
                getLock(), () -> collisionAreas.stream().anyMatch(
                        area -> area.collidesWith(other, translate, xMod, yMod)));
    }
    @Override public boolean collidesWith(boolean translate, @NotNull Number xMod, @NotNull Number yMod, @NotNull Shape... shapes) {
        return FX.forbidFX(() -> {
            final List<Shape> shapesThis = shapeList();
            for (Shape shape: shapesThis)
                for (Shape otherShape: shapes)
                    if (shape.intersects(otherShape, translate, xMod, yMod))
                        return true;
            return false;
        });
    }
    
    @Override public @NotNull List<Shape> shapeList() {
        return FX.forbidFX(() -> {
            final ArrayList<Shape> shapes = new ArrayList<>();
            for (CollisionArea<T> area: collisionAreasCopy())
                shapes.addAll(area.shapeList());
            return shapes;
        });
    }
    
    //
    
    public final boolean collidesWith(@NotNull Collidable<?> other, boolean translate, @NotNull Point2D mod) { return collidesWith(other, translate, mod.getX(), mod.getY()); }
    public final boolean collidesWith(@NotNull Collidable<?> other, boolean translate, @NotNull NumExpr2D<?> mod) { return collidesWith(other, translate, mod.asPoint()); }
    
    public final boolean collidesWith(@NotNull Collidable<?> other, boolean translate, @NotNull Number mod) { return collidesWith(other, translate, mod, mod); }
    public final boolean collidesWith(@NotNull Collidable<?> other, boolean translate) { return collidesWith(other, translate, 0, 0); }
    
    //</editor-fold>
    
    @Override public final @NotNull Box boundsBox(boolean synchronize, @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, Color> pixelGenerator) {
        final Lock lock = synchronize ? getLock() : null;
        return Calc.boundsBox(this, lock, pixelGenerator, getShapes());
    }
    
    public final @NotNull List<Shape> getShapes() {
        return sync(() -> collisionAreasCopy()
                .stream()
                .flatMap(area -> area.includedShapes().stream())
                .collect(Collectors.toCollection(ArrayList::new)));
    }
    
    //</editor-fold>
}
