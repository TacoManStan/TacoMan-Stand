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
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        this.gameMapProperty = new SimpleObjectProperty<>();
    }
    
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
        
        getGameMap().mapObjects().add(new GameObject(this, lock).init());
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
    
    @Override protected void handleKeyEvent(@NotNull KeyCode keyCode) {
        switch (keyCode) {
            case W -> getCamera().moveY(-getCamera().getGameMap().getTileSize());
            case A -> getCamera().moveX(-getCamera().getGameMap().getTileSize());
            case S -> getCamera().moveY(getCamera().getGameMap().getTileSize());
            case D -> getCamera().moveX(getCamera().getGameMap().getTileSize());
        }
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
