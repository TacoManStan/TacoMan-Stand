package com.taco.tacository.game.ui;

import com.taco.tacository.game.attributes.Attribute;
import com.taco.tacository.game.attributes.AttributeManager;
import com.taco.tacository.game.attributes.AttributePage;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_Blink;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_Cleave;
import com.taco.tacository.game.galaxy.abilities.specific.Ability_LaunchMissile;
import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.objects.tiles.GameTile;
import com.taco.tacository.game.GameMap;
import com.taco.tacository.game.ui.pages.GameTileEditorPage;
import com.taco.tacository.game.ui.pages.GameViewPage;
import com.taco.tacository.logic.triggers.Galaxy;
import com.taco.tacository.ui.*;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.*;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.tools.printing.Printer;
import com.taco.tacository.util.values.Value2D;
import com.taco.tacository.util.values.numbers.Num2D;
import com.taco.tacository.util.values.numbers.NumExpr2D;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Defines the root {@link Content} implementation for all {@link GameViewContent Game Logic}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link GameViewContent} instance is {@link GameViewContent#GameViewContent(Springable) Constructed} automatically by the {@link AppController} class.</li>
 *     <li>Once {@link GameViewContent#GameViewContent(Springable) Constructed}, a {@link GameViewContent} instance cannot be used until the <i>{@link #init()}</i> method is called.</li>
 *     <li>The {@link GameViewContentController} implementation of {@link ContentController} contains all {@code JavaFX UI Information} for this {@link GameViewContent} object.</li>
 *     <li>The {@link GameViewContentData} implementation of {@link ContentData} contains all {@code Internal Logic and Data} for this {@link GameViewContent} object.</li>
 *     <li>The {@link GameViewPage} implementation of {@link UIPage} defines the {@link Sidebar} {@link #getCoverPage() CoverPage} for this {@link GameViewContent} object.</li>
 *     <li>The {@link GameFooter} implementation of {@link Footer} defines the {@link Footer} content for this {@link GameViewContent} object.</li>
 *     <li>The {@link AttributePage} implementation of {@link UIPage} defines a demo display of the {@link Attribute Attributes} contained within an {@link AttributeManager}.</li>
 * </ol>
 */
//TO-EXPAND
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
    
    private final GameObject testObject1; //Move to Player class
    private final GameObject testObject2;
    private final GameObject testObjectTree;
    
    private ObjectBinding<Point2D> mouseOnMapBinding;
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
        
        this.testObject1 = new GameObject(this, "test_obj1");
        this.testObject2 = new GameObject(this, "test_obj2");
        this.testObjectTree = new GameObject(this, "tree1_obj", "tree1");
    }
    
    //<editor-fold desc="--- TEST OBJ ACCESSORS ---">
    
    public final GameObject getTestObject(int indexId) {
        return switch (indexId) {
            case 1 -> testObject1;
            case 2 -> testObject2;
            case 3 -> testObjectTree;
            
            default -> throw Exc.unsupported("No Test Object Matches ID: " + indexId);
        };
    }
    public final GameObject getTestObject(boolean obj1) { return getTestObject(obj1 ? 1 : 2); }
    public final GameObject getTestObject1() { return testObject1; }
    public final GameObject getTestObject2() { return testObject2; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public GameViewContent init() {
        initUIPage();
        initGame();
        
        ui().getContentManager().setContent(this);
        
        return super.init();
    }
    
    private void initUIPage() {
        bookshelf = injectBookshelf("Game View",
                                    new UIBook(
                                            this,
                                            "Tile Selector",
                                            "details",
                                            uiBook -> Stuff.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> tileEditorPage = new GameTileEditorPage(uiBook, this).init()),
                                            null),
                                    new UIBook(
                                            this,
                                            "Attribute List Test",
                                            "clients",
                                            uiBook -> Stuff.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> attributePage = new AttributePage(uiBook, this)),
                                            null),
                                    new UIBook(
                                            this,
                                            "Game View",
                                            "game_engine",
                                            uiBook -> Stuff.get(
                                                    "pages",
                                                    uiBook.getUID(uiBook.getButtonID()),
                                                    () -> coverPage = new GameViewPage(uiBook, this)),
                                            null)).select();
    }
    
    private void initGame() {
        gameMapProperty.addListener((observable, oldValue, newValue) -> {
            Obj.getIfNonNull(() -> oldValue, value -> syncFX(() -> {
                getController().getMapPane().getChildren().remove(value.getModel().getParentPane());
                return value.shutdown();
            }));
            
            Obj.getIf(() -> newValue, value -> value != null && !Obj.equals(newValue, oldValue), value -> {
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
        testObject1.init();
        testObject1.setTileLocationX(20, false);
        testObject1.setTileLocationY(20, false);
        testObject1.addToMap();
        //        getGameMap().addGameObject(testObject);
        //        getGameMap().gameObjects().add(testObject);
        
        testObject2.init();
        testObject2.setTileLocationX(30, false);
        testObject2.setTileLocationY(20, false);
        testObject2.addToMap();
        //        getGameMap().addGameObject(testObject2);
        //        getGameMap().gameObjects().add(testObject2);
        
        testObjectTree.init();
        testObjectTree.setTileLocationX(40, false);
        testObjectTree.setTileLocationY(10, false);
        testObjectTree.addToMap();
        //        getGameMap().addGameObject(testObjectTree);
        //        getGameMap().gameObjects().add(testObjectTree);
        
        getCamera().bindViewTo(testObject1);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    public final @NotNull ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    public final GameMap getGameMap() { return gameMapProperty.get(); }
    public final GameMap setGameMap(@NotNull GameMap newValue) { return Props.setProperty(gameMapProperty, newValue); }
    
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
            case Q -> keyInputAction(() -> getCamera().toggleViewBinding(getTestObject1()), !fx);
            
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
        switch (abilityNum) {
            case 1 -> new Ability_LaunchMissile(testObject1).use(new Value2D<>("target", getController().getMouseOnMapSafe()));
            case 2 -> blinkTest().use(new Value2D<>("target", getController().getMouseOnMapSafe()));
            case 3 -> new Ability_Cleave(testObject1).use(
                    new Value2D<>("target", getController().getMouseOnMapSafe()),
                    new Value2D<>("cleave_size", 45),
                    new Value2D<>("cleave_range", 75));
        }
    }
    
    private @NotNull Ability_Blink blinkTest() {
        final Ability_Blink ability = new Ability_Blink(testObject1);
        ability.validator().addValidator(Galaxy.newValidator(
                ability, params -> getGameMap().isPathable(ability.getSource(), false, L.get("target", Num2D.class, params))));
        return ability;
    }
    
    @Override protected boolean handleMousePressEvent(@NotNull MouseEvent event, boolean fx) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (fx)
                selectTileAtMouse();
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (!fx)
                getTestObject(!event.isShiftDown()).mover().moveAndBind(getController().mouseOnMapBindingSafeX(), getController().mouseOnMapBindingSafeY());
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            if (!fx)
                abilityTest(4);
        } else if (event.getButton().equals(MouseButton.BACK)) {
            if (!fx)
                abilityTest(1);
        } else if (event.getButton().equals(MouseButton.FORWARD)) {
            if (!fx)
                abilityTest(3);
        }
        
        return true;
    }
    @Override protected boolean handleMouseReleaseEvent(@NotNull MouseEvent event, boolean fx) {
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            if (!fx)
                getTestObject(!event.isShiftDown()).mover().unbindAndMove(getController().getMouseOnMapSafe());
        }
        
        return true;
    }
    
    @Override protected boolean handleMouseDragEvent(@NotNull MouseEvent event, boolean fx) {
        if (event.getButton() == MouseButton.SECONDARY) {
            if (!fx)
                getTestObject(!event.isShiftDown()).mover().moveAndBind(getController().mouseOnMapBindingSafeX(), getController().mouseOnMapBindingSafeY());
        }
        
        return true;
    }
    
    private void printTileInformation(@NotNull MouseEvent event) {
        //        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        final Num2D viewToMap = getController().getMouseOnMap();
        
        GameTile tile = getGameMap().getTileAtTileIndex(viewToMap);
        System.out.println("Tile At Point [" + viewToMap.a() + ", " + viewToMap.b() + "]: " + tile);
        debugger().printList(tile.getOccupyingObjects(), "Occupying Objects for Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]");
    }
    
    //
    
    private void selectTileAtMouse() { selectTileAtPoint(getController().getMouseOnMap()); }
    private void selectTileRoot() { selectTileAtPoint(new Num2D(1.0, 1.0)); }
    private void selectTileAtPoint(@NotNull NumExpr2D<?> targetPoint) {
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
            
            case NUMPAD5 -> getCamera().setLocation(getTestObject1(), true);
            
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
    
    public @NotNull GameViewContent getContent() { return this; }
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
