package com.taco.tacository.ui.contents.mandelbrot;

import com.taco.tacository.ui.FooterController;
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
