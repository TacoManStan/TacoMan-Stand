package com.taco.suit_lady.util.values.numbers;

import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.Exc;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum CardinalDirection {
    
    CENTER(0, 0),
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0),
    NORTH_EAST(1, -1), NORTH_WEST(-1, -1), SOUTH_EAST(1, 1), SOUTH_WEST(-1, 1);
    
    private final int xMod;
    private final int yMod;
    
    CardinalDirection(int xMod, int yMod) {
        this.xMod = xMod;
        this.yMod = yMod;
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final int xMod() { return xMod; }
    public final int yMod() { return yMod; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    public static CardinalDirection[] valuesUnidirectional() { return new CardinalDirection[]{NORTH, SOUTH, EAST, WEST}; }
    public static CardinalDirection[] valuesUnidirectionalC() { return A.concat(valuesUnidirectional(), CENTER); }
    
    public static CardinalDirection[] valuesMultiDirectional() { return new CardinalDirection[]{NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST}; }
    public static CardinalDirection[] valuesMultiDirectionalC() { return A.concat(valuesMultiDirectional(), CENTER); }
    
    public static CardinalDirection[] valuesNoC() { return A.concatMulti(valuesUnidirectional(), valuesMultiDirectional()); }
    
    //
    
    public static CardinalDirection getMatching(@NotNull NumExpr2D<?> input) {
        if (input.aInt() < -1 || input.aInt() > 1 || input.bInt() < -1 || input.bInt() > 1)
            throw Exc.unsupported("Input Values Must be in Range [-1,1]:  " + input);
        return Arrays.stream(values()).filter(direction -> direction.xMod() == input.aInt() && direction.yMod() == input.bInt()).findFirst().orElse(null);
    }
    
    //</editor-fold>
}
