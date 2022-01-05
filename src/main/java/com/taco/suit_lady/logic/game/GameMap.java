package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.objects.Object;
import com.taco.suit_lady.logic.game.objects.Tile;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GameMap
        implements SpringableWrapper {
    
    private final int tileSize = 25; // The number of "pixels" comprising each tile.
    
    //
    
    private final StrictSpringable springable;
    
    //
    
    private final int width;
    private final int height;
    
    private final Tile[][] tileMap;
    private final ArrayList<Object> mapObjects;
    
    public GameMap(@NotNull Springable springable, int width, int height) {
        this.springable = springable.asStrict();
        
        
        this.width = width;
        this.height = height;
        
        this.tileMap = new Tile[width][height];
        this.mapObjects = new ArrayList<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMap init() {
        initTiles();
        return this;
    }
    
    /**
     * <p>Populates the {@link Tile} array for this {@link GameMap} with new {@link Tile} objects.</p>
     * <p>Note that eventually, this method should load tiles based on a specified data template that represents the contents of the {@link GameMap}.</p>
     */
    private void initTiles() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tileMap[i][j] = new Tile(this, i, j);
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
    
    
    public final Tile[][] getTileMap() {
        return tileMap;
    }
    
    public final ArrayList<Object> mapObjects() {
        return mapObjects;
    }
    
    //</editor-fold>
    
    public final @Nullable Tile getNeighbor(@NotNull Tile tile, int xTranslate, int yTranslate) {
        int xTemp = tile.getXLoc() + xTranslate;
        int yTemp = tile.getYLoc() + yTranslate;
        if (xTemp < 0 || xTemp >= width || yTemp < 0 || yTemp >= height)
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return tileMap[xTemp][yTemp];
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Springable springable() {
        return springable;
    }
    
    //</editor-fold>
}
