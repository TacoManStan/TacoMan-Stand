package com.taco.suit_lady.ui.pages.impl.content_selector.mandelbrot_test;

import com.taco.suit_lady.ui.Content;
import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.pages.impl.content_selector.ContentHandler;
import com.taco.suit_lady.ui.pages.impl.content_selector.ContentSelectorPage;
import org.jetbrains.annotations.NotNull;

public class MandelbrotContentSelectorPage extends ContentSelectorPage<MandelbrotContent, MandelbrotContentSelectorPageController, MandelbrotElementController> {
    
    public MandelbrotContentSelectorPage(@NotNull UIBook owner, Object... constructorParams) {
        super(owner, constructorParams);
    }
    
    @Override
    protected ContentHandler<MandelbrotContent, MandelbrotElementController> constructContentHandler(ContentSelectorPage<MandelbrotContent, MandelbrotContentSelectorPageController, MandelbrotElementController> parentPage) {
        return new ContentHandler<>(this) {
            @Override
            protected @NotNull MandelbrotContent newInstance() {
                return new MandelbrotContent(this);
            }
    
            @Override
            protected void onShutdown(MandelbrotContent mandelbrotContent) {
            
            }
        };
    }
    
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
}
