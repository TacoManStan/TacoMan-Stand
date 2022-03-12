package com.taco.suit_lady.game.walking.pathfinding;

import org.tribot.api2007.types.RSTile;

/**
 *
 * @author Spencer
 */
public class TeleportObject {

    private final RSTile target;
    private final String option;

    public TeleportObject(final RSTile target, final String option) {
        this.target = target;
        this.option = option;
    }

    public final RSTile getTarget() {
        return target;
    }

    public final String getOption() {
        return option;
    }
}
