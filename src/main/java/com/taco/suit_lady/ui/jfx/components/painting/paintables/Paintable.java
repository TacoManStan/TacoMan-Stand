package com.taco.suit_lady.ui.jfx.components.painting.paintables;

import com.taco.suit_lady._to_sort._new.Self;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.Surface;
import com.taco.suit_lady.ui.jfx.util.Boundable;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.ui.jfx.util.BoundsBinding;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertyTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

public interface Paintable<P extends Paintable<P, S>, S extends Surface<P, S>>
        extends Self<P>, SpringableWrapper, Lockable, Comparable<P>, Boundable {
    
    @NotNull PaintableData<P, S> data();
    
    void onAdd(S surface);
    void onRemove(S surface);
    
    //
    
    @NotNull P paint();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull P init() {
        autoRemoveConditionProperty().addListener((observable, oldValue, newValue) -> repaintSurface());
        disabledProperty().addListener((observable, oldValue, newValue) -> repaintSurface());
        paintPriorityProperty().addListener((observable, oldValue, newValue) -> repaintSurface());
        
        boundsBinding().addListener((observable, oldValue, newValue) -> repaintSurface());
        
        return self();
    }
    
    //
    
    default boolean isValidDimensions() {
        return getBounds().getWidth() > 0 && getBounds().getHeight() > 0;
    }
    
    default P repaintSurface() {
//        System.out.println("Repainting Surface w/ Bounds : " + getBounds() + " for Paintable: " + this);
        S surface = getSurface();
        if (surface != null)
            sync(() -> FXTools.runFX(() -> surface.repaint(), true));
        return self();
    }
    
    //
    
    default @NotNull ObjectProperty<S> surfaceProperty() { return data().surfaceProperty(); }
    default S getSurface() { return surfaceProperty().get(); }
    default S setSurface(S newValue) { return PropertyTools.setProperty(surfaceProperty(), newValue); }
    
    default @NotNull ObjectProperty<Predicate<S>> autoRemoveConditionProperty() { return data().autoRemoveConditionProperty(); }
    default Predicate<S> getAutoRemoveCondition() { return autoRemoveConditionProperty().get(); }
    default Predicate<S> setAutoRemoveCondition(Predicate<S> newValue) { return PropertyTools.setProperty(autoRemoveConditionProperty(), newValue); }
    
    default @NotNull BooleanProperty disabledProperty() { return data().disabledProperty(); }
    default boolean isDisabled() { return disabledProperty().get(); }
    default boolean setDisabled(boolean newValue) { return PropertyTools.setProperty(disabledProperty(), newValue); }
    
    default @NotNull IntegerProperty paintPriorityProperty() { return data().paintPriorityProperty(); }
    default int getPaintPriority() { return paintPriorityProperty().get(); }
    default int setPaintPriority(int newValue) { return PropertyTools.setProperty(paintPriorityProperty(), newValue); }
    
    default @NotNull BoundsBinding boundsBinding() { return data().boundsBinding(); }
    
    //
    
    default @Override int getX() { return boundsBinding().getX(); }
    default @Override int getY() { return boundsBinding().getY(); }
    
    default @Override int getWidth() { return boundsBinding().getWidth(); }
    default @Override int getHeight() { return boundsBinding().getHeight(); }
    
    
    default @Override @NotNull Bounds getBounds() { return boundsBinding().getBounds(); }
    
    //
    
    default @Override @NotNull Lock getLock() { return data().getLock(); }
    default @Override @NotNull Springable springable() { return data().springable(); }
    
    //
    
    default @Override int compareTo(@NotNull P o) { return Integer.compare((Math.abs(self().getPaintPriority())), Math.abs(o.getPaintPriority())); }
    
    //</editor-fold>
}