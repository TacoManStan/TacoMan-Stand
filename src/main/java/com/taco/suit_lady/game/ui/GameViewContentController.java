package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import com.taco.suit_lady.ui.ui_internal.drag_and_drop.DragAndDropHandler;
import com.taco.suit_lady.util.Lockable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@FxmlView("/fxml/game/content/game_view_content.fxml")
@Scope("prototype")
public class GameViewContentController extends ContentController<GameViewContent, GameViewContentData, GameViewContentController>
        implements Lockable, GameComponent {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    @FXML private BorderPane borderPaneRoot;
    @FXML private AnchorPane mapPane;
    
    //</editor-fold>
    
    private GameViewContent content;
    private final ReentrantLock lock;
    
    //
    
    private DragAndDropHandler<GameObject> testDDHandler;
    
    public GameViewContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public GameViewContentController init(@NotNull GameViewContent content) {
        super.init(content);
        
        this.setGame(content);
        return this;
    }
    
    @Override public void initialize() {
        super.initialize();
        
        this.testDDHandler = new DragAndDropHandler<>(this, getLock(), root(), CellController.TEST_FORMAT, TransferMode.MOVE);
        testDDHandler.init();
        
        
        testDDHandler.setDragDetectedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragDoneHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        
        testDDHandler.setDragEnteredHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragExitedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragDroppedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
    }
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final void setGame(@NotNull GameViewContent content) { this.content = content; }
    
    public final AnchorPane getMapPane() { return mapPane; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    @Override public Pane root() { return root; }
    @Override public AnchorPane getContentPane() { return mapPane; }
    
    //</editor-fold>
}
