package com.taco.tacository.game.commands;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Bind;
import com.taco.tacository.util.tools.Props;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class MoveBehavior
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameObject owner;
    
    
    private final DoubleProperty speedProperty; //Tiles per Second
    private final DoubleBinding speedPerTickBinding;
    
    private final IntegerProperty xDirectionProperty;
    private final IntegerProperty yDirectionProperty;
    
    public MoveBehavior(@NotNull GameObject owner) {
        this(owner, 3.6);
    }
    
    public MoveBehavior(@NotNull GameObject owner, double initialSpeed) {
        this.owner = owner;
        
        this.speedProperty = new SimpleDoubleProperty(initialSpeed);
        this.speedPerTickBinding = Bind.doubleBinding(() -> (getSpeed() / 60) * getGameMap().getTileSize(), speedProperty, gameMapProperty());
        
        this.xDirectionProperty = new SimpleIntegerProperty();
        this.yDirectionProperty = new SimpleIntegerProperty();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final DoubleProperty speedProperty() { return speedProperty; }
    public final double getSpeed() { return speedProperty.get(); }
    public final double setSpeed(double newValue) { return Props.setProperty(speedProperty, newValue); }
    
    public final DoubleBinding speedPerTickBinding() { return speedPerTickBinding; }
    public final double getSpeedPerTick() { return speedPerTickBinding.get(); }
    
    
    public final IntegerProperty xDirectionProperty() { return xDirectionProperty; }
    public final int getDirectionX() { return xDirectionProperty.get(); }
    public final int setDirectionX(int newValue) { return Props.setProperty(xDirectionProperty, newValue); }
    
    public final IntegerProperty yDirectionProperty() { return yDirectionProperty; }
    public final int getDirectionY() { return yDirectionProperty.get(); }
    public final int setDirectionY(int newValue) { return Props.setProperty(yDirectionProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @NotNull Lock getLock() { return owner.getLock(); }
    
    //</editor-fold>
}
