package com.taco.suit_lady.game;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.collision.Collidable;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.jfx.components.painting.paintables.canvas.ImagePaintCommand;
import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.list_tools.A;
import com.taco.suit_lady.util.tools.printing.PrintData;
import com.taco.suit_lady.util.values.ValueExpr2D;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.expressions.BoundsExpr;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import com.taco.suit_lady.util.values.numbers.shapes.Shape;
import com.taco.tacository.json.*;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class GameMap
        implements SpringableWrapper, Lockable, GameComponent, JObject, JLoadableObject {
    
    private final GameViewContent content;
    private final ReentrantLock lock;
    
    //
    
    private final ReadOnlyIntegerWrapper tileSizeProperty; // The number of "pixels" comprising each tile.
    
    private final ReadOnlyObjectWrapper<GameTile[][]> tileMatrixProperty;
    private final ArrayList<GameObject> gameObjects;
    
    
    private final IntegerBinding widthBinding;
    private final IntegerBinding heightBinding;
    
    private final IntegerBinding pixelWidthBinding;
    private final IntegerBinding pixelHeightBinding;
    
    private final ObjectBinding<Num2D> dimensionsBinding;
    private final ObjectBinding<Num2D> pixelDimensionsBinding;
    
    
    private GameMapModel model;
    private final ImagePaintCommand testPaintCommand;
    
    
    private String mapID;
    
    private final PrintData p;
    
    public GameMap(@NotNull GameViewContent content, @Nullable ReentrantLock lock, int tileSize, String mapID) {
        this.content = content;
        this.lock = lock != null ? lock : new ReentrantLock();
        
        //
        
        this.tileMatrixProperty = new ReadOnlyObjectWrapper<>();
        
        setTileMatrix(new GameTile[96][64]);
        
        this.tileSizeProperty = new ReadOnlyIntegerWrapper(tileSize);
        
        //
        
        this.widthBinding = Bind.intBinding(() -> getTileMatrix().length, tileMatrixProperty);
        this.heightBinding = Bind.intBinding(() -> getWidth() > 0 ? getTileMatrix()[0].length : 0, tileMatrixProperty);
        
        this.pixelWidthBinding = Bind.intBinding(() -> getWidth() * getTileSize(), widthBinding, tileSizeProperty);
        this.pixelHeightBinding = Bind.intBinding(() -> getHeight() * getTileSize(), heightBinding, tileSizeProperty);
        
        this.dimensionsBinding = Bind.objBinding(() -> new Num2D(getWidth(), getHeight()), widthBinding, heightBinding);
        this.pixelDimensionsBinding = Bind.objBinding(() -> new Num2D(getPixelWidth(), getPixelHeight()), pixelWidthBinding, pixelHeightBinding);
        
        //
        
        this.gameObjects = new ArrayList<>();
        
        
        this.mapID = mapID;
        
        //
        
        this.testPaintCommand = new ImagePaintCommand(this, null);
        
        p = printer().get("print-data-1");
        p.setEnabled(false);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final GameMap init() {
        this.model = new GameMapModel(getGame(), lock);
        this.model.init();
        JFiles.load(this);
        
        //        ArraysSL.iterateMatrix(GameTile::init, getTileMatrix());
        this.model.refreshMapImage();
        
        this.testPaintCommand.init();
        this.testPaintCommand.setPaintPriority(0);
        this.getModel().getCanvas().addPaintable(testPaintCommand);
    
        this.getModel().getCanvas().widthBinding().addListener((observable, oldValue, newValue) -> refreshTestImage());
        this.getModel().getCanvas().heightBinding().addListener((observable, oldValue, newValue) -> refreshTestImage());
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final void setTestImage(@NotNull Image newImage) {
        testPaintCommand.setImage(newImage);
        refreshTestImage();
    }
    
    public final void refreshTestImage() {
        testPaintCommand.boundsBinding().setWidth(getModel().getCanvas().getWidth());
        testPaintCommand.boundsBinding().setHeight(getModel().getCanvas().getHeight());
        testPaintCommand.boundsBinding().setX(0);
        testPaintCommand.boundsBinding().setY(0);
        getModel().getCanvas().repaint();
    }
    
    //<editor-fold desc="> Dimensions Properties">
    
    protected final ReadOnlyIntegerWrapper tileSizeProperty() { return tileSizeProperty; }
    public final ReadOnlyIntegerProperty readOnlyTileSizeProperty() { return tileSizeProperty.getReadOnlyProperty(); }
    public final int getTileSize() { return tileSizeProperty.get(); }
    public final int setTileSize(@NotNull Number newValue) { return Props.setProperty(tileSizeProperty, newValue.intValue()); }
    
    //
    
    public final IntegerBinding widthBinding() { return widthBinding; }
    public final int getWidth() { return widthBinding.get(); }
    
    public final IntegerBinding heightBinding() { return heightBinding; }
    public final int getHeight() { return heightBinding.get(); }
    
    
    public final IntegerBinding pixelWidthBinding() { return pixelWidthBinding; }
    public final int getPixelWidth() { return pixelWidthBinding.get(); }
    
    public final IntegerBinding pixelHeightBinding() { return pixelHeightBinding; }
    public final int getPixelHeight() { return pixelHeightBinding.get(); }
    
    
    public final ObjectBinding<Num2D> dimensionsBinding() { return dimensionsBinding; }
    public final Num2D getDimensions() { return dimensionsBinding.get(); }
    
    public final ObjectBinding<Num2D> pixelDimensionsBinding() { return pixelDimensionsBinding; }
    public final Num2D getPixelDimensions() { return pixelDimensionsBinding.get(); }
    
    //</editor-fold>
    
    public final ReadOnlyObjectProperty<GameTile[][]> readOnlyTileMatrixProperty() { return tileMatrixProperty.getReadOnlyProperty(); }
    public final GameTile[][] getTileMatrix() { return tileMatrixProperty.get(); }
    private GameTile[][] setTileMatrix(GameTile[][] newValue) { return Props.setProperty(tileMatrixProperty, newValue); }
    
    //TODO: Limit accessors to synchronized add/remove methods & copy accessor
    public final List<GameObject> gameObjects() { return sync(() -> Collections.unmodifiableList(gameObjects)); }
    
    public final boolean  addGameObject(@NotNull GameObject obj) { return sync(() -> gameObjects.add(obj)); }
    public final boolean addGameObjects(@NotNull List<GameObject> objs) { return sync(() -> gameObjects.addAll(objs)); }
    public final boolean addGameObjects(@NotNull GameObject... objs) { return removeGameObjects(Arrays.asList(objs)); }
    
    public final boolean removeGameObject(@NotNull GameObject obj) { return sync(() -> gameObjects.remove(obj)); }
    public final boolean removeGameObjects(@NotNull List<GameObject> objs) { return sync(() -> gameObjects.removeAll(objs)); }
    public final boolean removeGameObjects(@NotNull GameObject... objs) { return removeGameObjects(Arrays.asList(objs)); }
    
    public final GameMapModel getModel() { return model; }
    
    
    /**
     * <p>Returns the {@link String} representing the {@code ID} used to {@link JFiles#save(JObject) save} and {@link JFiles#load(JLoadable) load} this {@link GameMap}.</p>
     *
     * @return The {@link String} representing the {@code ID} used to {@link JFiles#save(JObject) save} and {@link JFiles#load(JLoadable) load} this {@link GameMap}.
     */
    public final String getMapID() { return mapID; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Tile Accessors">
    
    public final @Nullable GameTile getNeighbor(@NotNull GameTile gameTile, int xTranslate, int yTranslate) {
        int xTemp = gameTile.getLocationX() + xTranslate;
        int yTemp = gameTile.getLocationY() + yTranslate;
        if (xTemp < 0 || xTemp >= getWidth() || yTemp < 0 || yTemp >= getHeight())
            return null; // Indicate to caller in some way that the returned value does not exist on this GMap.
        
        return getTileMatrix()[xTemp][yTemp];
    }
    
    
    /**
     * <p>Returns a {@link List} of {@link GameTile tiles} partially or entirely visible in the specified {@link Camera Camera's} bounds.</p>
     */
    // TO-EXPAND
    public final @NotNull List<GameTile> getTilesInView(@NotNull Camera camera) {
        int xLoc = camera.getLocationX();
        int yLoc = camera.getLocationY();
        
        //TODO: Include Offsets
        
        int pxMapWidth = getPixelWidth();
        int pxMapHeight = getPixelHeight();
        
        int tileMinX = Math.floorDiv(xLoc, getTileSize());
        int tileMinY = Math.floorDiv(yLoc, getTileSize());
        int tileMaxX = Calc.ceil(xLoc + pxMapWidth, getTileSize());
        int tileMaxY = Calc.ceil(yLoc + pxMapHeight, getTileSize());
        
        ArrayList<GameTile> returnTiles = new ArrayList<>();
        for (int i = tileMinX; i <= tileMaxX; i++)
            for (int j = tileMinY; j <= tileMaxY; j++)
                if (A.isInMatrixBounds(getTileMatrix(), i, j))
                    returnTiles.add(getTileMatrix()[i][j]);
        return returnTiles;
    }
    
    public final @NotNull GameTile getTileAtTileIndex(@NotNull Number x, @NotNull Number y) { return getTileAtTileIndex(new Num2D(x.doubleValue(), y.doubleValue())); }
    public final @NotNull GameTile getTileAtTileIndex(@NotNull NumExpr2D<?> point) { return getTileMatrix()[(int) Math.floor(point.aD())][(int) Math.floor(point.bD())]; }
    public final @NotNull GameTile getTileAtPoint(@NotNull Number x, @NotNull Number y) { return getTileAtPoint(new Num2D(x.doubleValue(), y.doubleValue())); }
    public final @NotNull GameTile getTileAtPoint(@NotNull NumExpr2D<?> point) { return getTileAtTileIndex(point.aD() / getTileSize(), point.bD() / getTileSize()); }
    
    //
    
    public final @NotNull ArrayList<GameTile> getTilesInBounds(@NotNull BoundsExpr bounds) {
        return getTilesInBounds(bounds.getLocation(LocType.MIN), bounds.getLocation(LocType.MAX));
    }
    
    public final @NotNull ArrayList<GameTile> getTilesInBounds(@NotNull NumExpr2D<?> minPoint, @NotNull NumExpr2D<?> maxPoint) {
        return sync(() -> {
            final ArrayList<GameTile> retTiles = new ArrayList<>();
            final GameTile[][] tileMatrix = getTileMatrix();
            
            final int iMin = pixelToTile(minPoint.a()).intValue();
            final int iMax = pixelToTile(maxPoint.a()).intValue();
            final int jMin = pixelToTile(minPoint.b()).intValue();
            final int jMax = pixelToTile(maxPoint.b()).intValue();
            
            for (int i = iMin; i <= iMax; i++) {
                for (int j = jMin; j <= jMax; j++) {
                    retTiles.add(tileMatrix[i][j]);
                }
            }
            return retTiles;
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> GameObject Accessors"> \
    
    @Contract("_ -> new")
    public final @NotNull ArrayList<GameObject> getObjectsAtPoint(@NotNull NumExpr2D<?> point) { return new ArrayList<>(getTileAtPoint(point).getOccupyingObjects()); }
    
    //<editor-fold desc="> Scan Methods">
    
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull FilterType filterType, @Nullable List<Predicate<GameObject>> filterList, @NotNull Predicate<GameObject>... filters) {
        final ArrayList<Predicate<GameObject>> filterListImpl = filterList != null ? new ArrayList<>(filterList) : new ArrayList<>();
        filterListImpl.addAll(Arrays.asList(filters));
        if (filterListImpl.isEmpty())
            return new ArrayList<>();
        return syncForbidFX(() -> filterType.filter(gameObjects(), filters));
    }
    
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull FilterType filterType, @NotNull Predicate<GameObject>... filters) { return scan(filterType, null, filters); }
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull Predicate<GameObject>... filters) { return scan(Enu.get(FilterType.class), filters); }
    
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull Point2D target, double radius, @NotNull FilterType filterType, @NotNull Predicate<GameObject>... filters) {
        final ArrayList<Predicate<GameObject>> filterList = new ArrayList<>(Arrays.asList(filters));
        filterList.add(gameObject -> gameObject.getLocation(true).distance(target) <= radius);
        return scan(filterType, filterList.toArray(new Predicate[0]));
    }
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull Point2D target, double radius, @NotNull Predicate<GameObject>... filters) { return scan(target, radius, Enu.get(FilterType.class), filters); }
    
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@Nullable List<Shape> included, @Nullable List<Shape> excluded, @NotNull FilterType filterType, @NotNull Predicate<GameObject>... filters) {
        if (included == null && excluded == null)
            return new ArrayList<>();
        
        final List<Shape> includedImpl = included != null ? included : new ArrayList<>();
        final List<Shape> excludedImpl = excluded != null ? excluded : new ArrayList<>();
        
        if (includedImpl.isEmpty() && excludedImpl.isEmpty())
            return new ArrayList<>();
        
        final Predicate<GameObject> includedFilter = gameObject -> includedImpl.isEmpty() || gameObject.collidesWith(included);
        final Predicate<GameObject> excludedFilter = gameObject -> excludedImpl.isEmpty() || !gameObject.collidesWith(excluded);
        
        return scan(filterType, A.asList(includedFilter, excludedFilter), filters);
    }
    @SafeVarargs public final @NotNull ArrayList<GameObject> scan(@NotNull List<Shape> shapes, @NotNull FilterType filterType, @NotNull Predicate<GameObject>... filters) { return scan(shapes, null, filterType, filters); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Pathability Check Methods">
    
    public final boolean isInBounds(@NotNull Number x, @NotNull Number y) {
        return sync(() -> {
            final double xD = x.doubleValue();
            final double yD = y.doubleValue();
            final double pixelWidth = getPixelWidth();
            final double pixelHeight = getPixelHeight();
            return xD >= 0 && yD >= 0 && xD < pixelWidth && yD < pixelHeight;
        });
    }
    
    //<editor-fold desc=">> Point Pathability Check Methods">
    
    public final boolean isPathable(@NotNull Number x, @NotNull Number y) {
        return !sync(() -> !isInBounds(x, y) || gameObjects().stream().anyMatch(gameObject -> gameObject.collisionMap().containsPoint(x, y)));
    }
    public final boolean isPathable(@NotNull Point2D point) { return isPathable(point.getX(), point.getY()); }
    public final boolean isPathable(@NotNull ValueExpr2D<Number, Number> point) { return isPathable(point.a(), point.b()); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Pathing Check Methods">
    
    public final boolean isPathable(@NotNull Collidable<?> collidable, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        final double xModD = xMod.doubleValue();
        final double yModD = yMod.doubleValue();
        p.print("Checking Collision For: [" + xMod + ", " + yMod + "]");
        return !sync(() -> gameObjects().stream().anyMatch(gameObject -> {
            return collidable.collidesWith(gameObject, translate, xModD, yModD);
        }));
    }
    
    public final boolean isPathable(@NotNull Collidable<?> collidable, boolean translate, @NotNull Point2D mod) { return isPathable(collidable, translate, mod.getX(), mod.getY()); }
    public final boolean isPathable(@NotNull Collidable<?> collidable, boolean translate, @NotNull ValueExpr2D<Number, Number> mod) { return isPathable(collidable, translate, mod.a(), mod.b()); }
    
    public final boolean isPathable(@NotNull Collidable<?> collidable, boolean translate) { return isPathable(collidable, translate, 0, 0); }
    
    //
    
    public final @NotNull Num2D nearestPathablePoint(@NotNull GameObject obj, @NotNull Number step, @NotNull Number maxRange, @NotNull Number targetAngle) {
        //TODO: Add desired angle param to indicate which direction is preferred
        //TODO: Add leniency param to indicate how much farther away than the actual closest point a target in the desired direction is allowed to be
        //TODO: Also allow a min and max angle to be specified to indicate a direction in which the pathable point has to be
        //TODO: ALso allow a target point & max distance the returned value is allowed to be from the specified point
        return sync(() -> {
            final Num2D pos = obj.getLocation(true);
            return Calc.nearestMatching(pos, obj.getDimensions(), LocType.CENTER, LocType.CENTER, LocType.CENTER, step, maxRange, targetAngle, p -> {
                boolean pathable = isPathable(obj, false, p);
                //                Printer.print("Checking Pathability of Point: " + p + "  (" + pathable + ")");
                return pathable;
            });
        });
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //
    
    
    //    public final @NotNull ArrayList<MapObject> scanMap(@NotNull GameObject target, double radius) {
    //        final ArrayList<MapObject> scannedObjects = new ArrayList<>();
    //        ArraysSL.iterateMatrix(tile -> scannedObjects.addAll(scanMap(tile, gameObject -> tile.inRange(gameObject, radius))), target.getOccupiedTiles());
    //        return scannedObjects;
    //    }
    //
    //    //TODO: Add support for different scan types - e.g., "any point on object", "center point", etc.
    //    private @NotNull ArrayList<MapObject> scanMap(@NotNull GameTile tile, @NotNull Predicate<GameObject> filter) {
    //        final ArrayList<MapObject> scannedObjects = new ArrayList<>();
    //        tile.getOccupyingObjects()
    //            .stream()
    //            .filter(filter)
    //            .forEach(scannedObjects::add);
    //        scannedObjects.add(tile);
    //        return scannedObjects;
    //    }
    
    public boolean shutdown() {
        //TODO
        return true;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    @Override public @NotNull GameMap getGameMap() { return this; }
    //
    
    @Override public @NotNull Springable springable() { return content; }
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    
    //<editor-fold desc="> JSON">
    
    @Override public String getJID() { return mapID; }
    @Override public void setJID(String jID) { mapID = jID; }
    
    @Override public JElement[] jFields() {
        final ArrayList<GameObject> gameObjectsImpl = new ArrayList<>(gameObjects);
        gameObjectsImpl.remove(getGame().getTestObject1());
        gameObjectsImpl.remove(getGame().getTestObject2());
        return new JElement[]{
                JUtil.create("tile-size", getTileSize()),
                JUtil.createArray("map-objects", gameObjectsImpl.toArray(new GameObject[0])),
                JUtil.createMatrix("tile-matrix", getTileMatrix())
        };
    }
    
    @Override public void doLoad(JsonObject parent) {
        setTileSize(JUtil.loadInt(parent, "tile-size"));
        
        final List<List<GameTile>> tileMatrixList = JUtil.loadMatrix(parent, "tile-matrix", o -> {
            JsonObject jsonObject = (JsonObject) o;
            GameTile gameTile = new GameTile(this);
            gameTile.load(jsonObject);
            return gameTile;
        });
        final int tileMatrixWidth = tileMatrixList.size();
        if (tileMatrixWidth > 0) {
            final int tileMatrixHeight = tileMatrixList.get(0).size();
            if (tileMatrixHeight > 0) {
                final GameTile[][] tileMatrixArray = new GameTile[tileMatrixWidth][tileMatrixHeight];
                for (int i = 0; i < tileMatrixList.size(); i++)
                    for (int j = 0; j < tileMatrixList.get(i).size(); j++)
                        tileMatrixArray[i][j] = tileMatrixList.get(i).get(j);
                setTileMatrix(tileMatrixArray);
            }
        }
        
        addGameObjects(JUtil.loadArray(parent, "map-objects", o -> {
            JsonObject jsonObject = (JsonObject) o;
            GameObject gameObject = new GameObject(getGame()).init();
            gameObject.load(jsonObject);
            return gameObject;
        }));
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC FACTORY METHODS ---">
    
    /**
     * <p>Identical to <i>{@link #newTestInstance(GameViewContent, ReentrantLock, String)}</i> except the {@link ReentrantLock} passed to the {@link GameMap Game Map's} {@link GameMap#GameMap(GameViewContent, ReentrantLock, int, String) constructor} is always {@code null}.</p>
     * <p>Note that a new {@link ReentrantLock} is automatically created by the {@link GameMap} constructor if the specified value is {@code null}, so the returned {@link GameMap} object will still be {@code synchronized}, just only with itself.</p>
     *
     * @param content Any non-null {@link Springable} object used to enable {@link Springable} features in the returned {@link GameMap} object.
     *
     * @return The newly constructed {@link GameMap} instance.
     *
     * @see #newTestInstance(GameViewContent, ReentrantLock, String)
     * @see GameMap
     * @see GameMap#GameMap(GameViewContent, ReentrantLock, int, String)
     */
    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content, @NotNull String mapID) { return newTestInstance(content, null, mapID); }
    public static @NotNull GameMap newTestInstance(@NotNull GameViewContent content, @Nullable ReentrantLock lock, @NotNull String mapID) { return new GameMap(content, lock, 32, mapID); }
    
    //</editor-fold>
}
