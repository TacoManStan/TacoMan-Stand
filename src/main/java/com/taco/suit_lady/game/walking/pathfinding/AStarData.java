package com.taco.suit_lady.game.walking.pathfinding;

import org.tribot.api2007.types.RSTile;
import scripts.starfox.api.util.ArrayUtil;
import scripts.starfox.api2007.walking.Map07;
import scripts.starfox.api2007.walking.Obstacles;

/**
 *
 * @author Spencer
 */
public class AStarData {

    private static final RSTile[] blacklist;

    static {
        blacklist = new RSTile[]{new RSTile(3152, 3403, 0), new RSTile(3153, 3403, 0), new RSTile(3154, 3403, 0)};
    }

    private final int x, y, plane, collisionData;
    private final boolean isBlocked, containsTeleport, containsDoor, containsBlockade, containsObstacle;
    public final boolean blockedN, blockedS, blockedE, blockedW;

    protected final boolean preloaded;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected AStarData(int x, int y, int plane, int collisionData, boolean isBlocked,
            boolean containsTeleport, boolean containsDoor, boolean containsBlockade, boolean containsObstacle,
            boolean blockedN, boolean blockedS, boolean blockedE, boolean blockedW, boolean preloaded) {
        this.x = x;
        this.y = y;
        this.plane = plane;

        this.containsTeleport = containsTeleport && !ArrayUtil.contains(toRSTile(), blacklist);
        this.containsDoor = containsDoor && !ArrayUtil.contains(toRSTile(), blacklist);
        this.containsBlockade = containsBlockade && !ArrayUtil.contains(toRSTile(), blacklist);
        this.containsObstacle = containsObstacle && !ArrayUtil.contains(toRSTile(), blacklist);

        this.collisionData = this.containsBlockade ? 0 : collisionData;
        this.isBlocked = isBlocked && !this.containsBlockade;
        this.blockedN = blockedN;
        this.blockedS = blockedS;
        this.blockedE = blockedE;
        this.blockedW = blockedW;

        this.preloaded = preloaded;
    }

    protected AStarData(int x, int y, int plane, int collisionData, boolean isBlocked, boolean preloaded) {
        this.x = x;
        this.y = y;
        this.plane = plane;
        this.collisionData = collisionData;
        this.isBlocked = isBlocked;
        this.containsTeleport = false;
        this.containsDoor = false;
        this.containsBlockade = false;
        this.containsObstacle = false;
        this.blockedN = false;
        this.blockedS = false;
        this.blockedE = false;
        this.blockedW = false;
        this.preloaded = preloaded;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlane() {
        return plane;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public int getCollisionData() {
        return collisionData;
    }

    public boolean containsTeleport() {
        return containsTeleport;
    }

    public boolean containsDoor() {
        return containsDoor;
    }

    public boolean containsBlockade() {
        return containsBlockade;
    }

    public boolean containsObstacle() {
        return containsObstacle;
    }

    /**
     * Returns an RSTile representation of this AStarData.
     * @return An RSTile representation of this AStarData.
     */
    public RSTile toRSTile() {
        return new RSTile(x, y, plane);
    }

    /**
     * Checks to see if this {@link AStarData} matches the specified {@link AStarData}.
     * <p>
     * Two {@link AStarData AStarData's} match if their x coordinates, y coordinates, and planes are equal.
     * @param data The {@link AStarData} being compared.
     * @return True if this {@link AStarData} matches the specified {@link AStarData}, false otherwise.
     */
    public boolean matches(final AStarData data) {
        if (data != null)
            return x == data.x && y == data.y && plane == data.plane;
        return false;
    }

    /**
     * Returns an AStarData object from the specified tile. If the tile is not loaded, this method returns null.
     * @param tile The tile that is being converted into an AStarData.
     * @return An AStarData object from the specified tile.
     */
    public static AStarData fromRSTile(RSTile tile) {
        if (Map07.isLoaded(tile)) {
            RSTile north = tile.translate(0, 1);
            RSTile south = tile.translate(0, -1);
            RSTile east = tile.translate(1, 0);
            RSTile west = tile.translate(-1, 0);
            boolean[] contains = Obstacles.getContains(tile);
            return new AStarData(tile.getX(), tile.getY(), tile.getPlane(), Map07.getCollisionData(tile), Map07.isStandardBlocked(tile),
                    contains[0], contains[1], contains[2], contains[0] || contains[1] || contains[2],
                    //                    Obstacles.containsTeleport(tile), Obstacles.containsDoor(tile), Obstacles.containsBlockade(tile), Obstacles.containsObstacle(tile),
                    Map07.isStandardBlocked(north), Map07.isStandardBlocked(south), Map07.isStandardBlocked(east), Map07.isStandardBlocked(west), false);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AStarData{" + "x=" + x + ", y=" + y + ", plane=" + plane + ", collisionData=" + collisionData + ", isBlocked=" + isBlocked
                + ", containsTeleport=" + containsTeleport + ", containsDoor=" + containsDoor + ", containsBlockade=" + containsBlockade
                + ", containsObstacle=" + containsObstacle + ", blockedN=" + blockedN + ", blockedS=" + blockedS + ", blockedE=" + blockedE
                + ", blockedW=" + blockedW + ", preloaded=" + preloaded + '}';
    }
}
