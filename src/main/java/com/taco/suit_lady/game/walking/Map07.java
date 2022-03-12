package com.taco.suit_lady.game.walking;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.Game;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;
import scripts.starfox.api.util.ArrayUtil;
import scripts.starfox.api2007.entities.Entities;

/**
 * The RSMap class provides utility methods related to information about the Old School Runescape map.
 * <p>
 * A map is defined as a 104x104 tile area in which the player resides. The base X and base Y coordinates are the coordinates of the most south-western tile in the current map.
 * <p>
 * @author Nolan
 */
public class Map07 {

    /**
     * Gets the collision data for the specified RSTile.
     * <p>
     * @param tile The RSTile being tested.
     * @return The collision data for the specified RSTile.
     */
    public static final int getCollisionData(RSTile tile) {
        int xOffset = Game.getBaseX();
        int yOffset = Game.getBaseY();
        int xT = tile.getX();
        int yT = tile.getY();
        int x = xT - xOffset;
        int y = yT - yOffset;
        int[][] collisionData = PathFinding.getCollisionData();
        if (collisionData == null || x < 0 || y < 0 || x > collisionData.length - 1 || y > collisionData[0].length - 1)
            return -1;
        return collisionData[x][y];
    }

    /**
     * Gets the tile located at the current map base.
     * <p>
     * The map base is defined as the most south-western tile of the current map.
     * @return The map base.
     */
    public static final RSTile getBase() {
        return new RSTile(Game.getBaseX(), Game.getBaseY(), Game.getPlane());
    }

    /**
     * Gets the current map.
     * @return The current map.
     */
    public static final RSArea getMap() {
        RSTile base = getBase();
        return new RSArea(base.translate(1, 1), new RSTile(base.getX() + 103 - 5, base.getY() + 103 - 5, base.getPlane()));
    }

    /**
     * Gets all of the tiles in the current map.
     * @return The tiles in the current map.
     */
    public static RSTile[] getMapTiles() {
        return getMap().getAllTiles();
    }

    /**
     * Gets the loaded tiles as a 2d array.
     * @return A 2d array representing the loaded tiles.
     */
    public static final RSTile[][] getMapTiles2D() {
        RSTile base = getBase().translate(1, 1);
        RSTile[] mapTiles = getMapTiles();
        RSTile[][] tiles = new RSTile[103 - 5][103 - 5];
        for (RSTile tile : mapTiles)
            tiles[tile.getX() - base.getX()][tile.getY() - base.getY()] = tile;
        return tiles;
    }

    /**
     * Checks if the specified tile is loaded.
     * @param tile The tile.
     * @return True if the specified tile is loaded, false otherwise.
     */
    public static boolean isLoaded(final RSTile tile) {
        if (tile != null) {
            int bx = Game.getBaseX(), by = Game.getBaseY();
            return tile.getX() >= bx && tile.getX() <= bx + 104
                    && tile.getY() >= by && tile.getY() <= by + 104;
        }
        return false;
    }

    /**
     * Checks the specified collision flags on the specified collision data. Returns true if any of the flags exist, false otherwise.
     * @param flag       The collision data that is being tested.
     * @param checkFlags The collision flags that are being checked.
     * @return True if any of the flags exist, false otherwise.
     */
    public static final boolean checkCollision(final int flag, final int... checkFlags) {
        for (final int checkFlag : checkFlags)
            if ((flag & checkFlag) == checkFlag)
                return true;
        return false;
    }

    /**
     * Returns true if there is any standard pathfinding block on the specified RSTile, false otherwise.
     * @param tile The RSTile that is being checked.
     * @return True if there is any standard pathfinding block on the specified RSTile, false otherwise.
     */
    public static final boolean isStandardBlocked(final RSTile tile) {
        int flag = getCollisionData(tile);
        return !PathFinding.isTileWalkable(tile)
                || checkCollision(flag, Flags.DECORATION_BLOCK)
                || checkCollision(flag, Flags.OBJECT_BLOCK)
                || checkCollision(flag, Flags.BLOCKED)
                || checkCollision(flag, Flags.OBJECT_TILE);
    }

    /**
     * Checks to see if the target tile can be walked to from the start tile.
     * <p>
     * This ONLY works with directly adjacent tiles, and does NOT take obstacles into account.
     * <p>
     * Designed to be used with {@link Entities#canReach(Positionable)}.
     * @param start  The start tile.
     * @param target The target tile.
     * @return True if the target tile can be walked to from the start tile, false otherwise.
     */
    public static boolean canWalkTo(RSTile start, RSTile target) {
        if (start == null || target == null)
            return false;
        int startFlag = getCollisionData(start);
        int targetFlag = getCollisionData(target);
        if (start.getY() + 1 == target.getY()) //Check NORTH
            if ((checkCollision(startFlag, Flags.WALL_NORTH))
                    || (checkCollision(targetFlag, Flags.WALL_SOUTH)))
                return false;
        if (start.getX() + 1 == target.getX()) //Check EAST
            if ((checkCollision(startFlag, Flags.WALL_EAST))
                    || (checkCollision(targetFlag, Flags.WALL_WEST)))
                return false;
        if (start.getY() - 1 == target.getY()) //Check SOUTH
            if ((checkCollision(startFlag, Flags.WALL_SOUTH))
                    || (checkCollision(targetFlag, Flags.WALL_NORTH)))
                return false;
        if (start.getX() - 1 == target.getX()) //Check WEST
            if ((checkCollision(startFlag, Flags.WALL_WEST))
                    || (checkCollision(targetFlag, Flags.WALL_EAST)))
                return false;
        return true;
    }

    /**
     * Returns true if any of the objects with the specified names are at the specified positionable, false otherwise.
     * <p>
     * The name only needs to match partially in order for this to return true.
     * @param p      The positionable.
     * @param oNames The names.
     * @return True if any of the objects with the specified names are at the specified positionable, false otherwise.
     */
    public static boolean isObjectAtTile(Positionable p, String... oNames) {
        for (RSObject o : Objects.getAt(p)) {
            RSObjectDefinition def = o.getDefinition();
            if (def != null)
                if (!def.getName().isEmpty() && ArrayUtil.containsPartOf(def.getName(), oNames))
                    return true;
        }
        return false;
    }
}
