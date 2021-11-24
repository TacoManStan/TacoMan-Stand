package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummyInstancesController;

public class DummyInstancesPage extends UIPage<DummyInstancesController>
{
    public DummyInstancesPage(UINode owner)
    {
        super(owner);
    }
    
    @Override
    protected Class<DummyInstancesController> controllerDefinition()
    {
        return DummyInstancesController.class;
    }
}
