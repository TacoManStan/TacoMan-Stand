package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.attributes.Attribute;
import com.taco.suit_lady.game.objects.collision.Collidable;
import com.taco.suit_lady.game.objects.collision.CollisionMap;
import com.taco.suit_lady.logic.GameTask;
import com.taco.suit_lady.logic.triggers.implementations.UnitArrivedEvent;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.Value2D;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

public class Mover
        extends GameTask<GameObject>
        implements Collidable<GameObject> {
    
    public static final String SPEED_ID = "move-speed";
    public static final String MAX_SPEED_ID = "max-speed";
    public static final String ACCELERATION_ID = "acceleration";
    
    //
    
    private final Lock lock;
    
    //
    
    private final DoubleProperty xTargetProperty;
    private final DoubleProperty yTargetProperty;
    private final ObjectBinding<Num2D> targetBinding;
    
    private final ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetXProperty;
    private final ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetYProperty;
    
    private final BooleanProperty pausedProperty;
    
    
    private final BooleanProperty debugEnabledProperty;
    
    public Mover(@NotNull GameObject owner) {
        super(owner, owner);
        
        this.lock = owner.collisionMap().getLock();
        
        this.xTargetProperty = new SimpleDoubleProperty(owner.getLocationX(false));
        this.yTargetProperty = new SimpleDoubleProperty(owner.getLocationY(false));
        this.targetBinding = Bind.objBinding(() -> sync(() -> new Num2D(getTargetX(), getTargetY())), xTargetProperty, yTargetProperty);
        
        this.observableTargetXProperty = new ReadOnlyObjectWrapper<>();
        this.observableTargetYProperty = new ReadOnlyObjectWrapper<>();
        
        
        this.pausedProperty = new SimpleBooleanProperty(true);
        
        this.debugEnabledProperty = new SimpleBooleanProperty(false);
        
        this.targetBinding.addListener((observable, oldValue, newValue) -> {
            if (isDebugEnabled()) {
                Printer.print("Movement Target Updated  [ " + oldValue + "  -->  " + newValue + " ]", false);
                Printer.print("Owner Location: " + getOwner().getLocation(false), false);
                Printer.print("Owner Location Centered: " + getOwner().getLocation(true), false);
            }
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Target Properties">
    
    private @NotNull DoubleProperty xTargetProperty() { return xTargetProperty; }
    public final double getTargetX() { return sync(xTargetProperty::get); }
    public final double setTargetX(@NotNull Number newValue) { return sync(() -> Props.setProperty(xTargetProperty, newValue)); }
    
    //    public final @NotNull DoubleProperty yTargetProperty() { return yTargetProperty; }
    public final double getTargetY() { return sync(yTargetProperty::get); }
    public final double setTargetY(@NotNull Number newValue) { return sync(() -> Props.setProperty(yTargetProperty, newValue)); }
    
    @Contract(" -> new") public final @NotNull Point2D getTarget() { return sync(() -> new Point2D(getTargetX(), getTargetY())); }
    public final @NotNull Point2D setTarget(@Nullable Point2D newValue) { return sync(() -> new Point2D(setTargetX(newValue.getX()), setTargetY(newValue.getY()))); }
    
    // Observable Target
    
    private @NotNull ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetXProperty() { return observableTargetXProperty; }
    public final @NotNull ReadOnlyObjectProperty<ObservableValue<? extends Number>> readOnlyObservableTargetXProperty() { return sync(observableTargetXProperty::getReadOnlyProperty); }
    public final @Nullable ObservableValue<? extends Number> getObservableTargetX() { return sync(observableTargetXProperty::get); }
    public final double getObservableTargetValueX() {
        return sync(() -> Obj.getIfNonNull(this::getObservableTargetX, obs -> obs.getValue().doubleValue(), obs -> 0D));
    }
    private @Nullable ObservableValue<? extends Number> setObservableTargetX(@Nullable ObservableValue<? extends Number> newValue) {
        return sync(() -> {
            final ObservableValue<? extends Number> oldValue = getObservableTargetX();
            if (!Objects.equals(newValue, oldValue))
                Props.setProperty(observableTargetXProperty, newValue);
            return oldValue;
        });
    }
    public final boolean isBoundX() { return getObservableTargetX() != null; }
    
    private @NotNull ReadOnlyObjectWrapper<ObservableValue<? extends Number>> observableTargetYProperty() { return observableTargetYProperty; }
    public final @NotNull ReadOnlyObjectProperty<ObservableValue<? extends Number>> readOnlyObservableTargetYProperty() { return sync(observableTargetYProperty::getReadOnlyProperty); }
    public final @Nullable ObservableValue<? extends Number> getObservableTargetY() { return sync(observableTargetYProperty::get); }
    public final double getObservableTargetValueY() {
        return sync(() -> Obj.getIfNonNull(this::getObservableTargetY, obs -> obs.getValue().doubleValue(), obs -> 0D));
    }
    private @Nullable ObservableValue<? extends Number> setObservableTargetY(@Nullable ObservableValue<? extends Number> newValue) {
        return sync(() -> {
            final ObservableValue<? extends Number> oldValue = getObservableTargetY();
            if (!Objects.equals(newValue, oldValue))
                Props.setProperty(observableTargetYProperty, newValue);
            return oldValue;
        });
    }
    public final boolean isBoundY() { return getObservableTargetY() != null; }
    
    
    public final Point2D getObservableTargetValue() { return sync(() -> new Point2D(getObservableTargetX().getValue().doubleValue(), getObservableTargetY().getValue().doubleValue())); }
    public final Value2D<ObservableValue<? extends Number>, ObservableValue<? extends Number>> setObservableTargetValues(@Nullable ObservableValue<? extends Number> obsX, @Nullable ObservableValue<? extends Number> obsY) {
        return sync(() -> new Value2D<>(setObservableTargetX(obsX), setObservableTargetY(obsY)));
    }
    
    //</editor-fold>
    
    //
    
    public final @NotNull BooleanProperty pausedProperty() { return pausedProperty; }
    public final boolean isPaused() { return sync(pausedProperty::get); }
    public final boolean setPaused(boolean newValue) { return sync(() -> Props.setProperty(pausedProperty, newValue)); }
    
    //
    
    public final BooleanProperty debugEnabledProperty() { return debugEnabledProperty; }
    public final boolean isDebugEnabled() { return sync(debugEnabledProperty::get); }
    public final boolean setDebugEnabled(boolean newValue) { return sync(() -> Props.setProperty(debugEnabledProperty, newValue)); }
    
    //<editor-fold desc="> Move Methods">
    
    public final @NotNull Point2D move(@NotNull Point2D targetPoint) {
        return sync(() -> {
            final Point2D oldValue = setTarget(targetPoint);
            setPaused(false);
            
            return oldValue;
        });
    }
    
    public final @NotNull Point2D unbindAndMove(@NotNull Point2D targetPoint) {
        return sync(() -> {
            setObservableTargetX(null);
            setObservableTargetY(null);
            
            return move(targetPoint);
        });
    }
    
    public final @NotNull Point2D moveAndBind(@NotNull ObservableValue<? extends Number> observableTargetX, @NotNull ObservableValue<? extends Number> observableTargetY) {
        return sync(() -> {
            if (isDebugEnabled())
                Printer.err("Move and Bind:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            
            final Point2D oldValue = getTarget();
            
            setObservableTargetX(observableTargetX);
            setObservableTargetY(observableTargetY);
            
            setPaused(false);
            
            if (isDebugEnabled())
                Printer.err("Move and Bind 2b:  [" + observableTargetX.getValue() + ", " + observableTargetY.getValue() + "]", false);
            
            return oldValue;
        });
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void tick() {
        sync(() -> {
            final Point2D target = syncTarget().asPoint();
            final double targetX = target.getX();
            final double targetY = target.getY();
            
            final Point2D loc = getOwner().getLocation(true);
            final double locX = loc.getX();
            final double locY = loc.getY();
            
            if (!isPaused()) {
                final double acceleration = getOwner().attributes().getDoubleValue(Mover.ACCELERATION_ID, () -> 1D);
                final Attribute<Double> speedAttribute = getOwner().attributes().getDoubleAttribute(Mover.SPEED_ID);
                speedAttribute.setValue(speedAttribute.getValue() * acceleration);
                final double speed = logiCore().secondsToTicks(getOwner().attributes().getDoubleValue(Mover.SPEED_ID) * getGameMap().getTileSize());
                
                final double xDistance = targetX - locX;
                final double yDistance = targetY - locY;
                
                final double multiplierNumerator = Math.pow(speed, 2);
                final double multiplierDenominator = Math.pow(xDistance, 2) + Math.pow(yDistance, 2);
                
                if (multiplierDenominator != 0) {
                    final double multiplier = Math.sqrt(multiplierNumerator / multiplierDenominator);
                    
                    double xMovement = (multiplier * xDistance);
                    double yMovement = (multiplier * yDistance);
                    
                    if (xMovement > 0 && targetX < locX + xMovement) {
                        if (targetX != locX)
                            xMovement = targetX - locX;
                    } else if (xMovement < 0 && targetX >= locX + xMovement) {
                        if (targetX != locX)
                            xMovement = targetX - locX;
                    }
                    
                    if (yMovement > 0 && targetY < locY + yMovement) {
                        if (targetY != locY)
                            yMovement = targetY - locY;
                    } else if (yMovement < 0 && targetY > locY + yMovement) {
                        if (targetY != locY)
                            yMovement = targetY - locY;
                    }
                    
                    final double xMove = xMovement;
                    final double yMove = yMovement;
                    
                    if (getGameMap().isPathable(getOwner(), true, xMove, yMove)) {
                        getOwner().translateLocation(xMove, yMove);
                    } else {
                        setPaused(true);
                    }
                }
                
                final Point2D newLoc = new Point2D(getOwner().getLocationX(true), getOwner().getLocationY(true));
                if (getOwner().isAtPoint(getTarget(), true)) {
                    logiCore().triggers().submit(new UnitArrivedEvent(getOwner(), loc, newLoc));
                    setPaused(true);
                }
            }
        });
    }
    @Override protected void shutdown() { }
    @Override protected boolean isDone() { return false; }
    
    @Override public @NotNull CollisionMap<GameObject> collisionMap() { return getOwner().collisionMap(); }
    
    //
    
    @Override public final @NotNull Springable springable() { return collisionMap(); }
    @Override public final @Nullable Lock getLock() { return lock; }
    
    
    //</editor-fold>
    
    private Num2D syncTarget() {
        return sync(() -> {
            ObservableValue<? extends Number> obsX = getObservableTargetX();
            ObservableValue<? extends Number> obsY = getObservableTargetY();
            Number x = obsX != null ? obsX.getValue() : getTargetX();
            Number y = obsY != null ? obsY.getValue() : getTargetY();
            
            setTarget(new Point2D(x.doubleValue(), y.doubleValue()));
            return new Num2D(x, y);
        });
    }
}
