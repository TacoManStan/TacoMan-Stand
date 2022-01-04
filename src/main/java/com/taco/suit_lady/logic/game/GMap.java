package com.taco.suit_lady.logic.game;

import com.taco.suit_lady.logic.game.objects.GTile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GMap {
    
    private final int tileSize = 20; // The number of "pixels" comprising each tile.
    
    //
    
    private final int width;
    private final int height;
    
    private final GTile[][] tileMap;
    
    public GMap(int width, int height) {
        this.width = width;
        this.height = height;
        
        this.tileMap = new GTile[width][height];
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GMap init() {
        initTiles();
        
        return this;
    }
    
    /**
     * <p>Populates the {@link GTile} array for this {@link GMap} with new {@link GTile} objects.</p>
     * <p>Note that eventually, this method should load tiles based on a specified data template that represents the contents of the {@link GMap}.</p>
     */
    private void initTiles() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                tileMap[i][j] = new GTile(this, i, j);
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
    
    
    public final GTile[][] getTileMap() {
        return tileMap;
    }
    
    //</editor-fold>
    
    public final @Nullable GTile getNeighbor(@NotNull GTile tile, int xTranslate, int yTranslate) {
        int xTemp = tile.getXLoc() + xTranslate;
        int yTemp = tile.getYLoc() + yTranslate;
        if (xTemp < 0 || xTemp >= width || yTemp < 0 || yTemp >= height)
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return tileMap[xTemp][yTemp];
    }
}
