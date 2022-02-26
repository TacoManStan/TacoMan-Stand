package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.ui.FooterController;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public class GameFooterController
        extends FooterController<GameFooter, GameFooterController, GameViewContent, GameViewContentData, GameViewContentController> {
    
    protected GameFooterController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public Pane root() {
        return null;
    }
    @Override public void initialize() {
    
    }
    
    //</editor-fold>
}
