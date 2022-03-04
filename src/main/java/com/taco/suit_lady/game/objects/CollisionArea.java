package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.shapes.Shape;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.NumberValuePair;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CollisionArea
        implements WrappedGameComponent {
    
    private final ReentrantLock lock;
    private final ReadOnlyObjectWrapper<CollisionMap> ownerProperty;
    
    private final ListProperty<Shape> includedShapes;
    private final ListProperty<Shape> excludedShapes;
    
    protected CollisionArea(CollisionMap owner) {
        this.lock = new ReentrantLock();
        this.ownerProperty = new ReadOnlyObjectWrapper<>(owner);
        
        this.includedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.excludedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<CollisionMap> ownerProperty() { return ownerProperty; }
    public final ReadOnlyObjectProperty<CollisionMap> readOnlyOwnerProperty() { return ownerProperty.getReadOnlyProperty(); }
    public final CollisionMap getOwner() { return ownerProperty.get(); }
    protected final CollisionMap setOwner(CollisionMap newValue) { return Props.setProperty(ownerProperty, newValue); }
    
    protected final ListProperty<Shape> includedShapes() { return includedShapes; }
    protected final ListProperty<Shape> excludedShapes() { return excludedShapes; }
    
    //</editor-fold>
    
    public final boolean contains(@NotNull NumberValuePair point) {
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
    
    public final boolean intersects(@NotNull CollisionArea other) { return intersects(other, 0, 0); }
    public final boolean intersects(@NotNull CollisionArea other, @NotNull Number xMod, @NotNull Number yMod) {
        return sync(() -> {
            for (Shape included: includedShapes())
                for (NumberValuePair borderPoint: included.getBorderPoints(xMod, yMod))
                    if (other.contains(borderPoint))
                        return true;
//            for (Shape otherIncluded: other.includedShapes())
//                for (NumberValuePair borderPoint: otherIncluded.getBorderPoints(xMod, yMod))
//                    if (contains(borderPoint))
//                        return true;
            return false;
        });
    }
    
    public final boolean intersectsLegacy(@NotNull CollisionArea other) {
        return sync(() -> {
            printer().get().setPrintPrefix(false);
//            printer().get().print("Checking Intersection...");
//            printer().get().print("Included: " + includedShapes);
//            printer().get().print("Excluded: " + excludedShapes);
            
            for (Shape excluded: excludedShapes())
                for (NumberValuePair borderPoint: excluded.getBorderPoints())
                    if (other.contains(borderPoint))
                        return false;
            for (Shape included: includedShapes())
                for (NumberValuePair borderPoint: included.getBorderPoints())
                    if (other.contains(borderPoint))
                        return true;
            return false;
        });
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @Nullable Lock getLock() { return lock; }
    @Override public @NotNull GameViewContent getGame() { return Exc.nullCheck(getOwner(), "CollisionMap Owner").getGame(); }
    //</editor-fold>
}
