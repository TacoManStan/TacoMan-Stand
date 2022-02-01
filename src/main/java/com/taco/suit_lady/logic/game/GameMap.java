package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.logic.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.MathSL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final int tileSize; // The number of "pixels" comprising each tile.
    
    //
    
    private final GameViewContent content;
    private final ReentrantLock lock;
    
    //
    
    private final int width;
    private final int height;
    
    private final GameTile[][] tileMap;
    private final ArrayList<GameObject> mapObjects;
    
    
    private GameMapModel model;
    
    public GameMap(@NotNull GameViewContent content, @Nullable ReentrantLock lock, int width, int height, int tileSize) {
        this.content = content;
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.width = width;
        this.height = height;
        
        this.tileSize = tileSize;
        
        
        this.tileMap = new GameTile[width][height];
        this.mapObjects = new ArrayList<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMap init() {
        initTiles();
        initModel();
        return this;
    }
    
    public final GameMap initModel() {
        this.model = new GameMapModel(getGame(), lock);
        this.model.init();
        return this;
    }
    
    /**
     * <p>Populates the {@link GameTile} array for this {@link GameMap} with new {@link GameTile} objects.</p>
     * <p>Note that eventually, this method should load tiles based on a specified data template that represents the contents of the {@link GameMap}.</p>
     */
    private void initTiles() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tileMap[i][j] = new GameTile(this, i, j);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final int getTileSize() { return tileSize; }
    
    
    /**
     * <p>Returns the {@code width} of this {@link GameMap} in {@link GameTile tiles}.</p>
     *
     * @return The {@code width} of this {@link GameMap} in {@link GameTile tiles}.
     */
    public final int getWidth() { return width; }
    
    /**
     * <p>Returns the {@code height} of this {@link GameMap} in {@link GameTile tiles}.</p>
     *
     * @return The {@code height} of this {@link GameMap} in {@link GameTile tiles}.
     */
    public final int getHeight() { return height; }
    
    /**
     * <p>Returns the {@code width} of this {@link GameMap} in {@code virtual pixels}.</p>
     * <p><b>Passthrough Definition:</b></p>
     * <blockquote><i>{@link #getWidth()} <b>*</b> {@link #getTileSize()}</i></blockquote>
     *
     * @return The {@code width} of this {@link GameMap} in {@code virtual pixels}.
     */
    public final int getFullWidth() { return width * tileSize; }
    
    /**
     * <p>Returns the {@code height} of this {@link GameMap} in {@code virtual pixels}.</p>
     * <p><b>Passthrough Definition:</b></p>
     * <blockquote><i>{@link #getHeight()} <b>*</b> {@link #getTileSize()}</i></blockquote>
     *
     * @return The {@code height} of this {@link GameMap} in {@code virtual pixels}.
     */
    public final int getFullHeight() { return height * tileSize; }
    
    
    public final GameTile[][] getTileMap() { return tileMap; }
    public final ArrayList<GameObject> mapObjects() { return mapObjects; }
    
    public final GameMapModel getModel() { return model; }
    
    //</editor-fold>
    
    public final @Nullable GameTile getNeighbor(@NotNull GameTile gameTile, int xTranslate, int yTranslate) {
        int xTemp = gameTile.getXLoc() + xTranslate;
        int yTemp = gameTile.getYLoc() + yTranslate;
        if (xTemp < 0 || xTemp >= width || yTemp < 0 || yTemp >= height)
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return tileMap[xTemp][yTemp];
    }
    
    public final @NotNull GameTile[][] getNeighbors(@NotNull GameTile gameTile, int xReach, int yReach) {
        // Reach parameters define how many tiles in each direction should be returned, excluding the host tile.
        // Therefore, the reach value must be doubled to account for opposite directions and then 1 must be added to account for the host tile.
        final GameTile[][] neighbors = new GameTile[(xReach * 2) + 1][(yReach * 2) + 1];
        
        for (int i = -xReach; i < xReach; i++)
            for (int j = -yReach; j < yReach; j++) {
                int xLoc = gameTile.getXLoc() + i;
                int yLoc = gameTile.getYLoc() + j;
                if (ArraysSL.isInMatrixBounds(getTileMap(), xLoc, yLoc))
                    neighbors[i][j] = tileMap[gameTile.getXLoc() + i][gameTile.getYLoc() + j];
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
        
        int pxMapWidth = getFullWidth();
        int pxMapHeight = getFullHeight();
        
        int tileMinX = Math.floorDiv(xLoc, getTileSize());
        int tileMinY = Math.floorDiv(yLoc, getTileSize());
        int tileMaxX = MathSL.ceil(xLoc + pxMapWidth, getTileSize());
        int tileMaxY = MathSL.ceil(yLoc + pxMapHeight, getTileSize());
        
        ArrayList<GameTile> returnTiles = new ArrayList<>();
        for (int i = tileMinX; i <= tileMaxX; i++)
            for (int j = tileMinY; j <= tileMaxY; j++)
                if (ArraysSL.isInMatrixBounds(getTileMap(), i, j))
                    returnTiles.add(getTileMap()[i][j]);
        return returnTiles;
    }
    
    public boolean shutdown() {
        //TODO
        return true;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC FACTORY METHODS ---">
    
    /**
     * <p>Identical to <i>{@link #newTestInstance(GameViewContent, ReentrantLock)}</i> except the {@link ReentrantLock} passed to the {@link GameMap Game Map's} {@link GameMap#GameMap(GameViewContent, ReentrantLock, int, int, int) constructor} is always {@code null}.</p>
     * <p>Note that a new {@link ReentrantLock} is automatically created by the {@link GameMap} constructor if the specified value is {@code null}, so the returned {@link GameMap} object will still be {@code synchronized}, just only with itself.</p>
     *
     * @param content Any non-null {@link Springable} object used to enable {@link Springable} features in the returned {@link GameMap} object.
     *
     * @return The newly constructed {@link GameMap} instance.
     *
     * @see #newTestInstance(GameViewContent, ReentrantLock)
     * @see GameMap
     * @see GameMap#GameMap(GameViewContent, ReentrantLock, int, int, int)
     */
    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content) {
        return newTestInstance(content, null);
    }
    

    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content, @Nullable ReentrantLock lock) {
        return new GameMap(content, lock, 96, 64, 32);
    }
    
    //</editor-fold>
}
