package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.game.ui.GameViewContentController;
import com.taco.suit_lady.game.ui.GameViewContentData;
import com.taco.suit_lady.ui.FooterController;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public class MandelbrotFooterController
        extends FooterController<MandelbrotFooter, MandelbrotFooterController, MandelbrotContent, MandelbrotContentData, MandelbrotContentController> {
    
    protected MandelbrotFooterController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
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
