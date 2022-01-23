package com.taco.suit_lady.logic.game.execution;

import com.taco.suit_lady.util.tools.SLExceptions;

public interface Tickable {
    
    void tick();
    
    long getTickCount();
    
    /**
     * <p>Called by the graphics updation thread (not yet implemented) to allow graphics objects to be visually updated based on the amount of time between ticks.</p>
     *
     * @param progress A float with bounds [0.0-1.0] indicating how close the loop is to the next tick, as a percentage.
     */
    // DO NOT IMPLEMENT THIS UNTIL THE CORE LOOP SYSTEM HAS BEEN FULLY ESTABLISHED AND POLISHED
    default void interpolate(float progress) {
        throw SLExceptions.nyi();
    }
}
