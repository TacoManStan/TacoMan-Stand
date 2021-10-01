package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummySidebarController;
import com.taco.util.quick.ConsoleBB;

public class DummyPage extends UIPage<DummySidebarController>
{
    public DummyPage(UINode owner)
    {
        super(owner);
    
        final DummySidebarController pageController = this.weaver().loadController(DummySidebarController.class);
        pageController.setPage(this);
        setController(pageController);
    }
    

}
