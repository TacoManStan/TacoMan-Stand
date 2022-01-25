package com.taco.suit_lady.logic.game.ui;

import com.taco.suit_lady.ui.UIPageController;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotColorScheme;
import com.taco.suit_lady.ui.jfx.components.ImagePane;
import com.taco.suit_lady.ui.jfx.components.button.ImageButton;
import com.taco.suit_lady.ui.jfx.components.formatted_controls.DoubleField;
import com.taco.suit_lady.ui.jfx.components.formatted_controls.IntField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@FxmlView("/fxml/game_view/pages/game_view_page.fxml")
@Scope("prototype")
public class GameViewPageController extends UIPageController<GameViewPage> {
    
    @FXML private AnchorPane root;
    
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
    public void initialize() { }
    
    //</editor-fold>
}
