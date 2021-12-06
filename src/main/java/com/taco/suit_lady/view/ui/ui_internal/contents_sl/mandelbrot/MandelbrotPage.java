package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class MandelbrotPage extends UIPage<MandelbrotPageController>
{
    private final SLMandelbrotContent slContent;
    
    public MandelbrotPage(@NotNull SLMandelbrotContent slContent, Object... constructorParams)
    {
        super(slContent, constructorParams);
        
        this.slContent = slContent;
    }
    
    public SLMandelbrotContent getSLContent()
    {
        return slContent;
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<MandelbrotPageController> controllerDefinition()
    {
        return MandelbrotPageController.class;
    }
}
