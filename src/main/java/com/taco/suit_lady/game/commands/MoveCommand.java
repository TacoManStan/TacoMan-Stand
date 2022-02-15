package com.taco.suit_lady.game.commands;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MoveCommand
        implements SpringableWrapper, Tickable {
    
    public static final String ATTRIBUTE_ID = "move-speed";
    
    //
    
    private final GameObject owner;
    
    
    private final IntegerProperty xTargetProperty;
    private final IntegerProperty yTargetProperty;
    
    private final BooleanProperty pausedProperty;
    
    
//    private final DoubleBinding speedBinding;
    
    public MoveCommand(@NotNull GameObject owner) {
        this.owner = owner;
        
        this.xTargetProperty = new SimpleIntegerProperty((int) owner.getLocationX(false));
        this.yTargetProperty = new SimpleIntegerProperty((int) owner.getLocationY(false));
        
        this.pausedProperty = new SimpleBooleanProperty(true);
        
        
//        this.speedBinding = BindingsSL.directDoubleBinding(owner.attributes().getDoubleProperty(MoveCommand.ATTRIBUTE_ID));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull GameObject getOwner() { return owner; }
    
    
    public final @NotNull IntegerProperty xTargetProperty() { return xTargetProperty; }
    public final int getTargetX() { return xTargetProperty.get(); }
    public final int setTargetX(int newValue) { return PropertiesSL.setProperty(xTargetProperty, newValue); }
    
    public final @NotNull IntegerProperty yTargetProperty() { return yTargetProperty; }
    public final int getTargetY() { return yTargetProperty.get(); }
    public final int setTargetY(int newValue) { return PropertiesSL.setProperty(yTargetProperty, newValue); }
    
    @Contract(" -> new") public final @NotNull Point2D getLocation() { return new Point2D(getTargetX(), getTargetY()); }
    
    
    public final @NotNull BooleanProperty pausedProperty() { return pausedProperty; }
    public final boolean isPaused() { return pausedProperty.get(); }
    public final boolean setPaused(boolean newValue) { return PropertiesSL.setProperty(pausedProperty, newValue); }
    
    //
    
//    public final @NotNull DoubleBinding speedBinding() { return speedBinding; }
//    public final double getSpeed() { return speedBinding.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public void tick(double ups, @NotNull GameViewContent game) {
        if (!isPaused()) {
            final double speed = (getOwner().attributes().getDoubleValue(MoveCommand.ATTRIBUTE_ID) / ups) * game.getGameMap().getTileSize();
            
            final double xDistance = getTargetX() - getOwner().getLocationX(true);
            final double yDistance = getTargetY() - getOwner().getLocationY(true);
            
            double multiplier = Math.sqrt(Math.pow(speed, 2) / (Math.pow(xDistance, 2) + Math.pow(yDistance, 2)));
            
            final double xMovement = (multiplier * xDistance);
            final double yMovement = (multiplier * yDistance);
            
            
//            ToolsFX.runFX(() -> {
                getOwner().moveX(xMovement);
                getOwner().moveY(yMovement);
                if (getOwner().isAtPoint(getLocation(), true))
                    setPaused(true);
//            }, true);
        }
    }
    
    @Override public @NotNull Springable springable() { return owner; }
    
    //</editor-fold>
}
