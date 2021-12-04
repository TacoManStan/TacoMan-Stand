package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.SLMandelbrotContentData.MandelbrotColor;
import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        getController().canvas().setCanvasListener((source, newWidth, newHeight) -> {
            final Task<MandelbrotColor[][]> worker = new Task<>()
            {
                @Override
                protected MandelbrotColor[][] call()
                {
                    return null;
                }
    
            };
            getData().widthProperty().set((int) newWidth);
            getData().heightProperty().set((int) newHeight);
            getData().regenerate();
        });
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLMandelbrotContentData loadData()
    {
        return new SLMandelbrotContentData(-1, -1, 1.2);
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
