package com.taco.tacository.ui.contents.mandelbrot;

import com.taco.tacository.ui.Footer;
import org.jetbrains.annotations.NotNull;

public class MandelbrotFooter
        extends Footer<MandelbrotFooter, MandelbrotFooterController, MandelbrotContent, MandelbrotContentData, MandelbrotContentController> {
    
    public MandelbrotFooter(@NotNull MandelbrotContent content, Object... constructorParams) {
        super(content, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected void initializeFooter(@NotNull Object[] constructorParams) { }
    @Override protected @NotNull Class<MandelbrotFooterController> controllerDefinition() { return MandelbrotFooterController.class; }
    
    //</editor-fold>
}
