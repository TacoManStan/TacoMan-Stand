package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.Camera;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ObjectsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class GameViewContent extends Content<GameViewContentData, GameViewContentController>
        implements UIDProcessable, Lockable, GameComponent {
    
    private final ReentrantLock lock;
    
    //
    
    private GameViewPage coverPage;
    
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
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public GameViewContent init() {
        injectBookshelf("Game View", new UIBook(
                this,
                "Game View",
                "game_engine",
                uiBook -> ResourcesSL.get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> coverPage = new GameViewPage(uiBook, this)),
                null));
        
        initUIPage();
        initGame();
        
        ui().getContentManager().setContent(this);
        
        return this;
    }
    
    private void initUIPage() { }
    
    private void initGame() {
        gameMapProperty.addListener((observable, oldValue, newValue) -> {
            ObjectsSL.getIfNonNull(() -> oldValue, value -> syncFX(() -> {
                getController().getMapPane().getChildren().remove(value.getModel().getParentPane());
                return value.shutdown();
            }));
            
            ObjectsSL.getIf(() -> newValue, value -> value != null && !ObjectsSL.equals(newValue, oldValue), value -> syncFX(() -> {
                value.init();
                
                getController().getMapPane().getChildren().retainAll();
                getController().getMapPane().getChildren().add(value.getModel().getParentPane());
                
                return value;
            }));
        });
        
        setGameMap(GameMap.newTestInstance(this, lock));
        testObject.setTileLocationX(20);
        testObject.setTileLocationY(20);
        getGameMap().mapObjects().add(testObject.init());
        
        testObject2.setTileLocationX(30);
        testObject2.setTileLocationY(20);
        getGameMap().mapObjects().add(testObject2.init());
        
        getCamera().xLocationProperty().bind(testObject.xLocProperty());
        getCamera().yLocationProperty().bind(testObject.yLocationProperty());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    @Override public final @NotNull ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    @Override public final GameMap getGameMap() { return gameMapProperty.get(); }
    @Override public final GameMap setGameMap(@NotNull GameMap newValue) { return PropertiesSL.setProperty(gameMapProperty, newValue); }
    
    public final @NotNull Camera getCamera() { return getGameMap().getModel().getCamera(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected boolean handleKeyEvent(@NotNull KeyCode keyCode) {
        return switch (keyCode) {
            case W -> keyInputAction(() -> testObject.moveTileY(-1));
            case A -> keyInputAction(() ->  testObject.moveTileX(-1));
            case S -> keyInputAction(() -> testObject.moveTileY(1));
            case D -> keyInputAction(() -> testObject.moveTileX(1));
            
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
    
    //
    
    @Override public @NotNull GameViewContent getGame() { return this; }
    
    
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
