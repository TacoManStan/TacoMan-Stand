package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.Camera;
import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ObjectsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class GameViewContent extends Content<GameViewContentData, GameViewContentController>
        implements UIDProcessable, Lockable {
    
    private final ReentrantLock lock;
    
    //
    
    private GameViewPage coverPage;
    
    private final ObjectProperty<GameMap> gameMapProperty;
    private ObjectProperty<Camera> cameraProperty;
    
    public GameViewContent(@NotNull Springable springable) {
        super(springable);
        this.lock = new ReentrantLock();
        
        //
        
        injectBookshelf("Game View", new UIBook(
                this,
                "Game View",
                "game_engine",
                uiBook -> ResourcesSL.get(
                        "pages",
                        uiBook.getUID(uiBook.getButtonID()),
                        () -> coverPage = new GameViewPage(uiBook, this)),
                null));
        
        this.gameMapProperty = new SimpleObjectProperty<>();
        this.cameraProperty = new SimpleObjectProperty<>();
        
        //
        
        initUIPage();
        initGame();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void initUIPage() { }
    
    private void initGame() {
        gameMapProperty.addListener((observable, oldValue, newValue) -> {
            ObjectsSL.doIfNonNull(() -> oldValue, value -> sync(() -> ToolsFX.runFX(() -> {
                getController().getMapPane().getChildren().remove(value.getModel().getParentPane());
                return value.shutdown();
            })));
            
            ObjectsSL.doIfNonNull(() -> newValue, value -> sync(() -> ToolsFX.runFX(() -> {
                value.init();
                
                getController().getMapPane().getChildren().add(value.getModel().getParentPane());
                
                return value;
            })));
        });
        
        setGameMap(new GameMap(this, lock, 40, 20));
        setCamera(new Camera(getGameMap(), getController().getMapPane().widthProperty(), getController().getMapPane().heightProperty()));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() { return coverPage; }
    
    public final ObjectProperty<GameMap> gameMapProperty() { return gameMapProperty; }
    public final GameMap getGameMap() { return gameMapProperty.get(); }
    public final GameMap setGameMap(GameMap newValue) { return PropertiesSL.setProperty(gameMapProperty, newValue); }
    
    public final ObjectProperty<Camera> cameraProperty() { return cameraProperty; }
    public final Camera getCamera() { return cameraProperty.get(); }
    public final Camera setCamera(Camera newValue) { return PropertiesSL.setProperty(cameraProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull ReentrantLock getLock() { return lock; }
    @Override public boolean isNullableLock() { return true; }
    
    //
    
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
    
    //</editor-fold>
}
