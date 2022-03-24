package com.taco.tacository.ui.contents.mandelbrot.mandelbrot_content_selector_page;

import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContent;
import com.taco.tacository.ui.contents.mandelbrot.MandelbrotContentData;
import com.taco.tacository.ui.pages.impl.content_selector.ContentHandler;
import com.taco.tacository.util.springable.Springable;
import org.jetbrains.annotations.NotNull;

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
        return new MandelbrotContent(this).init();
    }
    
    @Override
    protected void onShutdown(MandelbrotContent mandelbrotContent) {
    
    }
}
