package com.taco.suit_lady.game.commands;

import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

public class MoveCommand
        implements SpringableWrapper, Tickable {
    
    private final GameObject owner;
    
    private final IntegerProperty xTargetProperty;
    private final IntegerProperty yTargetProperty;
    
    private final BooleanProperty pausedProperty;
    
    public MoveCommand(@NotNull GameObject owner) {
        this(owner, (int) owner.getLocationX(false), (int) owner.getLocationY(false));
    }
    
    public MoveCommand(@NotNull GameObject owner, int targetX, int targetY) {
        this.owner = owner;
        
        this.xTargetProperty = new SimpleIntegerProperty(targetX);
        this.yTargetProperty = new SimpleIntegerProperty(targetY);
        
        this.pausedProperty = new SimpleBooleanProperty(true);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    
    public final IntegerProperty xTargetProperty() { return xTargetProperty; }
    public final int getTargetX() { return xTargetProperty.get(); }
    public final int setTargetX(int newValue) { return PropertiesSL.setProperty(xTargetProperty, newValue); }
    
    public final IntegerProperty yTargetProperty() { return yTargetProperty; }
    public final int getTargetY() { return yTargetProperty.get(); }
    public final int setTargetY(int newValue) { return PropertiesSL.setProperty(yTargetProperty, newValue); }
    
    public final Point2D getLocation() { return new Point2D(getTargetX(), getTargetY()); }
    
    
    public final BooleanProperty pausedProperty() { return pausedProperty; }
    public final boolean isPaused() { return pausedProperty.get(); }
    public final boolean setPaused(boolean newValue) { return PropertiesSL.setProperty(pausedProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public void tick() {
        if (!isPaused()) {
            final double speed = getOwner().attributes().getDoubleValue("move-speed", () -> 2D);
            
            final double xDistance = getTargetX() - getOwner().getLocationX(true);
            final double yDistance = getTargetY() - getOwner().getLocationY(true);
            
            double multiplier = Math.sqrt(Math.pow(speed, 2) / (Math.pow(xDistance, 2) + Math.pow(yDistance, 2)));
            
            final double xMovement = multiplier * xDistance;
            final double yMovement = multiplier * yDistance;
            
            
            ToolsFX.runFX(() -> {
                getOwner().moveX(xMovement);
                getOwner().moveY(yMovement);
                if (getOwner().isAtPoint(getLocation(), true))
                    setPaused(true);
            }, true);
        }
    }
    
    @Override public @NotNull Springable springable() { return owner; }
    
    //</editor-fold>
}
