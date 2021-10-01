package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.EntityDebugController;
import com.taco.util.quick.ConsoleBB;

public class EntityDebugPage extends UIPage<EntityDebugController>
{
    
    public EntityDebugPage(UINode owner)
    {
        super(owner);
    
        final EntityDebugController pageController = this.ctx().getBean(EntityDebugController.class);
        pageController.setPage(this);
        setController(pageController);
    }
    
    //
}

/*
 * TODO LIST:
 * [S] Make this of much higher quality.
 */