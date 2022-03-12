package com.taco.suit_lady.game.walking;

import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSTile;
import scripts.starfox.api2007.Player07;
import scripts.starfox.api2007.entities.Entities;
import scripts.starfox.api2007.walking.pathfinding.AStarPathfinder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The Path07 class represents a path made of RSTiles.
 * <p>
 * @author Nolan
 */
public class Path07 {

    private final RSTile[] path;
    private int nextDistance;

    /**
     * Constructs a new TilePath.
     * @param path The tiles on the path.
     */
    public Path07(RSTile[] path) {
        this.path = path;
        this.nextDistance = General.random(3, 6);
    }

    public Path07(RSTile target) {
        this.path = AStarPathfinder.get().getTilePath(target);
        this.nextDistance = General.random(3, 6);
    }

    /**
     * Gets the tiles on the path.
     * @return The tiles.
     */
    public RSTile[] getPath() {
        return this.path;
    }

    /**
     * Gets the tiles on the path as an ArrayList.
     * @return The tiles.
     */
    public ArrayList<RSTile> getPathList() {
        return new ArrayList<>(Arrays.asList(path));
    }

    /**
     * Gets the next distance between the game destination and the player position that should be used.
     * @return The next distance.
     */
    public int getNextDistance() {
        return this.nextDistance;
    }

    /**
     * Gets the tile at the end of the path.
     * @return The end tile. Null if path length is 0.
     */
    public RSTile getEnd() {
        if (path.length < 1)
            return null;
        return path[path.length - 1];
    }

    /**
     * Returns the length of this Path07.
     * @return The length of this Path07.
     */
    public int getLength() {
        return path.length;
    }

    /**
     * Gets the next walkable tile on the path.
     * @return The next walkable tile.
     */
    public RSTile getNextTile() {
        if (path == null)
            return null;
        if (path.length < 2)
            return getEnd();
        for (int i = path.length - 1; i >= 0; i--)
            if (Entities.canReach(path[i])) {
                Point mmTile = Projection.tileToMinimap(path[i]);
                if (mmTile != null && Projection.isInMinimap(mmTile))
                    return path[i];
            }
        return getEnd();
    }

    /**
     * Walks to the next tile on the path if the current game destination is null or the player is within the next distance range of the destination.
     * @return True if successful, false otherwise.
     */
    public boolean walkToNext() {
        RSTile destination = Game.getDestination();
        if (destination != null && Entities.distanceTo(destination) > getNextDistance())
            return true;
        resetNextDistance();
        return Walking07.straightWalk(getNextTile());
    }

    /**
     * Walks to the next tile on the path if the current game destination is null or the player is within the next distance range of the destination.
     * @param stopping_condition The stopping condition.
     * @return True if successful, false otherwise.
     */
    public boolean walkToNext(Condition stopping_condition) {
        RSTile destination = Game.getDestination();
        if (destination != null && Entities.distanceTo(destination) > getNextDistance())
            return true;
        resetNextDistance();
        return Walking07.straightWalk(getNextTile(), stopping_condition);
    }

    /**
     * Resets the next distance.
     */
    public void resetNextDistance() {
        this.nextDistance = General.random(3, 6);
    }

    /**
     * Returns true if the player is near this path, false otherwise.
     * @return True if the player is near this path, false otherwise.
     */
    public boolean isNear() {
        for (int i = 0; i < (path.length / 3 * 3); i += 3) {
            final RSTile tile = path[i];
            if (Entities.distanceTo(tile) < 12 && Entities.aStarDistanceTo(tile) < 12)
                return true;
        }
        return false;
    }

    /**
     * Returns the tile that is closest to the player on this path.
     * @return The tile that is closest to the player on this path.
     */
    public RSTile getClosest() {
        RSTile closest = null;
        RSTile dest = Game.getDestination();
        dest = dest != null ? dest : Player07.getPosition();
        for (RSTile tile : path) {
            if (tile.equals(dest))
                return tile;
            if (closest == null || (Entities.distanceTo(dest, tile) <= Entities.distanceTo(dest, closest)))
                closest = tile;
        }
        return closest;
    }

    /**
     * Reverses this Path07, and then returns the result as an RSTile array.
     * @return The reverse of the RSTile array represented by this Path07.
     */
    public RSTile[] reverse() {
        final int size = path.length;
        final RSTile[] reversedTiles = path.clone();

        for (int i = 0; i < size / 2; i++) {
            final RSTile temp = reversedTiles[i];
            reversedTiles[i] = reversedTiles[size - 1 - i];
            reversedTiles[size - 1 - i] = temp;
        }
        return reversedTiles;
    }

    /**
     * Returns an updated version of the specified tile path based on the specified target.
     * <p>
     * The target should be the same as the previous target of the specified Path07 in order for this to work correctly. Or, alternatively, passing in a null Path07 will result
     * in the standard generation of a path to the target specified.
     * <p>
     * Will only regenerate a new path to the target if the current path does not use the same target (usually meaning the target was previously out of range of the map) or if the
     * current path is less than 50 tiles long.
     * @param newTarget The target.
     * @param path      The path.
     * @return An updated version of the specified tile path based on the specified target.
     */
    public static Path07 update(RSTile newTarget, Path07 path) {
        if (path == null || (!newTarget.equals(path.getEnd()) && path.getLength() < 10))
            return new Path07(AStarPathfinder.get().getTilePath(newTarget));
        else {
            ArrayList<RSTile> newPath = new ArrayList<>();
            boolean adding = false;
            final RSTile closest = path.getClosest();
            if (closest == null)
                return path;
            boolean isPlayersPosition = closest.equals(Player07.getPosition());
            for (RSTile tile : path.getPath()) {
                if (adding)
                    newPath.add(tile);
                if (tile.equals(closest))
                    adding = true;
            }
            if (!isPlayersPosition) {
                ArrayList<RSTile> path2 = AStarPathfinder.get().getPath(closest);
                if (path2 != null) {
                    Collections.reverse(path2);
                    for (RSTile tempTile : path2)
                        newPath.add(0, tempTile);
                }
            } else
                newPath.add(0, closest);
            return new Path07(newPath.toArray(new RSTile[0]));
        }
    }
}
