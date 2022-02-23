package com.taco.suit_lady.game.commands;

import com.taco.suit_lady.game.objects.Attribute;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.GameTask;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MoveCommand extends GameTask<GameObject> {
    
    public static final String SPEED_ID = "move-speed";
    public static final String ACCELERATION_ID = "acceleration";
    
    //
    
    private final IntegerProperty xTargetProperty;
    private final IntegerProperty yTargetProperty;
    
    private final BooleanProperty pausedProperty;
    
    
    //    private final DoubleBinding speedBinding;
    
    public MoveCommand(@NotNull GameObject owner) {
        super(owner, owner);
        
        this.xTargetProperty = new SimpleIntegerProperty((int) owner.getLocationX(false));
        this.yTargetProperty = new SimpleIntegerProperty((int) owner.getLocationY(false));
        
        this.pausedProperty = new SimpleBooleanProperty(true);
        
        
        //        this.speedBinding = BindingsSL.directDoubleBinding(owner.attributes().getDoubleProperty(MoveCommand.ATTRIBUTE_ID));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
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
    
    @Override protected void tick() {
        if (!isPaused()) {
            //            final double speed = ((getOwner().attributes().getDoubleValue(MoveCommand.ATTRIBUTE_ID) * logiCore.getUPSMultiplier()) * logiCore.getGameMap().getTileSize()) / 100D;
            //            System.out.println("Pre-Speed: " + logiCore().secondsToTicks(getOwner().attributes().getDoubleValue(MoveCommand.ATTRIBUTE_ID)));
            final double acceleration = getOwner().attributes().getDoubleValue(MoveCommand.ACCELERATION_ID, () -> 1D);
            final Attribute<Double> speedAttribute = getOwner().attributes().getDoubleAttribute(MoveCommand.SPEED_ID);
            speedAttribute.setValue(speedAttribute.getValue() * acceleration);
            final double speed = logiCore().secondsToTicks(getOwner().attributes().getDoubleValue(MoveCommand.SPEED_ID) * getGameMap().getTileSize());
            //            System.out.println("Speed: " + speed);
            
            final double xDistance = getTargetX() - getOwner().getLocationX(true);
            final double yDistance = getTargetY() - getOwner().getLocationY(true);
            
            double multiplier = Math.sqrt(Math.pow(speed, 2) / (Math.pow(xDistance, 2) + Math.pow(yDistance, 2)));
            
            final double xMovement = (multiplier * xDistance);
            final double yMovement = (multiplier * yDistance);
            
            
            //            ToolsFX.runFX(() -> {
            final Point2D oldLoc = new Point2D(getOwner().getLocationX(false), getOwner().getLocationY(false));
            getOwner().moveX(xMovement);
            getOwner().moveY(yMovement);
            final Point2D newLoc = new Point2D(getOwner().getLocationX(false), getOwner().getLocationY(false));
            
            if (getOwner().isAtPoint(getLocation(), true)) {
                logiCore().triggers().submit(new UnitArrivedEvent(getOwner(), oldLoc, newLoc));
                setPaused(true);
            }
            //            }, true);
        }
    }
    @Override protected void shutdown() { }
    
    @Override protected boolean isDone() { return false; }
    
    //</editor-fold>
}
