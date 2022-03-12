package com.taco.suit_lady.game.walking;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;
import scripts.starfox.api.util.ArrayUtil;
import scripts.starfox.api2007.Player07;
import scripts.starfox.api2007.entities.Objects07;
import scripts.starfox.api2007.walking.pathfinding.AStarCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nolan
 */
public class Obstacles {

    public static final String[] doorsArr = {"Door", "Gate", "Gate of War", "Rickety door", "Large Door", "Prison Door"};
    public static final String[] blockadesArr = {"Wilderness ditch", "Doorway", "Rockfall"};
    public static final String[] teleportsArr = {"Stairs", "Staircase", "Cave", "Ladder"};
    public static final String[] commandsArr = {"Open", "Cross", "Mine Rockfall", "Climb", "Climb-down", "Climb-up", "Climb-up", "Enter"};

    public static final ArrayList<String> doors = new ArrayList<>(Arrays.asList(doorsArr));
    public static final ArrayList<String> blockades = new ArrayList<>(Arrays.asList(blockadesArr));
    public static final ArrayList<String> teleports = new ArrayList<>(Arrays.asList(teleportsArr));
    public static final ArrayList<String> commands = new ArrayList<>(Arrays.asList(commandsArr));

    /**
     * Checks whether the specified RSObject is a teleport.
     * @param object The RSObject.
     * @return True if the specified RSObject is a teleport, false otherwise.
     */
    public static boolean isTeleport(RSObject object) {
        if (object != null) {
            RSObjectDefinition def = object.getDefinition();
            if (def != null)
                return ArrayUtil.containsPartOf(def.getName(), teleportsArr)
                        && ArrayUtil.containsPartOf(commands, def.getActions());
        }
        return false;
    }

    /**
     * Checks whether the specified RSObject is a door.
     * @param object The RSObject.
     * @return True if the specified RSObject is a door, false otherwise.
     */
    public static boolean isDoor(RSObject object) {
        if (object != null) {
            RSObjectDefinition def = object.getDefinition();
            if (def != null)
                return ArrayUtil.containsPartOf(def.getName(), doorsArr) && ArrayUtil.containsPartOf(commands, def.getActions());
        }
        return false;
    }

    /**
     * Checks whether the specified RSObject is a blockades.
     * @param object The RSObject.
     * @return True if the specified RSObject is a blockades, false otherwise.
     */
    public static boolean isBlockade(RSObject object) {
        if (object != null) {
            RSObjectDefinition def = object.getDefinition();
            if (def != null)
                return ArrayUtil.containsPartOf(def.getName(), blockadesArr) && ArrayUtil.containsPartOf(commands, def.getActions());
        }
        return false;
    }

    /**
     * Checks whether the specified RSObject is an obstacle.
     * @param object The RSObject.
     * @return True if the specified RSObject is a obstacle, false otherwise.
     */
    public static boolean isObstacle(RSObject object) {
        return isDoor(object) || isBlockade(object) || isTeleport(object);
    }

    /**
     * Gets all of the obstacle names.
     * @return The obstacle names.
     */
    public static final String[] getAll() {
        List<String> names = new ArrayList<>(doors.size() + blockades.size() + teleports.size());
        names.addAll(doors);
        names.addAll(blockades);
        names.addAll(teleports);
        return names.toArray(new String[names.size()]);
    }

    /**
     * Gets all of the obstacles on the specified path.
     * @param path The path.
     * @return The obstacles on the path.
     */
    public static final RSObject[] getObstaclesOnPath(RSTile[] path) {
        if (path == null || path.length < 1)
            return new RSObject[0];
        List<RSObject> objects = new ArrayList<>();
        for (RSTile tile : path) {
            RSObject obstacle = Objects07.getRealObjectAt(tile, new Filter<RSObject>() {
                @Override
                public boolean accept(RSObject o) {
                    return isObstacle(o);
                }
            });
            if (obstacle != null)
                objects.add(obstacle);
        }
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Gets all of the obstacles on the specified path.
     * @param path The path.
     * @return The obstacles on the path.
     */
    public static final RSObject[] getObstaclesOnPath(List<RSTile> path) {
        if (path == null || path.size() < 1)
            return new RSObject[0];
        return getObstaclesOnPath(path.toArray(new RSTile[path.size()]));
    }

    /**
     * Gets the next obstacle on the specified path.
     * @param path The path.
     * @return The next obstacle on the path. Null if no obstacles that need to be traversed are on the path.
     */
    public static final RSObject getNextObstacle(RSTile[] path) {
        if (path != null && path.length > 0) {
            RSObject[] objects = getObstaclesOnPath(path);
            for (int i = 0; i < path.length; i++) {
                RSTile t = path[i];
                for (RSObject obstacle : objects)
                    for (RSTile oTile : obstacle.getAllTiles())
                        if (oTile.equals(t))
                            if ((i != path.length - 1 && !PathFinding.canReach(path[i + 1], false))
                                    || (i == path.length - 1 && !PathFinding.canReach(t, false))
                                    || AStarCache.isTeleportAtTile(t))
                                return obstacle;
            }
        }
        return null;
    }

    /**
     * Checks to see if the player is past the specified obstacle.
     * @param path     The path.
     * @param obstacle The obstacle.
     * @return True if the player is past the obstacle, false otherwise.
     */
    public static final boolean isPastObstacle(RSTile[] path, RSObject obstacle) {
        if (path == null || path.length < 1 || obstacle == null)
            return false;
        RSTile player = Player07.getPosition();
        for (int i = 0; i < path.length; i++)
            if (obstacle.getPosition().equals(path[i]))
                for (int j = i + 1; j < path.length; j++)
                    if (player.equals(path[j]))
                        return true;
        return false;
    }

    /**
     * Checks to see if the specified {@link RSTile} contains a teleport {@link RSObject object}.
     * @param tile The {@link RSTile}.
     * @return True if the specified {@link RSTile} contains a teleport {@link RSObject object}, false otherwise.
     */
    public static boolean containsTeleport(RSTile tile) {
        return contains(tile, teleports);
    }

    /**
     * Checks to see if the specified {@link RSTile} contains a door {@link RSObject object}.
     * @param tile The {@link RSTile}.
     * @return True if the specified {@link RSTile} contains a door {@link RSObject object}, false otherwise.
     */
    public static boolean containsDoor(RSTile tile) {
        return contains(tile, doors);
    }

    /**
     * Checks to see if the specified {@link RSTile} contains a blockade {@link RSObject object}.
     * @param tile The {@link RSTile}.
     * @return True if the specified {@link RSTile} contains a blockade {@link RSObject object}, false otherwise.
     */
    public static boolean containsBlockade(RSTile tile) {
        return contains(tile, blockades);
    }

    /**
     * Checks to see if the specified {@link RSTile} contains any type of obstacle {@link RSObject object}.
     * @param tile The {@link RSTile}.
     * @return True if the specified {@link RSTile} contains any type of obstacle {@link RSObject object}, false otherwise.
     */
    public static boolean containsObstacle(RSTile tile) {
        final ArrayList<String> names = new ArrayList<>();
        names.addAll(teleports);
        names.addAll(doors);
        names.addAll(blockades);
        return contains(tile, names);
    }

    public static boolean[] getContains(final RSTile tile) {
        final boolean[] contains = new boolean[3];
        for (RSObject obj : Objects07.getRealObjectsAt(tile))
            if (obj != null) {
                RSObjectDefinition def = obj.getDefinition();
                if (def == null)
                    continue;
                String defName = def.getName();
                if (defName != null && ArrayUtil.containsPartOf(commands, def.getActions()))
                    if (ArrayUtil.containsPartOf(defName, teleportsArr))
                        contains[0] = true;
                    else if (ArrayUtil.containsPartOf(defName, doorsArr))
                        contains[1] = true;
                    else if (ArrayUtil.containsPartOf(defName, blockadesArr))
                        contains[2] = true;
            }
//        ArrayList<RSObject> obstcls = Objects07.getRealObjectsAt(tile, new Filter<RSObject>() {
//            @Override
//            public boolean accept(RSObject obj) {
//                RSObjectDefinition def = obj.getDefinition();
//                if (def != null) {
//                    String defName = def.getName();
//                    if (defName != null && ArrayUtil.containsPartOf(commands, def.getActions()))
//                        if (ArrayUtil.containsPartOf(defName, teleportsArr))
//                            contains[0] = true;
//                        else if (ArrayUtil.containsPartOf(defName, doorsArr))
//                            contains[1] = true;
//                        else if (ArrayUtil.containsPartOf(defName, blockadesArr))
//                            contains[2] = true;
//                }
//                return false;
//            }
//        });
        return contains;
    }

    /**
     * A helper method that checks if the specified {@link RSTile} contains an {@link RSObject} matching any of the specified names.
     * @param tile  The {@link RSTile}.
     * @param names The list of names.
     * @return True if the specified {@link RSTile} contains an {@link RSObject} matching any of the specified names, false otherwise.
     */
    private static boolean contains(final RSTile tile, final ArrayList<String> names) {
        if (names == null)
            return false;
        final String[] namesArr = names.toArray(new String[0]);
        ArrayList<RSObject> obstcls = Objects07.getRealObjectsAt(tile, new Filter<RSObject>() {
            @Override
            public boolean accept(RSObject obj) {
                RSObjectDefinition def = obj.getDefinition();
                if (def != null)
                    return ArrayUtil.containsPartOf(def.getName(), namesArr)
                            && ArrayUtil.containsPartOf(commands, def.getActions());
                return false;
            }
        });
        return !obstcls.isEmpty();
    }
}
