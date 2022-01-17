package com.taco.suit_lady.ui.jfx.components.painting;

import com.taco.suit_lady.ui.jfx.components.canvas.BoundCanvas;
import com.taco.suit_lady.ui.jfx.util.Boundable;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertyTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.util.function.Predicate;

public interface Paintable<P extends Paintable<P, O>, O extends PaintableCanvas<P, O>>
        extends Springable, Lockable, Comparable<P>, Boundable {
    
    //For paint commands that you want to be painted on multiple surfaces, create a "PaintableGroup" implementation of PaintableCanvas that passes
    // all commands sent to it to its list of wrapped PaintableCanvas objects (e.g., Overlay or BoundCanvas).
    ObjectProperty<O> ownerProperty();
    default O getOwner() { return ownerProperty().get(); }
    default O setOwner(O newValue) { return PropertyTools.setProperty(ownerProperty(), newValue); }
    
    ObjectProperty<Predicate<O>> autoRemoveConditionProperty();
    default Predicate<O> getAutoRemoveCondition() { return autoRemoveConditionProperty().get(); }
    default Predicate<O> setAutoRemoveCondition(Predicate<O> newValue) { return PropertyTools.setProperty(autoRemoveConditionProperty(), newValue); }
    
    BooleanProperty disabledProperty();
    default boolean isDisabled() { return disabledProperty().get(); }
    default boolean setDisabled(boolean newValue) { return PropertyTools.setProperty(disabledProperty(), newValue); }
    
    IntegerProperty paintPriorityProperty();
    default int getPaintPriority() { return paintPriorityProperty().get(); }
    default int setPaintPriority(int newValue) { return PropertyTools.setProperty(paintPriorityProperty(), newValue); }
    
    BoundsBinding boundsBinding();
    
    //
    
    void onAdd(O owner);
    void onRemove(O owner);
    
    void paint();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default P init() {
        autoRemoveConditionProperty().addListener((observable, oldValue, newValue) -> repaintOwner());
        disabledProperty().addListener((observable, oldValue, newValue) -> repaintOwner());
        paintPriorityProperty().addListener((observable, oldValue, newValue) -> repaintOwner());
        
        boundsBinding().addListener((observable, oldValue, newValue) -> repaintOwner());
        
        
        return (P) this;
    }
    
    //
    
    default boolean hasOwner() { return getOwner() != null; }
    
    default void repaintOwner() {
        O owner = getOwner();
        if (owner != null)
            sync(() -> FXTools.runFX(() -> owner.repaint(), true));
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override default int getX() { return boundsBinding().getX(); }
    @Override default int getY() { return boundsBinding().getY(); }
    
    @Override default int getWidth() { return boundsBinding().getWidth(); }
    @Override default int getHeight() { return boundsBinding().getHeight(); }
    
    
    @Override default Bounds getBounds() { return boundsBinding().getBounds(); }
    
    //</editor-fold>
    
    //</editor-fold>
}
