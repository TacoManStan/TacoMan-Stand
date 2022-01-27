package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.ui.UIPageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/fxml/game_view/pages/game_view_page.fxml")
@Scope("prototype")
public class GameViewPageController extends UIPageController<GameViewPage> {
    
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
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override
    public void initialize() {
        testButton1.setOnAction(event -> {
            System.out.println("Canvas Width: " + getPage().getContent().getGameMap().getModel().getCanvas().getWidth());
            System.out.println("Canvas Height: " + getPage().getContent().getGameMap().getModel().getCanvas().getHeight());
        });
        
        testButton2.setOnAction(event -> {
            getPage().getContent().getCamera().print();
        });
        
        testButton3.setOnAction(event -> {
            getPage().getContent().getCamera().moveX(-getPage().getContent().getCamera().getMap().getTileSize());
        });
        testButton3.setText("Decrement X");
        
        testButton4.setOnAction(event -> {
            getPage().getContent().getCamera().moveX(getPage().getContent().getCamera().getMap().getTileSize());
        });
        testButton4.setText("Increment X");
        
        
        testButton5.setOnAction(event -> {
            getPage().getContent().getCamera().moveY(-getPage().getContent().getCamera().getMap().getTileSize());
        });
        testButton5.setText("Decrement Y");
        
        testButton6.setOnAction(event -> {
            getPage().getContent().getCamera().moveY(getPage().getContent().getCamera().getMap().getTileSize());
        });
        testButton6.setText("Increment Y");
        
        testButton7.setOnAction(event -> { });
        testButton8.setOnAction(event -> { });
    }
    
    //</editor-fold>
}
