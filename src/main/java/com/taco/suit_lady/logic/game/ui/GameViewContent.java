package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.GameMap;
import com.taco.suit_lady.ui.AppUI;
import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ResourcesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import com.taco.tacository.json.JFiles;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.ReentrantLock;

public class GameViewContent extends Content<GameViewContentData, GameViewContentController>
        implements UIDProcessable, Lockable {
    
    private final ReentrantLock lock;
    
    //
    
    private GameViewPage coverPage;
    
    //
    
    private final ObjectProperty<GameMap> gameMapProperty;
    
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
        
        //
        
        initUIPage();
        initGame();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void initUIPage() {
        //Bind the value properties of relevant JFX components to the matching MandelbrotData property bidirectionally
        getCoverPage().getController().getPrecisionTextField().getFormatter().valueProperty().bindBidirectional(getData().precisionProperty());
        
        getCoverPage().getController().getXMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().xMaxProperty());
        getCoverPage().getController().getYMaxTextField().getFormatter().valueProperty().bindBidirectional(getData().yMaxProperty());
        getCoverPage().getController().getXMinTextField().getFormatter().valueProperty().bindBidirectional(getData().xMinProperty());
        getCoverPage().getController().getYMinTextField().getFormatter().valueProperty().bindBidirectional(getData().yMinProperty());
        
        getCoverPage().getController().getColorSchemeChoiceBox().valueProperty().bindBidirectional(getData().colorSchemeProperty());
        getCoverPage().getController().getInvertColorSchemeImageButton().selectedProperty().bindBidirectional(getData().invertColorSchemeProperty());
        
        getCoverPage().getController().getPauseAutoRegenerationImageButton().selectedProperty().bindBidirectional(getData().pauseAutoRegenerationProperty());
        
        
        // Refresh the generated image when an applicable MandelbrotData property changes
        getData().precisionProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        
        getData().xMinProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().xMaxProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().yMinProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().yMaxProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        
        getData().colorSchemeProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        getData().invertColorSchemeProperty().addListener((observable, oldValue, newValue) -> refreshCanvasChecked());
        
        
        // Bind the text properties of applicable labels to reflect relevant MandelbrotData calculated values (bindings)
        getCoverPage().getController().getWidthLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getWidth(), getData().widthBinding()));
        getCoverPage().getController().getHeightLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getHeight(), getData().heightBinding()));
        getCoverPage().getController().getWidthScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledWidth(), getData().scaledWidthBinding()));
        getCoverPage().getController().getHeightScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledHeight(), getData().scaledHeightBinding()));
        
        getCoverPage().getController().getXMinScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMinX(), getData().scaledXMinBinding()));
        getCoverPage().getController().getYMinScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMinY(), getData().scaledYMinBinding()));
        getCoverPage().getController().getXMaxScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMaxX(), getData().scaledXMaxBinding()));
        getCoverPage().getController().getYMaxScaledLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getScaledMaxY(), getData().scaledYMaxBinding()));
        
        getCoverPage().getController().getCanvasWidthLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getCanvasWidth(), getData().canvasWidthProperty()));
        getCoverPage().getController().getCanvasHeightLabel().textProperty().bind(
                Bindings.createStringBinding(() -> "" + getData().getCanvasHeight(), getData().canvasHeightProperty()));
        
        
        getCoverPage().getController().getSaveConfigButton().setOnAction(event -> JFiles.save(getData()));
        getCoverPage().getController().getLoadConfigButton().setOnAction(event -> JFiles.load(getData()));
    }
    
    private void initGame() {
        setGameMap(new GameMap(this, lock, 40, 20));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected GameViewPage getCoverPage() {
        return coverPage;
    }
    
    //
    
    public final ObjectProperty<GameMap> gameMapProperty() {
        return gameMapProperty;
    }
    
    public final GameMap getGameMap() {
        return gameMapProperty.get();
    }
    
    public final GameMap setGameMap(GameMap newValue) {
        GameMap oldValue = getGameMap();
        gameMapProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull ReentrantLock getLock() {
        return lock;
    }
    
    @Override
    public boolean isNullableLock() {
        return true;
    }
    
    //
    
    @Override
    protected @NotNull GameViewContentData loadData() {
        return GameViewContentData.newDefaultInstance(this);
    }
    
    @Override
    protected @NotNull Class<GameViewContentController> controllerDefinition() {
        return GameViewContentController.class;
    }
    
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    //
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("mandelbrot_content");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void refreshCanvasChecked() {
        boolean autoRegenerationPaused = getData().isAutoRegenerationPaused();
        if (!autoRegenerationPaused)
            refreshCanvas();
    }
    
    private void refreshCanvas() {
        sync(() -> ToolsFX.runFX(() -> {
            ToolsFX.clearCanvasUnsafe(ctx().getBean(AppUI.class).getContentManager().getContentOverlayCanvas());
        }, true));
    }
    
    //</editor-fold>
}
