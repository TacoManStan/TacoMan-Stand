package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;


public class ContentSwitchDemoPage extends UIPage<ContentSwitchDemoPageController>
{
    public ContentSwitchDemoPage(@NotNull Springable springable, Object... constructorParams)
    {
        super(springable, constructorParams);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<ContentSwitchDemoPageController> controllerDefinition()
    {
        return ContentSwitchDemoPageController.class;
    }
}
