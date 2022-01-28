package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.util.Lockable;
import javafx.fxml.FXML;
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
@FxmlView("/fxml/game_view/content/game_view_content.fxml")
@Scope("prototype")
public class GameViewContentController extends ContentController
        implements Lockable, GameComponent {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    @FXML private BorderPane borderPaneRoot;
    @FXML private AnchorPane mapPane;
    
    //</editor-fold>
    
    private GameViewContent content;
    private final ReentrantLock lock;
    
    public GameViewContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.lock = new ReentrantLock();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final void setGame(@NotNull GameViewContent content) { this.content = content; }
    
    public final AnchorPane getMapPane() { return mapPane; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    //
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() {
    
    }
    
    //</editor-fold>
}
