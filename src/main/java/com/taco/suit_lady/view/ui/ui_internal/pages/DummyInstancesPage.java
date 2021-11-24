package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummyInstancesController;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesPage extends UIPage<DummyInstancesController>
{
    public DummyInstancesPage(UINode owner)
    {
        super(owner);
    }
    
    @Override
    protected @NotNull Class<DummyInstancesController> controllerDefinition()
    {
        return DummyInstancesController.class;
    }
}
