package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class SLMandelbrotContent extends SLContent<SLMandelbrotContentData, SLMandelbrotContentController>
{
    public SLMandelbrotContent(@NotNull Springable springable)
    {
        super(springable);
        
        getController().canvas().setCanvasListener((source, newWidth, newHeight) -> {
            for (int i = 0; i < 50; i++)
                for (int j = 0; j < 50; j++)
                    source.getGraphicsContext2D().getPixelWriter().setColor(i, j, Color.AQUA);
        });
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull SLMandelbrotContentData loadData()
    {
        return new SLMandelbrotContentData(-1, -1, 1);
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
