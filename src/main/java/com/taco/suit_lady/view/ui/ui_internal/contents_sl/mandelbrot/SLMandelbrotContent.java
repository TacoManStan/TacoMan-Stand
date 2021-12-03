package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.jfx.components.ImagePane;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import org.jetbrains.annotations.NotNull;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        final ImagePane canvas = getController().canvas();
        canvas.setRequireWritableContent(true);
        canvas.setImage();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLMandelbrotContentData loadData()
    {
        return new SLMandelbrotContentData();
    }
    
    @Override
    protected @NotNull Class<SLMandelbrotContentController> controllerDefinition()
    {
        return SLMandelbrotContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
