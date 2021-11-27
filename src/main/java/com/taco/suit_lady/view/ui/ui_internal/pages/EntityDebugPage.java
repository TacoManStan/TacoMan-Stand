package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.EntityDebugController;
import org.jetbrains.annotations.NotNull;

public class EntityDebugPage extends UIPage<EntityDebugController>
{
    
    public EntityDebugPage(UINode owner)
    {
        super(owner);
    }
    
    @Override
    protected void initializePage(Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<EntityDebugController> controllerDefinition()
    {
        return EntityDebugController.class;
    }
}

/*
 * TODO LIST:
 * [S] Make this of much higher quality.
 */