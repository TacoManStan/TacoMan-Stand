package com.taco.suit_lady.ui.ui_internal.console;

import com.taco.suit_lady.ui.UIBook;
import com.taco.suit_lady.ui.UIPage;
import com.taco.suit_lady.ui.ui_internal.controllers.ConsoleController;
import org.jetbrains.annotations.NotNull;

public class ConsolePage extends UIPage<ConsoleController>
{
    public ConsolePage(UIBook owner)
    {
        super(owner);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<ConsoleController> controllerDefinition()
    {
        return ConsoleController.class;
    }
}