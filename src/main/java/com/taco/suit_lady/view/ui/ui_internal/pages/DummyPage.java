package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummySidebarController;
import org.jetbrains.annotations.NotNull;

public class DummyPage extends UIPage<DummySidebarController>
{
    public DummyPage(UINode owner)
    {
        super(owner);
    }
    
    @Override protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<DummySidebarController> controllerDefinition()
    {
        return DummySidebarController.class;
    }
}
