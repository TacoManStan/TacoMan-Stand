package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.game.ui.GameViewContentController;
import com.taco.suit_lady.game.ui.GameViewContentData;
import com.taco.suit_lady.ui.Footer;
import org.jetbrains.annotations.NotNull;

public class MandelbrotFooter
        extends Footer<MandelbrotFooter, MandelbrotFooterController, MandelbrotContent, MandelbrotContentData, MandelbrotContentController> {
    
    public MandelbrotFooter(@NotNull MandelbrotContent content, Object... constructorParams) {
        super(content, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    @Override protected @NotNull Class<MandelbrotFooterController> controllerDefinition() {
        return null;
    }
    
    //</editor-fold>
}
