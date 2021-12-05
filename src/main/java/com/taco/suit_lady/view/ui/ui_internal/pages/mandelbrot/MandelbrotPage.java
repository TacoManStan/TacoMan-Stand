package com.taco.suit_lady.view.ui.ui_internal.pages.mandelbrot;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class MandelbrotPage extends UIPage<MandelbrotPageController>
{
    public MandelbrotPage(@NotNull Springable springable, Object... constructorParams)
    {
        super(springable, constructorParams);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<MandelbrotPageController> controllerDefinition()
    {
        return MandelbrotPageController.class;
    }
}
