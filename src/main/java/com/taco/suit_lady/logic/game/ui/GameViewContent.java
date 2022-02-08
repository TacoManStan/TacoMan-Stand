package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.Camera;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.SidebarBookshelf;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIBookshelf;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ObjectsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class GameViewContent extends Content<GameViewContent, GameViewContentData, GameViewContentController>
        implements UIDProcessable, Lockable, GameComponent {
    
    private final ReentrantLock lock;
    
    //
    
    private SidebarBookshelf bookshelf;
    private GameViewPage coverPage;
    private GameTileEditorPage tileEditorPage;
    
    private final ObjectProperty<GameMap> gameMapProperty;
    
    private final GameObject testObject;
    private final GameObject testObject2;
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
        
        this.testObject = new GameObject(this, lock);
        this.testObject2 = new GameObject(this, lock);
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
        
        //        getCamera().xLocationProperty().bind(testObject.xLocationProperty());
        //        getCamera().yLocationProperty().bind(testObject.yLocationProperty());s
        
        getCamera().setLocationX((int) testObject.getLocationX(true));
        getCamera().setLocationY((int) testObject.getLocationY(true));
        
        logiCore().submit(getTestObject().getCommand());
        logiCore().submit(getTestObject2().getCommand());
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
    
    @Override protected boolean handleKeyEvent(@NotNull KeyEvent keyEvent) {
        return switch (keyEvent.getCode()) {
            case W -> keyInputAction(() -> {
                if (keyEvent.isShiftDown())
                    getCamera().moveY(-1);
                else
                    getCamera().moveTileY(-1);
            });
            case A -> keyInputAction(() -> {
                if (keyEvent.isShiftDown())
                    getCamera().moveX(-1);
                else
                    getCamera().moveTileX(-1);
            });
            case S -> keyInputAction(() -> {
                if (keyEvent.isShiftDown())
                    getCamera().moveY(1);
                else
                    getCamera().moveTileY(1);
            });
            case D -> keyInputAction(() -> {
                if (keyEvent.isShiftDown())
                    getCamera().moveX(1);
                else
                    getCamera().moveTileX(1);
            });
            
            case UP -> keyInputAction(() -> testObject.moveY(-1));
            case LEFT -> keyInputAction(() -> testObject.moveX(-1));
            case DOWN -> keyInputAction(() -> testObject.moveY(1));
            case RIGHT -> keyInputAction(() -> testObject.moveX(1));
            
            default -> false;
        };
    }
    
    private boolean keyInputAction(@NotNull Runnable action) {
        action.run();
        return true;
    }
    
    @Override protected boolean handleMousePressEvent(@NotNull MouseEvent event) {
        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        
        if (event.getButton().equals(MouseButton.PRIMARY))
            selectTile(event);
        else if (event.getButton().equals(MouseButton.SECONDARY))
            processMovementOrder(event, getTestObject());
        
        return true;
    }
    @Override protected boolean handleMouseReleaseEvent(@NotNull MouseEvent event) {
        return true;
    }
    @Override protected boolean handleMouseDragEvent(@NotNull MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY)
            processMovementOrder(event, getTestObject());
        
        return true;
    }
    
    private void processMovementOrder(@NotNull MouseEvent event, @NotNull GameObject source) {
        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        
        source.getCommand().setTargetX((int) viewToMap.getX());
        source.getCommand().setTargetY((int) viewToMap.getY());
        
        source.getCommand().setPaused(false);
    }
    
    private void printTileInformation(@NotNull MouseEvent event) {
        final Point2D viewToMap = getCamera().viewToMap(event.getX(), event.getY());
        
        GameTile tile = getGameMap().getTileAtPoint(viewToMap);
        System.out.println("Tile At Point [" + viewToMap.getX() + ", " + viewToMap.getY() + "]: " + tile);
        debugger().printList(tile.getOccupyingObjects(), "Occupying Objects for Tile [" + tile.getLocationX() + ", " + tile.getLocationY() + "]");
    }
    
    private void selectTile(@NotNull MouseEvent event) {
        final GameTile tile = getGameMap().getTileAtPoint(getCamera().viewToMap(event.getX(), event.getY()));
        if (tile != null)
            getUIData().setSelectedTile(tile);
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
