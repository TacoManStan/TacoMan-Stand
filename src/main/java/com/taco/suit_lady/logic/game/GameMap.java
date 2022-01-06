package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.MathTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameMap
        implements SpringableWrapper {
    
    private final int tileSize = 25; // The number of "pixels" comprising each tile.
    
    //
    
    private final StrictSpringable springable;
    
    //
    
    private final int width;
    private final int height;
    
    private final GameTile[][] tileMap;
    private final ArrayList<GameObject> mapObjects;
    
    public GameMap(@NotNull Springable springable, int width, int height) {
        this.springable = springable.asStrict();
        
        
        this.width = width;
        this.height = height;
        
        this.tileMap = new GameTile[width][height];
        this.mapObjects = new ArrayList<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMap init() {
        initTiles();
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
    
    public final int getTileSize() {
        return tileSize;
    }
    
    
    public final int getWidth() {
        return width;
    }
    
    public final int getHeight() {
        return height;
    }
    
    public final int getFullWidth() {
        return width * tileSize;
    }
    
    public final int getFullHeight() {
        return height * tileSize;
    }
    
    
    public final GameTile[][] getTileMap() {
        return tileMap;
    }
    
    public final ArrayList<GameObject> mapObjects() {
        return mapObjects;
    }
    
    //</editor-fold>
    
    public final @Nullable GameTile getNeighbor(@NotNull GameTile gameTile, int xTranslate, int yTranslate) {
        int xTemp = gameTile.getXLoc() + xTranslate;
        int yTemp = gameTile.getYLoc() + yTranslate;
        if (xTemp < 0 || xTemp >= width || yTemp < 0 || yTemp >= height)
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return tileMap[xTemp][yTemp];
    }
    
    @SuppressWarnings("ManualArrayCopy")
    public final @NotNull GameTile[][] getNeighbors(@NotNull GameTile gameTile, int xReach, int yReach) {
        // Reach parameters define how many tiles in each direction should be returned, excluding the host tile.
        // Therefore, the reach value must be doubled to account for opposite directions and then 1 must be added to account for the host tile.
        final GameTile[][] neighbors = new GameTile[(xReach * 2) + 1][(yReach * 2) + 1];
        
        for (int i = -xReach; i < xReach; i++)
            for (int j = -yReach; j < yReach; j++)
                neighbors[i][j] = tileMap[gameTile.getXLoc() + i][gameTile.getYLoc() + j];
        
        return neighbors;
    }
    
    /**
     * <p>Returns a {@link List} of {@link GameTile tiles} partially or entirely visible in the specified {@link Camera Camera's} bounds.</p>
     */
    // TO-EXPAND
    public final @NotNull List<GameTile> getTilesInView(@NotNull Camera camera) {
        int xLoc = camera.getXLocation();
        int yLoc = camera.getYLocation();
        //TODO: Include Offsets
        
        int pxMapWidth = getFullWidth();
        int pxMapHeight = getFullHeight();
        
        int tileMinX = Math.floorDiv(xLoc, getTileSize());
        int tileMinY = Math.floorDiv(yLoc, getTileSize());
        int tileMaxX = MathTools.ceil(xLoc + pxMapWidth, getTileSize());
        int tileMaxY = MathTools.ceil(yLoc + pxMapHeight, getTileSize());
        
        ArrayList<GameTile> returnTiles = new ArrayList<>();
        for (int i = tileMinX; i <= tileMaxX; i++)
            for (int j = tileMinY; j <= tileMaxY; j++)
                if (ArrayTools.isInMatrixBounds(getTileMap(), i, j))
                    returnTiles.add(getTileMap()[i][j]);
        return returnTiles;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return springable;
    }
    
    //</editor-fold>
}
