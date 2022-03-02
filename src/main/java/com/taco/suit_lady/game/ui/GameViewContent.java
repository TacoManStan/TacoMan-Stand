package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.attributes.AttributePage;
import com.taco.suit_lady.game.galaxy.abilities.specific.Ability_LaunchMissile;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.Camera;
import com.taco.suit_lady.game.GameMap;
import com.taco.suit_lady.game.ui.pages.GameTileEditorPage;
import com.taco.suit_lady.game.ui.pages.GameViewPage;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.SidebarBookshelf;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.*;
import com.taco.suit_lady.util.tools.printer.Printer;
import com.taco.suit_lady.util.values.ValuePair;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class GameViewContent
        extends Content<GameViewContent, GameViewContentData, GameViewContentController, GameFooter, GameFooterController>
        implements UIDProcessable, Lockable, GameComponent {
    
    private final ReentrantLock lock;
    
    //
    
    private SidebarBookshelf bookshelf;
    
    private GameViewPage coverPage;
    private GameTileEditorPage tileEditorPage;
    private AttributePage attributePage;
    
    
    private final ObjectProperty<GameMap> gameMapProperty;
    
    private final GameObject testObject; //Move to Player class
    private final GameObject testObject2;
    private final GameObject testObjectTree;
    
    private ObjectBinding<Point2D> mouseOnMapBinding;
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
        
        this.testObject = new GameObject(this, "test_obj");
        this.testObject2 = new GameObject(this);
        this.testObjectTree = new GameObject(this, "tree1_obj", "tree1");
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
                                            "Attribute List Test",
                                            "clients",
                                            uiBook -> ResourcesSL.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> attributePage = new AttributePage(uiBook, this)),
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
        
        testObject2.init();
        testObject2.setTileLocationX(30);
        testObject2.setTileLocationY(20);
        getGameMap().gameObjects().add(testObject2);
        
        testObjectTree.init();
        testObjectTree.setTileLocationX(40);
        testObjectTree.setTileLocationY(10);
        getGameMap().gameObjects().add(testObjectTree);
        
        getCamera().bindViewTo(testObject);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    public final @NotNull ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    public final GameMap getGameMap() { return gameMapProperty.get(); }
    public final GameMap setGameMap(@NotNull GameMap newValue) { return PropertiesSL.setProperty(gameMapProperty, newValue); }
    
    public final @NotNull Camera getCamera() { return getGameMap().getModel().getCamera(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean handleKeyEvent(@NotNull KeyEvent keyEvent, boolean fx) {
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
            case Q -> keyInputAction(() -> getCamera().toggleViewBinding(getTestObject()), !fx);
            
            //                case UP -> keyInputAction(() -> testObject.moveY(-1), fx);
            //                case LEFT -> keyInputAction(() -> testObject.moveX(-1), fx);
            //                case DOWN -> keyInputAction(() -> testObject.moveY(1), fx);
            //                case RIGHT -> keyInputAction(() -> testObject.moveX(1), fx);
            
            case NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4, NUMPAD5, NUMPAD6, NUMPAD7, NUMPAD8, NUMPAD9 -> keyInputAction(() -> shiftTile(keyEvent.getCode()), !fx);
            
            case DIGIT1 -> keyInputAction(() -> abilityTest(1), fx);
            case DIGIT2 -> keyInputAction(() -> abilityTest(2), fx);
            case DIGIT3 -> keyInputAction(() -> abilityTest(3), fx);
            case DIGIT4 -> keyInputAction(() -> abilityTest(4), fx);
            
            default -> false;
        };
    }
    
    private boolean keyInputAction(@NotNull Runnable action, boolean skip) {
        if (!skip) {
            action.run();
            return true;
        }
        return false;
    }
    
    private void abilityTest(int abilityNum) {
        Printer.print("Ability Used: " + abilityNum);
        if (abilityNum == 1)
            new Ability_LaunchMissile(testObject).use(new ValuePair<>("target", getController().getMouseOnMapSafe()));
    }
    
    @Override protected boolean handleMousePressEvent(@NotNull MouseEvent event, boolean fx) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (fx)
                selectTileAtMouse();
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (fx)
                getTestObject().getCommand().moveAndBind(getController().mouseOnMapBindingSafeX(), getController().mouseOnMapBindingSafeY());
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            if (!fx)
                abilityTest(1);
        }
        
        return true;
    }
    @Override protected boolean handleMouseReleaseEvent(@NotNull MouseEvent event, boolean fx) {
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (fx)
                getTestObject().getCommand().unbindAndMove(getController().getMouseOnMapSafe());
        }
        
        return true;
    }
    
    @Override protected boolean handleMouseDragEvent(@NotNull MouseEvent event, boolean fx) {
        if (event.getButton() == MouseButton.SECONDARY) {
            if (fx)
                getTestObject().getCommand().moveAndBind(getController().mouseOnMapBindingSafeX(), getController().mouseOnMapBindingSafeY());
        }
        
        return true;
    }
    
    private void printTileInformation(@NotNull MouseEvent event) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        final Point2D viewToMap = getController().getMouseOnMap();
        
        GameTile tile = getGameMap().getTileAtTileIndex(viewToMap);
        System.out.println("Tile At Point [" + viewToMap.getX() + ", " + viewToMap.getY() + "]: " + tile);
        debugger().printList(tile.getOccupyingObjects(), "Occupying Objects for Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]");
    }
    
    //
    
    private void selectTileAtMouse() { selectTileAtPoint(getController().getMouseOnMap()); }
    private void selectTileRoot() { selectTileAtPoint(new Point2D(1.0, 1.0)); }
    private void selectTileAtPoint(@NotNull Point2D targetPoint) {
        final GameTile tile = getGameMap().getTileAtPoint(targetPoint);
        if (tile != null)
            getUIData().setSelectedTile(tile);
    }
    
    private void shiftTile(@NotNull KeyCode direction) {
        switch (direction) {
            case NUMPAD1 -> shiftTile(-1, 1);
            case NUMPAD2 -> shiftTile(0, 1);
            case NUMPAD3 -> shiftTile(1, 1);
            case NUMPAD4 -> shiftTile(-1, 0);
            
            case NUMPAD5 -> getCamera().setLocation(getTestObject(), true);
            
            case NUMPAD6 -> shiftTile(1, 0);
            case NUMPAD7 -> shiftTile(-1, -1);
            case NUMPAD8 -> shiftTile(0, -1);
            case NUMPAD9 -> shiftTile(1, -1);
            //            case UP -> shiftTile(0, -1);
            //            case DOWN -> shiftTile(0, 1);
            //            case LEFT -> shiftTile(0, -1);
            //            case RIGHT -> shiftTile(0, 1);
            
            default -> Printer.err("Unrecognized Direction :" + direction);
        }
    }
    
    private void shiftTile(int xShift, int yShift) {
        final GameTile selectedTile = getUIData().getSelectedTile();
        if (selectedTile != null)
            getUIData().setSelectedTile(getGameMap().getTileAtTileIndex(selectedTile.getTileLocationX() + xShift, selectedTile.getTileLocationY() + yShift));
        else
            Printer.err("Selected Tile is Null.");
    }
    
    //
    
    public @NotNull GameViewContent getGame() { return this; }
    public @NotNull GameUIData getUIData() { return getData().getUIData(); }
    
    
    @Override protected @NotNull GameViewContentData loadData() { return new GameViewContentData(this); }
    @Override protected @NotNull Class<GameViewContentController> controllerDefinition() { return GameViewContentController.class; }
    @Override protected GameFooter constructFooter() { return new GameFooter(this); }
    
    @Override protected void onActivate() { }
    @Override protected void onDeactivate() { }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("game");
        return uidProcessor;
    }
    
    //
    
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    @Override public boolean isNullableLock() { return true; }
    
    //</editor-fold>
}
