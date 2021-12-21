package com.taco.suit_lady.view.ui.pages.content_switch_demo_page;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;


public class ContentSwitchDemoPage extends UIPage<ContentSwitchDemoPageController>
{
    public ContentSwitchDemoPage(@NotNull UIBook owner, Object... constructorParams)
    {
        super(owner, constructorParams);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<ContentSwitchDemoPageController> controllerDefinition()
    {
        return ContentSwitchDemoPageController.class;
    }
}
