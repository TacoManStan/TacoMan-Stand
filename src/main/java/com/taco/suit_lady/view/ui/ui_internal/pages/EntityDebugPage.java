package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.EntityDebugController;

public class EntityDebugPage extends UIPage<EntityDebugController>
{
    
    public EntityDebugPage(UINode owner)
    {
        super(owner);
    }
    
    @Override
    protected Class<EntityDebugController> controllerDefinition()
    {
        return EntityDebugController.class;
    }
}

/*
 * TODO LIST:
 * [S] Make this of much higher quality.
 */