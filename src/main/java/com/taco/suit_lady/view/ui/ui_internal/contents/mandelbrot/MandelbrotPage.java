package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class MandelbrotPage extends UIPage<MandelbrotPageController>
{
    private final MandelbrotContent slContent;
    
    public MandelbrotPage(@NotNull UIBook owner, @NotNull MandelbrotContent slContent, Object... constructorParams)
    {
        super(owner, constructorParams);
        
        this.slContent = slContent;
    }
    
    public MandelbrotContent getSLContent()
    {
        return slContent;
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) {
    
    }
    
    @Override
    protected @NotNull Class<MandelbrotPageController> controllerDefinition()
    {
        return MandelbrotPageController.class;
    }
}
