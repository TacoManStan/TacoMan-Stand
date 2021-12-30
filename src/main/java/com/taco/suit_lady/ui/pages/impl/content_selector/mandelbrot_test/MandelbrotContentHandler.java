package com.taco.suit_lady.ui.pages.impl.content_selector.mandelbrot_test;

import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotContentData;
import com.taco.suit_lady.ui.pages.impl.content_selector.ContentHandler;
import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;

// CURRENTLY UNUSED - ANONYMOUS CLASS USED INSTEAD
public class MandelbrotContentHandler extends ContentHandler<
        MandelbrotContentData,
        MandelbrotContentSelectorPage,
        MandelbrotContentSelectorPageController,
        MandelbrotElementController,
        MandelbrotContentHandler,
        MandelbrotContent> {
    
    public MandelbrotContentHandler(Springable springable) {
        super(springable);
    }
    
    @Override
    protected @NotNull MandelbrotContent newInstance() {
        return new MandelbrotContent(this);
    }
    
    @Override
    protected void onShutdown(MandelbrotContent mandelbrotContent) {
    
    }
}
