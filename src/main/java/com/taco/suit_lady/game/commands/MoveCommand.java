package com.taco.suit_lady.game.commands;

import com.taco.suit_lady.game.attributes.Attribute;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.GameTask;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.suit_lady.util.tools.util.NumberValuePair;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MoveCommand extends GameTask<GameObject> {
    
    public static final String SPEED_ID = "move-speed";
    public static final String MAX_SPEED_ID = "max-speed";
    public static final String ACCELERATION_ID = "acceleration";
    
    //
    
    private final IntegerProperty xTargetProperty;
    private final IntegerProperty yTargetProperty;
    private final ObjectBinding<NumberValuePair> targetBinding;
    
    private final ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetXProperty;
    private final ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetYProperty;
    
    private final BooleanProperty pausedProperty;
    
    
    private final BooleanProperty debugEnabledProperty;
    
    public MoveCommand(@NotNull GameObject owner) {
        super(owner, owner);
        
        this.xTargetProperty = new SimpleIntegerProperty((int) owner.getLocationX(false));
        this.yTargetProperty = new SimpleIntegerProperty((int) owner.getLocationY(false));
        this.targetBinding = BindingsSL.objBinding(() -> new NumberValuePair(getTargetX(), getTargetY()), xTargetProperty, yTargetProperty);
        
        this.observableTargetXProperty = new ReadOnlyObjectWrapper<>();
        this.observableTargetYProperty = new ReadOnlyObjectWrapper<>();
        
        
        this.pausedProperty = new SimpleBooleanProperty(true);
        
        this.debugEnabledProperty = new SimpleBooleanProperty(false);
        
        this.targetBinding.addListener((observable, oldValue, newValue) -> {
            if (isDebugEnabled()) {
                Print.print("Movement Target Updated  [ " + oldValue + "  -->  " + newValue + " ]", false);
                Print.print("Test Object Location: " + getOwner().getLocation(false), false);
                Print.print("Test Object Location Centered: " + getOwner().getLocation(true), false);
            }
        });
        
        this.observableTargetXProperty.addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue))
                throw ExceptionsSL.unsupported("This shouldn't ever be possible X:  [" + oldValue + "  -->  " + newValue + "]");
            
            if (newValue != null)
                xTargetProperty.bind(newValue);
            else
                xTargetProperty.unbind();
        });
        this.observableTargetYProperty.addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue))
                throw ExceptionsSL.unsupported("This shouldn't ever be possible Y:  [" + oldValue + "  -->  " + newValue + "]");
            
            if (newValue != null)
                yTargetProperty.bind(newValue);
            else
                yTargetProperty.unbind();
        });
        
        
        //        this.xTargetProperty.addListener((observable, oldValue, newValue) -> Print.print("X Target Changed  [" + oldValue + " --> " + newValue + "]", false));
        //        this.yTargetProperty.addListener((observable, oldValue, newValue) -> Print.print("Y Target Changed  [" + oldValue + " --> " + newValue + "]", false));
        
        
        //        this.speedBinding = BindingsSL.directDoubleBinding(owner.attributes().getDoubleProperty(MoveCommand.ATTRIBUTE_ID));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull IntegerProperty xTargetProperty() { return xTargetProperty; }
    public final int getTargetX() { return xTargetProperty.get(); }
    public final int setTargetX(int newValue) { return PropertiesSL.setProperty(xTargetProperty, newValue); }
    
    public final @NotNull IntegerProperty yTargetProperty() { return yTargetProperty; }
    public final int getTargetY() { return yTargetProperty.get(); }
    public final int setTargetY(int newValue) { return PropertiesSL.setProperty(yTargetProperty, newValue); }
    
    @Contract(" -> new") public final @NotNull Point2D getTargetPoint() { return new Point2D(getTargetX(), getTargetY()); }
    public final @NotNull Point2D setTargetPoint(@Nullable Point2D newValue) {
        final Point2D oldValue = getTargetPoint();
        setTargetX((int) newValue.getX());
        setTargetY((int) newValue.getY());
        return oldValue;
    }
    
    
    private @NotNull ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetXProperty() { return observableTargetXProperty; }
    public final @NotNull ReadOnlyObjectProperty<ObservableValue<? extends Number>> readOnlyObservableTargetXProperty() { return observableTargetXProperty.getReadOnlyProperty(); }
    public final @Nullable ObservableValue<? extends Number> getObservableTargetX() { return observableTargetXProperty.get(); }
    private @Nullable ObservableValue<? extends Number> setObservableTargetX(@Nullable ObservableValue<? extends Number> newValue) {
        if (!Objects.equals(newValue, getObservableTargetX()))
            return PropertiesSL.setProperty(observableTargetXProperty, newValue);
        return getObservableTargetX();
    }
    public final boolean isBoundX() { return getObservableTargetX() != null; }
    
    private @NotNull ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetYProperty() { return observableTargetYProperty; }
    public final @NotNull ReadOnlyObjectProperty<ObservableValue<? extends Number>> readOnlyObservableTargetYProperty() { return observableTargetYProperty.getReadOnlyProperty(); }
    public final @Nullable ObservableValue<? extends Number> getObservableTargetY() { return observableTargetYProperty.get(); }
    private @Nullable ObservableValue<? extends Number> setObservableTargetY(@Nullable ObservableValue<? extends Number> newValue) {
        if (!Objects.equals(newValue, getObservableTargetY()))
            return PropertiesSL.setProperty(observableTargetYProperty, newValue);
        return getObservableTargetY();
    }
    public final boolean isBoundY() { return getObservableTargetY() != null; }
    
    //
    
    public final @NotNull BooleanProperty pausedProperty() { return pausedProperty; }
    public final boolean isPaused() { return pausedProperty.get(); }
    public final boolean setPaused(boolean newValue) { return PropertiesSL.setProperty(pausedProperty, newValue); }
    
    //
    
    public final BooleanProperty debugEnabledProperty() { return debugEnabledProperty; }
    public final boolean isDebugEnabled() { return debugEnabledProperty.get(); }
    public final boolean setDebugEnabled(boolean newValue) { return PropertiesSL.setProperty(debugEnabledProperty, newValue); }
    
    //<editor-fold desc="> Move Methods">
    
    public final @NotNull Point2D move(@NotNull Point2D targetPoint) {
        final Point2D oldValue = setTargetPoint(targetPoint);
        setPaused(false);
        return oldValue;
    }
    
    public final @NotNull Point2D unbindAndMove(@NotNull Point2D targetPoint) {
        setObservableTargetX(null);
        setObservableTargetY(null);
        return move(targetPoint);
    }
    
    
    //    public final @NotNull Point2D moveAndBind(@NotNull ObservableObjectValue<Point2D> observableTarget) {
    //        return ToolsFX.requireFX(() -> {
    //            final Point2D oldValue = getTargetPoint();
    //            if (!xTargetProperty.isBound() && !yTargetProperty.isBound()) {
    //                xTargetProperty.bind(BindingsSL.doubleBinding(() -> observableTarget.get().getX(), observableTarget));
    //                yTargetProperty.bind(BindingsSL.doubleBinding(() -> observableTarget.get().getY(), observableTarget));
    //                setPaused(false);
    //
    //                Print.err("Move and Bind 1: " + observableTarget, false);
    //            }
    //
    //            return oldValue;
    //        });
    //    }
    
    public final @NotNull Point2D moveAndBindLegacy(@NotNull ObservableValue<? extends Number> observableTargetX, @NotNull ObservableValue<? extends Number> observableTargetY) {
        return ToolsFX.requireFX(() -> {
            if (isDebugEnabled())
                Print.err("Move and Bind:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            
            final Point2D oldValue = getTargetPoint();
            if (!xTargetProperty.isBound() && !yTargetProperty.isBound()) {
                xTargetProperty.bind(observableTargetX);
                yTargetProperty.bind(observableTargetY);
                setPaused(false);
                
                Print.err("Move and Bind 2a:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            }
            
            return oldValue;
        });
    }
    
    public final @NotNull Point2D moveAndBind(@NotNull ObservableValue<? extends Number> observableTargetX, @NotNull ObservableValue<? extends Number> observableTargetY) {
        return ToolsFX.requireFX(() -> {
            if (isDebugEnabled())
                Print.err("Move and Bind:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            
            final Point2D oldValue = getTargetPoint();
            setObservableTargetX(observableTargetX);
            setObservableTargetY(observableTargetY);
            setPaused(false);
            
            if (isDebugEnabled())
                Print.err("Move and Bind 2b:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            
            return oldValue;
        });
    }
    
    //</editor-fold>
    
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
            
            final double multiplierNumerator = Math.pow(speed, 2);
            final double multiplierDenominator = Math.pow(xDistance, 2) + Math.pow(yDistance, 2);
            
            final Point2D oldLoc = getOwner().getLocation(true);
            
            if (multiplierDenominator != 0) {
                final double multiplier = Math.sqrt(multiplierNumerator / multiplierDenominator);
                
                final double xMovement = (multiplier * xDistance);
                final double yMovement = (multiplier * yDistance);
                
                
                //            ToolsFX.runFX(() -> {
                final double oldLocX = oldLoc.getX();
                final double oldLocY = oldLoc.getY();
                
                if (xMovement > 0 && getTargetX() <= oldLocX + xMovement) {
                    if (getTargetX() != oldLocX)
                        getOwner().setLocationX(getTargetX(), true);
                } else if (xMovement < 0 && getTargetX() >= oldLocX + xMovement) {
                    if (getTargetX() != oldLocX)
                        getOwner().setLocationX(getTargetX(), true);
                } else
                    getOwner().moveX(xMovement);
                
                if (yMovement > 0 && getTargetY() <= oldLocY + yMovement) {
                    if (getTargetY() != oldLocY)
                        getOwner().setLocationY(getTargetY(), true);
                } else if (yMovement < 0 && getTargetY() >= oldLocY + yMovement) {
                    if (getTargetY() != oldLocY)
                        getOwner().setLocationY(getTargetY(), true);
                } else
                    getOwner().moveY(yMovement);
            }
            
            final Point2D newLoc = new Point2D(getOwner().getLocationX(true), getOwner().getLocationY(true));
            if (getOwner().isAtPoint(getTargetPoint(), true)) {
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
