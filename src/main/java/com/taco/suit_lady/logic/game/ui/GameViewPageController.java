package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.logic.game.commands.MoveBehavior;
import com.taco.suit_lady.logic.game.interfaces.GameComponent;
import com.taco.suit_lady.logic.game.objects.GameObject;
import com.taco.suit_lady.logic.game.objects.GameTile;
import com.taco.suit_lady.ui.UIPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@FxmlView("/fxml/game_view/pages/game_view_page.fxml")
@Scope("prototype")
public class GameViewPageController extends UIPageController<GameViewPage>
        implements GameComponent {
    
    @FXML private AnchorPane root;
    
    @FXML private Button testButton1;
    @FXML private Button testButton2;
    @FXML private Button testButton3;
    @FXML private Button testButton4;
    @FXML private Button testButton5;
    @FXML private Button testButton6;
    @FXML private Button testButton7;
    @FXML private Button testButton8;
    
    protected GameViewPageController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getPage().getGame(); }
    
    //
    
    @Override public Pane root() { return root; }
    @Override public void initialize() {
        testButton1.setOnAction(event -> {
            System.out.println("Canvas Width: " + getGameMap().getModel().getCanvas().getWidth());
            System.out.println("Canvas Height: " + getGameMap().getModel().getCanvas().getHeight());
        });
        
        testButton2.setOnAction(event -> {
            debugger().setDebugEnabled(true);
            debugger().setStatusEnabled(true);
            debugger().setErrorEnabled(true);
            debugger().setWarnEnabled(true);
            
            debugger().printList(getGameMap().mapObjects(), "Map Objects");
        });
        testButton2.setText("Print Game Objects");
        
        testButton3.setOnAction(event -> {
            ArrayList<GameTile> tiles = new ArrayList<>();
            GameTile[][] tileMatrix = getGame().getTestObject().getOccupyingTiles();
            for (int i = 0; i < tileMatrix.length; i++)
                for (int j = 0; j < tileMatrix[i].length; j++)
                    System.out.println("Tile [" + i + ", " + j + "]: " + tileMatrix[i][j]);
        });
        testButton3.setText("Print Occupying Tiles");
        
        testButton4.setOnAction(event -> { });
        testButton5.setOnAction(event -> { });
        testButton6.setOnAction(event -> { });
        testButton7.setOnAction(event -> { });
        testButton8.setOnAction(event -> { });
    }
    
    //</editor-fold>
}
