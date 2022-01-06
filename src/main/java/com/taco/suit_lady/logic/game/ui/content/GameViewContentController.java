package com.taco.suit_lady.logic.game.ui.content;

import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.ui.jfx.components.CanvasPane;
import com.taco.suit_lady.ui.jfx.util.Bounds2D;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Component
@FxmlView("/fxml/game_view/content/game_view_content.fxml")
@Scope("prototype")
public class GameViewContentController extends ContentController
        implements Lockable {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private BorderPane borderPaneRoot;
    @FXML private Label titleLabel;
    
    @FXML private StackPane canvasStackPane;
    @FXML private AnchorPane canvasAnchorPane;
    private final CanvasPane canvasPane;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    public GameViewContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        this.canvasPane = new CanvasPane(this, 0.0);
    }
    
    protected BoundCanvas canvas() {
        return canvasPane.canvas();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        canvasAnchorPane.getChildren().add(canvasPane);
        FXTools.setAnchors(canvasPane);
    }
    
    //</editor-fold>
}
