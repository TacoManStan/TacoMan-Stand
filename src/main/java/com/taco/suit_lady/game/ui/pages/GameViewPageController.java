package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.tacository.json.JFiles;
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
@FxmlView("/fxml/game/pages/game_view_page.fxml")
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
            
            debugger().printList(getGameMap().gameObjects(), "Map Objects");
        });
        testButton2.setText("Print Game Objects");
        
        testButton3.setOnAction(event -> {
            ArrayList<GameTile> tiles = new ArrayList<>();
            GameTile[][] tileMatrix = getGame().getTestObject().getOccupiedTiles();
            for (int i = 0; i < tileMatrix.length; i++)
                for (int j = 0; j < tileMatrix[i].length; j++)
                    System.out.println("Tile [" + i + ", " + j + "]: " + tileMatrix[i][j]);
        });
        testButton3.setText("Print Occupying Tiles");
        
        testButton4.setOnAction(event -> {
            System.out.println("Movement Target X: " + getGame().getTestObject().getCommand().getTargetX());
            System.out.println("Movement Target Y: " + getGame().getTestObject().getCommand().getTargetY());
            
            
            System.out.println("Test Object Width: " + getGame().getTestObject().getWidth());
            System.out.println("Test Object Height: " + getGame().getTestObject().getHeight());
            
            
            System.out.println("Test Object Location X: " + getGame().getTestObject().getLocationX(false));
            System.out.println("Test Object Location Y: " + getGame().getTestObject().getLocationY(false));
    
            System.out.println("Test Object Location X (Center): " + getGame().getTestObject().getLocationX(true));
            System.out.println("Test Object Location Y (Center): " + getGame().getTestObject().getLocationY(true));
        });
        testButton4.setText("Print Movement Data");
        
        testButton5.setOnAction(event -> {
            JFiles.save(getGameMap());
        });
        testButton5.setText("Save Map");
        
        testButton6.setOnAction(event -> {
            JFiles.load(getGameMap());
            getGameMap().getModel().getCanvas().repaint();
        });
        testButton6.setText("Load Map");
        
        testButton7.setOnAction(event -> { });
        testButton8.setOnAction(event -> { });
    }
    
    //</editor-fold>
}
