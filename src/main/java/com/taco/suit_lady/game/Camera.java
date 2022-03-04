package com.taco.suit_lady.game;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.Exceptions;
import com.taco.suit_lady.util.tools.printer.Printer;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>An object that does not implement/extend any abstract game concept, but is rather a unique object that is assigned to a game window to define how the game is viewed.</p>
 * <p>All {@link Entity} objects should be able to generate a new {@link Camera} object that is bound to their location.</p>
 * <p>
 * {@link Camera} objects should also permit an unbounded view, in which case the {@link Camera} is not bound to the location of a {@link Entity}, but rather follows a pre-defined set of rules.
 *     <ul>
 *         <li>For example, an unbounded {@link Camera} could be used via a CameraTransition (not yet available) to pan/move/zoom/etc. to any location on the map given a set of movement instructions.</li>
 *     </ul>
 * </p>
 * <p>{@link Camera} objects should be able to have tints, zoom values, vision radius, etc.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The location of the {@link Camera} represents the <i>top left</i> of the {@link Camera camera's} view region, <i>not</i> the center.</li>
 * </ol>
 */
public class Camera
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final IntegerProperty xLocationProperty;
    private final IntegerProperty yLocationProperty;
    private final IntegerProperty xOffsetProperty;
    private final IntegerProperty yOffsetProperty;
    
    private final ObjectBinding<Point2D> locationBinding;
    private final ObjectBinding<Point2D> offsetsBinding;
    
    private final ObjectBinding<Point2D> tileLocationBinding;
    
    
    private final GameViewContent content;
    private final ObjectBinding<GameMap> mapBinding;
    
    //<editor-fold desc="--- BINDING FIELDS ---">
    
    private IntegerBinding xAggregateBinding;
    private IntegerBinding yAggregateBinding;
    
    
    private IntegerBinding viewportWidthBinding; //Bound to the specified observable value representing the width in pixels of the UI viewport object (e.g., CanvasSurface).
    private IntegerBinding viewportHeightBinding; //Bound to the specified observable value representing the height in pixels of the UI viewport object (e.g., CanvasSurface).
    
    
    private ObjectBinding<Image> mapImageBinding; //Bound to the actual raw game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    
    private IntegerBinding mapImageWidthBinding; //Bound via recursive binding to the width of the actual game map image loaded by the MapModel object assigned to the GameMap object assigned to this Camera object.
    private IntegerBinding mapImageHeightBinding; //Ditto
    
    
    private DoubleBinding xMultiplierBinding; //Multiplier representing the scaling ratio between the map image width and the map data object width
    private DoubleBinding yMultiplierBinding; //Multiplier representing the scaling ratio between the map image height and the map data object height
    
    //</editor-fold>
    
    public Camera(@NotNull GameViewContent content) {
        this.springable = content.asStrict();
        this.lock = content.getLock();
        
        //
        
        this.content = content;
        this.mapBinding = BindingsSL.directObjBinding(content.gameMapProperty());
        
        //
        
        this.xLocationProperty = new SimpleIntegerProperty(0);
        this.yLocationProperty = new SimpleIntegerProperty(0);
        
        this.xOffsetProperty = new SimpleIntegerProperty(0);
        this.yOffsetProperty = new SimpleIntegerProperty(0);
        
        
        this.locationBinding = BindingsSL.objBinding(() -> new Point2D(getLocationX(), getLocationY()), xLocationProperty, yLocationProperty);
        this.offsetsBinding = BindingsSL.objBinding(() -> new Point2D(getOffsetX(), getOffsetY()), xOffsetProperty, yOffsetProperty);
        
        this.tileLocationBinding = BindingsSL.objBinding(
                () -> new Point2D(getLocationX() * getGameMap().getTileSize(), getLocationY() * getGameMap().getTileSize()),
                xLocationProperty, yLocationProperty);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public Camera init() {
        initBindings();
        return this;
    }
    
    private void initBindings() {
        this.viewportWidthBinding = BindingsSL.directIntBinding(getGame().getController().getMapPane().widthProperty());
        this.viewportHeightBinding = BindingsSL.directIntBinding(getGame().getController().getMapPane().heightProperty());
        
        this.xAggregateBinding = BindingsSL.intBinding(() -> getLocationX() + getOffsetX(), xLocationProperty(), xOffsetProperty());
        this.yAggregateBinding = BindingsSL.intBinding(() -> getLocationY() + getOffsetY(), yLocationProperty(), yOffsetProperty());
        
        //
        
        this.mapImageBinding = BindingsSL.directObjBinding(getGameMap().getModel().mapImageProperty());
        
        this.mapImageWidthBinding = BindingsSL.recursiveIntBinding(lock, image -> image.widthProperty(), mapImageBinding);
        this.mapImageHeightBinding = BindingsSL.recursiveIntBinding(lock, image -> image.heightProperty(), mapImageBinding);
        
        
        this.xMultiplierBinding = BindingsSL.doubleBinding(() -> ((double) getMapImageWidth() / (double) getMapWidth()), mapImageWidthBinding);
        this.yMultiplierBinding = BindingsSL.doubleBinding(() -> ((double) getMapImageHeight() / (double) getMapHeight()), mapImageHeightBinding);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Coordinates">
    
    /**
     * <p>Defines the {@code x} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty xLocationProperty() { return xLocationProperty; }
    public final int getLocationX() { return xLocationProperty.get(); }
    public final int setLocationX(int newValue) {
        Printer.print("Changing Camera Location X: " + newValue);
        if (!isViewBound())
            return PropertiesSL.setProperty(xLocationProperty, newValue);
        
        Printer.err("Cannot change Camera location: View is bound.");
        return getLocationX();
    }
    
    /**
     * <p>Defines the {@code y} coordinate at which this camera is assigned.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Location values represent the coordinates at the top left (0, 0) point of the viewport (e.g., Canvas).</li>
     *     <li>Location values are represented in pixels, not tiles.</li>
     * </ol>
     */
    public final IntegerProperty yLocationProperty() { return yLocationProperty; }
    public final int getLocationY() { return yLocationProperty.get(); }
    public final int setLocationY(int newValue) {
        if (!isViewBound())
            return PropertiesSL.setProperty(yLocationProperty, newValue);
        
        Printer.err("Cannot change Camera location: View is bound.");
        return getLocationY();
    }
    
    //
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getOffsetX() X Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty xOffsetProperty() { return xOffsetProperty; }
    public final int getOffsetX() { return xOffsetProperty.get(); }
    public final int setOffsetX(int newValue) { return PropertiesSL.setProperty(xOffsetProperty, newValue); }
    
    /**
     * <p>Represents the number of units (pixels, not tiles) this camera's view is shifted on the {@code x} plane.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>For example, to center the camera around its target point, you would bind the {@link #getOffsetY() Y Offset} value to half of the viewport width.</li>
     *     <li>Negative values will result in the target point of the camera being off-screen (which is entirely legal and could potentially be useful in the future).</li>
     * </ol>
     */
    public final IntegerProperty yOffsetProperty() { return yOffsetProperty; }
    public final int getOffsetY() { return yOffsetProperty.get(); }
    public final int setOffsetY(int newValue) { return PropertiesSL.setProperty(yOffsetProperty, newValue); }
    
    //
    
    public final ObjectBinding<Point2D> locationBinding() { return locationBinding; }
    public final Point2D getLocation() { return locationBinding.get(); }
    public final Point2D setLocation(@NotNull Point2D newValue) {
        final Point2D oldValue = getLocation();
        setLocationX((int) newValue.getX());
        setLocationY((int) newValue.getY());
        return oldValue;
    }
    public final Point2D setLocation(@NotNull GameObject gameObject, boolean center) { return setLocation(gameObject.getLocation(center)); }
    
    public final ObjectBinding<Point2D> offsetsBinding() { return offsetsBinding; }
    public final Point2D getOffsets() { return offsetsBinding.get(); }
    public final Point2D setOffsets(@NotNull Point2D newValue) {
        final Point2D oldValue = getOffsets();
        setOffsetX((int) newValue.getX());
        setOffsetY((int) newValue.getY());
        return oldValue;
    }
    
    
    public final ObjectBinding<Point2D> tileLocationBinding() { return tileLocationBinding; }
    public final Point2D getTileLocation() { return tileLocationBinding.get(); }
    public final Point2D setTileLocation(@NotNull Point2D newValue) {
        final Point2D oldValue = getTileLocation();
        setLocation(new Point2D(newValue.getX() * getGameMap().getTileSize(), newValue.getY() * getGameMap().getTileSize()));
        return oldValue;
    }
    
    //
    
    public final int moveX(@NotNull Number amount) { return setLocationX((int) (getLocationX() + amount.doubleValue())); }
    public final int moveTileX(@NotNull Number amount) { return setLocationX(getLocationX() + (int) (amount.doubleValue() * getGameMap().getTileSize())); }
    
    public final int moveY(@NotNull Number amount) { return setLocationY((int) (getLocationY() + amount.doubleValue())); }
    public final int moveTileY(@NotNull Number amount) { return setLocationY(getLocationY() + (int) (amount.doubleValue() * getGameMap().getTileSize())); }
    
    
    public final Point2D move(@NotNull Point2D amounts) {
        final Point2D oldValue = getLocation();
        moveX(amounts.getX());
        moveY(amounts.getY());
        return oldValue;
    }
    
    public final Point2D moveTile(@NotNull Point2D amounts) {
        final Point2D oldValue = getTileLocation();
        moveTileX(amounts.getX());
        moveTileY(amounts.getY());
        return oldValue;
    }
    
    //
    
    public final boolean bindViewTo(@Nullable GameObject gameObject) {
        boolean isBound;
        if (gameObject == null) {
            xLocationProperty.unbind();
            yLocationProperty.unbind();
            isBound = false;
        } else {
            validateLocationBindings();
            
            xLocationProperty.bind(gameObject.xLocationProperty(true));
            yLocationProperty.bind(gameObject.yLocationProperty(true));
            
            validateLocationBindings();
            
            isBound = true;
        }
        return isBound;
    }
    
    public final boolean toggleViewBinding(@Nullable GameObject gameObject) {
        boolean isBound;
        if (gameObject == null) {
            xLocationProperty.unbind();
            yLocationProperty.unbind();
            
            isBound = false;
        } else {
            validateLocationBindings();
            
            if (xLocationProperty.isBound()) {
                xLocationProperty.unbind();
                isBound = false;
            } else {
                xLocationProperty.bind(gameObject.xLocationProperty(true));
                isBound = true;
            }
            
            if (yLocationProperty.isBound()) {
                yLocationProperty.unbind();
            } else {
                yLocationProperty.bind(gameObject.yLocationProperty(true));
            }
            
            validateLocationBindings();
        }
        
        return isBound;
    }
    
    public final boolean isViewBound() {
        validateLocationBindings();
        return xLocationProperty.isBound();
    }
    
    private void validateLocationBindings() {
        if (xLocationProperty.isBound() != yLocationProperty.isBound())
            throw Exceptions.ex("ERROR: X and Y Camera Location Property Binding Statuses Do Not Match  [X: " + xLocationProperty.isBound() + "  Y: " + yLocationProperty.isBound() + "]");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Bindings">
    
    public final IntegerBinding viewportWidthBinding() { return viewportWidthBinding; }
    public final int getViewportWidth() { return viewportWidthBinding.get(); }
    
    public final IntegerBinding viewportHeightBinding() { return viewportHeightBinding; }
    public final int getViewportHeight() { return viewportHeightBinding.get(); }
    
    //
    
    public final ObjectBinding<Image> mapImageBinding() { return mapImageBinding; }
    public final Image getMapImage() { return mapImageBinding.get(); }
    
    
    public final IntegerBinding mapImageWidthBinding() { return mapImageWidthBinding; }
    public final int getMapImageWidth() { return mapImageWidthBinding.get(); }
    
    public final IntegerBinding mapImageHeightBinding() { return mapImageHeightBinding; }
    public final int getMapImageHeight() { return mapImageHeightBinding.get(); }
    
    //
    
    public final IntegerBinding xAggregateBinding() { return xAggregateBinding; }
    public final int getAggregateX() { return xAggregateBinding.get(); }
    
    public final IntegerBinding yAggregateBinding() { return yAggregateBinding; }
    public final int getAggregateY() { return yAggregateBinding.get(); }
    
    
    public final DoubleBinding xMultiplierBinding() { return xMultiplierBinding; }
    public final double getMultiplierX() { return xMultiplierBinding.get(); }
    
    public final DoubleBinding yMultiplierBinding() { return yMultiplierBinding; }
    public final double getMultiplierY() { return yMultiplierBinding.get(); }
    
    //</editor-fold>
    
    public final int getMapWidth() { return getGameMap().getPixelWidth(); }
    public final int getMapHeight() { return getGameMap().getPixelHeight(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Springable springable() { return springable; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
    
    public final @NotNull Point2D viewToMap(double x, double y) {
        return viewToMap(new Point2D(x, y));
    }
    
    @Contract("_ -> new")
    public final @NotNull Point2D viewToMap(@NotNull Point2D pointOnView) {
        return new Point2D(pointOnView.getX() + getAggregateX(), pointOnView.getY() + getAggregateY());
    }
    
    public final void print() {
        System.out.println("X Location: " + getLocationX());
        System.out.println("Y Location: " + getLocationY());
        
        
        System.out.println("X Offset: " + getOffsetX());
        System.out.println("Y Offset: " + getOffsetY());
        
        
        System.out.println("Viewport Width: " + getViewportWidth());
        System.out.println("Viewport Height: " + getViewportHeight());
        
        
        System.out.println("Map Image Width: " + getMapImageWidth());
        System.out.println("Map Image Height: " + getMapImageHeight());
        
        System.out.println("Game Map Width: " + getGameMap().getPixelWidth());
        System.out.println("Game Map Height: " + getGameMap().getPixelHeight());
        
        
        System.out.println("X Multiplier: " + getMultiplierX());
        System.out.println("Y Multiplier: " + getMultiplierY());
    }
}
