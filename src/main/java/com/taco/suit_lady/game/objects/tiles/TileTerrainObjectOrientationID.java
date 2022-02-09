package com.taco.suit_lady.game.objects.tiles;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum TileTerrainObjectOrientationID {
    
    BORDERLESS(""), NORTH("_n"), SOUTH("_s"), EAST("_e"), WEST("_w"), NORTH_EAST("_ne"), NORTH_WEST("_nw"), SOUTH_EAST("_se"), SOUTH_WEST("_sw");
    
    private final String id;
    
    TileTerrainObjectOrientationID(@NotNull String id) {
        this.id = id;
    }
    
    //
    
    public final String value() { return id; }
    
    //
    
    public static TileTerrainObjectOrientationID defaultInstance() { return TileTerrainObjectOrientationID.BORDERLESS; }
    
    @Contract(pure = true)
    public static @NotNull String jsonId() { return "tile-terrain-obj-orientation-id"; }
}
