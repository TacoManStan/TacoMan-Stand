package com.taco.suit_lady.game.walking.pathfinding;

import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Spencer
 */
public class TeleportObjectCache {

    private final ConcurrentHashMap<RSTile, ArrayList<TeleportObject>> cache;

    public TeleportObjectCache() {
        cache = new ConcurrentHashMap<>();
    }
    
    public final void add(RSObject start, RSObject target, String option) {
        add(start.getPosition(), target.getPosition(), option);
    }
    
    public final void add(RSTile start, RSTile target, String option) {
        ArrayList<TeleportObject> teleports = cache.get(start);
        if (teleports == null) {
            teleports = new ArrayList<>();
            cache.put(start, teleports);
        }
        teleports.add(new TeleportObject(target, option));
    }
    
    public final ArrayList<TeleportObject> get(RSTile tile) {
        if (containsKey(tile)) {
            return cache.get(tile);
        }
        return null;
    }
    
    public final boolean containsKey(RSTile tile) {
        return cache.containsKey(tile);
    }
}
