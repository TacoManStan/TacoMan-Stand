package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummyInstancesController;
import com.taco.util.quick.ConsoleBB;

public class DummyInstancesPage extends UIPage<DummyInstancesController>
{
    public DummyInstancesPage(UINode owner)
    {
        super(owner);
        
        // TODO - Replace with bi-directional binding property?
//        final DummyInstancesController pageController = this.ctx().getBean(DummyInstancesController.class);
        final DummyInstancesController pageController = this.weaver().loadController(DummyInstancesController.class);
        pageController.setPage(this);
        setController(pageController);
//        pageController.initialize();
    }
}
