package com.taco.tacository.ui.contents.mandelbrot;

import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
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
