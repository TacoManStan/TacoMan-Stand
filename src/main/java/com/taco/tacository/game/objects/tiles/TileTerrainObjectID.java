package com.taco.tacository.game.objects.tiles;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum TileTerrainObjectID {
    
    ROCK("rock");
    
    private final String id;
    
    TileTerrainObjectID(@NotNull String id) {
        this.id = id;
    }
    
    //
    
    public final String value() { return id; }
    
    //
    
    public static TileTerrainObjectID defaultInstance() { return TileTerrainObjectID.ROCK; }
    
    @Contract(pure = true)
    public static @NotNull String jsonId() { return "tile-terrain-obj-id"; }
}
