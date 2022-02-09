package com.taco.suit_lady.game;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.MathSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.tacository.json.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadableObject {
    
    private final GameViewContent content;
    private final ReentrantLock lock;
    
    //
    
    private final ReadOnlyIntegerWrapper tileSizeProperty; // The number of "pixels" comprising each tile.
    
    private final ReadOnlyObjectWrapper<GameTile[][]> tileMatrixProperty;
    private final ArrayList<GameObject> gameObjects;
    
    
    private final IntegerBinding widthBinding;
    private final IntegerBinding heightBinding;
    
    private final IntegerBinding pixelWidthBinding;
    private final IntegerBinding pixelHeightBinding;
    
    
    private GameMapModel model;
    
    
    private String mapID;
    
    public GameMap(@NotNull GameViewContent content, @Nullable ReentrantLock lock, int tileSize, String mapID) {
        this.content = content;
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.tileMatrixProperty = new ReadOnlyObjectWrapper<>();
        
        setTileMatrix(new GameTile[96][64]);
        
        this.tileSizeProperty = new ReadOnlyIntegerWrapper(tileSize);
        
        this.widthBinding = BindingsSL.intBinding(() -> getTileMatrix().length, tileMatrixProperty);
        this.heightBinding = BindingsSL.intBinding(() -> getWidth() > 0 ? getTileMatrix()[0].length : 0, tileMatrixProperty);
        
        this.pixelWidthBinding = BindingsSL.intBinding(() -> getWidth() * getTileSize(), widthBinding, tileSizeProperty);
        this.pixelHeightBinding = BindingsSL.intBinding(() -> getHeight() * getTileSize(), heightBinding, tileSizeProperty);
        
        
        //
        
        this.gameObjects = new ArrayList<>();
        
        
        this.mapID = mapID;
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMap init() {
        this.model = new GameMapModel(getGame(), lock);
        this.model.init();
        JFiles.load(this);
        
//        ArraysSL.iterateMatrix(GameTile::init, getTileMatrix());
        this.model.refreshMapImage();
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="> Dimensions Properties">
    
    protected final ReadOnlyIntegerWrapper tileSizeProperty() { return tileSizeProperty; }
    public final ReadOnlyIntegerProperty readOnlyTileSizeProperty() { return tileSizeProperty.getReadOnlyProperty(); }
    public final int getTileSize() { return tileSizeProperty.get(); }
    public final int setTileSize(@NotNull Number newValue) { return PropertiesSL.setProperty(tileSizeProperty, newValue.intValue()); }
    
    //
    
    public final IntegerBinding widthBinding() { return widthBinding; }
    public final int getWidth() { return widthBinding.get(); }
    
    public final IntegerBinding heightBinding() { return heightBinding; }
    public final int getHeight() { return heightBinding.get(); }
    
    
    public final IntegerBinding pixelWidthBinding() { return pixelWidthBinding; }
    public final int getPixelWidth() { return pixelWidthBinding.get(); }
    
    public final IntegerBinding pixelHeightBinding() { return pixelHeightBinding; }
    public final int getPixelHeight() { return pixelHeightBinding.get(); }
    
    //</editor-fold>
    
    public final ReadOnlyObjectProperty<GameTile[][]> readOnlyTileMatrixProperty() { return tileMatrixProperty.getReadOnlyProperty(); }
    public final GameTile[][] getTileMatrix() { return tileMatrixProperty.get(); }
    private GameTile[][] setTileMatrix(GameTile[][] newValue) { return PropertiesSL.setProperty(tileMatrixProperty, newValue); }
    
    public final ArrayList<GameObject> gameObjects() { return gameObjects; }
    public final GameMapModel getModel() { return model; }
    
    
    /**
     * <p>Returns the {@link String} representing the {@code ID} used to {@link JFiles#save(JObject) save} and {@link JFiles#load(JLoadable) load} this {@link GameMap}.</p>
     *
     * @return The {@link String} representing the {@code ID} used to {@link JFiles#save(JObject) save} and {@link JFiles#load(JLoadable) load} this {@link GameMap}.
     */
    public final String getMapID() { return mapID; }
    
    //</editor-fold>
    
    public final @Nullable GameTile getNeighbor(@NotNull GameTile gameTile, int xTranslate, int yTranslate) {
        int xTemp = gameTile.getLocationX() + xTranslate;
        int yTemp = gameTile.getLocationY() + yTranslate;
        if (xTemp < 0 || xTemp >= getWidth() || yTemp < 0 || yTemp >= getHeight())
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return getTileMatrix()[xTemp][yTemp];
    }
    
    public final @NotNull GameTile[][] getNeighbors(@NotNull GameTile gameTile, int xReach, int yReach) {
        // Reach parameters define how many tiles in each direction should be returned, excluding the host tile.
        // Therefore, the reach value must be doubled to account for opposite directions and then 1 must be added to account for the host tile.
        final GameTile[][] neighbors = new GameTile[(xReach * 2) + 1][(yReach * 2) + 1];
        
        for (int i = -xReach; i < xReach; i++)
            for (int j = -yReach; j < yReach; j++) {
                int xLoc = gameTile.getLocationX() + i;
                int yLoc = gameTile.getLocationY() + j;
                if (ArraysSL.isInMatrixBounds(getTileMatrix(), xLoc, yLoc))
                    neighbors[i][j] = getTileMatrix()[gameTile.getLocationX() + i][gameTile.getLocationY() + j];
            }
        
        return neighbors;
    }
    
    
    /**
     * <p>Returns a {@link List} of {@link GameTile tiles} partially or entirely visible in the specified {@link Camera Camera's} bounds.</p>
     */
    // TO-EXPAND
    public final @NotNull List<GameTile> getTilesInView(@NotNull Camera camera) {
        int xLoc = camera.getLocationX();
        int yLoc = camera.getLocationY();
        
        //TODO: Include Offsets
        
        int pxMapWidth = getPixelWidth();
        int pxMapHeight = getPixelHeight();
        
        int tileMinX = Math.floorDiv(xLoc, getTileSize());
        int tileMinY = Math.floorDiv(yLoc, getTileSize());
        int tileMaxX = MathSL.ceil(xLoc + pxMapWidth, getTileSize());
        int tileMaxY = MathSL.ceil(yLoc + pxMapHeight, getTileSize());
        
        ArrayList<GameTile> returnTiles = new ArrayList<>();
        for (int i = tileMinX; i <= tileMaxX; i++)
            for (int j = tileMinY; j <= tileMaxY; j++)
                if (ArraysSL.isInMatrixBounds(getTileMatrix(), i, j))
                    returnTiles.add(getTileMatrix()[i][j]);
        return returnTiles;
    }
    
    public final @NotNull GameTile getTileAtPoint(@NotNull Number x, @NotNull Number y) { return getTileAtPoint(new Point2D(x.doubleValue(), y.doubleValue())); }
    public final @NotNull GameTile getTileAtPoint(@NotNull Point2D point) { return getTileMatrix()[(int) Math.floor(point.getX() / getTileSize())][(int) Math.floor(point.getY() / getTileSize())]; }
    
    @Contract("_ -> new")
    public final @NotNull ArrayList<GameObject> getObjectsAtPoint(@NotNull Point2D point) { return new ArrayList<>(getTileAtPoint(point).getOccupyingObjects()); }
    
    public boolean shutdown() {
        //TODO
        return true;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //<editor-fold desc="> JSON">
    
    @Override public String getJID() { return mapID; }
    @Override public void setJID(String jID) { mapID = jID; }
    
    @Override public JElement[] jFields() {
        final ArrayList<GameObject> gameObjectsImpl = new ArrayList<>(gameObjects);
        gameObjectsImpl.remove(getGame().getTestObject());
        gameObjectsImpl.remove(getGame().getTestObject2());
        return new JElement[]{
                JUtil.create("tile-size", getTileSize()),
                JUtil.createArray("map-objects", gameObjectsImpl.toArray(new GameObject[0])),
                JUtil.createMatrix("tile-matrix", getTileMatrix())
        };
    }
    
    @Override public void doLoad(JsonObject parent) {
        setTileSize(JUtil.loadInt(parent, "tile-size"));
        
        final List<List<GameTile>> tileMatrixList = JUtil.loadMatrix(parent, "tile-matrix", o -> {
            JsonObject jsonObject = (JsonObject) o;
            GameTile gameTile = new GameTile(this);
            gameTile.load(jsonObject);
            return gameTile;
        });
        final int tileMatrixWidth = tileMatrixList.size();
        if (tileMatrixWidth > 0) {
            final int tileMatrixHeight = tileMatrixList.get(0).size();
            if (tileMatrixHeight > 0) {
                final GameTile[][] tileMatrixArray = new GameTile[tileMatrixWidth][tileMatrixHeight];
                for (int i = 0; i < tileMatrixList.size(); i++)
                    for (int j = 0; j < tileMatrixList.get(i).size(); j++)
                        tileMatrixArray[i][j] = tileMatrixList.get(i).get(j);
                setTileMatrix(tileMatrixArray);
            }
        }
        
        gameObjects().addAll(JUtil.loadArray(parent, "map-objects", o -> {
            JsonObject jsonObject = (JsonObject) o;
            GameObject gameObject = new GameObject(getGame(), lock).init();
            gameObject.load(jsonObject);
            return gameObject;
        }));
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC FACTORY METHODS ---">
    
    /**
     * <p>Identical to <i>{@link #newTestInstance(GameViewContent, ReentrantLock, String)}</i> except the {@link ReentrantLock} passed to the {@link GameMap Game Map's} {@link GameMap#GameMap(GameViewContent, ReentrantLock, int, String) constructor} is always {@code null}.</p>
     * <p>Note that a new {@link ReentrantLock} is automatically created by the {@link GameMap} constructor if the specified value is {@code null}, so the returned {@link GameMap} object will still be {@code synchronized}, just only with itself.</p>
     *
     * @param content Any non-null {@link Springable} object used to enable {@link Springable} features in the returned {@link GameMap} object.
     *
     * @return The newly constructed {@link GameMap} instance.
     *
     * @see #newTestInstance(GameViewContent, ReentrantLock, String)
     * @see GameMap
     * @see GameMap#GameMap(GameViewContent, ReentrantLock, int, String)
     */
    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content, @NotNull String mapID) { return newTestInstance(content, null, mapID); }
    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content, @Nullable ReentrantLock lock, @NotNull String mapID) { return new GameMap(content, lock, 32, mapID); }
    
    //</editor-fold>
}
