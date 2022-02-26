package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.FooterController;
import com.taco.suit_lady.util.tools.Print;
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
@FxmlView("/fxml/game/content/game_view_footer.fxml")
@Scope("prototype")
public class GameFooterController
        extends FooterController<GameFooter, GameFooterController, GameViewContent, GameViewContentData, GameViewContentController> {
    
    @FXML private AnchorPane root;
    
    @FXML private Button heheXD;
    
    protected GameFooterController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() { return root; }
    
    @Override public void initialize() {
        super.initialize();
        heheXD.setOnAction(event -> Print.print("HeheXD"));
    }
    
    //</editor-fold>
}
