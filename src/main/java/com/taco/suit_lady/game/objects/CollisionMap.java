package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CollisionMap
        implements WrappedGameComponent {
    
    private final GameObject owner;
    
    //    private final ReadOnlyIntegerWrapper widthProperty;
    //    private final ReadOnlyIntegerWrapper heightProperty;
    
    private final ListProperty<CollisionArea> collisionAreas;
    
    public CollisionMap(GameObject owner) {
        this.owner = owner;
        
        //        this.widthProperty = new ReadOnlyIntegerWrapper();
        //        this.heightProperty = new ReadOnlyIntegerWrapper();
        
        this.collisionAreas = new SimpleListProperty<>(FXCollections.observableArrayList());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected CollisionMap init() {
        //        widthProperty.bind(getOwner().widthProperty());
        //        heightProperty.bind(getOwner().heightProperty());
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    public final List<CollisionArea> collisionAreas() { return new ArrayList<>(collisionAreas); }
    public final boolean addCollisionArea(@NotNull CollisionArea area) {
        return ToolsFX.forbidFX(getLock(), () -> {
            if (collisionAreas.contains(area))
                throw ExceptionsSL.ex("Collision Area is already contained in this Collision Map:  [" + area + "]");
            return collisionAreas.add(area);
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean collidesWith(@NotNull CollisionMap other) { return ToolsFX.forbidFX(getLock(), () -> collisionAreas.stream().anyMatch(other::collidesWith)); }
    public final boolean collidesWith(@NotNull CollisionArea other) { return ToolsFX.forbidFX(getLock(), () -> collisionAreas.stream().anyMatch(area -> area.collidesWith(other))); }
    
    //</editor-fold>
}
