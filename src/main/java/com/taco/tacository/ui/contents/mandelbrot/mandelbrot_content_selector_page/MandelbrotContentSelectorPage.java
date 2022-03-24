package com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page;

import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContentData;
import com.taco.tacository.ui.pages.impl.content_selector.ContentSelectorPage;
import org.jetbrains.annotations.NotNull;

public class MandelbrotContentSelectorPage extends ContentSelectorPage<
        MandelbrotContentData,
        MandelbrotContentSelectorPage,
        MandelbrotContentSelectorPageController,
        MandelbrotElementController,
        MandelbrotContentHandler,
        MandelbrotContent> {
    
    public MandelbrotContentSelectorPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected Class<MandelbrotElementController> elementControllerDefinition() {
        return MandelbrotElementController.class;
    }
    
    @Override
    protected @NotNull Class<MandelbrotContentSelectorPageController> controllerDefinition() {
        return MandelbrotContentSelectorPageController.class;
    }
    
    @Override
    protected void initializePage(@NotNull Object @NotNull [] constructorParams) {
        super.initializePage(constructorParams);
        getController().doInit();
    }
    
    
    @Override
    protected MandelbrotContentHandler constructContentHandler(MandelbrotContentSelectorPage parentPage) {
        return new MandelbrotContentHandler(this);
    }
    
    //</editor-fold>
}
