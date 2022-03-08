package com.taco.suit_lady.ui.jfx.components.painting.paintables;

import com.taco.suit_lady._to_sort._new.Self;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.Surface;
import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.values.numbers.expressions.BoundsExpr;
import com.taco.suit_lady.util.values.numbers.Bounds;
import com.taco.suit_lady.util.values.numbers.bindings.BoundsBinding;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.enums.LocType;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;

public interface Paintable<P extends Paintable<P, S>, S extends Surface<P, S>>
        extends Self<P>, SpringableWrapper, Lockable, Comparable<P>, BoundsExpr {
    
    @NotNull PaintableData<P, S> data();
    
    void onAdd(S surface);
    void onRemove(S surface);
    
    //
    
    @NotNull P paint();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull P init() {
        autoRemoveConditionProperty().addListener((observable, oldValue, newValue) -> repaintSurface());
        activeBinding().addListener((observable, oldValue, newValue) -> repaintSurface());
        paintPriorityProperty().addListener((observable, oldValue, newValue) -> repaintSurface());
        
        boundsBinding().addListener((observable, oldValue, newValue) -> repaintSurface());
        
        setDisabled(false);
        
        return self();
    }
    
    //
    
    default boolean isValidDimensions() {
        return getBounds().wD() > 0 && getBounds().hD() > 0;
    }
    
    default P repaintSurface() {
        //        System.out.println("Repainting Surface w/ Bounds : " + getBounds() + " for Paintable: " + this);
        if (!isSurfaceRepaintDisabled()) {
            S surface = getSurface();
            if (surface != null)
                surface.repaint();
        }
        return self();
    }
    
    //
    
    default @NotNull ObjectProperty<S> surfaceProperty() { return data().surfaceProperty(); }
    default S getSurface() { return surfaceProperty().get(); }
    default S setSurface(S newValue) { return Props.setProperty(surfaceProperty(), newValue); }
    
    default @NotNull ObjectProperty<Predicate<S>> autoRemoveConditionProperty() { return data().autoRemoveConditionProperty(); }
    default Predicate<S> getAutoRemoveCondition() { return autoRemoveConditionProperty().get(); }
    default Predicate<S> setAutoRemoveCondition(Predicate<S> newValue) { return Props.setProperty(autoRemoveConditionProperty(), newValue); }
    
    default @NotNull BooleanProperty disabledProperty() { return data().disabledProperty(); }
    default boolean isDisabled() { return disabledProperty().get(); }
    default boolean setDisabled(boolean newValue) { return Props.setProperty(disabledProperty(), newValue); }
    
    default @NotNull BooleanProperty pausedProperty() { return data().pausedProperty(); }
    default boolean isPaused() { return pausedProperty().get(); }
    default boolean setPaused(boolean newValue) { return Props.setProperty(pausedProperty(), newValue); }
    default boolean pause() { return setPaused(true); }
    default boolean resume() { return setPaused(false); }
    
    default @NotNull BooleanProperty surfaceRepaintDisabledProperty() { return data().repaintSurfaceDisabledProperty(); }
    default boolean isSurfaceRepaintDisabled() { return surfaceRepaintDisabledProperty().get(); }
    default boolean setSurfaceRepaintDisabled(boolean newValue) { return Props.setProperty(surfaceRepaintDisabledProperty(), newValue); }
    
    default @NotNull IntegerProperty paintPriorityProperty() { return data().paintPriorityProperty(); }
    default int getPaintPriority() { return paintPriorityProperty().get(); }
    default int setPaintPriority(int newValue) { return Props.setProperty(paintPriorityProperty(), newValue); }
    
    default @NotNull BoundsBinding boundsBinding() { return data().boundsBinding(); }
    
    default @NotNull BooleanBinding activeBinding() { return data().activeBinding(); }
    default boolean isActive() { return activeBinding().get(); }
    
    //
    
    //<editor-fold desc="> Boundable">
    
    default @Override @NotNull Number x() { return boundsBinding().x(); }
    default @Override @NotNull Number y() { return boundsBinding().y(); }
    
    default @Override @NotNull Number w() { return boundsBinding().w(); }
    default @Override @NotNull Number h() { return boundsBinding().h(); }
    
    
    default @Override @NotNull Bounds getBounds() { return boundsBinding().getBounds(); }
    default @Override @NotNull LocType locType() { return Enu.get(LocType.class); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Foundational">
    
    default @Override @NotNull Lock getLock() { return data().getLock(); }
    default @Override @NotNull Springable springable() { return data().springable(); }
    
    //
    
    default @Override int compareTo(@NotNull P o) { return Integer.compare((Math.abs(self().getPaintPriority())), Math.abs(o.getPaintPriority())); }
    
    //</editor-fold>
    
    //</editor-fold>
}
