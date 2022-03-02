package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.util.values.NumberValuePair;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class CollisionArea
        implements WrappedGameComponent {
    
    private final ReadOnlyObjectWrapper<CollisionMap> ownerProperty;
    
    protected CollisionArea(CollisionMap owner) {
        this.ownerProperty = new ReadOnlyObjectWrapper<>(owner);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final ReadOnlyObjectWrapper<CollisionMap> ownerProperty() { return ownerProperty; }
    public final ReadOnlyObjectProperty<CollisionMap> readOnlyOwnerProperty() { return ownerProperty.getReadOnlyProperty(); }
    public final CollisionMap getOwner() { return ownerProperty.get(); }
    protected final CollisionMap setOwner(CollisionMap newValue) { return PropertiesSL.setProperty(ownerProperty, newValue); }
    
//    protected final ReadOnlyObjectWrapper<S> shapeProperty() { return shapeProperty; }
//    public final ReadOnlyObjectProperty<S> readOnlyShapeProperty() { return shapeProperty.getReadOnlyProperty(); }
//    public final S getShape() { return shapeProperty.get(); }
//    protected final S setShape(S newValue) { return PropertiesSL.setProperty(shapeProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return ExceptionsSL.nullCheck(getOwner(), "CollisionMap Owner").getGame(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean collidesWith(CollisionArea other);
    protected abstract boolean containsPoint(NumberValuePair point);
    
    //</editor-fold>
}
