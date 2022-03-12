package com.taco.suit_lady.game.walking;

import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.starfox.api.AntiBan;
import scripts.starfox.api.Client07;
import scripts.starfox.api.Printing;
import scripts.starfox.api.conditions.PositionableCondition;
import scripts.starfox.api.util.ArrayUtil;
import scripts.starfox.api.util.Timer;
import scripts.starfox.api2007.Camera07;
import scripts.starfox.api2007.Clicking07;
import scripts.starfox.api2007.Game07;
import scripts.starfox.api2007.Player07;
import scripts.starfox.api2007.banking.Bank07;
import scripts.starfox.api2007.entities.Entities;
import scripts.starfox.api2007.entities.Objects07;
import scripts.starfox.api2007.walking.pathfinding.AStarCache;
import scripts.starfox.api2007.walking.pathfinding.AStarPathfinder;
import scripts.starfox.graphics.Drawing;
import scripts.starfox.javafx.fxmlutil.FXUtil;
import scripts.starfox.manager.moveSet.ActionSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nolan
 */
public class Walking07 {

    private static ArrayList<RSTile> current_path;
    private static Positionable target;
    private static final ReentrantLock path_lock;

    private static final Color path_color = new Color(0, 50, 200);
    private static final Color dest_path_color = new Color(0, 200, 50);
    private static final Color object_target_color = new Color(0, 50, 255, 60);
    private static final Color target_color = new Color(225, 0, 0, 80);
    private static final Color visited_color_start = new Color(0, 255, 255);
    private static final Color visited_color_finish = new Color(255, 0, 255);

    private static final ArrayList<RSTile> startVisitedTiles = new ArrayList<>();
    private static final ArrayList<RSTile> finishVisitedTiles = new ArrayList<>();

    static {
        path_lock = new ReentrantLock();
    }

    public static ArrayList<RSTile> getVisitedTiles(boolean start) {
        if (start)
            return (ArrayList<RSTile>) startVisitedTiles.clone();
        else
            return (ArrayList<RSTile>) finishVisitedTiles.clone();
    }

    public static void visitTile(RSTile tile, boolean start) {
        if (start)
            if (!startVisitedTiles.contains(tile))
                startVisitedTiles.add(tile);
            else if (!finishVisitedTiles.contains(tile))
                finishVisitedTiles.add(tile);
    }

    public synchronized static void draw(final Graphics2D g2) {
        drawPath(g2);
        drawTarget(g2);
    }

    public static void drawVisisted(final Graphics2D g2) {
        FXUtil.runEDT(() -> {
            Drawing.drawPath(g2, visited_color_start, visited_color_start, getVisitedTiles(true).toArray(new RSTile[0]));
            Drawing.drawPath(g2, visited_color_finish, visited_color_finish, getVisitedTiles(false).toArray(new RSTile[0]));
        }, true);
    }

    public static void drawPath(final Graphics2D g2) {
        FXUtil.runEDT(() -> {
            Drawing.drawPath(g2, getPathColor(), getPathColor(), Walking07.getPathArr());
        }, true);
    }

    private static Color getPathColor() {
        return current_path != null ? path_color : dest_path_color;
    }

    public static void drawTarget(final Graphics2D g2) {
        FXUtil.runEDT(() -> {
            if (target != null)
                if (target instanceof RSObject) {
                    RSObject target2 = (RSObject) target;
                    RSModel model = target2.getModel();
                    if (model != null)
                        Drawing.drawModel(g2, model, object_target_color);
                } else if (target instanceof RSNPC) {
                    RSNPC target2 = (RSNPC) target;
                    RSModel model = target2.getModel();
                    if (model != null)
                        Drawing.drawModel(g2, model, object_target_color);
                } else
                    Drawing.drawTile(g2, target.getPosition(), Color.BLACK, object_target_color, object_target_color);
            if (AStarPathfinder.non_blocked != null)
                Drawing.drawTile(g2, AStarPathfinder.non_blocked, Color.BLACK, target_color, target_color);
        }, true);
    }

    public static ArrayList<RSTile> getPath() {
        if (path_lock.tryLock())
            try {
                if (current_path != null)
                    return (ArrayList<RSTile>) current_path.clone();
                else {
                    RSTile dest = Game.getDestination();
                    if (dest != null)
                        return AStarPathfinder.get().getPath(dest);
                }
            } finally {
                try {
                    path_lock.unlock();
                } catch (Exception e) {
                }
            }
        return null;
    }

    public static final RSTile[] getPathArr() {
        ArrayList<RSTile> tempPath = getPath();
        if (tempPath == null)
            return null;
        return tempPath.toArray(new RSTile[0]);
    }

    /**
     * Returns the reverse of the specified RSTile path.
     * @param path The path.
     * @return The reverse of the specified RSTile path.
     */
    public static RSTile[] reverse(RSTile[] path) {
        return new Path07(path).reverse();
    }

    /**
     * Walks to the nearest bank using a* to generate the path to the target.
     * <p>
     * Does not use presets or WebWalking backups like {@link Bank07#walkTo(boolean)}
     * does.
     * @return True if the walking was successful, false otherwise.
     * @see Bank07#walkTo(boolean)
     */
    public static boolean aStarWalkToBank() {
        return aStarWalk(Bank07.getNearestLoaded());
    }

    /**
     * Walks to the nearest object with the specified names and/or options using
     * a* to generate the path to the target.
     * <p>
     * If either names or options is null, the
     * @param names   The names of the objects.
     * @param options The options of the objects.
     * @return True if the walking was successful, false otherwise.
     * @see Objects07#getObject(String[], String[], int)
     */
    public static boolean aStarWalkToObject(final String[] names, final String[] options) {
        return aStarWalk(Objects07.getObject(names, options, 104));
    }

    /**
     * Walks to the specified target using a* to generate the path to the
     * target.
     * <p>
     * This method will return true when the following are true:
     * <ol>
     * <li>The target is within 5 tiles of the player.</li>
     * <li>The target's center is on the screen.</li>
     * <li>If the target's center cannot be turned to, the target must be on the
     * screen.</li>
     * <li>If the target is reachable.</li>
     * </ol>
     * <p>
     * This means that any precise movements should NOT use this method, or you
     * should use {@link #aStarWalk(Positionable, Condition, InitialWalkType...)} instead and provide your own condition.
     * @param target The target.
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalk(final Positionable target) {
        return aStarWalk(target, new InitialWalkType[]{});
    }

    /**
     * Walks to the specified target using a* to generate the path to the target.
     * @param target    The target.
     * @param walkTypes The types of walking that should be attempted before A* is used.
     *                  Each type will be executed in order they are added.
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalk(final Positionable target, final InitialWalkType... walkTypes) {
        return aStarWalk(target, WalkingConditions.genericCondition(target), walkTypes);
    }

    /**
     * Walks to the specified target using a* to generate the path to the target.
     * @param target             The target.
     * @param stopping_condition The condition that, when true, will cause this method to stop execution and return true.
     * @param walkTypes          The types of walking that should be attempted before A* is used.
     *                           Each type will be executed in order they are added.
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalk(final Positionable target, final Condition stopping_condition, final InitialWalkType... walkTypes) {
        if (target == null)
            return false;
        if (stopping_condition == null)
            return aStarWalk(target);
        if (walkTypes != null)
            for (InitialWalkType walkType : walkTypes)
                if (walkType == InitialWalkType.WEB_WALK && WebWalking.walkTo(target, stopping_condition, 1000))
                    return true;
                else if (walkType == InitialWalkType.STRAIGHT_WALK && straightWalk(target.getPosition(), stopping_condition))
                    return true;
        Timer t = new Timer(20000);
        Timer updateT = new Timer(6000);
        AStarCache.update();
        Path07 tilePath = new Path07(target.getPosition());
        t.start();
        updateT.start();
        int j = 0;
        RSTile oldBase = Map07.getBase();
        Walking07.target = target;
        while (!t.isTimedOut()) {
            j++;
            if (stopping_condition.active() || comparePlayerPosition(target.getPosition())) {
                current_path = null;
                Walking07.target = null;
                return true;
            }
            if (Entities.distanceTo(tilePath.getEnd()) <= 1.5) {
                current_path = null;
                Walking07.target = null;
                Client07.sleepSD(50);
                return false;
            }
            if (j == 5 && Player07.isMoving()) {
                j = 0;
                t.reset();
            }
            if (Game07.isGameLoaded()) {
                AntiBan.activateRun();
                if ((Map07.getBase().equals(oldBase) || updateT.isTimedOut()) && AStarCache.update()) {
                    current_path = null;
                    Walking07.target = null;
                    Client07.sleepSD(50);
                    return false;
                }
                tilePath = Path07.update(target.getPosition(), tilePath);
                if (path_lock.tryLock())
                    try {
                        current_path = tilePath.getPathList();
                    } catch (Exception e) {
                    } finally {
                        try {
                            path_lock.unlock();
                        } catch (Exception e) {
                        }
                    }
                final RSObject obstacle = Obstacles.getNextObstacle(tilePath.getPath());
                final Path07 finalPath = tilePath;
                if (obstacle != null)
                    if (Entities.isOnScreen(obstacle)) {
                        final RSTile playerPos = Player07.getPosition();
                        return Clicking07.click(obstacle, new Condition() {
                            @Override
                            public boolean active() {
                                Client07.sleepCondition();
                                return !Objects.isAt(obstacle, obstacle.getID()) || Obstacles.isPastObstacle(finalPath.getPath(), obstacle)
                                        || (Player.getPosition().getPlane() != playerPos.getPlane() && !Game07.isGameLoading());
                            }
                        }, 2500, 500, Obstacles.commandsArr);
                    } else {
                        Path07 obstaclePath = new Path07(obstacle.getPosition());
                        obstaclePath.walkToNext(WalkingConditions.genericCondition(obstacle));
                    }
                else
                    tilePath.walkToNext(stopping_condition);
            }
            Client07.sleepLoop();
        }
        current_path = null;
        Walking07.target = null;
        return false;
    }

    /**
     * Walks the specified path using a* to generate the path to each target.
     * <p>
     * The tiles in the path should be reasonably spaced apart, but it is not a
     * requirement. After each step in the path (with the exception of the last
     * one), any nearby obstacles will be handled, if necessary.
     * @param stopping_condition The condition that, when true, will cause this
     *                           method to stop execution and return true.
     * @param path               The path that is being walked.
     * @see #aStarWalk(Positionable, Condition, InitialWalkType...)
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalkPath(Condition stopping_condition, RSTile... path) {
        path = getRefinedPath(path);
        final boolean isNull = stopping_condition == null;
        for (int i = 0; i < path.length; i++) {
            final RSTile tile = path[i];
            final RSTile tempTarget = path[path.length - 1];
            final RSTile nextTile = !tile.equals(tempTarget) ? path[i + 1] : null;
            if (isNull)
                stopping_condition = WalkingConditions.genericCondition(tile);
            if (!aStarWalk(tile, stopping_condition)
                    || (nextTile != null && !ObstacleHandler.handleTeleport(tile, nextTile, tile)))
                return false;
        }
        return true;
    }

    /**
     * Walks the specified path using a* to generate the path to each target.
     * <p>
     * The tiles in the path should be reasonably spaced apart, but it is not a
     * requirement. After each step in the path (with the exception of the last
     * one), any nearby obstacles will be handled, if necessary.
     * @param path The path that is being walked.
     * @see #aStarWalk(Positionable)
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalkPath(RSTile... path) {
        return aStarWalkPath(null, path);
    }

    /**
     * Walks the specified path using a* to generate the path to each target.
     * <p>
     * The tiles in the path should be reasonably spaced apart, but it is not a
     * requirement. After each step in the path (with the exception of the last
     * one), any nearby obstacles will be handled, if necessary.
     * @param path The path that is being walked.
     * @see #aStarWalkPath(RSTile...)
     * @see #aStarWalk(Positionable)
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalkPath(ArrayList<RSTile> path) {
        return aStarWalkPath(path.toArray(new RSTile[0]));
    }

    /**
     * Walks the specified path using a* to generate the path to each target.
     * <p>
     * The tiles in the path should be reasonably spaced apart, but it is not a
     * requirement. After each step in the path (with the exception of the last
     * one), any nearby obstacles will be handled, if necessary.
     * @param stopping_condition The condition that, when true, will cause this
     *                           method to stop execution and return true.
     * @param path               The path that is being walked.
     * @see #aStarWalkPath(Condition, RSTile...)
     * @see #aStarWalk(Positionable, Condition, InitialWalkType...)
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean aStarWalkPath(final Condition stopping_condition, ArrayList<RSTile> path) {
        return aStarWalkPath(stopping_condition, path.toArray(new RSTile[0]));
    }

    /**
     * Walks the specified preset movement path.
     * <p>
     * Currently only used in the bot farm manager. Going to be removed once
     * walking is upgraded.
     * @param target  The target.
     * @param presets The presets.
     * @return True if the preset was walked correctly, false otherwise.
     */
    public static boolean walkPresetMovement(Positionable target, MovementPreset... presets) {
        if (target == null || presets == null)
            return false;
        Client07.println("Executing Presets");
        Positionable source = Player07.getPosition();
        MovementPreset getOutMovement = null;
        MovementPreset getToMovement = null;
        for (MovementPreset preset : presets) {
            if (preset.isGetOut() && preset.contains(source) && !preset.contains(target))
                getOutMovement = preset;
            if (!preset.isGetOut() && preset.contains(target) && !preset.contains(source))
                getToMovement = preset;
            if (getOutMovement != null && getToMovement != null)
                break;
        }
        boolean failed = false;
        if (getOutMovement != null && !getOutMovement.execute()) {
            Client07.println("Get out execution failed.");
            failed = true;
        }
        if (getToMovement != null && !getToMovement.execute()) {
            Client07.println("Get to execution failed.");
            failed = true;
        }
        return !failed;
    }

    /**
     * Returns the rest of the path relative to where the player is currently
     * standing.
     * @param originPath The original path.
     * @return The rest of the path relative to where the player is currently
     *         standing.
     */
    private static RSTile[] getRefinedPath(RSTile[] originPath) {
        Client07.println("Getting refined path...");
        final RSTile[] withinRange = Entities.getWithinRange(originPath, 104);
        originPath = withinRange.length == 0 ? originPath : withinRange;
        RSTile closestTile = null;
        ArrayList<RSTile> closeTiles = new ArrayList<>();
        Printing.taco("Running loop...");
        for (int i = 0; i < originPath.length; i++) {
            Printing.taco("Running loop " + i);
            final RSTile tile = originPath[i];
            if (closestTile == null
                    || (ArrayUtil.contains(tile, withinRange) && Entities.aStarDistanceTo(closestTile) > Entities.aStarDistanceTo(tile))) {
                closestTile = tile;
                closeTiles.clear();
            }
            closeTiles.add(tile);
        }
        return closeTiles.toArray(new RSTile[closeTiles.size()]);
    }

    /**
     * Walks in a straight line towards the destination.
     * @param tile The destination.
     * @return True if we reached the destination, false otherwise.
     */
    public static boolean straightWalk(final RSTile tile) {
        if (tile == null)
            return false;
        if (compareGameDestination(tile) || comparePlayerPosition(tile))
            return true;
        return Walking.blindWalkTo(tile, new Condition() {
            @Override
            public boolean active() {
                Client07.sleep(50);
                AntiBan.activateRun();
                return compareGameDestination(tile);
            }
        }, 500);
    }

    /**
     * Walks in a straight line towards the destination.
     * @param tile      The destination.
     * @param condition The stopping condition.
     * @return True if we reached the destination, false otherwise.
     */
    public static boolean straightWalk(final RSTile tile, final Condition condition) {
        if (tile == null)
            return false;
        if (compareGameDestination(tile) || comparePlayerPosition(tile))
            return true;
        return Walking.blindWalkTo(tile, new Condition() {
            @Override
            public boolean active() {
                Client07.sleep(50);
                AntiBan.activateRun();
                if (condition != null && condition.active())
                    return true;
                return compareGameDestination(tile);
            }
        }, 500);
    }

    /**
     * Attempts to screen-walk to a random reachable nearby tile.
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean walkNearbyTile() {
        return walkNearbyTile(new PositionableCondition() {
            @Override
            public boolean isActive(Positionable positionable) {
                return !Game07.isGameLoading() && positionable.getPosition().isOnScreen() && PathFinding.canReach(positionable, false);
            }
        });
    }

    /**
     * Attempts to screen-walk to a random reachable nearby tile.
     * @param condition This condition must be true in order for the tile to be clicked.
     * @return True if the walking was successful, false otherwise.
     */
    public static boolean walkNearbyTile(PositionableCondition condition) {
        final RSTile pos = Player07.getPosition();
        if (pos != null) {
            RSTile tile = pos;
            Timer t = new Timer(3000);
            t.start();
            do {
                tile = tile.translate(General.random(-2, 2), General.random(-2, 2));
                if (!tile.isOnScreen()) {
                    Camera07.turnTo(tile);
                    Client07.sleep(500);
                }
            } while ((!t.isTimedOut() && (!condition.isActive(tile) || Map07.isStandardBlocked(tile))));
            boolean didWalk = Walking.clickTileMS(tile, "Walk here");
            if (didWalk)
                Client07.sleep(1000);
            return didWalk;
        }
        return false;
    }

    /**
     * Attempts to walk directly to a target tile using the screen.
     * <p>
     * This method will attempt to reach the EXACT tile that you pass in for 7.5
     * seconds. If the tile that is passed in is not pathable, this method
     * returns false.
     * @param target The target tile.
     * @return True if the target was reached, false otherwise.
     */
    public static boolean walkDirect(RSTile target) {
        RSTile destination = Game.getDestination();
        if (destination != null && destination.equals(target))
            return true;
        final RSTile pos = Player07.getPosition();
        if (target != null && pos != null) {
            if (Map07.isStandardBlocked(target))
                return false;
            if (Entities.distanceTo(target) > 8 && Entities.aStarDistanceTo(target) > 8)
                if (!aStarWalk(target))
                    return false;
            if (!target.isOnScreen()) {
                Camera.turnToTile(target);
                Client07.sleep(150, 300);
            }
            Timer timer = new Timer(7500);
            timer.start();
            while (!timer.isTimedOut() && !Player07.getPosition().equals(target)) {
                Client07.sleep(25, 200);
                AntiBan.moveCamera();
                if (!Player07.isMoving())
                    Walking.clickTileMS(target, "Walk here");
            }
        }
        return false;
    }

    /**
     * Walks to the destination via web walking.
     * @param tile The destination.
     * @return True if we reached the destination, false otherwise.
     */
    public static boolean webWalk(final RSTile tile) {
        if (tile == null)
            return false;
        return WebWalking.walkTo(tile, new Condition() {
            @Override
            public boolean active() {
                AntiBan.activateRun();
                return compareGameDestination(tile);
            }
        }, 500);
    }

    /**
     * Walks to the nearest bank via {@link Bank07#walkTo(boolean)}.
     * @see Bank07#walkTo(boolean)
     * @return True if we reached a bank, false otherwise.
     */
    public static boolean walkToBank() {
        return Bank07.walkTo(true);
    }

    public static boolean canReach(final Positionable pos) {
        if (pos != null) {
            final RSTile tile = pos.getPosition();
            if (tile.getPlane() == Game.getPlane())
                return getCollisionMap()[tile.getX() - Game.getBaseX()][tile.getY() - Game.getBaseY()];
        }
        return false;
    }

    private static boolean[][] getCollisionMap() {
        final int[][] flags = PathFinding.getCollisionData();
        final RSTile pLoc = Player.getPosition().toLocalTile();
        boolean[][] map = new boolean[104][104];
        visitTile(map, flags, pLoc.getX(), pLoc.getY());
        return map;
    }

    private static void visitTile(final boolean[][] map, final int[][] flags,
            final int x, final int y) {
        map[x][y] = true;
        if (y > 0 && !map[x][y - 1] // South
                && (flags[x][y - 1] & 0x1280102) == 0)
            visitTile(map, flags, x, y - 1);
        if (x > 0 && !map[x - 1][y] // West
                && (flags[x - 1][y] & 0x1280108) == 0)
            visitTile(map, flags, x - 1, y);
        if (y < 103 && !map[x][y + 1] // North
                && (flags[x][y + 1] & 0x1280120) == 0)
            visitTile(map, flags, x, y + 1);
        if (x < 103 && !map[x + 1][y] // East
                && (flags[x + 1][y] & 0x1280180) == 0)
            visitTile(map, flags, x + 1, y);
    }

    /**
     * Compares the game destination to the specified tile to see if they are
     * related.
     * @param tile The tile to compare.
     * @return True if the game destination and the specified tile are related,
     *         false otherwise.
     */
    private static boolean compareGameDestination(RSTile tile) {
        if (!Player07.isMoving())
            return false;
        RSTile gameDestination = Game.getDestination();
        if (tile == null || gameDestination == null)
            return false;
        return Entities.distanceTo(tile, gameDestination) < 1.5
                && Entities.aStarDistanceTo(tile, gameDestination) < 2;
    }

    /**
     * Compares the player position to the specified tile to see if they are
     * related.
     * @param tile The tile to compare.
     * @return True if the game destination and the specified tile are related,
     *         false otherwise.
     */
    private static boolean comparePlayerPosition(RSTile tile) {
        RSTile player_position = Player07.getPosition();
        if (tile == null || player_position == null)
            return false;
        return tile.equals(player_position);
    }

    /**
     * Executes the specified of ActionSets in order.
     * @param actionSets The ActionSets.
     * @return True if the ActionSets were successfully executed, false
     *         otherwise.
     */
    public static boolean executeActionSets(ActionSet... actionSets) {
        for (ActionSet actionSet : actionSets)
            if (!actionSet.execute())
                return false;
        return true;
    }

    /**
     * Executes the specified of ActionSets in order.
     * @param actionSets The ActionSets.
     * @return True if the ActionSets were successfully executed, false
     *         otherwise.
     */
    public static boolean executeActionSets(List<ActionSet> actionSets) {
        return executeActionSets(actionSets.toArray(new ActionSet[actionSets.size()]));
    }

    /**
     * The type of walking the script will attempt before using A*.
     */
    public static enum InitialWalkType {
        NONE, WEB_WALK, STRAIGHT_WALK;
    }
}
