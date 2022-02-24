package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.commands.MoveCommand;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.Camera;
import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.SidebarBookshelf;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class GameViewContent extends Content<GameViewContent, GameViewContentData, GameViewContentController>
        implements UIDProcessable, Lockable, GameComponent {
    
    private final ReentrantLock lock;
    
    //
    
    private SidebarBookshelf bookshelf;
    private GameViewPage coverPage;
    private GameTileEditorPage tileEditorPage;
    
    private final ObjectProperty<GameMap> gameMapProperty;
    
    private final GameObject testObject; //Move to Player class
    private final GameObject testObject2;
    
    private ReadOnlyObjectWrapper<Point2D> mouseOnMapProperty;
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
        
        this.testObject = new GameObject(this);
        this.testObject2 = new GameObject(this);
    }
    
    public final GameObject getTestObject() { return testObject; }
    public final GameObject getTestObject2() { return testObject2; }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public GameViewContent init() {
        bookshelf = injectBookshelf("Game View",
                                    new UIBook(
                                            this,
                                            "Tile Selector",
                                            "details",
                                            uiBook -> ResourcesSL.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> tileEditorPage = new GameTileEditorPage(uiBook, this).init()),
                                            null),
                                    new UIBook(
                                            this,
                                            "Game View",
                                            "game_engine",
                                            uiBook -> ResourcesSL.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> coverPage = new GameViewPage(uiBook, this)),
                                            null)).select();
        
        initUIPage();
        initGame();
        
        
        ui().getContentManager().setContent(this);
        
        this.mouseOnMapProperty = new ReadOnlyObjectWrapper<>(new Point2D(-1, -1));
        
        return super.init();
    }
    
    private void initUIPage() { }
    
    private void initGame() {
        gameMapProperty.addListener((observable, oldValue, newValue) -> {
            ObjectsSL.getIfNonNull(() -> oldValue, value -> syncFX(() -> {
                getController().getMapPane().getChildren().remove(value.getModel().getParentPane());
                return value.shutdown();
            }));
            
            ObjectsSL.getIf(() -> newValue, value -> value != null && !ObjectsSL.equals(newValue, oldValue), value -> {
                value.init();
                
                getController().getMapPane().getChildren().retainAll();
                getController().getMapPane().getChildren().add(value.getModel().getParentPane());
                
                return value;
            });
        });
        
        setGameMap(GameMap.newTestInstance(this, lock, "test-map-jid"));
        
        initTestObjects();
    }
    
    private void initTestObjects() {
        testObject.init();
        testObject.setTileLocationX(20);
        testObject.setTileLocationY(20);
        getGameMap().gameObjects().add(testObject);
        
        //        testObject2.init();
        //        testObject2.setTileLocationX(30);
        //        testObject2.setTileLocationY(20);
        //        getGameMap().gameObjects().add(testObject2);
        
        getCamera().setLocationX((int) testObject.getLocationX(true));
        getCamera().setLocationY((int) testObject.getLocationY(true));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    public final @NotNull ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    public final GameMap getGameMap() { return gameMapProperty.get(); }
    public final GameMap setGameMap(@NotNull GameMap newValue) { return PropertiesSL.setProperty(gameMapProperty, newValue); }
    
    public final @NotNull Camera getCamera() { return getGameMap().getModel().getCamera(); }
    
    //
    
    public final ReadOnlyObjectProperty<Point2D> readOnlyMouseOnMapProperty() { return mouseOnMapProperty.getReadOnlyProperty(); }
    public final Point2D getMouseOnMap() { return mouseOnMapProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean handleKeyEvent(@NotNull KeyEvent keyEvent, boolean fx) {
        if (!fx)
            return switch (keyEvent.getCode()) {
                case W -> keyInputAction(() -> {
                    if (keyEvent.isShiftDown())
                        getCamera().moveY(-1);
                    else
                        getCamera().moveTileY(-1);
                }, fx);
                case A -> keyInputAction(() -> {
                    if (keyEvent.isShiftDown())
                        getCamera().moveX(-1);
                    else
                        getCamera().moveTileX(-1);
                }, fx);
                case S -> keyInputAction(() -> {
                    if (keyEvent.isShiftDown())
                        getCamera().moveY(1);
                    else
                        getCamera().moveTileY(1);
                }, fx);
                case D -> keyInputAction(() -> {
                    if (keyEvent.isShiftDown())
                        getCamera().moveX(1);
                    else
                        getCamera().moveTileX(1);
                }, fx);
                
                case UP -> keyInputAction(() -> testObject.moveY(-1), fx);
                case LEFT -> keyInputAction(() -> testObject.moveX(-1), fx);
                case DOWN -> keyInputAction(() -> testObject.moveY(1), fx);
                case RIGHT -> keyInputAction(() -> testObject.moveX(1), fx);
                
                case DIGIT1 -> keyInputAction(() -> abilityTest(1), fx);
                case DIGIT2 -> keyInputAction(() -> abilityTest(2), fx);
                case DIGIT3 -> keyInputAction(() -> abilityTest(3), fx);
                case DIGIT4 -> keyInputAction(() -> abilityTest(4), fx);
                
                default -> false;
            };
        return true;
    }
    
    private boolean keyInputAction(@NotNull Runnable action, boolean fx) {
        if (!fx) {
            action.run();
            return true;
        }
        return false;
    }
    
    private void abilityTest(int abilityNum) {
        Print.print("Ability Used: " + abilityNum);
        if (abilityNum == 1)
            processMovementOrder(launchMissileTest());
    }
    
    private @NotNull GameObject launchMissileTest() {
        final GameObject missile = new GameObject(getGame()).init();
        
        missile.setLocationX(getTestObject().getLocationX(false), false);
        missile.setLocationY(getTestObject().getLocationY(false), false);
        
        missile.attributes().addDoubleAttribute(MoveCommand.ACCELERATION_ID, 1.025D);
        missile.attributes().getDoubleAttribute(MoveCommand.SPEED_ID).setValue(1D);
        
        logiCore().triggers().register(Galaxy.newUnitArrivedTrigger(missile, event -> {
            Print.print("Unit Arrived [" + missile + "]  ||  [" + event.getMovedFrom() + "  -->  " + event.getMovedTo());
            missile.taskManager().shutdown();
        }));
        
        return missile;
    }
    
    @Override protected boolean handleMousePressEvent(@NotNull MouseEvent event, boolean fx) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        final Point2D viewToMap = getMouseOnMap();
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (fx)
                selectTile(event);
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (!fx)
                processMovementOrder(getTestObject());
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            if (!fx)
                processMovementOrder(testObject.launchMissileTest());
        }
        
        return true;
    }
    @Override protected boolean handleMouseReleaseEvent(@NotNull MouseEvent event, boolean fx) {
        return true;
    }
    @Override protected boolean handleMouseDragEvent(@NotNull MouseEvent event, boolean fx) {
        if (!fx)
            sync(() -> mouseOnMapProperty.set(getCamera().viewToMap(event.getX(), event.getY())));
        if (event.getButton() == MouseButton.SECONDARY) {
            if (!fx)
                processMovementOrder(getTestObject());
        }
        
        return true;
    }
    
    @Override protected boolean handleMouseMoveEvent(@NotNull MouseEvent event, boolean fx) {
        if (!fx)
            sync(() -> mouseOnMapProperty.set(getCamera().viewToMap(event.getX(), event.getY())));
        return false;
    }
    
    private void processMovementOrder(@NotNull GameObject source) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        final Point2D viewToMap = getMouseOnMap();
        
        Print.print("View To Map: " + viewToMap);
        
        source.getCommand().setTargetX((int) viewToMap.getX());
        source.getCommand().setTargetY((int) viewToMap.getY());
        
        source.getCommand().setPaused(false);
    }
    
    private void printTileInformation(@NotNull MouseEvent event) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        final Point2D viewToMap = getMouseOnMap();
        
        GameTile tile = getGameMap().getTileAtPoint(viewToMap);
        System.out.println("Tile At Point [" + viewToMap.getX() + ", " + viewToMap.getY() + "]: " + tile);
        debugger().printList(tile.getOccupyingObjects(), "Occupying Objects for Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]");
    }
    
    private void selectTile(@NotNull MouseEvent event) {
        final GameTile tile = getGameMap().getTileAtPoint(getCamera().viewToMap(event.getX(), event.getY()));
        if (tile != null)
            ToolsFX.runFX(() -> getUIData().setSelectedTile(tile));
    }
    
    //
    
    public @NotNull GameViewContent getGame() { return this; }
    public @NotNull GameUIData getUIData() { return getData().getUIData(); }
    
    
    @Override protected @NotNull GameViewContentData loadData() { return new GameViewContentData(this); }
    @Override protected @NotNull Class<GameViewContentController> controllerDefinition() { return GameViewContentController.class; }
    
    @Override protected void onActivate() { }
    @Override protected void onDeactivate() { }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("mandelbrot_content");
        return uidProcessor;
    }
    
    //
    
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    @Override public boolean isNullableLock() { return true; }
    
    //</editor-fold>
}
