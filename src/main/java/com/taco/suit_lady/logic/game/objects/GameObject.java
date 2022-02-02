package com.taco.suit_lady.logic.game.objects;

import com.taco.suit_lady.logic.game.AttributeContainer;
import com.taco.suit_lady.logic.game.Entity;
import com.taco.suit_lady.logic.game.commands.MoveCommand;
import com.taco.suit_lady.logic.game.interfaces.AttributeContainable;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameObject
        implements Lockable, AttributeContainable, Entity {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final GameViewContent content;
    
    private final GameObjectModel model;
    private final AttributeContainer attributes;
    
    
    private final DoubleProperty xLocationProperty;
    private final DoubleProperty yLocationProperty;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    //
    
    private final DoubleBinding xLocationCenteredBinding;
    private final DoubleBinding yLocationCenteredBinding;
    
    //
    
    /**
     * <p>Represents the number of units (pixels, for now, at least) this GameObject can move per tick.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>
     *         Interpolation can be used to handle decimal values.
     *         <ul>
     *             <li>For example, if the unit can move .25 units per tick, the object's location is expected to have moved 1 unit every 4 ticks.</li>
     *         </ul>
     *     </li>
     *     <li>Keep in mind that you're going to need properties for both max move speed & current move speed (i.e., velocity).</li>
     *     <li>
     *         A collection of "forces" dictates how an object moves.
     *         <ul>
     *             <li>For example, if a unit is knocked-back, it has the force of the knock-back applied to it.</li>
     *             <li>Eventually, forces will be defined by velocity, impact location, impact angle, impacting object weight, impacting object speed, etc.</li>
     *             <li>For now, however, forces should be defined by simple equations that dictate how the speed of an object is influenced each tick.</li>
     *         </ul>
     *     </li>
     * </ol>
     */
    // TO-EXPAND
    private final ReadOnlyDoubleWrapper moveSpeedProperty;
    
    private final MoveCommand command;
    
    public GameObject(@NotNull GameViewContent content, @Nullable ReentrantLock lock) {
        this(content, lock, 0, 0);
    }
    
    public GameObject(@NotNull GameViewContent content, @Nullable ReentrantLock lock, int locationX, int locationY) {
        this.springable = content.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.content = content;
        
        this.model = new GameObjectModel(this);
        this.attributes = new AttributeContainer(this, lock, this);
        
        
        this.xLocationProperty = new SimpleDoubleProperty(locationX);
        this.yLocationProperty = new SimpleDoubleProperty(locationY);
        
        this.widthProperty = new SimpleIntegerProperty(32);
        this.heightProperty = new SimpleIntegerProperty(32);
        
        //
        
        this.xLocationCenteredBinding = BindingsSL.doubleBinding(() -> getLocationX(false) + (getWidth() / 2D), xLocationProperty, widthProperty);
        this.yLocationCenteredBinding = BindingsSL.doubleBinding(() -> getLocationY(false) + (getHeight() / 2D), yLocationProperty, heightProperty);
        
        //
        
        this.moveSpeedProperty = new ReadOnlyDoubleWrapper();
        
        //
        
        this.command = new MoveCommand(this);
    }
    
    public final GameObject init() {
        getModel().init();
        return this;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObjectModel getModel() { return model; }
    
    public final MoveCommand getCommand() { return command; }
    
    //<editor-fold desc="> Map Properties">
    
    //<editor-fold desc=">> Location Properties">
    
    public final DoubleProperty xLocationProperty() { return xLocationProperty; }
    public final DoubleBinding xLocationCenteredBinding() { return xLocationCenteredBinding; }
    
    public final double getLocationX(boolean center) { return center ? xLocationCenteredBinding.get() : xLocationProperty.get(); }
    public final double setLocationX(@NotNull Number newValue) { return PropertiesSL.setProperty(xLocationProperty, newValue.doubleValue()); }
    
    public final double setTileLocationX(@NotNull Number newValue) { return PropertiesSL.setProperty(xLocationProperty, newValue.doubleValue() * getGameMap().getTileSize()); }
    public final double moveX(@NotNull Number amount) { return setLocationX(getLocationX(false) + amount.doubleValue()); }
    public final double moveTileX(@NotNull Number amount) { return setLocationX(getLocationX(false) + (amount.doubleValue() * getGameMap().getTileSize())); }
    
    
    public final DoubleProperty yLocationProperty() { return yLocationProperty; }
    public final DoubleBinding yLocationCenteredBinding() { return yLocationCenteredBinding; }
    
    public final double getLocationY(boolean center) { return center ? yLocationCenteredBinding.get() : yLocationProperty.get(); }
    public final double setLocationY(@NotNull Number newValue) { return PropertiesSL.setProperty(yLocationProperty, newValue.doubleValue()); }
    
    public final double setTileLocationY(@NotNull Number newValue) { return PropertiesSL.setProperty(yLocationProperty, newValue.doubleValue() * getGameMap().getTileSize()); }
    public final double moveY(@NotNull Number amount) { return setLocationY(getLocationY(false) + amount.doubleValue()); }
    public final double moveTileY(@NotNull Number amount) { return setLocationY(getLocationY(false) + (amount.doubleValue() * getGameMap().getTileSize())); }
    
    
    //</editor-fold>
    
    public final boolean isAtPoint(@NotNull Point2D point, boolean center) {
        return Math.abs(Math.round((getLocationX(center))) - Math.round(point.getX())) <= 1 && Math.abs(Math.round(getLocationY(center)) - Math.round(point.getY())) <= 1;
    }
    
    public final IntegerProperty widthProperty() {
        return widthProperty;
    }
    
    public final int getWidth() {
        return widthProperty.get();
    }
    
    public final int setWidth(int newValue) {
        int oldValue = getWidth();
        widthProperty.set(newValue);
        return oldValue;
    }
    
    
    public final IntegerProperty heightProperty() {
        return heightProperty;
    }
    
    public final int getHeight() {
        return heightProperty.get();
    }
    
    public final int setHeight(int newValue) {
        int oldValue = getHeight();
        heightProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- GAME STAT PROPERTIES ---">
    
    public final ReadOnlyDoubleProperty readOnlyMoveSpeedProperty() {
        return moveSpeedProperty.getReadOnlyProperty();
    }
    
    public final double getMoveSpeed() {
        return moveSpeedProperty.get();
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull AttributeContainer attributes() { return attributes; }
    
    //<editor-fold desc="--- GENERIC ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    
    @Override
    public @NotNull Lock getLock() {
        return null;
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull GameTile[][] getOccupyingTiles() {
        final int adjustedMinX = (int) getLocationX(false) / getGameMap().getTileSize();
        final int adjustedMinY = (int) getLocationY(false) / getGameMap().getTileSize();
        final int adjustedMaxX = (int) Math.ceil((getWidth() + getLocationX(false)) / (double) getGameMap().getTileSize());
        final int adjustedMaxY = (int) Math.ceil((getHeight() + getLocationY(false)) / (double) getGameMap().getTileSize());
        
        final GameTile[][] occupyingGameTiles = new GameTile[(adjustedMaxX - adjustedMinX) + 1][(adjustedMaxY - adjustedMinY) + 1];
        for (int i = 0; i < occupyingGameTiles.length; i++)
            for (int j = 0; j < occupyingGameTiles[i].length; j++)
                occupyingGameTiles[i][j] = getGameMap().getTileMap()[i + adjustedMinX][j + adjustedMinY];
        
        return occupyingGameTiles;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    @Override public void tick() {
        //TODO
    }
    
    //</editor-fold>
}
