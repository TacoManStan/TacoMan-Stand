package com.taco.suit_lady.game.ui.pages;

import com.taco.suit_lady.game.objects.tiles.GameTile;
import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.util.values.formulas.pathfinding.AStarNode;
import com.taco.suit_lady.util.values.formulas.pathfinding.AStarPathfinder;
import com.taco.suit_lady.util.values.formulas.pathfinding.DummyElement;
import com.taco.suit_lady.util.values.formulas.pathfinding.PathfindingTest;
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
import java.util.List;

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
            List<GameTile> tileMatrix = getGame().getTestObject1().getOccupiedTiles();
            for (GameTile tile: tileMatrix)
                System.out.println("Tile [" + tile.getTileLocationX() + ", " + tile.getTileLocationY() + "]: " + tile);
        });
        testButton3.setText("Print Occupying Tiles");
        
        testButton4.setOnAction(event -> {
            System.out.println("Map Width: " + getGameMap().getPixelWidth() + "::" + getGameMap().getWidth());
            System.out.println("Map Height: " + getGameMap().getPixelHeight() + "::" + getGameMap().getHeight());
            
            System.out.println("Map Aggregate X: " + getCamera().getAggregateX());
            System.out.println("Map Aggregate Y: " + getCamera().getAggregateY());
            
            System.out.println("Movement Target X: " + getGame().getTestObject1().mover().getTargetX());
            System.out.println("Movement Target Y: " + getGame().getTestObject1().mover().getTargetY());
            
            
            System.out.println("Test Object Width: " + getGame().getTestObject1().getWidth());
            System.out.println("Test Object Height: " + getGame().getTestObject1().getHeight());
            
            
            System.out.println("Test Object Location X: " + getGame().getTestObject1().getLocationX(false));
            System.out.println("Test Object Location Y: " + getGame().getTestObject1().getLocationY(false));
            
            System.out.println("Test Object Location X (Center): " + getGame().getTestObject1().getLocationX(true));
            System.out.println("Test Object Location Y (Center): " + getGame().getTestObject1().getLocationY(true));
            
            
            System.out.println("Test Object Bounds Box:" + getGame().getTestObject1().boundsBox());
            
            System.out.println("Test Object Model Definition Bounds: " + getGame().getTestObject1().getModel().getDefinition().getBounds());
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
        
        testButton7.setOnAction(event -> logiCore().execute(() -> {
            printer().get(getClass()).print("Saving GameObject Model Definitions...");
            getGameMap().gameObjects().forEach(gameObject -> {
                printer().get(getClass()).print("Saving Definition: " + gameObject.getModel().getDefinition());
                JFiles.save(gameObject.getModel().getDefinition());
            });
        }));
        testButton7.setText("Model Definition Save Test");
        
        testButton8.setOnAction(event -> {
            final AStarPathfinder<DummyElement> pathfinder = PathfindingTest.newPathfinder();
            final List<AStarNode<DummyElement>> path = PathfindingTest.aStar(pathfinder);
            getGameMap().setTestImage(pathfinder.generateDefaultImage(path, 30));
        });
        testButton8.setText("A* Pathfinding Test");
    }
    
    //</editor-fold>
}
