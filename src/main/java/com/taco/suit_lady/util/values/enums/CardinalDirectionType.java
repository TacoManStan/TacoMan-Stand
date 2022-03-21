package com.taco.suit_lady.util.values.enums;

import com.taco.suit_lady.util.enums.DefaultableEnum;
import com.taco.suit_lady.util.tools.list_tools.A;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.taco.suit_lady.util.values.enums.CardinalDirection.*;

/**
 * <p>Defines the types of {@link CardinalDirection} constants matching a {@link CardinalDirectionType} constant.</p>
 */
//TO-EXPAND
public enum CardinalDirectionType
        implements DefaultableEnum<CardinalDirectionType> {
    
    ALL {
        @Override public @NotNull CardinalDirection[] directions() {
            return CardinalDirection.values();
        }
    },
    ALL_BUT_CENTER {
        @Override public @NotNull CardinalDirection[] directions() {
            return A.concat(UNIDIRECTIONAL.directions(), MULTIDIRECTIONAL.directions());
        }
    },
    UNIDIRECTIONAL {
        @Override public @NotNull CardinalDirection[] directions() {
            return new CardinalDirection[]{NORTH, SOUTH, EAST, WEST};
        }
    },
    MULTIDIRECTIONAL {
        @Override public @NotNull CardinalDirection[] directions() {
            return new CardinalDirection[]{NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST};
        }
    },
    UNIDIRECTIONAL_C {
        @Override public @NotNull CardinalDirection[] directions() {
            return A.concat(UNIDIRECTIONAL.directions(), CENTER);
        }
    },
    MULTIDIRECTIONAL_C {
        @Override public @NotNull CardinalDirection[] directions() {
            return A.concat(MULTIDIRECTIONAL.directions(), CENTER);
        }
    },
    CENTER_ONLY {
        @Override public @NotNull CardinalDirection[] directions() {
            return new CardinalDirection[]{CENTER};
        }
    };
    
    CardinalDirectionType() { }
    
    public abstract @NotNull CardinalDirection[] directions();
    public final @NotNull List<CardinalDirection> directionList() { return Arrays.asList(directions()); }
    
    @Override public final @NotNull CardinalDirectionType defaultValue() { return ALL; }
}
