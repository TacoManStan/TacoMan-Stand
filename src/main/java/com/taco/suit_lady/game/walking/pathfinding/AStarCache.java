package com.taco.suit_lady.game.walking.pathfinding;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSTile;
import scripts.starfox.api.Printing;
import scripts.starfox.api.Tau;
import scripts.starfox.api.util.DebugTimer;
import scripts.starfox.api.util.files.FileUtil;
import scripts.starfox.api2007.Game07;
import scripts.starfox.api2007.Player07;
import scripts.starfox.api2007.banking.Bank07;
import scripts.starfox.api2007.entities.Entities;
import scripts.starfox.api2007.walking.Flags;
import scripts.starfox.api2007.walking.Map07;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import static scripts.starfox.api2007.walking.Map07.checkCollision;

/**
 * @author Spencer
 */
public class AStarCache {

    private static final ReentrantLock lock;
    private static final Point bottomLeft;
    private static final Point topRight;
    private static boolean hasLoaded;

    private static final ArrayList<RSTile> banks;
    private static final TeleportObjectCache teleports;

    private static final MapCacheContainer[] cache;

    static {
        hasLoaded = false;
        lock = new ReentrantLock();
        bottomLeft = new Point(-1, -1);
        topRight = new Point(-1, -1);
        cache = new MapCacheContainer[3];
        banks = new ArrayList<>();
        teleports = new TeleportObjectCache();
        teleports.add(new RSTile(3187, 3434, 0), new RSTile(3189, 9834), "Climb-down");
        teleports.add(new RSTile(3189, 9834, 0), new RSTile(3187, 3434), "Climb-up");
        for (int i = 0; i < cache.length; i++)
            cache[i] = new MapCacheContainer();
    }

    public static boolean isBlocked(int x, int y, int plane) {
        AStarData data = getData(new RSTile(x, y, plane));
        return data != null ? !data.containsObstacle() && isStandardBlocked(data) : false;
    }

    public static Point getBottomLeft() {
        return bottomLeft;
    }

    public static Point getTopRight() {
        return topRight;
    }

    public static final boolean update() {
        if (Game07.isGameLoading())
            return false;
        int trans = 103 - 5;
        final RSTile cornerSW = Map07.getBase();
        final RSTile cornerNE = cornerSW.translate(trans, trans);
        final RSTile cornerSE = cornerSW.translate(trans, 0);
        final RSTile cornerNW = cornerSW.translate(0, trans);
        boolean updated = false;
        lock.lock();
        try {
            if (!isLoaded(cornerSW) || !isLoaded(cornerNE) || !isLoaded(cornerSE) || !isLoaded(cornerNW)) {
                RSTile[] mapTiles = Map07.getMapTiles();
                for (RSTile tile : mapTiles) {
                    if (!Map07.getBase().equals(cornerSW))
                        return false;
                    RSTile north = tile.translate(0, 1);
                    RSTile south = tile.translate(0, -1);
                    RSTile east = tile.translate(1, 0);
                    RSTile west = tile.translate(-1, 0);
                    if (!isLoaded(tile)) {
                        boolean bN = Map07.isStandardBlocked(north);
                        boolean bS = Map07.isStandardBlocked(south);
                        boolean bE = Map07.isStandardBlocked(east);
                        boolean bW = Map07.isStandardBlocked(west);
                        AStarData tempData = null;
                        if ((Map07.getCollisionData(tile) != 0 || bN || bS || bE || bW)) {
                            tempData = AStarData.fromRSTile(tile);
                            if (!banks.contains(tile) && Bank07.isBankAtTile(tile))
                                banks.add(tile);
                            if (tempData != null && tempData.getX() < 5000 && tempData.getY() < 5000) {
                                if (bottomLeft.x == -1 || bottomLeft.x > tempData.getX())
                                    bottomLeft.x = tempData.getX();
                                if (bottomLeft.y == -1 || bottomLeft.y > tempData.getY())
                                    bottomLeft.y = tempData.getY();
                                if (topRight.x == -1 || topRight.x < tempData.getX())
                                    topRight.x = tempData.getX();
                                if (topRight.y == -1 || topRight.y < tempData.getY())
                                    topRight.y = tempData.getY();
                            }
                        }
                        if (cache[tile.getPlane()].addData(new Point(tile.getX(), tile.getY()), tempData))
                            updated = true;
                    }
                }
            }
            if (!isLoaded(Player07.getPosition()))
                Printing.err("Player position is not loaded after updating cache.");
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return updated;
    }

    public static ArrayList<AStarData> getDatas() {
        if (!lock.tryLock())
            return null;
        try {
            return cache[Player.getPosition().getPlane()].getAll();
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return null;
    }

    public static ArrayList<Point> getLoadedDatas() {
        if (!lock.tryLock())
            return null;
        try {
            return (ArrayList<Point>) (cache[0].getAllLoaded().clone());
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return null;
    }

    public static ArrayList<RSTile> getBanks() {
        if (!lock.tryLock())
            return null;
        try {
            return banks;
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return null;
    }

    public static ArrayList<TeleportObject> getTeleports(RSTile tile) {
        if (!lock.tryLock())
            return null;
        try {
            if (teleports.containsKey(tile))
                return teleports.get(tile);
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return null;
    }

    public static boolean isTeleportAtTile(RSTile tile) {
        if (!lock.tryLock())
            return false;
        try {
            ArrayList<TeleportObject> ts = getTeleports(tile);
            return ts != null && !ts.isEmpty();
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return false;
    }

    public static void addTeleportTile(RSTile startTile, RSTile targetTile) {
        if (!lock.tryLock())
            return;
        try {
            if (startTile != null && targetTile != null)
                teleports.add(startTile, targetTile, null);
        } catch (Exception e) {
        } finally {
            unlock();
        }
    }

    /**
     * Returns the AStarData that is mapped to the specified RSTile, or null if no AStarData is mapped to the specified RSTile or the specified tile is null.
     * @param tile The tile.
     * @return The AStarData that is mapped to the specified RSTile, or null if no AStarData is mapped to the specified RSTile or the specified tile is null.
     */
    public static AStarData getData(RSTile tile) {
        if (!lock.tryLock())
            return null;
        try {
            if (tile != null) {
                MapCacheContainer container = cache[tile.getPlane()];
                if (container != null)
                    return container.getData(new Point(tile.getX(), tile.getY()));
            }
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return null;
    }

    /**
     * Checks to see if the specified RSTile is loaded in the cache.
     * <p>
     * If the specified tile is null, this method immediately returns false.
     * @param tile The tile.
     * @return True if the specified tile is loaded in the cache, false otherwise.
     */
    public static boolean isLoaded(RSTile tile) {
        if (!lock.tryLock())
            return false;
        try {
            if (cache != null && tile != null)
                return cache[tile.getPlane()].isLoaded(new Point(tile.getX(), tile.getY()));
        } catch (Exception e) {
        } finally {
            unlock();
        }
        return false;
    }

    public static boolean canWalkTo(int x1, int y1, int x2, int y2, int plane1, int plane2) {
        return canWalkTo(x1, y1, x2, y2, plane1, plane2, false);
    }

    public static boolean canWalkTo(int x1, int y1, int x2, int y2, int plane1, int plane2, boolean checkDiagWalk) {
        if (plane1 != plane2)
            return false;
        else {
            AStarData data1 = getData(new RSTile(x1, y1, plane1));
            AStarData data2 = getData(new RSTile(x2, y2, plane2));
            data1 = data1 == null ? new AStarData(x1, y1, plane1, 0, true, false) : data1;
            data2 = data2 == null ? new AStarData(x2, y2, plane2, 0, true, false) : data2;
            return canWalkTo(data1, data2, checkDiagWalk);
        }
    }

    public static double total = 0;
    public static int timesCalled = 0;

    public static boolean canWalkTo(AStarData start, AStarData target) {
        return canWalkTo(start, target, true);
    }

    public static boolean canWalkTo(AStarData start, AStarData target, boolean checkDiagWalk) {
        return canWalkTo(start, target, checkDiagWalk, true);
    }

    public static boolean canWalkTo(AStarData start, AStarData target, boolean checkDiagWalk, boolean checkObject) {
        if (start == null || target == null)
            return false;
        int dX = start.getX() - target.getX();
        int dY = start.getY() - target.getY();
        if (Math.abs(dX) > 1 || Math.abs(dY) > 1)
            return false;
        return canWalkToSub(start, target, checkDiagWalk, checkObject);
    }
    
    private static final int north = 2;
    private static final int south = 8;
    private static final int east = 4;
    private static final int west = 1;

    private static boolean canWalkToSub(AStarData start, AStarData target, boolean checkDiagWalk, boolean checkObject) {
        DebugTimer t = new DebugTimer("Can Walk To", 500);
        timesCalled++;
        int startFlag = start.getCollisionData();
        int targetFlag = target.getCollisionData();
        boolean northS = start.blockedN;
        boolean eastS = start.blockedE;
        boolean southS = start.blockedS;
        boolean westS = start.blockedW;
        boolean northT = target.blockedN;
        boolean eastT = target.blockedE;
        boolean southT = target.blockedS;
        boolean westT = target.blockedW;

        //Check NORTHWEST
        if (start.getX() - 1 == target.getX() && start.getY() + 1 == target.getY()) {
            if (check(northS, westS, checkDiagWalk) || check(southT, eastT, checkDiagWalk)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
            if (checkCollision(startFlag, Flags.WALL_BLOCK_NORTH, Flags.WALL_BLOCK_WEST, Flags.WALL_BLOCK_NORTHWEST)
                    || checkCollision(targetFlag, Flags.WALL_BLOCK_SOUTH, Flags.WALL_BLOCK_EAST, Flags.WALL_BLOCK_SOUTHEAST)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        }
        //Check NORTH
        if (start.getY() + 1 == target.getY())
            if (!start.containsDoor() && !target.containsDoor() && (checkObject || !start.isBlocked())
                    && (checkCollision(startFlag, Flags.WALL_BLOCK_NORTH) || checkCollision(targetFlag, Flags.WALL_BLOCK_SOUTH))) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        //Check NORTHEAST
        if (start.getX() + 1 == target.getX() && start.getY() + 1 == target.getY()) {
            if (check(northS, eastS, checkDiagWalk) || check(southT, westT, checkDiagWalk)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
            if (checkCollision(startFlag, Flags.WALL_BLOCK_NORTH, Flags.WALL_BLOCK_EAST, Flags.WALL_BLOCK_NORTHEAST)
                    || checkCollision(targetFlag, Flags.WALL_BLOCK_SOUTH, Flags.WALL_BLOCK_WEST, Flags.WALL_BLOCK_SOUTHWEST)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        }
        //Check EAST
        if (start.getX() + 1 == target.getX())
            if (!start.containsDoor() && !target.containsDoor() && (checkObject || !start.isBlocked())
                    && (checkCollision(startFlag, Flags.WALL_BLOCK_EAST) || checkCollision(targetFlag, Flags.WALL_BLOCK_WEST))) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        //Check SOUTHEAST
        if (start.getX() + 1 == target.getX() && start.getY() - 1 == target.getY()) {
            if (check(southS, eastS, checkDiagWalk) || check(northT, westT, checkDiagWalk)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
            if (checkCollision(startFlag, Flags.WALL_BLOCK_SOUTH, Flags.WALL_BLOCK_EAST, Flags.WALL_BLOCK_SOUTHEAST)
                    || checkCollision(targetFlag, Flags.WALL_BLOCK_NORTH, Flags.WALL_BLOCK_WEST, Flags.WALL_BLOCK_NORTHWEST)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        }
        //Check SOUTH
        if (start.getY() - 1 == target.getY())
            if (!start.containsDoor() && !target.containsDoor() && (checkObject || !start.isBlocked())
                    && (checkCollision(startFlag, Flags.WALL_BLOCK_SOUTH) || checkCollision(targetFlag, Flags.WALL_BLOCK_NORTH))) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        //Check SOUTHWEST
        if (start.getX() - 1 == target.getX() && start.getY() - 1 == target.getY()) {
            if (check(southS, westS, checkDiagWalk) || check(northT, eastT, checkDiagWalk)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
            if (checkCollision(startFlag, Flags.WALL_BLOCK_SOUTH, Flags.WALL_BLOCK_WEST, Flags.WALL_BLOCK_SOUTHWEST)
                    || checkCollision(targetFlag, Flags.WALL_BLOCK_NORTH, Flags.WALL_BLOCK_EAST, Flags.WALL_BLOCK_NORTHEAST)) {
                total += t.getElapsed();
                t.print();
                return false;
            }
        }
        //Check WEST
        if (start.getX() - 1 == target.getX())
            if (!start.containsDoor() && !target.containsDoor() && (checkObject || !start.isBlocked())
                    && (checkCollision(startFlag, Flags.WALL_BLOCK_WEST) || checkCollision(targetFlag, Flags.WALL_BLOCK_EAST))) {
                total += t.getElapsed();
                t.print();
                return false;
            }

        total += t.getElapsed();
        t.print();
        return true;
    }

    private static boolean check(boolean blocked1, boolean blocked2, boolean checkDiagWalk) {
        if (checkDiagWalk)
            return blocked1 || blocked2;
        else
            return blocked1 && blocked2;
    }

    public static boolean isStandardBlocked(AStarData data) {
        if (data != null) {
            final int flag = data.getCollisionData();
            return data.isBlocked()
                    || checkCollision(flag, Flags.DECORATION_BLOCK)
                    || checkCollision(flag, Flags.OBJECT_BLOCK)
                    || checkCollision(flag, Flags.BLOCKED)
                    || checkCollision(flag, Flags.OBJECT_TILE);
        } else
            return false;
    }

    public static Positionable getNonBlocked(final AStarData data) {
        final AStarData nonBlocked = getNonBlocked(data, null, 0);
        if (nonBlocked != null)
            return nonBlocked.toRSTile();
        return null;
    }

    private static AStarData getNonBlocked(final AStarData start, final AStarData previous, int iterations) {
        iterations++;
        if (iterations == 6)
            return previous;
        if (start == null)
            return null;
        AStarData leastBlocked = null;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (Math.abs(i) != Math.abs(j)) {
                    final AStarData target = getData(start.toRSTile().translate(i, j));
                    if (isNonBlockedSurrounding(start, target, iterations > 1)) {
                        total++;
                        final AStarData tempLeastBlocked = getNonBlocked(target, start, iterations);
                        if (tempLeastBlocked != null)
                            leastBlocked = target;
                    }
                }
        return leastBlocked != null ? leastBlocked : start;
    }

    public static boolean isNonBlockedSurrounding(final AStarData start, final AStarData target, final boolean checkObject) {
        if (start != null && target != null
                && !isStandardBlocked(target) && canWalkTo(start, target, true, checkObject))
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                    if (Math.abs(i) != Math.abs(j)) {
                        final AStarData tempData = getData(target.toRSTile().translate(i, j));
                        if (tempData != null && !tempData.matches(start)
                                && !tempData.isBlocked() && canWalkTo(target, tempData, true, checkObject))
                            return true;
                    }
        return false;
    }

    public static Positionable getNonBlockedReal(final AStarData data) {
        AStarData leastBlocked = null;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (Math.abs(i) != Math.abs(j)) {
                    final AStarData tempData = getData(data.toRSTile().translate(i, j));
                    if ((!isStandardBlocked(tempData)) && Map07.canWalkTo(data.toRSTile(), tempData.toRSTile())
                            && (Entities.distanceTo(tempData.toRSTile()) < 25
                            && Map07.isLoaded(tempData.toRSTile()) ? PathFinding.canReach(tempData.toRSTile(), false) : true))
                        leastBlocked = tempData;
                }
        return leastBlocked != null ? leastBlocked.toRSTile() : data.toRSTile();
    }

    public static void save() {
        if (!Tau.save_walking_cache)
            return;
        Printing.status("Synching map cache.");
        load();
        Printing.status("Map cache synched.");
        Printing.status("Saving map cache. Please wait.");
        String text = "";
        text += topRight.x + "\n";
        text += topRight.y + "\n";
        text += bottomLeft.x + "\n";
        text += bottomLeft.y + "\n";
        FileUtil.writeTextFileContents(text, "m_B", "cache", true, "Walking");
        text = "";
        for (RSTile tile : banks) {
            text += tile.getX() + ":";
            text += tile.getY() + ":";
            text += tile.getPlane() + "\n";
        }
        FileUtil.writeTextFileContents(text, "banks", "cache", true, "Walking");
        int savedCount = 0;
        for (int i = 0; i < 3; i++)
            savedCount += cache[i].save(i);
        cache[0].clear();
        cache[1].clear();
        cache[2].clear();
        cache[0] = null;
        cache[1] = null;
        cache[2] = null;
        System.gc();
        Printing.status("The map cache has been saved (" + savedCount + ").");
    }

    public static void load() {
        if (!Tau.load_walking_cache)
            return;
        Printing.status(hasLoaded ? "Map cache already loaded. Synching map cache." : "Loading map cache. Please wait.");
        for (int i = 0; i < 3; i++)
            cache[i].load(i);
        FileUtil.TextTraversable traversable = new FileUtil.TextTraversable() {
            @Override
            public void traverseNext(String next, int lineNumber) {
                int newI = Integer.parseInt(next);
                switch (lineNumber) {
                    case 1:
                        if (topRight.x == -1 || topRight.x < newI)
                            topRight.x = newI;
                        break;
                    case 2:
                        if (topRight.y == -1 || topRight.y < newI)
                            topRight.y = newI;
                    case 3:
                        if (bottomLeft.x == -1 || bottomLeft.x > newI)
                            bottomLeft.x = newI;
                        break;
                    case 4:
                        if (bottomLeft.y == -1 || bottomLeft.y > newI)
                            bottomLeft.y = newI;
                        break;
                }
            }
        };
        FileUtil.traverseTextFile(traversable, "m_B", "cache", "Walking");
        traversable = new FileUtil.TextTraversable() {
            @Override
            public void traverseNext(String next, int lineNumber) {
                if (!next.isEmpty()) {
                    String[] keys = next.split(":");
                    RSTile bankTile = new RSTile(parseInt(keys, 0), parseInt(keys, 1), parseInt(keys, 2));
                    if (!banks.contains(bankTile))
                        banks.add(bankTile);
                }
            }
        };
        FileUtil.traverseTextFile(traversable, "banks", "cache", "Walking");
        Printing.status("Map cache loaded.");
        hasLoaded = true;
    }

    private static int parseInt(String[] nexts, int index) {
        return nexts != null && nexts.length != 0 ? Integer.parseInt(nexts[index]) : -1;
    }

    public static void unlock() {
        try {
            lock.unlock();
        } catch (Exception e) {
        }
    }
}
